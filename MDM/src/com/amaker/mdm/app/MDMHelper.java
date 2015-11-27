package com.amaker.mdm.app;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MDMHelper {
	
	public static Bitmap getBitmap(Context context ,int id){
		return BitmapFactory.decodeResource(context.getResources(), id);
	}
	
	public static String getLocalIpAddress() {     
	   try {     
	       for (Enumeration<NetworkInterface> en = NetworkInterface     
	               .getNetworkInterfaces(); en.hasMoreElements();) {     
	           NetworkInterface intf = en.nextElement();     
	           for (Enumeration<InetAddress> enumIpAddr = intf     
	                   .getInetAddresses(); enumIpAddr.hasMoreElements();) {     
	               InetAddress inetAddress = enumIpAddr.nextElement();     
	               if (!inetAddress.isLoopbackAddress()) {    
	                   return inetAddress.getHostAddress().toString();     
	               }     
	           }     
	       }     
	   } catch (SocketException ex) {     
	       Log.e("ifo", ex.toString());     
	   }     
	   return "";     
	}  
	/*
	SoapObject request = new SoapObject(serviceNamespace,methodName);
	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	//(new MarshalBase64()).register(envelope);
	envelope.bodyOut = request;
	//envelope.dotNet = true;
	envelope.setOutputSoapObject(request);
	
	HttpTransportSE ht = new HttpTransportSE(serviceUrl);
	ht.debug = true;
	
	try {
		ht.call("http://tempuri.org/HelloWorld",envelope);
		if (envelope.getResponse() != null) {
			SoapObject result = (SoapObject) envelope.bodyIn;
			parse(result);
		}
	} catch (IOException e) {
		
	} catch (XmlPullParserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
	/*
	String str = result.getProperty(methodName+"Result").toString();
	if (str != null && str.length() > 0)
	{
		int key = 0x10,value = 0x00;
		Log.i("MDM","MDMClient parse:"+str);
		if (str.equals("true"))
		{
			value = 0x01;
		}
		sendUpdateResultMessage2MDMService(key,value);
	}
	*/
}
