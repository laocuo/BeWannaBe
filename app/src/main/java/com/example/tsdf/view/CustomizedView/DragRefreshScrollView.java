
package com.example.tsdf.view.CustomizedView;

import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class DragRefreshScrollView extends ScrollView {

    private LinearLayout mContainer;
    private LayoutInflater mInflater;
    private LinearLayout mHeadView;
    @SuppressWarnings("unused")
    private TextView mTipsTextView;
    @SuppressWarnings("unused")
    private TextView mLastUpdatedTextView;
    @SuppressWarnings("unused")
    private ImageView mArrowImageView;
    @SuppressWarnings("unused")
    private ProgressBar mProgressBar;
    private int mHeadContentWidth;
    private int mHeadContentHeight;
    private Handler mHandler;
    private final int EVENT_UP = 1;
    private final int EVENT_UP_DURATION = 50;
    private int mLastSmoothY = 0;

    public DragRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setVerticalScrollBarEnabled(false);
        mInflater = LayoutInflater.from(context);
    }

    public void setContainer(ViewGroup vp) {
        mContainer = (LinearLayout) vp;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case EVENT_UP:
                        if (mLastSmoothY == computeVerticalScrollOffset()) {
                            // smooth stop
                            if (computeVerticalScrollOffset() < mHeadContentHeight) {
                                displayStart();
                            }
                        } else {
                            mLastSmoothY = computeVerticalScrollOffset();
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_UP),
                                    EVENT_UP_DURATION);
                        }
                        break;
                }
            }
        };
        initHeadView();
    }

    private void initHeadView() {
        // TODO Auto-generated method stub
        mHeadView = (LinearLayout) mInflater.inflate(R.layout.dragrefresh_listview_head, this,
                false);
        mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
        mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
        mTipsTextView = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);
        mHeadContentHeight = mHeadView.getMeasuredHeight();
        LogUtils.print("mHeadContentHeight = " + mHeadContentHeight);
        mHeadContentWidth = mHeadView.getMeasuredWidth();
        LogUtils.print("mHeadContentWidth = " + mHeadContentWidth);

        LayoutParams lp = new LayoutParams(mHeadContentWidth, mHeadContentHeight);
        mContainer.addView(mHeadView, 0, lp);
    }

    private void measureView(View child) {
        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        }
        int parentWidth = MeasureSpec.makeMeasureSpec(LogUtils.getScreenW(), MeasureSpec.EXACTLY);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(parentWidth, 0 + 0, params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_UP), EVENT_UP_DURATION);
        }
        return super.onTouchEvent(ev);
    }

    public void displayStart() {
        // TODO Auto-generated method stub
        smoothScrollTo(0, mHeadContentHeight);
    }
}
