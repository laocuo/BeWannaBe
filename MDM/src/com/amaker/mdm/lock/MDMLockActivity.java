package com.amaker.mdm.lock;

import com.amaker.mdm.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MDMLockActivity extends Activity {
	public static final int MSG_UNLOCK_SUCESS = 0x01;
	
	public static boolean lock_status = false;
	//private Button unlock;
	private MDMLockView lockview;
	private ImageView start,scroll,end;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.lock);
        
        lock_status = true;
        
        lockview = (MDMLockView)findViewById(R.id.lockview);
        
        start = (ImageView) findViewById(R.id.StartImage);
        scroll = (ImageView) findViewById(R.id.ScrolImage);
        end = (ImageView) findViewById(R.id.EndImage);
        
        Handler handler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                switch (msg.what) {  
                case MSG_UNLOCK_SUCESS:  
                	Log.i("MDM", "SCREEN UNLOCK!");
                    finish();
                    break;  
                default:  
                    break;  
                }  
                super.handleMessage(msg);  
            }  
        };  
        
        lockview.setMainHandler(handler);
        lockview.setTextView((TextView) findViewById(R.id.locktext));
        lockview.setImageView(start,scroll,end);
		lockview.initLockScreenLayout();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return false;
    }
    
    public void onAttachedToWindow() {
	  getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	  super.onAttachedToWindow();
    }
    
    public void onDestroy() {
    	lock_status = false;
    	super.onDestroy();
    }
}
