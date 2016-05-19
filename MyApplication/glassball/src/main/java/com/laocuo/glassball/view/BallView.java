package com.laocuo.glassball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.laocuo.glassball.R;

/**
 * Created by Laocuo on 2016/4/27.
 */
public class BallView extends View {
    private int mRaidus = 40;
    private Paint mPaint;

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.gold));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawCircle(mRaidus, mRaidus, mRaidus, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRaidus*2,mRaidus*2);
    }

    public void setRaidus(int r) {
        mRaidus = r;
        measure(mRaidus*2,mRaidus*2);
        invalidate();
    }
}
