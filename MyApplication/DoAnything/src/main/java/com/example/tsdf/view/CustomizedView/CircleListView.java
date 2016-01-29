package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.graphics.Bitmap;
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
    public interface OnCircleListItemListener {
        void OnCircleListItemClick(int pos);
    }
    private final int TEXT_ICON_GAP = 2;
    private final int PADDING = 4;
    private final int FAST_MOVE_VALUE = 6;
    private List<Integer> list = null;
    private List<Integer> iconList = null;
    private int width, height;
    private Paint mPaint;
    private int mCount;
    private float mRadius;
    private int mInitAngel = 0, mMoveAngel = 0;
    private float mSpiltAngel;
    private Context mContext;
    private int mBg_Color,mContentBg_Color,mContentBg_SelectColor,mText_Color;
    private OnCircleListItemListener mOnCircleListItemListener = null;

    public CircleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtils.sp2px(context, 12));
        mBg_Color = Color.argb(255,0,200,200);
        mContentBg_Color = Color.argb(255, 200, 200, 200);
        mContentBg_SelectColor = Color.argb(255, 200, 0, 200);
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
        ItemParams ip = new ItemParams();
        String title = mContext.getResources().getString(list.get(i));
        Drawable icon = mContext.getResources().getDrawable(iconList.get(i));
        BitmapDrawable bd = (BitmapDrawable) icon;
        Rect rect = new Rect();
        mPaint.getTextBounds(title, 0, title.length(), rect);
        int mwidth = Math.max(icon.getIntrinsicWidth(), rect.width()) + PADDING;
        int mheight = icon.getIntrinsicHeight() + rect.height() + TEXT_ICON_GAP;
        ip.title_x = x - mwidth / 2 + (mwidth - rect.width()) / 2;
        ip.title_y = y - mheight / 2 + rect.height();
        ip.icon_x = x - mwidth / 2 + (mwidth - icon.getIntrinsicWidth()) / 2;
        ip.icon_y = y - mheight / 2 + rect.height() + TEXT_ICON_GAP;
        rect.left = (int) (x - mwidth / 2);
        rect.top = (int) (y - mheight / 2);
        rect.right = (int) (x + mwidth / 2);
        rect.bottom = (int) (y + mheight / 2);
        ip.rect = rect;
        ip.title = title;
        ip.icon = bd.getBitmap();
        if (mClickItemIndex == i) {
            mPaint.setColor(mContentBg_SelectColor);
        } else {
            mPaint.setColor(mContentBg_Color);
        }
        canvas.drawRect(ip.rect.left, ip.rect.top, ip.rect.right, ip.rect.bottom, mPaint);
        mPaint.setColor(mText_Color);
        canvas.drawText(ip.title, ip.title_x, ip.title_y, mPaint);
        canvas.drawBitmap(ip.icon, ip.icon_x, ip.icon_y, mPaint);
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

    public void setOnCircleListItemListener (OnCircleListItemListener l){
        mOnCircleListItemListener = l;
    }

    private void init() {
        mCount = list.size();
        mSpiltAngel = 360/mCount;
    }

    private Point prev = new Point(), next = new Point(), center = new Point();
    private int mClickItemIndex = -1;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (isRotating == true) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                prev.setPosition(x, y);
                mClickItemIndex = calcWhichItemClick(prev);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                next.setPosition(x, y);

                handlerMoveEvent();
                prev.setPosition(x, y);
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mMoveAngel == 0){
                    int pos = mClickItemIndex;
                    mClickItemIndex = -1;
                    invalidate();
                    if (mOnCircleListItemListener != null) {
                        if (pos != -1) {
                            mOnCircleListItemListener.OnCircleListItemClick(pos);
                        }
                    }
                } else {
                    if (Math.abs(mMoveAngel) > FAST_MOVE_VALUE) {
                        isRotating = true;
                        post(mAutoRotateRunnable);
                    } else {
                        mMoveAngel = 0;
                    }
                }
                break;
            }
        }
        return true;
//        return super.dispatchTouchEvent(event);
    }

    private int calcWhichItemClick(Point prev) {
        for (int i=0;i<mCount;i++) {
            float hudu = (float) ((mInitAngel + i*mSpiltAngel) * 2*Math.PI/360);
            float x = mRadius + (float) (mRadius * 3/4 * Math.sin(hudu));
            float y = mRadius - (float) (mRadius * 3/4 * Math.cos(hudu));
            String title = mContext.getResources().getString(list.get(i));
            Drawable icon = mContext.getResources().getDrawable(iconList.get(i));
            Rect rect = new Rect();
            mPaint.getTextBounds(title, 0, title.length(), rect);
            int mwidth = Math.max(icon.getIntrinsicWidth(), rect.width());
            int mheight = icon.getIntrinsicHeight() + rect.height() + TEXT_ICON_GAP;
            rect.left = (int) (x - mwidth / 2);
            rect.top = (int) (y - mheight / 2);
            rect.right = (int) (x + mwidth / 2);
            rect.bottom = (int) (y + mheight / 2);
            if (prev.x < rect.right && prev.x > rect.left && prev.y < rect.bottom && prev.y > rect.top) {
                return i;
            }
        }
        return -1;
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
        int tempAngel = mInitAngel;

        if (next.x > mRadius) {
            if (mapY < next.y) {
            } else if (mapY > next.y) {
                angel = angel * -1;
            }
        } else {
            if (mapY < next.y) {
                angel = angel * -1;
            } else if (mapY > next.y) {
            }
        }
        tempAngel += angel;
        if (mapY == next.y || tempAngel == mInitAngel) {
            return;
        }
        mClickItemIndex = -1;
        mMoveAngel = (int) angel * 3;
        if (tempAngel >= 360) {
            tempAngel -= 360;
        }
        if (tempAngel < 0) {
            tempAngel += 360;
        }
        mInitAngel = tempAngel;
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

    private class ItemParams{
        Rect rect;
        String title;
        Bitmap icon;
        float title_x,title_y,icon_x,icon_y;
    }

    private boolean isRotating = false;
    private AutoRotateRunnable mAutoRotateRunnable = new AutoRotateRunnable();
    private class AutoRotateRunnable implements Runnable {

        @Override
        public void run() {
            if (mMoveAngel != 0) {
                mInitAngel += mMoveAngel;
                postInvalidate();
                mMoveAngel = (mMoveAngel / Math.abs(mMoveAngel)) * (Math.abs(mMoveAngel) - 1);
                if (mMoveAngel != 0) {
                    postDelayed(this, 30);
                }
            }
            if (mMoveAngel == 0) {
                isRotating = false;
            }
        }
    }
}
