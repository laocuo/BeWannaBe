
package com.example.doanything.view.fragment;

import java.util.ArrayList;

import com.example.doanything.R;
import com.example.doanything.utils.LogUtils;
import com.example.doanything.view.CustomizedView.CustomHorizontalScrollView;
import com.example.doanything.view.CustomizedView.CustomHorizontalScrollView.OnOverScrolledListener;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class ShowMatrixGalleryFragment extends Fragment implements OnTouchListener {

    private Context mContext;
    private CustomHorizontalScrollView mHorizontalScrollView;
    private LinearLayout mLinearLayout;
    private int mImageWidth, mImageHeight;
    private int[] mImageList = {
            R.drawable.wp_0001,
            R.drawable.wp_0002,
            R.drawable.wp_0003,
            R.drawable.wp_0004,
            R.drawable.wp_0005
    };
    private ArrayList<ImageView> mImageViewList;
    private Matrix mMatrix_s, mMatrix_e;
    private float SCALE_Y = 4f;
    private float SCALE_X = 1f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable
            ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.show_gallery_matrix, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        // TODO Auto-generated method stub
        mMatrix_s = new Matrix();
        mMatrix_e = new Matrix();
        mImageViewList = new ArrayList<ImageView>();
        mHorizontalScrollView = (CustomHorizontalScrollView) rootView
                .findViewById(R.id.gallery_matrix_view);
        mHorizontalScrollView.setHorizontalScrollBarEnabled(false);
        mHorizontalScrollView.setOnTouchListener(this);
        mHorizontalScrollView.setOnOverScrolledListener(new OnOverScrolledListener() {

            @Override
            public void onOverScrolled(int scrollX, boolean clampedX) {
                // TODO Auto-generated method stub
                showImageMatrix();
            }
        });

        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.gallery_matrix_container);
        for (int i = 0; i < mImageList.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ScaleType.MATRIX);
            iv.setImageResource(mImageList[i]);
            measureView(iv);
            LayoutParams dd = new LayoutParams(iv.getMeasuredWidth(), iv.getMeasuredHeight());
            mImageViewList.add(iv);
            mLinearLayout.addView(iv, dd);
        }
        mImageWidth = mHorizontalScrollView.getLayoutParams().width;
        mImageHeight = mHorizontalScrollView.getLayoutParams().height;
        LogUtils.print("ShowMatrixGalleryFragment mImageWidth=" + mImageWidth + " mImageHeight="
                + mImageHeight);
    }

    private void measureView(View child) {
        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
        }
        LayoutParams parentParams = mHorizontalScrollView.getLayoutParams();
        int parentWidth = MeasureSpec.makeMeasureSpec(parentParams.width, MeasureSpec.EXACTLY);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(parentWidth, 0 + 0, params.width);
        int parentHeight = MeasureSpec.makeMeasureSpec(parentParams.height, MeasureSpec.EXACTLY);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(parentHeight, 0 + 0, params.height);
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        if (arg0.getId() == R.id.gallery_matrix_view) {
            switch (arg1.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // showImageMatrix();
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    resetImageMatrix();
                    return true;
            }
        }
        return false;
    }

    private void showImageMatrix() {
        // TODO Auto-generated method stub
        int scrollx = mHorizontalScrollView.getScrollX();
        int focusIndex = 0;
        for (int i = 0; i < mImageList.length; i++) {
            int start = i * mImageWidth;
            int end = start + mImageWidth;
            if (scrollx < end && scrollx >= start) {
                focusIndex = i;
                break;
            }
        }
        int focusIndex_next = focusIndex + 1;
        // LogUtils.print("focusIndex="+focusIndex+" focusIndex_next="+focusIndex_next);
        doImageMatrix_S(focusIndex);
        doImageMatrix_E(focusIndex_next);
    }

    private void doImageMatrix_E(int i) {
        // TODO Auto-generated method stub
        if (i < 0 || i > (mImageViewList.size() - 1)) {
            return;
        }
        int scrollx = mHorizontalScrollView.getScrollX();
        float mYGap = mImageHeight / SCALE_Y;
        float movePos = Math.abs(scrollx - i * mImageWidth);
        float mStepGapX = movePos * SCALE_X;
        float mStepGapY = (movePos * mYGap) / mImageHeight;
        // LogUtils.print("mStepGapX="+mStepGapX+" mStepGapY="+mStepGapY);
        // if (mStepGapY > mYGap/2) {
        // mStepGapY = mYGap/2 - (mStepGapY - mYGap/2);
        // }
        float[] src = {
                0, 0,
                mImageWidth, 0,
                mImageWidth, mImageHeight,
                0, mImageHeight
        };

        float[] dst = {
                0, 0,
                mImageWidth - mStepGapX, 0 + mStepGapY,
                mImageWidth - mStepGapX, mImageHeight - mStepGapY,
                0, mImageHeight
        };
        mMatrix_e.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        mImageViewList.get(i).setImageMatrix(mMatrix_e);
    }

    private void doImageMatrix_S(int i) {
        // TODO Auto-generated method stub
        if (i < 0 || i > (mImageViewList.size() - 1)) {
            return;
        }
        int scrollx = mHorizontalScrollView.getScrollX();
        float mYGap = mImageHeight / SCALE_Y;
        float movePos = Math.abs(scrollx - i * mImageWidth);
        float mStepGapX = movePos * SCALE_X;
        float mStepGapY = (movePos * mYGap) / mImageHeight;
        // LogUtils.print("mStepGapX="+mStepGapX+" mStepGapY="+mStepGapY);
        // if (mStepGapY > mYGap/2) {
        // mStepGapY = mYGap/2 - (mStepGapY - mYGap/2);
        // }
        float[] src = {
                0, 0,
                mImageWidth, 0,
                mImageWidth, mImageHeight,
                0, mImageHeight
        };

        float[] dst = {
                0 + mStepGapX, 0 + mStepGapY,
                mImageWidth, 0,
                mImageWidth, mImageHeight,
                0 + mStepGapX, mImageHeight - mStepGapY
        };
        mMatrix_s.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        mImageViewList.get(i).setImageMatrix(mMatrix_s);
    }

    private void resetImageMatrix() {
        // TODO Auto-generated method stub
        int scrollx = mHorizontalScrollView.getScrollX();
        // LogUtils.print("ShowMatrixGalleryFragment scrollx="+scrollx);
        int focusIndex = 0;
        for (int i = 0; i < mImageList.length; i++) {
            int start = i * mImageWidth;
            int end = start + mImageWidth;
            if (scrollx < end && scrollx >= start) {
                if ((scrollx - start) > mImageWidth / 2) {
                    focusIndex = i + 1;
                } else {
                    focusIndex = i;
                }
                break;
            }
        }
        mHorizontalScrollView.smoothScrollTo(focusIndex * mImageWidth, 0);
    }

}
