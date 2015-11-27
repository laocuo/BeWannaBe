/*
* Copyright (C) 2013-2016 laocuo@163.com .
* Modification based on code covered by the mentioned copyright
* and/or permission notice(s).
*/
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jumpjump;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class WifiConnectClient {
    private Handler mMainHandler = null;
    private String mClientIP;
    private boolean isWaittingRetrunRoom = true;
    private ArrayList<String> mServerIPList = new ArrayList<String>();
    private SendServiceHandler mSendServiceHandler = null;
    private HandlerThread mSendServiceThread = null;
    private RecvServiceHandler mRecvServiceHandler = null;
    private HandlerThread mRecvServiceThread = null;
    private DatagramSocket mUdpSocket = null;
    private Socket mTcpSocket = null;
    private Context mContext;

    public WifiConnectClient(Handler mServiceHandler, Context context) {
        // TODO Auto-generated constructor stub
        mMainHandler = mServiceHandler;
        mContext = context;
    }

    public void start(String ip) {
        mClientIP = ip;
        try {
            mUdpSocket = new DatagramSocket(MessageIDCommon.SERVER_UDP_PORT);
            mUdpSocket.setBroadcast(true);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new Thread(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    byte data[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    while(isWaittingRetrunRoom) {
                        mUdpSocket.receive(packet);
                        String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        if (result != null) {
                            Log.d("JumpJump", "recv:"+result);
                            if (result.startsWith("return_room:")) {
                                String[] params = result.substring("return_room:".length()).split("\\|");
                                mServerIPList.add(params[1]);
                                Message msg = mMainHandler.obtainMessage(MessageIDCommon.CLIENT_RETURN_ROOM_NAME);
                                msg.arg1 = mServerIPList.size();
                                msg.obj = params[0];
                                mMainHandler.sendMessage(msg);
                            }
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    stopRecvSocket();
                }
            }}.start();

        new Thread(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    InetAddress serverAddress = InetAddress.getByName(MessageIDCommon.SERVER_BROAD_ADDRESS);
//                    InetAddress serverAddress = InetAddress.getByName(getStartIp(mClientIP)+255);
                    String content = "search_room:"+mClientIP;
                    Log.d("JumpJump", "send:"+content);
                    byte data[] = content.getBytes();
                    DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,MessageIDCommon.SERVER_UDP_PORT);
                    mUdpSocket.send(packet);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }}.start();
    }

    @SuppressWarnings("unused")
    private String getStartIp(String ip) {
        // TODO Auto-generated method stub
        String startIp = "";
        String[] iplist = ip.split("\\.");
        if (iplist.length>1) {
            for (int i=0;i<iplist.length-1;i++) {
                startIp += iplist[i];
                startIp += ".";
            }
        }
        return startIp;
    }

    public void joinRoom(int index) {
        final String ip = mServerIPList.get(index-1);
        isWaittingRetrunRoom = false;
        stopRecvSocket();
        Log.d("JumpJump", "joinRoom:"+ip);
        new Thread(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    mTcpSocket = new Socket(ip, MessageIDCommon.SERVER_TCP_PORT);
                    if (mTcpSocket != null) {
                        mRecvServiceThread = new HandlerThread("HandlerThread");
                        mRecvServiceThread.start();
                        mRecvServiceHandler = new RecvServiceHandler(mRecvServiceThread.getLooper(), mTcpSocket);
                        mRecvServiceHandler.sendEmptyMessageDelayed(MessageIDCommon.CLIENT_TIMER_ONE_SECOND, MessageIDCommon.THREAD_SLEEP_TIME);

                        mSendServiceThread = new HandlerThread("HandlerThread");
                        mSendServiceThread.start();
                        mSendServiceHandler = new SendServiceHandler(mSendServiceThread.getLooper(), mTcpSocket);
                        Message msg = mSendServiceHandler.obtainMessage(MessageIDCommon.CLIENT_JOIN_ROOM);
                        msg.obj = ip;
                        mSendServiceHandler.sendMessageDelayed(msg, MessageIDCommon.THREAD_SLEEP_TIME);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    mMainHandler.sendEmptyMessage(MessageIDCommon.CLIENT_JOIN_ROOM_FAIL);
                    e.printStackTrace();
                }
            }}.start();
    }

    private class RecvServiceHandler extends Handler {

        private Socket mSocket = null;
        private DataInputStream dis = null;
        private boolean isRunning = true;
        private int mFailTimes = 0;

        public RecvServiceHandler(Looper looper, Socket s) {
            // TODO Auto-generated constructor stub
            super(looper);
            mSocket = s;
            try {
                dis = new DataInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
                switch (msg.what) {
                    case 0x303://CLIENT_TIMER_ONE_SECOND
                        try {
                            Log.d("JumpJump", "CLIENT_TIMER_ONE_SECOND");
                            parseRecvCmd(dis.readUTF());
                            mFailTimes = 0;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            mFailTimes++;
                            e.printStackTrace();
                        } finally {
                            if (isRunning == true) {
                                if (mFailTimes > 10) {
                                    mFailTimes = 0;
                                    Toast.makeText(mContext, "CONNECT FAILED!", MessageIDCommon.TOAST_TIME).show();
                                }
                                sendEmptyMessageDelayed(MessageIDCommon.CLIENT_TIMER_ONE_SECOND, MessageIDCommon.THREAD_SLEEP_TIME);
                            }
                        }
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
        }

        private void parseRecvCmd(String readS) throws IOException {
            // TODO Auto-generated method stub
            if (readS != null && readS.length() > 0) {
                Log.d("JumpJump", "parseRecvCmd:"+readS);
                if (readS.startsWith("join:")) {
                    mMainHandler.sendEmptyMessage(MessageIDCommon.CLIENT_JOIN_ROOM_SUCCESS);
                } else if (readS.startsWith("R:") || readS.startsWith("G:")) {
                    Message msg = mMainHandler.obtainMessage(MessageIDCommon.CLIENT_RETURN_SERVER_STEP);
                    msg.obj = readS;
                    mMainHandler.sendMessage(msg);
                } else if (readS.equalsIgnoreCase("reset")) {
                    mMainHandler.sendEmptyMessage(MessageIDCommon.CLIENT_RETURN_SERVER_RESET);
                } else if (readS.startsWith("reset:")) {
                    Message msg = mMainHandler.obtainMessage(MessageIDCommon.CLIENT_RETURN_SERVER_CONFIRM_RESET);
                    msg.obj = readS;
                    mMainHandler.sendMessage(msg);
                }
            }
        }

        public void stop() {
            // TODO Auto-generated method stub
            if (dis != null) {
                isRunning = false;
                Log.d("JumpJump", "dis.close()");
                try {
                    dis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dis = null;
            }
        }
    }

    private class SendServiceHandler extends Handler {

        private Socket mSocket = null;
        private DataOutputStream dos = null;
        public SendServiceHandler(Looper looper, Socket s) {
            // TODO Auto-generated constructor stub
            super(looper);
            mSocket = s;
            try {
                dos = new DataOutputStream(mSocket.getOutputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            try {
                switch (msg.what) {
                    case 0x301://CLIENT_JOIN_ROOM
                        if (dos != null) {
                            Log.d("JumpJump", "CLIENT_JOIN_ROOM");
                            dos.writeUTF("join");
                        }
                        break;

                    case 0x302://CLIENT_NOTIFY_CHESS_STEP
                        if (dos != null) {
                            Log.d("JumpJump", "CLIENT_NOTIFY_CHESS_STEP");
                            String step = (String)msg.obj;
                            dos.writeUTF(step);
                        }
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void stop() {
            // TODO Auto-generated method stub
            if (dos != null) {
                Log.d("JumpJump", "dos.close()");
                try {
                    dos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dos = null;
            }
        }
    }

    public void notifyAnotherThread(String rec) {
        // TODO Auto-generated method stub
        Message msg = mSendServiceHandler.obtainMessage(MessageIDCommon.CLIENT_NOTIFY_CHESS_STEP);
        msg.obj = rec;
        mSendServiceHandler.sendMessage(msg);
    }

    public void stop() {
        // TODO Auto-generated method stub
        Log.d("JumpJump", "WifiConnectClientNew stop");
        try {
            isWaittingRetrunRoom = false;
            stopRecvSocket();
            if (mRecvServiceThread != null) {
                mRecvServiceHandler.stop();
                mRecvServiceThread.getLooper().quit();
            }
            if (mSendServiceThread != null) {
                mSendServiceHandler.stop();
                mSendServiceThread.getLooper().quit();
            }
            if (mTcpSocket != null) {
                Log.d("JumpJump", "mTcpSocket.close()");
                mTcpSocket.close();
                mTcpSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecvSocket() {
        // TODO Auto-generated method stub
        if (mUdpSocket != null) {
            Log.d("JumpJump", "release recv DatagramSocket");
            mUdpSocket.close();
            mUdpSocket = null;
        }
    }
}
