package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tsdf.utils.DensityUtils;
import com.example.tsdf.utils.L;

import java.util.ArrayList;

/**
 * Created by administrator on 1/26/16.
 */
public class CircleListView extends View {
    public interface OnCircleListItemListener {
        void OnCircleListItemClick(int pos);
    }
    private boolean isRotateItem = true;
    private final int TEXT_ICON_GAP = 2;
    private final int PADDING = 4;
    private final int FAST_MOVE_VALUE = 6;
    private ArrayList<String> titleList = null;
    private ArrayList<Bitmap> iconList = null;
    private int width, height;
    private Paint mPaint;
    private int mCount;
    private float mRadius;
    private float mInitAngel = 0;
    private int mMoveAngel = 0;
    private float mSpiltAngel;
    private Context mContext;
    private int mBg_Color,mContentBg_Color,mContentBg_SelectColor,mText_Color;
    private OnCircleListItemListener mOnCircleListItemListener = null;
    private ItemParams mItemParams = new ItemParams();

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
        if (titleList != null && mCount > 0) {
            drawBackground(canvas);
            for(int i=0;i<mCount;i++) {
                drawItem(i, canvas);
            }
        } else {
            drawBackground(canvas);
        }
    }

    private void drawItem(int i, Canvas canvas) {
        float hudu = (float) ((mInitAngel + i*mSpiltAngel) * 2*Math.PI/360);
        float x = mRadius + (float) (mRadius * 3/4 * Math.sin(hudu));
        float y = mRadius - (float) (mRadius * 3/4 * Math.cos(hudu));
        String title = titleList.get(i);
        Bitmap icon = iconList.get(i);
        Rect rect = new Rect();
        mPaint.getTextBounds(title, 0, title.length(), rect);
        int mwidth = Math.max(icon.getWidth(), rect.width()) + PADDING;
        int mheight = icon.getHeight() + rect.height() + TEXT_ICON_GAP;
        mItemParams.title_x = x - mwidth / 2 + (mwidth - rect.width()) / 2;
        mItemParams.title_y = y - mheight / 2 + rect.height();
        mItemParams.icon_x = x - mwidth / 2 + (mwidth - icon.getWidth()) / 2;
        mItemParams.icon_y = y - mheight / 2 + rect.height() + TEXT_ICON_GAP;
        rect.left = (int) (x - mwidth / 2);
        rect.top = (int) (y - mheight / 2);
        rect.right = (int) (x + mwidth / 2);
        rect.bottom = (int) (y + mheight / 2);
        mItemParams.rect = rect;
        mItemParams.title = title;
        mItemParams.icon = icon;
        if (mClickItemIndex == i) {
            mPaint.setColor(mContentBg_SelectColor);
        } else {
            mPaint.setColor(mContentBg_Color);
        }
        canvas.save();
        if (isRotateItem == true) {
            canvas.rotate(mInitAngel + i * mSpiltAngel, x, y);
        }
        canvas.drawRect(mItemParams.rect.left, mItemParams.rect.top, mItemParams.rect.right, mItemParams.rect.bottom, mPaint);
        mPaint.setColor(mText_Color);
        canvas.drawText(mItemParams.title, mItemParams.title_x, mItemParams.title_y, mPaint);
        canvas.drawBitmap(mItemParams.icon, mItemParams.icon_x, mItemParams.icon_y, mPaint);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {
        mRadius = width / 2;
        mPaint.setColor(mBg_Color);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        mPaint.setColor(mContentBg_Color);
        canvas.drawCircle(mRadius, mRadius, mRadius / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width <= 0) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = width;
            center.setPosition(width/2, width/2);
        }
        setMeasuredDimension(width, height);
    }

    public void setListContent(ArrayList<String> titleList, ArrayList<Bitmap> iconList) {
        this.titleList = titleList;
        this.iconList = iconList;
        init();
    }

    public void setOnCircleListItemListener (OnCircleListItemListener l){
        mOnCircleListItemListener = l;
    }

    private void init() {
        mCount = titleList.size();
        mSpiltAngel = 360/mCount;
        invalidate();
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
                if (Math.abs(mMoveAngel) < 1) {
                    int pos = mClickItemIndex;
                    mClickItemIndex = -1;
                    invalidate();
                    if (mOnCircleListItemListener != null) {
                        if (pos != -1) {
                            mOnCircleListItemListener.OnCircleListItemClick(pos);
                        }
                    }
                }
                if (Math.abs(mMoveAngel) > FAST_MOVE_VALUE) {
                        isRotating = true;
                        post(mAutoRotateRunnable);
                } else {
                    mMoveAngel = 0;
                }
                break;
            }
        }
        return true;
    }

    private int calcWhichItemClick(Point prev) {
        for (int i=0;i<mCount;i++) {
            float hudu = (float) ((mInitAngel + i*mSpiltAngel) * 2*Math.PI/360);
            float x = (float) (mRadius * 3/4 * Math.sin(hudu));
            float y = -1 * (float) (mRadius * 3/4 * Math.cos(hudu));
            String title = titleList.get(i);
            Bitmap icon = iconList.get(i);
            Rect rect = new Rect();
            mPaint.getTextBounds(title, 0, title.length(), rect);
            int mwidth = Math.max(icon.getWidth(), rect.width());
            int mheight = icon.getHeight() + rect.height() + TEXT_ICON_GAP;
            rect.left = (int) (x - mwidth / 2);
            rect.top = (int) (y - mheight / 2);
            rect.right = (int) (x + mwidth / 2);
            rect.bottom = (int) (y + mheight / 2);
            Point p = new Point();
            p.setPosition(prev.x - center.x, prev.y - center.y);
            if (p.x < rect.right && p.x > rect.left && p.y < rect.bottom && p.y > rect.top) {
                return i;
            }
        }
        return -1;
    }

    private void handlerMoveEvent() {
        double a = calcDistance(prev, next);
        double b = calcDistance(center, prev);
        double c = calcDistance(center, next);
        double cosA = (b*b+c*c-a*a)/(2*b*c);
        double hudu = Math.acos(cosA);
        double angel = (hudu*360)/(2*Math.PI);
        //y=x*i+j
        float i = (prev.y-mRadius)/(prev.x-mRadius);
        float j = mRadius - mRadius*i;
        float mapY = next.x * i + j;
        float tempAngel = mInitAngel;

        if (next.x > mRadius) {
            if (mapY > next.y) {
                angel = angel * -1;
            }
        } else {
            if (mapY < next.y) {
                angel = angel * -1;
            }
        }
        tempAngel += angel;
        if (mapY == next.y || tempAngel == mInitAngel) {
            return;
        }
        mClickItemIndex = -1;
        mMoveAngel = (int) angel * 2;
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
