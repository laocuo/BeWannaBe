package com.amaker.mdm.app;


import java.net.MalformedURLException;
import com.amaker.mdm.client.MDMClient;
import com.amaker.mdm.cp.Employees.Employee;
import com.amaker.mdm.lock.MDMLockActivity;
import com.amaker.mdm.lock.MDMLockLisnter;
import com.amaker.mdm.lock.MDMLockView;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MDMService extends Service{
	private static final String OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";

	private static final String CALL_STATE = "android.intent.action.PHONE_STATE";
	private static final String DIAL_CALL = "android.intent.action.DIAL";
	private static final String DIAL_CALLING = "android.intent.action.CALL";
	private static final String DIAL_CALL_BUTTON = "android.intent.action.CALL_BUTTON";
	private static final String REC_SMS = "android.provider.Telephony.SMS_RECEIVED";
	private static final String SEND_SMS = "android.intent.action.SEND";
	
	public static final int UPDATE_RESULT = 0x100;
	public static final int LOGIN_RESULT = 0x101;
	
	private static int UPDATE_DURATION = 15000;//15s
	
	private static ServiceHandler servicehandler;
	private Intent mIntent;

	private KeyguardManager mKeyguardManager;
	private KeyguardManager.KeyguardLock mKeyguardLock;
	
	private MDMLockLisnter lockreceiver;
	
	private callctl call_receiver;
	
	private smsctl sms_receiver;
	
	private MDMClient thread;
	
	private boolean timer30s_flag = false;
	
	private SharedPreferences mdm_service_sp; //保存全部数据
	
	private Camera mCamera;
	
	private NotificationManager nm;
	
	// 可以返回null，通常返回一个有aidl定义的接口
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
        //disableSystemScreenLock();
        mdm_service_sp = getSharedPreferences("MDMService",MODE_WORLD_READABLE);
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //registerMDMLockLisnter();
	}

	@SuppressWarnings("unused")
	private void disableSystemScreenLock() {
		// TODO Auto-generated method stub
		mKeyguardManager = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);  
        mKeyguardLock = mKeyguardManager.newKeyguardLock("autolock");   
        mKeyguardLock.disableKeyguard();
	}

	@SuppressWarnings("unused")
	private void registerMDMLockLisnter() {
		// TODO Auto-generated method stub
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentfilter.addAction(Intent.ACTION_SCREEN_ON);
        lockreceiver = new MDMLockLisnter();
        registerReceiver(lockreceiver, intentfilter); 
	}

	private void registerMDMCallLisnter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		//为IntentFilter添加一个Action
		filter.addAction(OUTGOING_CALL);
		filter.addAction(CALL_STATE);
		filter.addAction(DIAL_CALL);
		filter.addAction(DIAL_CALLING);
		filter.addAction(DIAL_CALL_BUTTON);
		filter.setPriority(1000);
		call_receiver = new callctl();
		registerReceiver(call_receiver, filter);
	}
	
	private void registerMDMMessageLisnter() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		//为IntentFilter添加一个Action
		filter.addAction(SEND_SMS);
		filter.addAction(REC_SMS);
		filter.setPriority(1000);
		sms_receiver = new smsctl();
		registerReceiver(sms_receiver, filter);
	}
	
	public void onStart(Intent intent, int startId) {
		mIntent = intent;
		Toast.makeText(getApplicationContext(),"Start MDM service!",Toast.LENGTH_SHORT).show();
		startMDMClient();
	}

	private void startMDMClient() {
		// TODO Auto-generated method stub
		thread  = new MDMClient(getApplicationContext());
		thread.start();
		servicehandler = new ServiceHandler();
		try {
			thread.initClientHandler(servicehandler);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		servicehandler.postDelayed(new Runnable() {
			public void run() {
				sendLoginMessage2MDMClient();
			}
		},1000);
	}

	private void sendLoginMessage2MDMClient() {
		// TODO Auto-generated method stub
		boolean flag = getModuleData("LOGIN");
		
		flag = false; //for debug
		
		if (false == flag) {
			Message m = MDMClient.getClientHandler().obtainMessage();
	 		m.what = MDMClient.LOGIN;
	 		MDMClient.getClientHandler().sendMessage(m);
		}
		else {
			startTimer30sUpdate();
		}
	}

	private Runnable timer30s = new Runnable() {
		public void run() {
			if (timer30s_flag)
			{
				//Log.i("MDM","MDMService LocalIpAddress:"+MDMHelper.getLocalIpAddress());
				//send msg to MDMClient
				sendUpdateMessage2MDMClient();
				
				servicehandler.postDelayed(timer30s,UPDATE_DURATION);
			}
		}
	};
	
	private void sendUpdateMessage2MDMClient() {
		// TODO Auto-generated method stub
		if (MDMClient.getClientRunningFlag() == true)
		{
			Message m = MDMClient.getClientHandler().obtainMessage();
	 		m.what = MDMClient.TIME_30_UPDATE;
	 		MDMClient.getClientHandler().sendMessage(m);
		}
	}
	
	private void startTimer30sUpdate() {
		// TODO Auto-generated method stub
		timer30s_flag = true;
		sendMDMnotify(1,"LOGIN SUCCESS!");
		//sendMDMAppList2Server();
		servicehandler.postDelayed(timer30s,UPDATE_DURATION);
	}

	@SuppressWarnings("unused")
	private void sendMDMAppList2Server() {
		// TODO Auto-generated method stub
		Message m = MDMClient.getClientHandler().obtainMessage();
 		m.what = MDMClient.APP_LIST_UPDATE;
 		MDMClient.getClientHandler().sendMessage(m);
	}

	public void onDestroy() {
		timer30s_flag = false;
		MDMClient.getClientHandler().getLooper().quit();
		//mKeyguardLock.reenableKeyguard();
		unRegisterMDMLisnter();
		Toast.makeText(getApplicationContext(),"Stop MDM service!",Toast.LENGTH_SHORT).show();
		if (mIntent != null)
		{
			//startService(mIntent);
		}
	}
	
	private void unRegisterMDMLisnter() {
		// TODO Auto-generated method stub
		if (null != lockreceiver) {
			unregisterReceiver(lockreceiver); 
		}
		if (null != call_receiver) {
			unregisterReceiver(call_receiver);
		}
		if (null != sms_receiver) {
			unregisterReceiver(sms_receiver);
		}
	}

	public static Handler getServiceHandler() {
		return servicehandler;
	}
	
	private boolean getModuleData(String module_name) {
		return mdm_service_sp.getBoolean(module_name,false);
	}
	
	private void putModuleData(String module_name,boolean value) {
		mdm_service_sp.edit().putBoolean(module_name,value);
		mdm_service_sp.edit().commit();
	}
	
	class ServiceHandler extends Handler {
		public void handleMessage (Message msg)  {
			switch(msg.what){
			case LOGIN_RESULT:
				if (msg.arg1 == 1) {
					putModuleData("LOGIN",true);
					initMDMContentProvider();
					startTimer30sUpdate();
				}
				else {
					putModuleData("LOGIN",false);
				}
				break;
				
			case UPDATE_RESULT:
				//Log.i("MDM","UPDATE_RESULT msg.arg1="+msg.arg1);
				
				if (msg.arg1 == 0x10) //ScreenLock
				{
					if (msg.arg2 == 0x01) {
						screenLock();
					}
					else {
						screenUnLock();
					}
				}
				else if (msg.arg1 == 0x11) //Call
				{
					if (msg.arg2 == 0x01) {
						if (call_receiver == null) {
							registerMDMCallLisnter();
						}
					}
					else {
						if (null != call_receiver) {
							unregisterReceiver(call_receiver);
							call_receiver = null;
						}
					}
				}
				else if (msg.arg1 == 0x12) //Message
				{
					if (msg.arg2 == 0x01) {
						if (sms_receiver == null) {
							registerMDMMessageLisnter();
						}
					}
					else {
						if (null != sms_receiver) {
							unregisterReceiver(sms_receiver);
							sms_receiver = null;
						}
					}
				}
				else if (msg.arg1 == 0x13) //Camera
				{
					if (msg.arg2 == 0x01) {
						processCameraOffClick();
					}
					else {
						processCameraOnClick();
					}
				}
				else if (msg.arg1 == 0x14) //BlueTooth
				{
					if (msg.arg2 == 0x01) {
						
					}
					else {
						
					}
				}
				else if (msg.arg1 == 0x15) //USB
				{
					if (msg.arg2 == 0x01) {
						
					}
					else {
						
					}
				}
				else if (msg.arg1 == 0x16) //WebBroswer
				{
					if (msg.arg2 == 0x01) {
						
					}
					else {
						
					}
				}
				else if (msg.arg1 == 0x17) //Setting
				{
					if (msg.arg2 == 0x01) {
						
					}
					else {
						
					}
				}
				else if (msg.arg1 == 0x20) //Profile
				{
					set_profile(msg.arg2);
				}
				
				break;
			}
			super.handleMessage(msg);
		}
	}

	private void processCameraOnClick() {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	private void processCameraOffClick() {
		// TODO Auto-generated method stub
		if (mCamera == null) {
			try{
				mCamera = Camera.open();
			}
			catch( Exception e ){             
			    Log.i("MDM", "Impossible d'ouvrir la camera");         
			}
		}
	}
	
	private void screenLock() {
		// TODO Auto-generated method stub
		if (MDMLockActivity.lock_status == false) {
			Intent i = new Intent(getApplicationContext(),MDMLockActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplicationContext().startActivity(i);
		}
		MDMLockView.unLock_flag = false;
	}

	private void screenUnLock() {
		// TODO Auto-generated method stub
		MDMLockView.unLock_flag = true;
	}
	
	private void set_profile(int profile_mode)
    {
		AudioManager audio = null;
		audio = (AudioManager)(getSystemService(Context.AUDIO_SERVICE));
    	switch(profile_mode){
    	  case 1://静音
    		silent(audio);
    		break;
    	  case 2://有声音也有振动
    		ringAndVibrate(audio);
    		break;
    	  case 3://振动
    		vibrate(audio);
    		break;
    	  case 4://只声音，无振动
    		ring(audio);
    		break;
    	  default:
    		//ring(audio);
    		break;
    	}
    }
    //只声音，无振动：
    private void ring(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
    }
    //有声音也有振动
    private void ringAndVibrate(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
    }
    //振动
    private void vibrate(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
    }
    //静音
    private void silent(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
    }
    
    private void sendMDMnotify(int id,String msg) {
    	Notification n = new Notification(R.drawable.icon,msg,System.currentTimeMillis());
    	Intent intent = new Intent(this,MDMActivity.class);
    	PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
    	n.setLatestEventInfo(this, "MDM", msg, pi);
    	nm.notify(id,n);
    }
    
    @SuppressWarnings("unused")
	private void enableUSBDebug(boolean on) {
		MDMModifySettings ms = new MDMModifySettings();
		ms.MDMSetDevUSBDebug(getContentResolver(), on);
	}

    @SuppressWarnings("unused")
	private void enableUSBMassStorage(boolean on) {
    	MDMModifySettings ms = new MDMModifySettings();
    	ms.MDMSetBkgdMassStor(getContentResolver(), on);
	}
    
    private int queryModuleIndex(String module){
        ContentResolver cr = getContentResolver();

 	    String[] PROJECTION = new String[] { 
 	    		Employee._ID, 		// 0
  			   Employee.NAME, 		// 1
  			  Employee.CONTENT, 	// 2
 	    };

		Cursor c = cr.query(Employee.CONTENT_URI, PROJECTION, null,
				null, null);//this.DEFAULT_SORT_ORDER);

		int save_index = 0;
		
		if (c.moveToFirst()) {
			save_index = 1;
			int i = c.getCount() - 1;
			
			while (i >= 0) {
				c.moveToPosition(i);
				if (module.compareTo(c.getString(c.getColumnIndex(Employee.NAME))) == 0) {
					break;
				}
				save_index++;
				i--;
			}
			if (i < 0 ) {
				save_index = 0;
			}
		}
		return save_index;
    }
    
    private void insert(String name,String content){
    	Uri uri = Employee.CONTENT_URI;
    	ContentValues values = new ContentValues();

    	values.put(Employee.NAME, name);
    	values.put(Employee.CONTENT, content);
    	getContentResolver().insert(uri, values);
    }
    
    private void update(long id,String name,String content){
    	Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, id);
    	ContentValues values = new ContentValues();

    	values.put(Employee.NAME, name);
    	values.put(Employee.CONTENT, content);
		getContentResolver().update(uri, values, null, null);
    }
    
    @SuppressWarnings("unused")
	private void update_module(String name,String content){
    	int index = queryModuleIndex(name);
    	if (index > 0) {
    		update(index,name,content);
    	}
    }
    
    private void delall(){
    	// 获得ContentResolver，并删除
    	getContentResolver().delete(Employee.CONTENT_URI, null, null);
    }
    
	private void initMDMContentProvider() {
    	Log.i("MDM","MDMService delall:");
    	
    	delall();
    	
    	//if (queryModuleIndex("bluetooth") <= 0)
    	{
    		insert("bluetooth","false");
    	}
    	
    	//if (queryModuleIndex("screenlock") <= 0)
    	{
    		insert("screenlock","false");
    	}
    	
    	//if (queryModuleIndex("call") <= 0)
    	{
    		insert("call","false");
    	}
    	
    	//if (queryModuleIndex("message") <= 0)
    	{
    		insert("message","false");
    	}
    	
    	//if (queryModuleIndex("camera") <= 0)
    	{
    		insert("camera","false");
    	}
    	
    	//if (queryModuleIndex("webbroswer") <= 0)
    	{
    		insert("webbroswer","false");
    	}
    	
    	Log.i("MDM","MDMService initMDMContentProvider OK!!!");
    }
}
