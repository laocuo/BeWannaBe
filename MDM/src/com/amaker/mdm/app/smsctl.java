package com.amaker.mdm.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class smsctl extends BroadcastReceiver {  

	    //private static boolean bPlayingFlg = false;  
	
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			
			if(action.equals("android.provider.Telephony.SMS_RECEIVED")) {
	            //获取intent参数
	            Bundle bundle=intent.getExtras();
	            //判断bundle内容
	            if (bundle!=null)
	            {
	                //取pdus内容,转换为Object[]
	                Object[] pdus=(Object[])bundle.get("pdus");
	                //解析短信
	                SmsMessage[] messages = new SmsMessage[pdus.length];
	                for(int i=0;i<messages.length;i++)
	                {
	                    byte[] pdu=(byte[])pdus[i];
	                    messages[i]=SmsMessage.createFromPdu(pdu);
	                }    
	                //解析完内容后分析具体参数
	                for(SmsMessage msg:messages)
	                {
	                    //获取短信内容
	                    //String content=msg.getMessageBody();
	                    //String sender=msg.getOriginatingAddress();
	                    //Date date = new Date(msg.getTimestampMillis());
	                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    //String sendTime = sdf.format(date);
	                    //TODO:根据条件判断,然后进一般处理
	                    //if ("10060".equals(sender)) 
	                    //{
	                        // 屏蔽手机号为10060的短信，这里还可以时行一些处理，如把这个信息发送到第三人的手机等等。
	                        //TODO:测试
	                        //Toast.makeText(context, "收到10060的短信"+"内容:"+content, Toast.LENGTH_LONG).show();
	                        //对于特定的内容,取消广播
	                        abortBroadcast();
	                    //}
	                }
	                
	            }
	        }//if 判断广播消息结束
			else if (action.equals("android.intent.action.SEND")){
				//setResultData(null);
				//abortBroadcast();
			}
	    }	
		
} 

