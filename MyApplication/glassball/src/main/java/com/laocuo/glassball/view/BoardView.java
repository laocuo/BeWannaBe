package com.laocuo.glassball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.laocuo.glassball.R;

/**
 * Created by Laocuo on 2016/4/27.
 */
public class BoardView extends View {
    private int width = 100, height = 20;
    private Paint mPaint;
    private RectF mRect;
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.darkblack));
        mRect = new RectF(0,0,width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawRoundRect(mRect, height/2, height/2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
        mRect.right = width;
        mRect.bottom = height;
        measure(width,height);
        invalidate();
    }
}
