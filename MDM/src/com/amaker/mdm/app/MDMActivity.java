package com.amaker.mdm.app;


import com.amaker.mdm.cp.Employees.Employee;
import com.amaker.mdm.client.MDMClient;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MDMActivity extends Activity {
	private Button start,stop;
	private Button tb1,tb2;
	private Button tb3,tb4;
	
	private static final String SERVICE_ACTION = "com.amaker.mdm.app.action.MDMService";
	
	private SharedPreferences.Editor editor;
	
	private final String AUTHORITY = "com.amaker.mdm.provider.Employees";
	private final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/employee");
	private final String DEFAULT_SORT_ORDER = "_id desc";// ��id����
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        start = (Button) findViewById(R.id.Button01);
        stop = (Button) findViewById(R.id.Button02);
        
        start.setOnClickListener(startclicklistener);
        stop.setOnClickListener(stopclicklistener);
        
        tb1 = (Button) findViewById(R.id.Button03);
        tb2 = (Button) findViewById(R.id.Button04);
        
        tb3 = (Button) findViewById(R.id.Button05);
        tb4 = (Button) findViewById(R.id.Button06);
        
        editor = getSharedPreferences("AUTO_RUN",MODE_WORLD_WRITEABLE).edit();
        
        tb1.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        	}
        });
        
        tb2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        	}
        });
        
        tb3.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        	}
        });
        
        tb4.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		/*
        		Handler handler = MDMClient.getClientHandler();
        		Message msg = new Message();
        		msg.what = MDMClient.TIME_30_UPDATE;
        		handler.sendMessage(msg);
        		*/
        	}
        });
    }
    
    private OnClickListener startclicklistener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setAction(SERVICE_ACTION);
			startService(intent);
			editor.putBoolean("AUTO_RUN_FLAG",true);
			editor.commit();
		}
    };
    
    private OnClickListener stopclicklistener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setAction(SERVICE_ACTION);
			stopService(intent);
			editor.putBoolean("AUTO_RUN_FLAG",false);
			editor.commit();
		}
    };
    
    private void del(long id){
    	// ɾ��IDΪ1�ļ�¼
    	Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, id);
    	// ���ContentResolver����ɾ��
    	getContentResolver().delete(uri, null, null);
    }
    
    private void delall(){
    	// ���ContentResolver����ɾ��
    	getContentResolver().delete(Employee.CONTENT_URI, null, null);
    }
    
    // ����
    private void update(long id,String name,String content){
    	// ����IDΪ1�ļ�¼
    	Uri uri = ContentUris.withAppendedId(Employee.CONTENT_URI, id);
    	ContentValues values = new ContentValues();
    	// ���Ա����Ϣ
    	values.put(Employee.NAME, name);
    	values.put(Employee.CONTENT, content);
    	// ���ContentResolver��������
		getContentResolver().update(uri, values, null, null);
    }
    
    // ��ѯ
    private void query(){
        ContentResolver cr = getContentResolver();
    	// ��ѯ������
 	    String[] PROJECTION = new String[] { 
 	    		Employee._ID, 		// 0
 			   Employee.NAME, 		// 1
 			  Employee.CONTENT, 	// 2
 	    };
 	// ��ѯ���б���¼��Ϣ
		Cursor c = cr.query(this.CONTENT_URI, PROJECTION, null,
				null, null);//this.DEFAULT_SORT_ORDER);
		// �ж��α��Ƿ�Ϊ��
		if (c.moveToFirst()) {
			// �����α�
			/*
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				// �������
				String name = c.getString(1);
				String gender = c.getString(2);
				// �����־
				Log.i("emp", i+"||"+name+":"+gender);
			}
			*/
			int i = c.getCount() - 1;
			int save_index = 1;
			while (i >= 0) {
				c.moveToPosition(i);
				// �������
				String name = c.getString(c.getColumnIndex(Employee.NAME));
				String content = c.getString(c.getColumnIndex(Employee.CONTENT));
				// �����־
				Log.i("emp", i+"||"+name+":"+content+save_index);
				save_index++;
				i--;
			}
		}
    }
    
    private int queryModuleIndex(String module){
        ContentResolver cr = getContentResolver();

 	    String[] PROJECTION = new String[] { 
 	    		Employee._ID, 		// 0
  			   Employee.NAME, 		// 1
  			  Employee.CONTENT, 	// 2
 	    };

		Cursor c = cr.query(this.CONTENT_URI, PROJECTION, null,
				null, this.DEFAULT_SORT_ORDER);

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
    
    // ����
    private void insert(String name,String content){
    	// ����Uri
    	Uri uri = Employee.CONTENT_URI;
    	// ʵ����ContentValues
    	ContentValues values = new ContentValues();
    	// ���Ա����Ϣ
    	values.put(Employee.NAME, name);
    	values.put(Employee.CONTENT, content);
    	// ���ContentResolver��������
    	getContentResolver().insert(uri, values);
    }
}
