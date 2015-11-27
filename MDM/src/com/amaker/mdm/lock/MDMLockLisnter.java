package com.amaker.mdm.lock;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MDMLockLisnter extends BroadcastReceiver {

	private static boolean gScreenLockFlag = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals("android.intent.action.SCREEN_OFF")) {
			if (false == gScreenLockFlag) {
				gScreenLockFlag = true;
				Intent it = new Intent(context,MDMLockActivity.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(it);
			}
		}
		
		if(action.equals("android.intent.action.SCREEN_ON")) {
			if (true == gScreenLockFlag) {
				gScreenLockFlag = false;
			}
		}
	}

}
