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

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {

    private Boolean isWifiConnect;
    private MainView mv;
    private MainBgView mbgv;
    private Button b1, b2, b3, b4;
    private ImageView selectedChess, currentChess;
    private ListView chessRecordList;
    private MainButtonClick mbc;
    private ArrayList<Integer> width_list;
    private ArrayList<Integer> height_list;
    private int son_width;
    private Bitmap SonBitmap = null;
    private int screen_width;
    private final int padding_width = 10;
    private final int padding_height = 10;
    private PopupWindow mWifiConnectPop;
    private View wifi_connect_select;
    private RelativeLayout mRelativeLayout;
    private ServiceHandler mServiceHandler;
    private ProgressDialog mCreateRoomDialog;
    private AlertDialog mJoinRoomDialog;
    private AlertDialog mWaittingConfirmResetDialog;
    private ProgressDialog mWaittingDialog;
    private DialogInterface.OnClickListener mRoomSelectListener;
    private WifiConnectServer mWifiConnectServer;
    private WifiConnectClient mWifiConnectClient;
    private RoomListAdapter mRoomListAdapter;
    private ArrayList<String> mRoomList;
    private WIFI_CONNECT_OWNER mWIFI_CONNECT_OWNER = WIFI_CONNECT_OWNER.IDLE;
//    private boolean bisWaitting = false;

    public enum WIFI_CONNECT_OWNER{
        IDLE,
        SERVER,
        CLIENT
    };

    public enum WAITTING_DIALOG_CATEGORY{
        WAITTING_CONNECT,
        WAITTING_RESET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        isWifiConnect = getIntent().getBooleanExtra("WIFI_CONNECT", false);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.main_activity);
        mv = (MainView)findViewById(R.id.mainview);
        mv.setMainActivity(this);
        mbgv = (MainBgView)findViewById(R.id.mainbgview);
        selectedChess = (ImageView)findViewById(R.id.selectedChess);
        selectedChess.setImageResource(R.drawable.yellowstar);
        selectedChess.setAlpha(1f);
        selectedChess.setVisibility(View.GONE);
        currentChess = (ImageView)findViewById(R.id.currentChess);
        currentChess.setImageResource(R.drawable.redstar);
        chessRecordList = (ListView)findViewById(R.id.chessRecordList);
        init();
        b1 = (Button) this.findViewById(R.id.button1);
        b2 = (Button) this.findViewById(R.id.button2);
        b3 = (Button) this.findViewById(R.id.button3);
        b4 = (Button) this.findViewById(R.id.button4);

        mbc = new MainButtonClick();

        b1.setOnClickListener(mbc);
        b2.setOnClickListener(mbc);
        b3.setOnClickListener(mbc);
        b4.setOnClickListener(mbc);

        mServiceHandler = new ServiceHandler();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (isWifiConnect == true) {
            AlertDialog dialog = new AlertDialog.Builder(this)
            .setMessage("PRESS YES TO EXIT..")
            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    finish();
                }
            })
            .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    
                }})
            .create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (isWifiConnect == true) {
            if (mWifiConnectPop != null && mWifiConnectPop.isShowing()) {
                mWifiConnectPop.dismiss();
                mWifiConnectPop = null;
            }
            if (mWifiConnectClient != null) {
                mWifiConnectClient.stop();
                mWifiConnectClient = null;
            }
            if (mWifiConnectServer != null) {
                mWifiConnectServer.stop();
                mWifiConnectServer = null;
            }
        }
        super.onDestroy();
    }

    private void init() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screen_width = dm.widthPixels;
        SonBitmap = FactoryInterface.getBitmap(this, R.drawable.greenstar);
        son_width = SonBitmap.getWidth();
        // son_height = GreenBitmap.getHeight();

        int common_width = (screen_width - padding_width * 2 - son_width) / 4;

        width_list = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++)
        {
            width_list.add(padding_width + i * common_width);
        }

        height_list = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++)
        {
            height_list.add(padding_height + i * common_width);
        }
        mv.setScreenParam(width_list, height_list);
        mv.setSelectChess(selectedChess, currentChess);
        mv.setChessRecordList(chessRecordList);
        mbgv.setScreenParam(width_list, height_list);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mbgv.invalidate();
        mv.invalidate();
        if (isWifiConnect) {
            b4.setTextColor(Color.DKGRAY);
            initWifiConnectPop();
        }
    }

    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (isWifiConnect) {
            mWifiConnectPop.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
        }
    }

    private void initWifiConnectPop() {
        // TODO Auto-generated method stub
        wifi_connect_select = LayoutInflater.from(this).inflate(R.layout.wifi_connect_select, mRelativeLayout, false);
        wifi_connect_select.findViewById(R.id.create_room).setOnClickListener(mbc);
        wifi_connect_select.findViewById(R.id.join_room).setOnClickListener(mbc);
        measureView(wifi_connect_select);
        mWifiConnectPop = new PopupWindow(wifi_connect_select, wifi_connect_select.getMeasuredWidth(), wifi_connect_select.getMeasuredHeight());
        mWifiConnectPop.setFocusable(true);
//        mWifiConnectPop.setOutsideTouchable(false);
//        wifi_connect_select.setFocusable(true);
        wifi_connect_select.setFocusableInTouchMode(true);
        wifi_connect_select.setOnKeyListener(new OnKeyListener(){

            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == KeyEvent.KEYCODE_BACK) {
                    finish();
                }
                return false;
            }});
