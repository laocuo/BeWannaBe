package com.example.doanything.view.CustomizedView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Laocuo on 2016/6/29.
 */
public class BezerPointView extends View {
    private int w = 100;
    private int smallRadius = 25;
    private Paint mPaint;

    public BezerPointView(Context context, int color) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    public BezerPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(w/2, w/2,smallRadius,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(w, w);
    }
}
