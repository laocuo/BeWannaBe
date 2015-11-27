package com.amaker.mdm.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

public class callctl extends BroadcastReceiver {  

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			if (intent.getAction().equals(Intent.ACTION_DIAL)) {                 

	        	 //System.out.println("dddddddddd");
	        	 

	         }
			
			if (intent.getAction().equals(Intent.ACTION_CALL)) {                 

	        	 //System.out.println("eeeeee");
	        	 

	         }
			
			if (intent.getAction().equals(Intent.ACTION_CALL_BUTTON)) {                 

				
	        	 

	         }
			// 呼出电话  
			
	         if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) { 
	        	 setResultData(null);
		         abortBroadcast();
	         }  
	         else {  

	             // 呼入电话  
	        	
	             TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE); 
	             Class <TelephonyManager> c = TelephonyManager.class;
	        	 Method getITelephonyMethod = null;
	        	 
	        	//初始化iTelephony   	        	 
	        	 try {
	        	 getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[])null);
	        	 getITelephonyMethod.setAccessible(true);
	        	 } catch (SecurityException e) {
	        	 // TODO Auto-generated catch block
	        	 e.printStackTrace();
	        	 } catch (NoSuchMethodException e) {
	        	 // TODO Auto-generated catch block
	        	 e.printStackTrace();
	        	 }

	        	 try {
	        		 ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[])null);
	        		 iTelephony.endCall();
	        	 } catch (IllegalArgumentException e) {
	        	 // TODO Auto-generated catch block
	        	 e.printStackTrace();
	        	 } catch (IllegalAccessException e) {
	        	 // TODO Auto-generated catch block
	        	 e.printStackTrace();
	        	 } catch (InvocationTargetException e) {
	        	 // TODO Auto-generated catch block
	        	 e.printStackTrace();
	        	 } catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 abortBroadcast();
	        	 return;
	        	 
	        	 /*String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
	        	 if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                       //初始化iTelephony   	        	 
    	        	 try {
    	        	 getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[])null);
    	        	 getITelephonyMethod.setAccessible(true);
    	        	 } catch (SecurityException e) {
    	        	 // TODO Auto-generated catch block
    	        	 e.printStackTrace();
    	        	 } catch (NoSuchMethodException e) {
    	        	 // TODO Auto-generated catch block
    	        	 e.printStackTrace();
    	        	 }

    	        	 try {
    	        		 ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[])null);
    	        		 iTelephony.endCall();
    	        	 } catch (IllegalArgumentException e) {
    	        	 // TODO Auto-generated catch block
    	        	 e.printStackTrace();
    	        	 } catch (IllegalAccessException e) {
    	        	 // TODO Auto-generated catch block
    	        	 e.printStackTrace();
    	        	 } catch (InvocationTargetException e) {
    	        	 // TODO Auto-generated catch block
    	        	 e.printStackTrace();
    	        	 } catch (RemoteException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    	        	 return;
	        	 }
	        	 
	        	 switch (tm.getCallState()) {  

	                 case TelephonyManager.CALL_STATE_RINGING:  

	                     // 当前是来电  
	                	//初始化iTelephony
	    	        	 
	    	        	 try {
	    	        	 getITelephonyMethod = c.getDeclaredMethod("getITelephony", (Class[])null);
	    	        	 getITelephonyMethod.setAccessible(true);
	    	        	 } catch (SecurityException e) {
	    	        	 // TODO Auto-generated catch block
	    	        	 e.printStackTrace();
	    	        	 } catch (NoSuchMethodException e) {
	    	        	 // TODO Auto-generated catch block
	    	        	 e.printStackTrace();
	    	        	 }

	    	        	 try {
	    	        		 ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[])null);
	    	        		 iTelephony.endCall();
	    	        	 } catch (IllegalArgumentException e) {
	    	        	 // TODO Auto-generated catch block
	    	        	 e.printStackTrace();
	    	        	 } catch (IllegalAccessException e) {
	    	        	 // TODO Auto-generated catch block
	    	        	 e.printStackTrace();
	    	        	 } catch (InvocationTargetException e) {
	    	        	 // TODO Auto-generated catch block
	    	        	 e.printStackTrace();
	    	        	 } catch (RemoteException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	            

	                     break;  

	         

	                 case TelephonyManager.CALL_STATE_OFFHOOK:  
	                	//初始化iTelephony
	    	        	 
	    	        	  break;  

	         

	                 case TelephonyManager.CALL_STATE_IDLE: // 挂机  Device call state: No activity.  
	                	//初始化iTelephony
	    	        	 
	    	        	  break;  

	             }   */ 

	         }

	     }		
} 

