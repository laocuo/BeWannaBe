package com.amaker.mdm.lock;

import com.amaker.mdm.app.MDMHelper;
import com.amaker.mdm.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MDMLockView extends View implements Runnable{
	public static final int UPDATE = 0x100;
	
	public static boolean unLock_flag = true;
	
	private Context mContext = null;
	private TextView tv;
	private ImageView scorll,end;
	private Bitmap dragBitmap = null;
	private int mLastMoveX = 1000;
	private int mStartMoveX = 0;
	private boolean isHit = false;
	private int scorllmaxdistence = 0;
	private Handler mainHandler = null;
	private RefreshHandler mRefreshHandler;
	
	public MDMLockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public MDMLockView(Context context,AttributeSet attr) {
		super(context,attr);
		// TODO Auto-generated constructor stub
		mContext = context;
		//initLockScreenLayout();
	}
	
	public void initLockScreenLayout()
	{
		if (null == dragBitmap)
		{
			dragBitmap = MDMHelper.getBitmap(mContext,R.drawable.scroll);
		}
		scorll.setVisibility(View.INVISIBLE);
		//invalidate();
		resetViewState();
		Log.i("MDM", "SCREEN LOCK!");
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return handleActionDownEvenet(event);

		case MotionEvent.ACTION_MOVE:
			mLastMoveX = x;
			invalidate();
			return true;
	                        
		case MotionEvent.ACTION_UP:
			return handleActionUpEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	private boolean handleActionUpEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean isSucess = false;
		
		if (mLastMoveX >= mStartMoveX + scorllmaxdistence)
		{
			isSucess = true;
		}
		if(isSucess && unLock_flag){
			mainHandler.obtainMessage(MDMLockActivity.MSG_UNLOCK_SUCESS).sendToTarget();
		}
		else
		{
			//mLastMoveX = x;
	        int distance = mLastMoveX - mStartMoveX;

	        if(distance > 0)
	        {
	            //mHandler.postDelayed(BackDragImgTask, BACK_DURATION);
	        	mRefreshHandler = new RefreshHandler();
	        	new Thread(this).start();
	        }
	        else 
	        {
	            resetViewState();
	        }
		}
		return true;
	}

	private void resetViewState() {
		// TODO Auto-generated method stub
		mLastMoveX = 0;
		mStartMoveX = 0;
		//scorll.setVisibility(View.VISIBLE);
		isHit = false;
        invalidate();
	}

	private boolean handleActionDownEvenet(MotionEvent event) {
		// TODO Auto-generated method stub
		Rect rect = new Rect();
		scorll.getHitRect(rect);
		isHit = rect.contains((int) event.getX(), (int) event.getY());
		if(isHit) {
			//scorll.setVisibility(View.INVISIBLE);
			mStartMoveX = (int) event.getX();
			mLastMoveX = mStartMoveX;
		}
		return isHit;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//if (isHit == true)
		{
			invalidateDragImg(canvas);
		}
	}
	
	private void invalidateDragImg(Canvas canvas) {
		// TODO Auto-generated method stub
		int drawXCor,drawYCor;
		Paint p = new Paint();
		
		if (scorllmaxdistence == 0) {
			scorllmaxdistence = end.getLeft() - scorll.getRight();
		}
		if (mLastMoveX < mStartMoveX)
		{
			mLastMoveX = mStartMoveX;
		}
		else if (mLastMoveX > mStartMoveX + scorllmaxdistence)
		{
			mLastMoveX = mStartMoveX + scorllmaxdistence;
		}
		drawXCor = scorll.getLeft() + mLastMoveX - mStartMoveX;
        drawYCor = scorll.getTop();
        canvas.drawBitmap(dragBitmap,  drawXCor, drawYCor , p);
	}

	public void setTextView(TextView findViewById) {
		// TODO Auto-generated method stub
		tv = findViewById;
		tv.setTextColor(Color.RED);
	}

	public void setScreenSize(int screenWidth, int screenHeight) {
	}

	public void setImageView(ImageView start,ImageView scroll, ImageView end) {
		// TODO Auto-generated method stub
		//this.start = start;
		this.scorll = scroll;
		this.end = end;
	}
	
	 private static int BACK_DURATION = 20 ;   // 20ms

	 private static float VE_HORIZONTAL = 0.7f ;  //0.1dip/ms
	  
	public void run() {
		while (!(Thread.currentThread().isInterrupted())) {
			mLastMoveX = mLastMoveX - (int)(BACK_DURATION * VE_HORIZONTAL);
			boolean shouldEnd = Math.abs(mLastMoveX - mStartMoveX) <= 8; 
			if (!shouldEnd)
			{
				Message m = new Message();
				m.what = UPDATE;
				mRefreshHandler.sendMessage(m);

				try {
					Thread.sleep(BACK_DURATION);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				//scorll.setVisibility(View.VISIBLE);
				isHit = false;
				Message m = new Message();
				m.what = UPDATE;
				mRefreshHandler.sendMessage(m);
				break;
			}
		}
	}
	
	class RefreshHandler extends Handler {
		public void handleMessage (Message msg)  {
			if (UPDATE == msg.what) {
				invalidate();
			}
    		super.handleMessage(msg);
		}
	}
	
	public void setMainHandler(Handler handler){
		mainHandler = handler;//activity所在的Handler对象
	}
}
