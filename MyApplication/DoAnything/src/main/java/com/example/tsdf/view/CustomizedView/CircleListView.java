package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tsdf.utils.DensityUtils;
import com.example.tsdf.utils.L;

import java.util.List;

/**
 * Created by administrator on 1/26/16.
 */
public class CircleListView extends View {
    private final int TEXT_ICON_GAP = 3;
    private List<Integer> list = null;
    private List<Integer> iconList = null;
    private int width, height;
    private Paint mPaint;
    private int mCount;
    private float mRadius;
    private int mInitAngel = 0;
    private float mSpiltAngel;
    private Context mContext;
    private int mBg_Color,mContentBg_Color,mText_Color;
    public CircleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtils.sp2px(context, 12));
        mBg_Color = Color.argb(255,0,200,200);
        mContentBg_Color = Color.argb(255, 200, 200, 200);
        mText_Color = Color.argb(255, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (list != null && mCount > 0) {
            drawBackground(canvas);
            for(int i=0;i<mCount;i++) {
                drawItem(i, canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private void drawItem(int i, Canvas canvas) {
        float hudu = (float) ((mInitAngel + i*mSpiltAngel) * 2*Math.PI/360);
        float x = mRadius + (float) (mRadius * 3/4 * Math.sin(hudu));
        float y = mRadius - (float) (mRadius * 3/4 * Math.cos(hudu));
        String title = mContext.getResources().getString(list.get(i));
        Drawable icon = mContext.getResources().getDrawable(iconList.get(i));
        BitmapDrawable bd = (BitmapDrawable) icon;
        Rect rect = new Rect();
        mPaint.getTextBounds(title, 0, title.length(), rect);
        int mwidth = Math.max(icon.getIntrinsicWidth(), rect.width());
        int mheight = icon.getIntrinsicHeight() + rect.height() + TEXT_ICON_GAP;
        mPaint.setColor(mContentBg_Color);
        canvas.drawRect(x - mwidth / 2, y - mheight / 2, x + mwidth / 2, y + mheight / 2, mPaint);
        mPaint.setColor(mText_Color);
        canvas.drawText(title, x - mwidth / 2 + (mwidth - rect.width()) / 2, y - mheight / 2 + rect.height(), mPaint);
        canvas.drawBitmap(bd.getBitmap(), x - mwidth / 2 + (mwidth - icon.getIntrinsicWidth()) / 2,
                y - mheight / 2 + rect.height() + TEXT_ICON_GAP, mPaint);
    }

    private void drawBackground(Canvas canvas) {
        mRadius = width/2;
        mPaint.setColor(mBg_Color);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        mPaint.setColor(mContentBg_Color);
        canvas.drawCircle(mRadius,mRadius,mRadius/2,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width <= 0) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = width;
        }
        setMeasuredDimension(width, height);
    }

    public void setListContent(List<Integer> list, List<Integer> iconList) {
        this.list = list;
        this.iconList = iconList;
        init();
        center.setPosition(width/2, width/2);
        invalidate();
    }

    private void init() {
        mCount = list.size();
        mSpiltAngel = 360/mCount;
    }

    private Point prev = new Point(), next = new Point(), center = new Point();
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                prev.setPosition(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                next.setPosition(x, y);
                handlerMoveEvent();
                prev.setPosition(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
//        return super.dispatchTouchEvent(event);
    }

    private void handlerMoveEvent() {
        double a = calcDistance(prev, next);
        double b = calcDistance(center, prev);
        double c = calcDistance(center, next);
        double cosA = (b*b+c*c-a*b)/(2*b*c);
        double hudu = Math.acos(cosA);
        double angel = (hudu*360)/(2*Math.PI);

        angel = angel / 2;//confuse me, to be continue...

        //y=x*i+j
        float i = (prev.y-mRadius)/(prev.x-mRadius);
        float j = mRadius - mRadius*i;
        float mapY = next.x * i + j;
        double tempAngel = mInitAngel;

        if (next.x > mRadius) {
            if (mapY < next.y) {
                tempAngel += angel;
            } else if (mapY > next.y) {
                tempAngel -= angel;
            }
        } else {
            if (mapY < next.y) {
                tempAngel -= angel;
            } else if (mapY > next.y) {
                tempAngel += angel;
            }
        }
        if (mapY == next.y || tempAngel == mInitAngel) {
            return;
        }
        if (tempAngel > 360) {
            tempAngel -= 360;
        }
        if (tempAngel < 0) {
            tempAngel += 360;
        }
        mInitAngel = (int) tempAngel;
        invalidate();
    }

    private double calcDistance(Point p1, Point p2) {
        float x = p1.x - p2.x;
        float y = p1.y - p2.y;
        return Math.hypot(x, y);
    }

    private class Point {
        float x=0f,y=0f;
        public Point() {
        }

        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
