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
	            //��ȡintent����
	            Bundle bundle=intent.getExtras();
	            //�ж�bundle����
	            if (bundle!=null)
	            {
	                //ȡpdus����,ת��ΪObject[]
	                Object[] pdus=(Object[])bundle.get("pdus");
	                //��������
	                SmsMessage[] messages = new SmsMessage[pdus.length];
	                for(int i=0;i<messages.length;i++)
	                {
	                    byte[] pdu=(byte[])pdus[i];
	                    messages[i]=SmsMessage.createFromPdu(pdu);
	                }    
	                //���������ݺ�����������
	                for(SmsMessage msg:messages)
	                {
	                    //��ȡ��������
	                    //String content=msg.getMessageBody();
	                    //String sender=msg.getOriginatingAddress();
	                    //Date date = new Date(msg.getTimestampMillis());
	                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    //String sendTime = sdf.format(date);
	                    //TODO:���������ж�,Ȼ���һ�㴦��
	                    //if ("10060".equals(sender)) 
	                    //{
	                        // �����ֻ���Ϊ10060�Ķ��ţ����ﻹ����ʱ��һЩ������������Ϣ���͵������˵��ֻ��ȵȡ�
	                        //TODO:����
	                        //Toast.makeText(context, "�յ�10060�Ķ���"+"����:"+content, Toast.LENGTH_LONG).show();
	                        //�����ض�������,ȡ���㲥
	                        abortBroadcast();
	                    //}
	                }
	                
	            }
	        }//if �жϹ㲥��Ϣ����
			else if (action.equals("android.intent.action.SEND")){
				//setResultData(null);
				//abortBroadcast();
			}
	    }	
		
} 

