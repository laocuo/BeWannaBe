package com.amaker.mdm.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MDMReceiver extends BroadcastReceiver {

	private static final String SERVICE_ACTION = "com.amaker.mdm.app.action.MDMService";
	
	//private KeyguardManager mKeyguardManager;
	//private KeyguardManager.KeyguardLock mKeyguardLock;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SharedPreferences pre = context.getSharedPreferences("AUTO_RUN",Context.MODE_WORLD_READABLE);
		boolean flag = pre.getBoolean("AUTO_RUN_FLAG",false);
		String action = intent.getAction();
		
		flag = false; //for debug
		
		if (true == flag) {
	        if(action.equals("android.intent.action.BOOT_COMPLETED")) {
	    		Intent it = new Intent();
	    		it.setAction(SERVICE_ACTION);
	    		context.startService(it);
	        }
		}
		
		
		/*
		if (action.equals("android.intent.action.NEW_OUTGOING_CALL")) {
			setResultData(null);
		}
		*/
        /*
        if(action.equals("android.intent.action.SCREEN_OFF")  
                || action.equals("android.intent.action.SCREEN_ON")) {  
            mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);  
            mKeyguardLock = mKeyguardManager.newKeyguardLock("zdLock 1");   
            mKeyguardLock.disableKeyguard();
            //startActivity(zdLockIntent);  
        }
        */
	}
}
