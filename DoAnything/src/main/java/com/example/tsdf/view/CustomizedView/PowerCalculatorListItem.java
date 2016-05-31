
package com.example.tsdf.view.CustomizedView;

import com.example.tsdf.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PowerCalculatorListItem extends LinearLayout {

    private ImageView mIv;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }

    public PowerCalculatorListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mIv = (ImageView) findViewById(R.id.list_image);
    }

    public void bindContent(int arg0) {
        mIv.setImageResource(arg0);
    }
}