//        mWifiConnectPop.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.setting_bg));

        mCreateRoomDialog = new ProgressDialog(this);
        mCreateRoomDialog.setCancelable(false);
        mCreateRoomDialog.setMessage("WAITTING JOIN...");
        mCreateRoomDialog.setButton(-1, "CANCEL", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                mWifiConnectServer.stop();
                mWifiConnectPop.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
            }});

        mRoomSelectListener = new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                String selectRoom = mRoomList.get(arg1);
                String sIndex = selectRoom.substring(0, selectRoom.indexOf("."));
                int index = Integer.valueOf(sIndex);
                Log.d("JumpJump", "SelectRoom:"+mRoomList.get(arg1)+" index:"+index);
                showWaittingDialog("CONNECTING...", WAITTING_DIALOG_CATEGORY.WAITTING_CONNECT);
                mWifiConnectClient.joinRoom(index);
            }
        };

        mRoomList = new ArrayList<String>();
        mRoomListAdapter = new RoomListAdapter();
        mJoinRoomDialog = new AlertDialog.Builder(this)
            .setTitle("ROOM LIST:")
            .setCancelable(false)
            .setAdapter(mRoomListAdapter, mRoomSelectListener)
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    mWifiConnectClient.stop();
                    mWifiConnectPop.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
                }})
            .create();

        mWaittingDialog = new ProgressDialog(this);
        mWaittingDialog.setCancelable(false);
    }

    private void showWaittingDialog(String msg, WAITTING_DIALOG_CATEGORY category) {
        // TODO Auto-generated method stub
        if (mWaittingDialog != null && mWaittingDialog.isShowing() == false) {
            mWaittingDialog.setMessage(msg);
            mWaittingDialog.show();
            Message m = mServiceHandler.obtainMessage(MessageIDCommon.MAIN_HANDLER_TIMER_30S);
            m.obj = category;
            mServiceHandler.sendMessageDelayed(m, 15000);
        }
    }

    private void closeWaittingDialog() {
        if (mWaittingDialog != null && mWaittingDialog.isShowing() == true) {
            mWaittingDialog.dismiss();
            mServiceHandler.removeMessages(MessageIDCommon.MAIN_HANDLER_TIMER_30S);
        }
    }

    private void closeWaittingDialog30S() {
        if (mWaittingDialog != null && mWaittingDialog.isShowing() == true) {
            mWaittingDialog.dismiss();
            Toast.makeText(MainActivity.this, "CONNECT TIMEOUT!", MessageIDCommon.TOAST_TIME).show();
        }
    }

    private class RoomListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mRoomList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            View v = arg1;
            if (v == null) {
                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.wifi_connect_room_list, arg2, false);
            }
            TextView tv = (TextView)v.findViewById(R.id.wifi_connect_room_item);
            tv.setText(mRoomList.get(arg0));
            return v;
        }
    }

    protected class MainButtonClick implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId())
            {
                case R.id.button1: {
                    if (mv.isComRuning() == false) {
                        if (isWifiConnect == false) {
                            mv.Restart();
                        } else {
                            notifyAnotherThread("reset");
                            showWaittingDialog("WAITTING CONFIRM...", WAITTING_DIALOG_CATEGORY.WAITTING_RESET);
                        }
                    }
                    break;
                }

                case R.id.button2: {
                    Intent i = new Intent(MainActivity.this, SettingPreferenceActivity.class);
                    startActivity(i);
                    break;
                }

                case R.id.button3: {
                    Intent i = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(i);
                    break;
                }

                case R.id.button4: {
                    if (isWifiConnect == false) {
                        mv.goBackStep();
                    }
                    break;
                }

                case R.id.create_room: {
                    CreateRoom();
                    break;
                }

                case R.id.join_room: {
                    JoinRoom();
                    break;
                }
            }
        }
    }

    private void measureView(View v) {
        int childWidthSpec = 0;
        int childHeightSpec = 0;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        if (params.width > 0) {
            childWidthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
        }
        if (params.height > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        }
        v.measure(childWidthSpec, childHeightSpec);
    }

    private void CreateRoom() {
        // TODO Auto-generated method stub
      WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
      WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();

      if (mWifiManager.isWifiEnabled() && mWifiInfo.getIpAddress() != 0) {
          String ip = intToIP(mWifiInfo.getIpAddress());
          Log.d("JumpJump", "ip:"+ip);
          showCreateRoomPop();
          mWifiConnectServer = new WifiConnectServer(mServiceHandler, this);
          mWifiConnectServer.start(ip);
      } else {
          Toast.makeText(this, "WIFI DISCONNECTED!", MessageIDCommon.TOAST_TIME).show();
      }
    }

    private void showCreateRoomPop() {
        // TODO Auto-generated method stub
        if (mWifiConnectPop.isShowing()) {
            mWifiConnectPop.dismiss();
        }
        if (mCreateRoomDialog != null && mCreateRoomDialog.isShowing() == false) {
            mCreateRoomDialog.show();
        }
    }

    private String intToIP(int ip) {
        // TODO Auto-generated method stub
        return (ip & 0xFF)+"."+
                (ip>>8 & 0xFF)+"."+
                (ip>>16 & 0xFF)+"."+
                (ip>>24 & 0xFF);
    }

    private void JoinRoom() {
        // TODO Auto-generated method stub
        WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();

        if (mWifiManager.isWifiEnabled() && mWifiInfo.getIpAddress() != 0) {
            String ip = intToIP(mWifiInfo.getIpAddress());
            Log.d("JumpJump", "ip:"+ip);
            showJoinRoomPop();
            mWifiConnectClient = new WifiConnectClient(mServiceHandler, this);
            mWifiConnectClient.start(ip);
        } else {
            Toast.makeText(this, "WIFI DISCONNECTED!", MessageIDCommon.TOAST_TIME).show();
        }
    }

    private void showJoinRoomPop() {
        // TODO Auto-generated method stub
        mRoomList.clear();
        if (mWifiConnectPop.isShowing()) {
            mWifiConnectPop.dismiss();
        }
        if (mJoinRoomDialog != null && mJoinRoomDialog.isShowing() == false) {
            mJoinRoomDialog.show();
        }
    }

    private class ServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0x101://CLIENT_RETURN_ROOM_NAME
                    if (mJoinRoomDialog != null && mJoinRoomDialog.isShowing() == true) {
                        mRoomList.add(msg.arg1 + "." + (String)msg.obj);
                        mRoomListAdapter.notifyDataSetChanged();
                    }
                    break;

                case 0x102://CLIENT_SEARCH_ROOM_DONE
                    Toast.makeText(MainActivity.this, "SEARCHING DONE!", MessageIDCommon.TOAST_TIME).show();
                    break;

                case 0x103://CLIENT_JOIN_ROOM_SUCCESS
                    if (isWifiConnect) {
                        mWIFI_CONNECT_OWNER = WIFI_CONNECT_OWNER.CLIENT;
                        mv.setWifiConnectOwner(mWIFI_CONNECT_OWNER);
                    }
                    closeWaittingDialog();
                    break;

                case 0x201://SERVER_RETURN_JOIN_ROOM_SUCCESS
                    if (isWifiConnect) {
                        mWIFI_CONNECT_OWNER = WIFI_CONNECT_OWNER.SERVER;
                        mv.setWifiConnectOwner(mWIFI_CONNECT_OWNER);
                    }
                    if (mCreateRoomDialog.isShowing() == true) {
                        mCreateRoomDialog.dismiss();
                    }
                    mWifiConnectServer.stopOtherReceiveThread(msg.arg1);
                    break;

                case 0x104://CLIENT_RETURN_SERVER_STEP
                case 0x202://SERVER_RETURN_CLIENT_STEP
                    mv.parseChessStep((String)msg.obj);
                    break;

                case 0x105://CLIENT_RETURN_SERVER_RESET
                case 0x204://SERVER_RETURN_CLIENT_RESET
                    if (mWaittingConfirmResetDialog == null) {
                        mWaittingConfirmResetDialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setTitle("AGREE TO RESET?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                notifyAnotherThread("reset:YES");
                                mv.Restart();
                            }})
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                notifyAnotherThread("reset:NO");
                            }
                        })
                        .create();
                    }
                    mWaittingConfirmResetDialog.show();
                    break;

                case 0x106://CLIENT_RETURN_SERVER_CONFIRM_RESET
                case 0x203://SERVER_RETURN_CLIENT_CONFIRM_RESET
                    String result = (String)msg.obj;
                    String confirm = result.substring(result.indexOf("reset:")+"reset:".length());
                    String str = "";
                    if (confirm.equalsIgnoreCase("YES")) {
                        mv.Restart();
                        str += "RESET SUCCESS!";
                    } else {
                        str += "RESET REJECT!";
                    }
                    closeWaittingDialog();
                    Toast.makeText(MainActivity.this, str, MessageIDCommon.TOAST_TIME).show();
                    break;

                case 0x107://CLIENT_JOIN_ROOM_FAIL
                    closeWaittingDialog();
                    if (mJoinRoomDialog != null && mJoinRoomDialog.isShowing() == false) {
                        mJoinRoomDialog.show();
                    }
                    Toast.makeText(MainActivity.this, "CONNECT FAILED!", MessageIDCommon.TOAST_TIME).show();
                    break;

                case 0x205://MAIN_HANDLER_TIMER_30S
                    WAITTING_DIALOG_CATEGORY category = (WAITTING_DIALOG_CATEGORY)msg.obj;
                    closeWaittingDialog30S();
                    if (category == WAITTING_DIALOG_CATEGORY.WAITTING_CONNECT) {
                        if (mJoinRoomDialog != null && mJoinRoomDialog.isShowing() == false) {
                            mJoinRoomDialog.show();
                        }
                    } else if (category == WAITTING_DIALOG_CATEGORY.WAITTING_RESET) {
                        //TODO do nothing
                    }
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void notifyAnotherThread(String rec) {
        // TODO Auto-generated method stub
        if (isWifiConnect) {
            if (mWIFI_CONNECT_OWNER == WIFI_CONNECT_OWNER.SERVER) {
                mWifiConnectServer.notifyAnotherThread(rec);
            } else if (mWIFI_CONNECT_OWNER == WIFI_CONNECT_OWNER.CLIENT) {
                mWifiConnectClient.notifyAnotherThread(rec);
            }
        }
    }
}
