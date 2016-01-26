package com.example.tsdf.view.CustomizedView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.tsdf.R;


/**
 * Created by administrator on 1/21/16.
 */
public class FolderLayout extends ViewGroup {
    private final int FOLDER_COUNT = 8;
    private float folder_scale = 1.0f;
    private boolean isFirst = true;
    private int width,height;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = new Canvas();
    private Matrix mMatrix[] = new Matrix[FOLDER_COUNT];
    private Paint mShadowPaint, mSolidPaint;
    private Matrix mShadowGradientMatrix;
    private LinearGradient mShadowGradientShader;
    private float singleWidth, singleRealWidth, depth;
//    private GestureDetector mScrollGestureDetector;
    private int mTranslation = -1;

    //default value is false, means folder from left to right
    private boolean mFolderOrientation = false;

    public FolderLayout(Context context) {
        super(context);
//        mSolidPaint = new Paint();
//        mSolidPaint.setStyle(Paint.Style.FILL);
//        mSolidPaint.setColor(Color.BLACK);
//
//        mShadowPaint = new Paint();
//        mShadowPaint.setStyle(Paint.Style.FILL);
//        mShadowGradientShader = new LinearGradient(0,0,0.5f,0,Color.BLACK,Color.TRANSPARENT, Shader.TileMode.CLAMP);
//        mShadowPaint.setShader(mShadowGradientShader);
//        mShadowGradientMatrix = new Matrix();
//
//        for(int i=0;i<FOLDER_COUNT;i++) {
//            mMatrix[i] = new Matrix();
//        }
    }

    public FolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init(context, attrs);
        TypedArray a = context.getResources().obtainAttributes(attrs, R.styleable.FolderLayout);
        String orientation = a.getString(R.styleable.FolderLayout_orientation);
        if (!orientation.equals("left")) {
            mFolderOrientation = true;
        }
        a.recycle();
        mSolidPaint = new Paint();
        mSolidPaint.setStyle(Paint.Style.FILL);
        mSolidPaint.setColor(Color.BLACK);

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowGradientShader = new LinearGradient(0,0,0.5f,0,Color.BLACK,Color.TRANSPARENT, Shader.TileMode.CLAMP);
        mShadowPaint.setShader(mShadowGradientShader);
        mShadowGradientMatrix = new Matrix();

        for(int i=0;i<FOLDER_COUNT;i++) {
            mMatrix[i] = new Matrix();
        }
    }

//    public void init(Context context, AttributeSet attrs)
//    {
//        mScrollGestureDetector = new GestureDetector(context,
//                new ScrollGestureDetector());
//
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View v = getChildAt(0);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas.setBitmap(mBitmap);
            updateFolder();
        }
    }

    private void updateFolder() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        mSolidPaint.setAlpha((int) ((1 - folder_scale) * 255 * 0.8));
        mShadowPaint.setAlpha((int) ((1 - folder_scale) * 255 * 0.8));
        singleWidth = width/FOLDER_COUNT;
        singleRealWidth = singleWidth * folder_scale;
        depth = (float) Math.sqrt(singleWidth*singleWidth-singleRealWidth*singleRealWidth)/2;
        mShadowGradientMatrix.setScale(singleWidth, 1);
        mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
        float anchorPoint = folder_scale * width;
        float[] src = new float[8];
        float[] dst = new float[8];
        for(int i=0;i<FOLDER_COUNT;i++) {
            src[0] = i * singleWidth;
            src[1] = 0;
            src[2] = src[0] + singleWidth;
            src[3] = 0;
            src[4] = src[2];
            src[5] = height;
            src[6] = src[0];
            src[7] = src[5];

            boolean isEven = i % 2 == 0;

            if (mFolderOrientation == true) {
                dst[0] = i * singleRealWidth + width - anchorPoint;
            } else {
                dst[0] = i * singleRealWidth;
            }
            dst[1] = isEven ? 0 : depth;
            dst[2] = dst[0] + singleRealWidth;
            dst[3] = isEven ? depth : 0;
            dst[4] = dst[2];
            dst[5] = isEven ? height - depth : height;
            dst[6] = dst[0];
            dst[7] = isEven ? height : height
                    - depth;

            for (int y = 0; y < 8; y++)
            {
                dst[y] = Math.round(dst[y]);
            }

            mMatrix[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View v = getChildAt(0);
        measureChild(v, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mTranslation == -1) {
            mTranslation = (int) (getWidth() * folder_scale);
        }
        if (folder_scale == 0) {
            return;
        }
        if (folder_scale == 1f) {
            super.dispatchDraw(canvas);
            return;
        }

        for(int i=0;i<FOLDER_COUNT;i++) {
            canvas.save();
            canvas.concat(mMatrix[i]);
            canvas.clipRect(singleWidth * i, 0, singleWidth * i + singleWidth,
                    height);
            if (isFirst == true) {
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(mBitmap,0,0,null);
                isFirst = false;
            } else {
                canvas.drawBitmap(mBitmap,0,0,null);
            }
            super.dispatchDraw(canvas);
            canvas.translate(singleWidth * i, 0);
            if (i % 2 == 0) {
                canvas.drawRect(0, 0, singleWidth, height,
                        mSolidPaint);
            } else {
                canvas.drawRect(0, 0, singleWidth, height,
                        mShadowPaint);
            }
            canvas.restore();
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        return mScrollGestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }

//    class ScrollGestureDetector extends GestureDetector.SimpleOnGestureListener
//    {
//        @Override
//        public boolean onDown(MotionEvent e)
//        {
//            return true;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY)
//        {
//            mTranslation -= distanceX;
//
//            if (mTranslation < 0)
//            {
//                mTranslation = 0;
//            }
//            if (mTranslation > getWidth())
//            {
//                mTranslation = getWidth();
//            }
//
//            float factor = Math.abs(((float) mTranslation)
//                    / ((float) getWidth()));
//
//            setScale(factor);
//
//            return true;
//        }
//    }

    public void setScale(float scale) {
        if (folder_scale != scale) {
            folder_scale = scale;
            if (folder_scale > 0 && folder_scale < 1f) {
                updateFolder();
            }
            invalidate();
        }
    }

    public float getScale() {
        return folder_scale;
    }
}
