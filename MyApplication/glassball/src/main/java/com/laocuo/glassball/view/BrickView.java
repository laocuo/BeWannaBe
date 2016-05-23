package com.laocuo.glassball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Laocuo on 2016/5/11.
 */
public class BrickView extends View {
    private int width, height;
    private Paint mPaint;
    private RectF mRect;
    private int mBrickIndex;
    private int mColor = Color.GRAY;

    public GlassGameView.Center getmCenter() {
        return mCenter;
    }

    private GlassGameView.Center mCenter;
    public int getBrickIndex() {
        return mBrickIndex;
    }

    public void setBrickIndex(int mBrickIndex) {
        this.mBrickIndex = mBrickIndex;
    }

    public BrickView(Context context, AttributeSet attrs, int w, int h, int color) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = color;
        mPaint.setColor(mColor);
        width = w;
        height = h;
        mRect = new RectF(0,0,width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawRect(mRect, mPaint);
        canvas.drawRoundRect(mRect, height/5, height/5, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    public void setCenter(GlassGameView.Center mC) {
        mCenter = mC;
    }
}
