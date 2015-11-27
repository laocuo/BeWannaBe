package com.amaker.mdm.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.amaker.mdm.app.MDMService;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MDMClient extends Thread {
	/*MDMClient message start*/
	public static final int LOGIN = 0x100;
	public static final int TIME_30_UPDATE = 0x101;
	public static final int APP_LIST_UPDATE = 0x102;
	public static final int STOP = 0x200;
	public static final int TEST = 0x1ff;
	public static final int TEST1 = 0x1fe;
	/*MDMClient message end*/
	
	private String installApkUrl = "";
	
	//private String serviceNamespace = "http://tempuri.org/";
	private String serviceUrl = "http://10.20.72.25/MDM_Service.asmx/";
	//private String serviceUrl = "http://192.168.1.2/MDM_Service.asmx/";
	private String methodName = "SendXml";

	private static boolean mdmclient_run_flag = false;
	public static ClientHandler threadhandler;
	private Context mContext;

	private Handler mServicehandler;
	
	public MDMClient(Context applicationContext) {
		// TODO Auto-generated constructor stub
		mContext = applicationContext;
	}
	
	public static Handler getClientHandler() {
		return threadhandler;
	}
	
	public static boolean getClientRunningFlag() {
		return mdmclient_run_flag;
	}
	
	public void initClientHandler(Handler h) throws MalformedURLException {
		mServicehandler = h;
		mdmclient_run_flag = true;
	}
	
	private void DownloadApp(String ApkUrl) throws MalformedURLException, InterruptedException {
		// TODO Auto-generated method stub
		String FILE_NAME = ApkUrl.substring(ApkUrl.lastIndexOf("/")+1);
		String mSavePath = "";
		try  
        {  
            // 判断SD卡是否存在，并且是否具有读写权限  
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
                // 获得存储卡的路径  
                String sdpath = Environment.getExternalStorageDirectory() + "/";  
                mSavePath += sdpath;  
                mSavePath += "download";
                URL url = new URL(ApkUrl);  
                // 创建连接  
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                conn.connect();  
                // 获取文件大小  
                //int length = conn.getContentLength();  
                // 创建输入流  
                InputStream is = conn.getInputStream();  

                File file = new File(mSavePath);  
                // 判断文件目录是否存在  
                if (!file.exists()) {  
                    file.mkdir();  
                }  
                File apkFile = new File(mSavePath, FILE_NAME);  
                FileOutputStream fos = new FileOutputStream(apkFile);  
                int count = 0;  
                // 缓存  
                byte buf[] = new byte[1024];  
                // 写入到文件中  
                do  
                {  
                    int numread = is.read(buf);  
                    count += numread;  
                    if (numread <= 0) {  
                        break;  
                    }  
                    // 写入文件  
                    fos.write(buf, 0, numread);  
                } while (true);// 点击取消就停止下载.  
                fos.close();  
                is.close();  
                
                //this.wait(2000);
                
                install_app(mSavePath+"/"+FILE_NAME);
            }  
        } catch (MalformedURLException e)  
        {  
            e.printStackTrace();  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
	}

	@SuppressWarnings("unused")
	private void login_http() throws MalformedURLException {
		// TODO Auto-generated method stub
		String FILE_NAME = "baidu.html";
		String urlStr = "http://www.baidu.com";
		try
		{
			URL url = new URL(urlStr);
			Log.i("MDM","MDMClient HTTP connecting");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i("MDM","MDMClient HTTP_OK");
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				BufferedReader bufferReader = new BufferedReader(in);
				String readLine = null;
				while((readLine = bufferReader.readLine())!= null) {
					SaveFile(FILE_NAME,readLine.getBytes());
				}
				bufferReader.close();
			}
			else
			{
				Log.i("MDM","MDMClient HTTP ErrorCode:"+conn.getResponseCode());
			}
			conn.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void login_apache() throws MalformedURLException {
		// TODO Auto-generated method stub
		String FILE_NAME = "google.html";
		String urlStr = "http://www.google.com/m";
		//HttpPost request = new HttpPost(urlStr);
		HttpGet request = new HttpGet(urlStr);
		try
		{
			Log.i("MDM","MDMClient APACHE connecting");
			DefaultHttpClient hc = new DefaultHttpClient();
			HttpResponse response = hc.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.i("MDM","MDMClient APACHE_OK");
				HttpEntity entry = response.getEntity();
				InputStreamReader in = new InputStreamReader(entry.getContent());
				BufferedReader bufferReader = new BufferedReader(in);
				String readLine = null;
				while((readLine = bufferReader.readLine()) != null) {
					SaveFile(FILE_NAME,readLine.getBytes());
				}
				bufferReader.close();
			}
			else
			{
				Log.i("MDM","MDMClient APACHE ErrorCode:"+response.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void SaveFile(String filename, byte[] buffer) {
		// TODO Auto-generated method stub
		try
		{
			FileOutputStream fos = mContext.openFileOutput(filename,Context.MODE_APPEND);
			fos.write(buffer);
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	class ClientHandler extends Handler {
		public void handleMessage (Message msg)  {
			switch(msg.what){
			case LOGIN:
				Log.i("MDM","MDMClient recv msg.what = LOGIN");
				try {
					login();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case TIME_30_UPDATE:
				Log.i("MDM","MDMClient recv msg.what = TIME_30_UPDATE");
				try {
					update();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case APP_LIST_UPDATE:
				Log.i("MDM","MDMClient recv msg.what = APP_LIST_UPDATE");
				try {
					applist_update();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case STOP:
				mdmclient_run_flag = false;
				Log.i("MDM","MDMClient recv msg.what = STOP");
				Toast.makeText(mContext,"MDMClient STOP!",Toast.LENGTH_SHORT).show();
				super.handleMessage(msg);
				Looper.myLooper().quit();
				return;
			
			case TEST:
				try {
					DownloadApp("http://image.52rd.com/2012/File201210298504351952.jpg"); //test
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case TEST1:
				
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	public void run() {
		Looper.prepare();
		Log.i("MDM","MDMClient is running!");
		threadhandler = new ClientHandler();
		Looper.loop();
	}

	private void login() throws MalformedURLException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		//String username = "hoperun";
		//String password = "zhouhongwei";
		//String receviceContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		//receviceContent += "<root><DeviceId>2</DeviceId></root>";
		//TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String receviceContent = domCreateLoginXML();
		//String strRegister = "<xml><name>DeviceId:";
		//strRegister += tm.getDeviceId();
		//strRegister += "</name></xml>";
		
		String urlStr = serviceUrl+methodName;
		HttpPost request = new HttpPost(urlStr);
		
		//List<NameValuePair> params = new ArrayList<NameValuePair>();
		//params.add(new BasicNameValuePair("mdm_xml",username));
		//params.add(new BasicNameValuePair("mdm_xml2",password));
		
		//UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		//entity.setContentType("text/xml");
		
		try
		{
			Log.i("MDM","MDMClient login connecting");
			
			//request.setEntity(entity);
			
			//request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			request.setEntity(new ByteArrayEntity(receviceContent.getBytes()));
			//request.setEntity(new StringEntity(strRegister));

			DefaultHttpClient hc = new DefaultHttpClient();
			
			HttpResponse response = hc.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				
				HttpEntity entry = response.getEntity();
				/*
				InputStreamReader in = new InputStreamReader(entry.getContent());
				BufferedReader bufferReader = new BufferedReader(in);
				String readLine = null;
				String result = "";
				while((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				Log.i("MDM","MDMClient login:"+result);
				SaveFile("login_result.txt", result.getBytes());
				*/
				parse_login(entry.getContent());
				//bufferReader.close();
				Log.i("MDM","MDMClient login_OK");
			}
			else
			{
				Log.i("MDM","MDMClient APACHE ErrorCode:"+response.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void update() throws MalformedURLException {
		// TODO Auto-generated method stub
		String receviceContent = domCreateUpdateXML();
		
		String urlStr = serviceUrl+methodName;
		HttpPost request = new HttpPost(urlStr);
		
		try
		{
			Log.i("MDM","MDMClient update connecting");
			request.setEntity(new ByteArrayEntity(receviceContent.getBytes()));

			DefaultHttpClient hc = new DefaultHttpClient();
			
			HttpResponse response = hc.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				
				HttpEntity entry = response.getEntity();
				/*
				InputStreamReader in = new InputStreamReader(entry.getContent());
				BufferedReader bufferReader = new BufferedReader(in);
				String readLine = null;
				String result = "";
				while((readLine = bufferReader.readLine()) != null) {
					result += readLine;
				}
				Log.i("MDM","MDMClient update:"+result);
				*/
				//SaveFile("update_result.txt", result.getBytes());
				parse_update(entry.getContent());
				//bufferReader.close();
				Log.i("MDM","MDMClient update_OK");
			}
			else
			{
				Log.i("MDM","MDMClient APACHE ErrorCode:"+response.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void applist_update() throws MalformedURLException {
		// TODO Auto-generated method stub
		String receviceContent = domCreateAppListUpdateXML();
		
		String urlStr = serviceUrl+methodName;
		HttpPost request = new HttpPost(urlStr);
		
		try
		{
			Log.i("MDM","MDMClient applist_update connecting");
			request.setEntity(new ByteArrayEntity(receviceContent.getBytes()));

			DefaultHttpClient hc = new DefaultHttpClient();
			
			HttpResponse response = hc.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.i("MDM","MDMClient applist_update_OK");
				//HttpEntity entry = response.getEntity();
				//parse_update(entry.getContent());
			}
			else
			{
				Log.i("MDM","MDMClient APACHE ErrorCode:"+response.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	private void parse_login(InputStream result) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> key_list = new ArrayList<String>();
		ArrayList<String> str_list = new ArrayList<String>();
	      
	    InputStream is = result;  
	    try {  
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        Document doc = builder.parse(is);  
	  
	        doc.getDocumentElement().normalize();  
	        NodeList nlRoot = doc.getElementsByTagName("plist");  
	        Element eleRoot = (Element)nlRoot.item(0);  
	        //String version = eleRoot.getAttribute("version");  
	          
	        NodeList nlDict = eleRoot.getElementsByTagName("dict");  
	        int personsLen = nlDict.getLength();    
	        for(int i=0; i<personsLen; i++) {  
	            Element elePerson = (Element) nlDict.item(i);     // person节点  
	              
	            NodeList nlKey = elePerson.getElementsByTagName("key");
	            for (int k=0; k<nlKey.getLength(); k++) {
	            	Element eleKey = (Element)nlKey.item(k); 
	            	Log.i("MDM","MDMClient key:"+eleKey.getChildNodes().item(0).getNodeValue());
	            	key_list.add(eleKey.getChildNodes().item(0).getNodeValue());  
	            }
	              
	            NodeList nlStr = elePerson.getElementsByTagName("string");  
	            for (int k=0; k<nlStr.getLength(); k++) {
	            	Element eleStr = (Element)nlStr.item(k); 
	            	Log.i("MDM","MDMClient string:"+eleStr.getChildNodes().item(0).getNodeValue());
	            	str_list.add(eleStr.getChildNodes().item(0).getNodeValue());  
	            }
	            
	            if (nlKey.getLength() == nlStr.getLength()) {
		            for (int k=0; k<nlKey.getLength(); k++) {
		            	if (key_list.get(k).compareTo("Result") == 0) {
		            		if (str_list.get(k).compareTo("Success") == 0) {
		    	            	sendLoginResultMessage2MDMService(1);
		    	    	    }
		            		else {
		            			sendLoginResultMessage2MDMService(0);
		            		}
		            	}
		            }
	            }
	        }  
	          
	    } catch (ParserConfigurationException e) {      // factory.newDocumentBuilder  
	        e.printStackTrace();  
	    } catch (IOException e) {       // builder.parse  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }   
	}

	private void parse_update(InputStream result) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<String> key_list = new ArrayList<String>();
		ArrayList<String> str_list = new ArrayList<String>();
	      
	    InputStream is = result;  
	    try {  
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        Document doc = builder.parse(is);  
	        
	        doc.getDocumentElement().normalize();  
	        NodeList nlRoot = doc.getElementsByTagName("plist");  
	        Element eleRoot = (Element)nlRoot.item(0);  
	        //String version = eleRoot.getAttribute("version");  
	        
	        NodeList nlDict = eleRoot.getElementsByTagName("dict");  
	        for(int i=0; i<nlDict.getLength(); i++) {  
	            Element elePerson = (Element) nlDict.item(i);     // person节点  
	              
	            NodeList nlKey = elePerson.getElementsByTagName("key");
	            for (int k=0; k<nlKey.getLength(); k++) {
	            	Element eleKey = (Element)nlKey.item(k); 
	            	Log.i("MDM","MDMClient key:"+eleKey.getChildNodes().item(0).getNodeValue());
	            	key_list.add(eleKey.getChildNodes().item(0).getNodeValue());  
	            }
	            
	            NodeList nlStr = elePerson.getElementsByTagName("string");  
	            for (int k=0; k<nlStr.getLength(); k++) {
	            	Element eleStr = (Element)nlStr.item(k); 
	            	Log.i("MDM","MDMClient string:"+eleStr.getChildNodes().item(0).getNodeValue());
	            	str_list.add(eleStr.getChildNodes().item(0).getNodeValue());  
	            }
	            
	            String deviceID = "";
	            if (nlKey.getLength() == nlStr.getLength()) {
		            for (int k=0; k<nlKey.getLength(); k++) {
		            	if (key_list.get(k).compareTo("ScreenLock") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x10,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x10,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Call") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x11,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x11,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Message") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x12,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x12,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Camera") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x13,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x13,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("BlueTooth") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x14,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x14,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("USB") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x15,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x15,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("WebBroswer") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x16,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x16,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Setting") == 0) {
		            		if (str_list.get(k).compareTo("True") == 0) {
		            			sendUpdateResultMessage2MDMService(0x17,0x01);
		    	    	    }
		            		else {
		            			sendUpdateResultMessage2MDMService(0x17,0x00);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Profile") == 0) {
		            		if (str_list.get(k).compareTo("Silent") == 0) {
		            			sendUpdateResultMessage2MDMService(0x20,0x01);
		            		}
		            		else if (str_list.get(k).compareTo("General") == 0) {
		            			sendUpdateResultMessage2MDMService(0x20,0x02);
		            		}
		            		else if (str_list.get(k).compareTo("Vibrate") == 0) {
		            			sendUpdateResultMessage2MDMService(0x20,0x03);
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("UDID") == 0) {
		            		deviceID += str_list.get(k);
		            	}
		            	else if (key_list.get(k).compareTo("Uninstall") == 0) {
		            		//判断设备ID是否是当前设备
		            		if (true == CheckUDID(deviceID)) {
			            		if (true == queryAppIsExist(str_list.get(k))) {
			            			remove_app(str_list.get(k));
			            		}
		            		}
		            	}
		            	else if (key_list.get(k).compareTo("Install") == 0) {
		            		//判断设备ID是否是当前设备
		            		if (true == CheckUDID(deviceID)) {
		            			//URL地址相同则不下载安装
		            			//if (str_list.get(k).compareTo(installApkUrl) != 0)
		            			{
		            				installApkUrl = "";
			            			installApkUrl = str_list.get(k);
			            			DownloadApp(installApkUrl);
		            			}
		            		}
		            	}
		            }
	            }
	        }  
	    } catch (ParserConfigurationException e) {      // factory.newDocumentBuilder  
	        e.printStackTrace();  
	    } catch (IOException e) {       // builder.parse  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }   
	}

	private void sendLoginResultMessage2MDMService(int b) {
		// TODO Auto-generated method stub
		Message m = mServicehandler.obtainMessage();
 		m.what = MDMService.LOGIN_RESULT;
 		m.arg1 = b;
 		mServicehandler.sendMessage(m);
	}
	
	private void sendUpdateResultMessage2MDMService(int key,int value) {
		// TODO Auto-generated method stub
		Message m = mServicehandler.obtainMessage();
 		m.what = MDMService.UPDATE_RESULT;
 		m.arg1 = key;
 		m.arg2 = value;
 		mServicehandler.sendMessage(m);
	}
	
	/** Dom方式，创建 XML  */  
	private String domCreateLoginXML() {  
	    String xmlWriter = null;  
	    TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	    
	    try {  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        org.w3c.dom.Document doc = builder.newDocument();  
	        
	        org.w3c.dom.Element eleRoot = doc.createElement("plist");  
	        eleRoot.setAttribute("version", "1.0");  
	        doc.appendChild(eleRoot);  
	        
	        int personsLen = 1;//persons.length;
	        for(int i=0; i<personsLen; i++) {  
	            org.w3c.dom.Element elePerson = doc.createElement("dict");  
	            eleRoot.appendChild(elePerson);  
	            
	            org.w3c.dom.Element eleId = doc.createElement("key");  
	            org.w3c.dom.Node nodeId = doc.createTextNode("MessageType");  
	            eleId.appendChild(nodeId);  
	            elePerson.appendChild(eleId);  
	  
	            org.w3c.dom.Element eleId_string = doc.createElement("string");  
	            org.w3c.dom.Node nodeId_string = doc.createTextNode("Authenticate");  
	            eleId_string.appendChild(nodeId_string);  
	            elePerson.appendChild(eleId_string); 
	            
	            org.w3c.dom.Element deviceId = doc.createElement("key");  
	            org.w3c.dom.Node devicenodeId = doc.createTextNode("UDID");  
	            deviceId.appendChild(devicenodeId);  
	            elePerson.appendChild(deviceId);  
	  
	            org.w3c.dom.Element deviceId_string = doc.createElement("string");  
	            org.w3c.dom.Node devicenodeId_string = doc.createTextNode("PC10451");//(tm.getDeviceId());  
	            deviceId_string.appendChild(devicenodeId_string);  
	            elePerson.appendChild(deviceId_string); 
	        }  
	          
	        Properties properties = new Properties();  
	        properties.setProperty(OutputKeys.INDENT, "yes");  
	        properties.setProperty(OutputKeys.MEDIA_TYPE, "xml");  
	        properties.setProperty(OutputKeys.VERSION, "1.0");  
	        properties.setProperty(OutputKeys.ENCODING, "utf-8");  
	        properties.setProperty(OutputKeys.METHOD, "xml");  
	        properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");  
	          
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();  
	        Transformer transformer = transformerFactory.newTransformer();  
	        transformer.setOutputProperties(properties);  
	          
	        DOMSource domSource = new DOMSource(doc.getDocumentElement());  
	        OutputStream output = new ByteArrayOutputStream();  
	        StreamResult result = new StreamResult(output);  
	        transformer.transform(domSource, result);  
	          
	        xmlWriter = output.toString();  
	          
	    } catch (ParserConfigurationException e) {      // factory.newDocumentBuilder  
	        e.printStackTrace();  
	    } catch (DOMException e) {                      // doc.createElement  
	        e.printStackTrace();  
	    } catch (TransformerFactoryConfigurationError e) {      // TransformerFactory.newInstance  
	        e.printStackTrace();  
	    } catch (TransformerConfigurationException e) {     // transformerFactory.newTransformer  
	        e.printStackTrace();  
	    } catch (TransformerException e) {              // transformer.transform  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }
	      
	    return xmlWriter.toString();  
	}  
	
	private String domCreateUpdateXML() {  
	    String xmlWriter = null;  
	    TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	    
	    try {  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        org.w3c.dom.Document doc = builder.newDocument();  
	        
	        org.w3c.dom.Element eleRoot = doc.createElement("plist");  
	        eleRoot.setAttribute("version", "1.0");  
	        doc.appendChild(eleRoot);  
	        
	        int personsLen = 1;//persons.length;
	        for(int i=0; i<personsLen; i++) {  
	            org.w3c.dom.Element elePerson = doc.createElement("dict");  
	            eleRoot.appendChild(elePerson);  
	            
	            org.w3c.dom.Element eleId = doc.createElement("key");  
	            org.w3c.dom.Node nodeId = doc.createTextNode("MessageType");  
	            eleId.appendChild(nodeId);  
	            elePerson.appendChild(eleId);  
	  
	            org.w3c.dom.Element eleId_string = doc.createElement("string");  
	            org.w3c.dom.Node nodeId_string = doc.createTextNode("TokenUpdate");  
	            eleId_string.appendChild(nodeId_string);  
	            elePerson.appendChild(eleId_string); 
	            
	            org.w3c.dom.Element deviceId = doc.createElement("key");  
	            org.w3c.dom.Node devicenodeId = doc.createTextNode("UDID");  
	            deviceId.appendChild(devicenodeId);  
	            elePerson.appendChild(deviceId);  
	  
	            org.w3c.dom.Element deviceId_string = doc.createElement("string");  
	            org.w3c.dom.Node devicenodeId_string = doc.createTextNode("PC10451");//(tm.getDeviceId());  
	            deviceId_string.appendChild(devicenodeId_string);  
	            elePerson.appendChild(deviceId_string); 
	            
	            List<String> applist = get_appinfo();
	            for (int j=0; j<applist.size(); j++) {
	            	org.w3c.dom.Element packageId = doc.createElement("key");  
		            org.w3c.dom.Node packagenodeId = doc.createTextNode("PackageName");  
		            packageId.appendChild(packagenodeId);  
		            elePerson.appendChild(packageId);  
		  
		            org.w3c.dom.Element packageId_string = doc.createElement("string");  
		            org.w3c.dom.Node packagenodeId_string = doc.createTextNode(applist.get(j).toString());  
		            packageId_string.appendChild(packagenodeId_string);  
		            elePerson.appendChild(packageId_string);
	            }
	            /*
	            List<String> applist = get_appinfo();
	            String app = "";
	            if (applist.size() > 0) {
	            	app += applist.get(0).toString();
		            for (int j=1; j<applist.size(); j++) {
		            	app += "$";
		            	app += applist.get(j).toString();
		            }
	            	org.w3c.dom.Element packageId = doc.createElement("key");  
		            org.w3c.dom.Node packagenodeId = doc.createTextNode("App");  
		            packageId.appendChild(packagenodeId);  
		            elePerson.appendChild(packageId);  
		  
		            org.w3c.dom.Element packageId_string = doc.createElement("string");  
		            org.w3c.dom.Node packagenodeId_string = doc.createTextNode(app);  
		            packageId_string.appendChild(packagenodeId_string);  
		            elePerson.appendChild(packageId_string);
	            }
	            */
	        }  
	          
	        Properties properties = new Properties();  
	        properties.setProperty(OutputKeys.INDENT, "yes");  
	        properties.setProperty(OutputKeys.MEDIA_TYPE, "xml");  
	        properties.setProperty(OutputKeys.VERSION, "1.0");  
	        properties.setProperty(OutputKeys.ENCODING, "utf-8");  
	        properties.setProperty(OutputKeys.METHOD, "xml");  
	        properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");  
	          
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();  
	        Transformer transformer = transformerFactory.newTransformer();  
	        transformer.setOutputProperties(properties);  
	          
	        DOMSource domSource = new DOMSource(doc.getDocumentElement());  
	        OutputStream output = new ByteArrayOutputStream();  
	        StreamResult result = new StreamResult(output);  
	        transformer.transform(domSource, result);  
	          
	        xmlWriter = output.toString();  
	          
	    } catch (ParserConfigurationException e) {      // factory.newDocumentBuilder  
	        e.printStackTrace();  
	    } catch (DOMException e) {                      // doc.createElement  
	        e.printStackTrace();  
	    } catch (TransformerFactoryConfigurationError e) {      // TransformerFactory.newInstance  
	        e.printStackTrace();  
	    } catch (TransformerConfigurationException e) {     // transformerFactory.newTransformer  
	        e.printStackTrace();  
	    } catch (TransformerException e) {              // transformer.transform  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }
	      
	    return xmlWriter.toString();  
	}
	
	private String domCreateAppListUpdateXML() {  
	    String xmlWriter = null;  
	    TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	    
	    try {  
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	        DocumentBuilder builder = factory.newDocumentBuilder();  
	        org.w3c.dom.Document doc = builder.newDocument();  
	        
	        org.w3c.dom.Element eleRoot = doc.createElement("plist");  
	        eleRoot.setAttribute("version", "1.0");  
	        doc.appendChild(eleRoot);  
	        
	        int personsLen = 1;//persons.length;
	        for(int i=0; i<personsLen; i++) {  
	            org.w3c.dom.Element elePerson = doc.createElement("dict");  
	            eleRoot.appendChild(elePerson);  
	            
	            org.w3c.dom.Element eleId = doc.createElement("key");  
	            org.w3c.dom.Node nodeId = doc.createTextNode("MessageType");  
	            eleId.appendChild(nodeId);  
	            elePerson.appendChild(eleId);
	  
	            org.w3c.dom.Element eleId_string = doc.createElement("string");  
	            org.w3c.dom.Node nodeId_string = doc.createTextNode("AppListUpdate");  
	            eleId_string.appendChild(nodeId_string);  
	            elePerson.appendChild(eleId_string); 
	            
	            org.w3c.dom.Element deviceId = doc.createElement("key");  
	            org.w3c.dom.Node devicenodeId = doc.createTextNode("UDID");  
	            deviceId.appendChild(devicenodeId);  
	            elePerson.appendChild(deviceId);  
	  
	            org.w3c.dom.Element deviceId_string = doc.createElement("string");  
	            org.w3c.dom.Node devicenodeId_string = doc.createTextNode(tm.getDeviceId());  
	            deviceId_string.appendChild(devicenodeId_string);  
	            elePerson.appendChild(deviceId_string);
	            
	            List<String> applist = get_appinfo();
	            for (int j=0; j<applist.size(); j++) {
	            	org.w3c.dom.Element packageId = doc.createElement("key");  
		            org.w3c.dom.Node packagenodeId = doc.createTextNode("PackageName");  
		            packageId.appendChild(packagenodeId);  
		            elePerson.appendChild(packageId);  
		  
		            org.w3c.dom.Element packageId_string = doc.createElement("string");  
		            org.w3c.dom.Node packagenodeId_string = doc.createTextNode(applist.get(j).toString());  
		            packageId_string.appendChild(packagenodeId_string);  
		            elePerson.appendChild(packageId_string);
	            }
	        }
	          
	        Properties properties = new Properties();  
	        properties.setProperty(OutputKeys.INDENT, "yes");  
	        properties.setProperty(OutputKeys.MEDIA_TYPE, "xml");  
	        properties.setProperty(OutputKeys.VERSION, "1.0");  
	        properties.setProperty(OutputKeys.ENCODING, "utf-8");  
	        properties.setProperty(OutputKeys.METHOD, "xml");  
	        properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");  
	          
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();  
	        Transformer transformer = transformerFactory.newTransformer();  
	        transformer.setOutputProperties(properties);  
	          
	        DOMSource domSource = new DOMSource(doc.getDocumentElement());  
	        OutputStream output = new ByteArrayOutputStream();  
	        StreamResult result = new StreamResult(output);  
	        transformer.transform(domSource, result);  
	          
	        xmlWriter = output.toString();  
	          
	    } catch (ParserConfigurationException e) {      // factory.newDocumentBuilder  
	        e.printStackTrace();  
	    } catch (DOMException e) {                      // doc.createElement  
	        e.printStackTrace();  
	    } catch (TransformerFactoryConfigurationError e) {      // TransformerFactory.newInstance  
	        e.printStackTrace();  
	    } catch (TransformerConfigurationException e) {     // transformerFactory.newTransformer  
	        e.printStackTrace();  
	    } catch (TransformerException e) {              // transformer.transform  
	        e.printStackTrace();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }
	      
	    return xmlWriter.toString();  
	}
	
	private List<String> get_appinfo()
    {
    	List<PackageInfo> packageInfo;
    	List<String> userPackageInfo;
    	
		packageInfo = mContext.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES  | PackageManager.GET_ACTIVITIES);
		userPackageInfo = new ArrayList<String>();
		for (int i =0;i<packageInfo.size();i++){
			PackageInfo temp = packageInfo.get(i);
			ApplicationInfo appInfo = temp.applicationInfo;
			boolean flag = false;
			//if((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
				// 表示是系统程序，但用户更新过，也算是用户安装的程序
				//flag = true;
			//}
			//else 
			if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==0){
				// 一定是用户安装的程序
				flag = true;
			}
			if(flag){
				userPackageInfo.add(temp.packageName);
			}
		}
		return userPackageInfo;
    }
	
	private void remove_app(String packageName)
    {
		Uri packagUri = Uri.parse("package:"  + packageName);
		Intent uninstallIntent = new Intent();
		uninstallIntent.setAction(Intent.ACTION_DELETE);
		uninstallIntent.setData(packagUri);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(uninstallIntent);
    }
    
	private void install_app(String filePath)
    {
		if (filePath.endsWith(".apk") == true) {
	    	Intent intent = new Intent(Intent.ACTION_VIEW);
	    	intent.setDataAndType(Uri.parse("file://" + filePath),"application/vnd.android.package-archive");
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	mContext.startActivity(intent);
		} 	
    }
	
	private boolean queryAppIsExist(String app) {
		// TODO Auto-generated method stub
		boolean isExist = false;
		List<String> applist = get_appinfo();
		for (int j=0; j<applist.size(); j++) {
            if (applist.get(j).toString().compareTo(app) == 0) {
            	isExist = true;
            	break;
            }
        }		
		return isExist;
	}
	
	private boolean CheckUDID(String UDID) {
		boolean isCompare = false;
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		
		if (tm.getDeviceId().compareTo(UDID) == 0) {
			isCompare = true;
		}
		return isCompare;
	}
}

