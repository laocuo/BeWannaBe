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
import java.net.ServerSocket;
import java.net.Socket;
import com.jumpjump.MessageIDCommon;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class WifiConnectServer {
    private Handler mMainHandler = null;
    private boolean isWaittingClientJoin = true;
    private String mServerIP;
    private ServerSocket mServerSocket = null;
    private SendServiceHandler mSendServiceHandler = null;
    private HandlerThread mSendServiceThread = null;
    private RecvServiceHandler mRecvServiceHandler = null;
    private HandlerThread mRecvServiceThread = null;
    private String model = null;
    private DatagramSocket mUdpSocket = null;
    private Socket mTcpSocket = null;
    private Context mContext;

    public WifiConnectServer(Handler mServiceHandler, Context context) {
        // TODO Auto-generated constructor stub
        mMainHandler = mServiceHandler;
        mContext = context;
        model = new Build().MODEL;
    }

    public void start(String ip) {
        mServerIP = ip;
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    mServerSocket = new ServerSocket(MessageIDCommon.SERVER_TCP_PORT);
                    mTcpSocket = mServerSocket.accept();
                    //TODO client join
                    if (mTcpSocket != null) {
                        Log.d("JumpJump", "client join");

                        mRecvServiceThread = new HandlerThread("HandlerThread");
                        mRecvServiceThread.start();
                        mRecvServiceHandler = new RecvServiceHandler(mRecvServiceThread.getLooper(), mTcpSocket);
                        mRecvServiceHandler.sendEmptyMessageDelayed(MessageIDCommon.SERVER_TIMER_ONE_SECOND, MessageIDCommon.THREAD_SLEEP_TIME);

                        mSendServiceThread = new HandlerThread("HandlerThread");
                        mSendServiceThread.start();
                        mSendServiceHandler = new SendServiceHandler(mSendServiceThread.getLooper(), mTcpSocket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();

        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    mUdpSocket = new DatagramSocket(MessageIDCommon.SERVER_UDP_PORT);
                    mUdpSocket.setBroadcast(true);
                    byte data[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    while(isWaittingClientJoin) {
                        mUdpSocket.receive(packet);
                        String result = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        if (result != null) {
                            Log.d("JumpJump", "recv:"+result);
                            if (result.startsWith("search_room:")) {
                                String clientIP = result.substring("search_room:".length());
                                sleep(MessageIDCommon.THREAD_SLEEP_TIME);
                                feedbackServerIpAndName(clientIP);
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
    }

    private void feedbackServerIpAndName(String clientIP) {
        // TODO Auto-generated method stub
        try {
            InetAddress serverAddress = InetAddress.getByName(clientIP);
            String content = "return_room:"+model+"|"+mServerIP;
            Log.d("JumpJump", "send:"+content);
            byte data[] = content.getBytes();
            DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,MessageIDCommon.SERVER_UDP_PORT);
            mUdpSocket.send(packet);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                    case 0x402://SERVER_TIMER_ONE_SECOND
                        try {
                            Log.d("JumpJump", "SERVER_TIMER_ONE_SECOND");
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
                                sendEmptyMessageDelayed(MessageIDCommon.SERVER_TIMER_ONE_SECOND, MessageIDCommon.THREAD_SLEEP_TIME);
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
                if (readS.equalsIgnoreCase("join")) {
                    Message msg = mSendServiceHandler.obtainMessage(MessageIDCommon.SERVER_NOTIFY_CHESS_STEP);
                    msg.obj = "join:"+model;
                    mSendServiceHandler.sendMessage(msg);
                    //TODO close waitting join dialog and start game
                    mMainHandler.sendEmptyMessage(MessageIDCommon.SERVER_RETURN_JOIN_ROOM_SUCCESS);
                } else if (readS != null && (readS.startsWith("R:") || readS.startsWith("G:"))) {
                    Message msg = mMainHandler.obtainMessage(MessageIDCommon.SERVER_RETURN_CLIENT_STEP);
                    msg.obj = readS;
                    mMainHandler.sendMessage(msg);
                } else if (readS.startsWith("reset:")) {
                    Message msg = mMainHandler.obtainMessage(MessageIDCommon.SERVER_RETURN_CLIENT_CONFIRM_RESET);
                    msg.obj = readS;
                    mMainHandler.sendMessage(msg);
                } else if (readS.equalsIgnoreCase("reset")) {
                    mMainHandler.sendEmptyMessage(MessageIDCommon.SERVER_RETURN_CLIENT_RESET);
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
            switch (msg.what) {
                case 0x401://SERVER_NOTIFY_CHESS_STEP
                    if (dos != null) {
                        Log.d("JumpJump", "SERVER_NOTIFY_CHESS_STEP");
                        String step = (String)msg.obj;
                        try {
                            dos.writeUTF(step);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
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
        Message msg = mSendServiceHandler.obtainMessage(MessageIDCommon.SERVER_NOTIFY_CHESS_STEP);
        msg.obj = rec;
        mSendServiceHandler.sendMessage(msg);
    }

    public void stop() {
        // TODO Auto-generated method stub
        Log.d("JumpJump", "WifiConnectServerNew stop");
        try {
            isWaittingClientJoin = false;
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
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

    public void stopOtherReceiveThread(int arg1) {
        isWaittingClientJoin = false;
        stopRecvSocket();
    }
}
