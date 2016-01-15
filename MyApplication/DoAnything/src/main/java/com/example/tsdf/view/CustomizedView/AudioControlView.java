package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.tsdf.R;
import com.example.tsdf.utils.L;

/**
 * Created by administrator on 12/1/15.
 */
public class AudioControlView extends View {
    /**
     * 第一圈的颜色
     */
    private int mFirstColor;

    /**
     * 第二圈的颜色
     */
    private int mSecondColor;
    /**
     * 圈的宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int mCurrentCount = 3;

    /**
     * 中间的图片
     */
    private Bitmap mImage;
    /**
     * 每个块块间的间隙
     */
    private int mSplitSize;
    /**
     * 个数
     */
    private int mCount;

    private Rect mRect;

    /**
     * 根据需要画的个数以及间隙计算每个块块所占的比例*360
     */
    private float itemSize;

    private boolean mImageDisplay = false;

    public AudioControlView(Context context) {
        super(context);
    }

    public AudioControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, 0, 0);
        mFirstColor = a.getColor(R.styleable.CustomVolumControlBar_firstColor, Color.BLUE);
        mSecondColor = a.getColor(R.styleable.CustomVolumControlBar_secondColor, Color.RED);
        mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.CustomVolumControlBar_bg, 0));
//        mImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        mCircleWidth = a.getDimensionPixelSize(R.styleable.CustomVolumControlBar_circleWidth, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        mCount = a.getInt(R.styleable.CustomVolumControlBar_dotCount, 8);
        mSplitSize = a.getInt(R.styleable.CustomVolumControlBar_splitSize, 20);
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(mCircleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径
        /**
         * 画块块去
         */
        drawOval(canvas, centre, radius);

        if (mImageDisplay == false) {
            /**
             * 计算内切正方形的位置
             */
            int relRadius = radius - mCircleWidth / 2;// 获得内圆的半径
            /**
             * 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
             */
            mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
            /**
             * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
             */
            mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
            mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
            mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

            /**
             * 如果图片比较小，那么根据图片的尺寸放置到正中心
             */
            if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
                mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
                mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
                mRect.right = (int) (mRect.left + mImage.getWidth());
                mRect.bottom = (int) (mRect.top + mImage.getHeight());
            }
            mImageDisplay = true;
        }
        // 绘图
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     *
     * @param canvas
     * @param centre
     * @param radius
     */
    private void drawOval(Canvas canvas, int centre, int radius)
    {
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        mPaint.setColor(mFirstColor); // 设置圆环的颜色
        for (int i = 0; i < mCount; i++)
        {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }

        mPaint.setColor(mSecondColor); // 设置圆环的颜色
        for (int i = 0; i < mCurrentCount; i++)
        {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); // 根据进度画圆弧
        }
    }

    /**
     * 当前数量+1
     */
    public void up()
    {
        mCurrentCount = mCurrentCount >= mCount ? mCount : mCurrentCount+1;
        postInvalidate();
    }

    /**
     * 当前数量-1
     */
    public void down()
    {
        mCurrentCount = mCurrentCount <= 0 ? 0 : mCurrentCount - 1;
        postInvalidate();
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;

            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown)// 下滑
                {
                    down();
                } else
                {
                    up();
                }
                break;
        }

        return true;
    }
}
