package com.example.doanything.view.CustomizedView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Laocuo on 2016/6/29.
 */
public class BezerLineView extends View {
    private Point start, start1, end1, end;
    private Paint mPaint;
    private boolean iscanDraw = false;
    public BezerLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (iscanDraw == true) {
            canvas.drawLine(start.x, start.y, start1.x, start1.y, mPaint);
            canvas.drawLine(end.x, end.y, end1.x, end1.y, mPaint);
            Path path=new Path();
            path.moveTo(start.x, start.y);
            path.cubicTo(start1.x, start1.y, end1.x, end1.y, end.x, end.y);
            canvas.drawPath(path, mPaint);
        }
    }

    public void setPoints(Point pstart, Point pstart1, Point pend1, Point pend) {
        start = pstart;
        start1 = pstart1;
        end1 = pend1;
        end = pend;
        iscanDraw = true;
        invalidate();
    }
}
