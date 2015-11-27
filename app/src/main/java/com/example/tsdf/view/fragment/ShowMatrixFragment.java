
package com.example.tsdf.view.fragment;

import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ShowMatrixFragment extends Fragment implements OnClickListener, OnTouchListener {

    private Button bt1, bt2, bt3, bt4, bt5;
    @SuppressWarnings("unused")
    private ImageView iv1_sm;
    private ImageView iv2_sm;
    private Matrix mMatrix;
    private int mImageWidth;
    private int mImageHeight;
    private float mStepGapX = 0;
    private float mStepGapY = 0;
    @SuppressWarnings("unused")
    private float[] mValue = {
            1, 0, 0,
            0, 1, 0,
            0, 0, 50
    };
    private RelativeLayout miv_container;
    private float mStartTouchPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable
            ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.show_matrix, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View v) {
        // TODO Auto-generated method stub
        bt1 = (Button) v.findViewById(R.id.bt1_sm);
        bt1.setOnClickListener(this);
        bt2 = (Button) v.findViewById(R.id.bt2_sm);
        bt2.setOnClickListener(this);
        bt3 = (Button) v.findViewById(R.id.bt3_sm);
        bt3.setOnClickListener(this);
        bt4 = (Button) v.findViewById(R.id.bt4_sm);
        bt4.setOnClickListener(this);
        bt5 = (Button) v.findViewById(R.id.bt5_sm);
        bt5.setOnClickListener(this);

        iv1_sm = (ImageView) v.findViewById(R.id.iv1_sm);
        iv2_sm = (ImageView) v.findViewById(R.id.iv2_sm);
        // iv2_sm.setFocusable(false);
        iv2_sm.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mImageWidth = iv2_sm.getWidth();
                mImageHeight = iv2_sm.getHeight();
                LogUtils.print("ShowMatrixFragment mImageWidth = " + mImageWidth
                        + " mImageHeight = " + mImageHeight);
                // initPosition();
            }
        });
        mMatrix = new Matrix();

        miv_container = (RelativeLayout) v.findViewById(R.id.iv_container);
        miv_container.setOnTouchListener(this);
        // miv_container.setFocusableInTouchMode(true);
        // miv_container.setFocusable(true);
        // bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_0);
        // Bitmap bp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
        // mMatrix, true);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.bt1_sm:
                mStepGapX = 45;
                mStepGapY = 10;
                break;
            case R.id.bt2_sm:
                mStepGapX = 90;
                mStepGapY = 20;
                break;
            case R.id.bt3_sm:
                mStepGapX = 135;
                mStepGapY = 30;
                break;
            case R.id.bt4_sm:
                mStepGapX = 180;
                mStepGapY = 40;
                break;
            case R.id.bt5_sm:
            default:
                mStepGapX = 0;
                mStepGapY = 0;
                break;
        }
        if (mStepGapX > 0) {
            // float mB = mImageWidth / mImageHeight;
            // mStepGapY = (float)(mStepGapX / mB);
            float[] src = {
                    0, 0,
                    mImageWidth, 0,
                    mImageWidth, mImageHeight,
                    0, mImageHeight
            };
            float[] dst = {
                    0 + mStepGapX, 0 + mStepGapY / 2,
                    mImageWidth - mStepGapX, mStepGapY,
                    mImageWidth - mStepGapX, mImageHeight - mStepGapY,
                    0 + mStepGapX, mImageHeight - mStepGapY / 2
            };
            mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        } else {
            mMatrix.reset();
        }
        iv2_sm.setImageMatrix(mMatrix);
    }

    @SuppressWarnings("unused")
    private void initPosition() {
        // TODO Auto-generated method stub
        mMatrix.postTranslate((LogUtils.getScreenW() - mImageWidth) / 2, 100);
        iv2_sm.setImageMatrix(mMatrix);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        // LogUtils.print("onTouch--"+arg1.getAction());
        if (arg0.getId() == R.id.iv_container) {
            // LogUtils.print("onTouch--"+arg1.getAction());
            switch (arg1.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartTouchPos = arg1.getX();
                    // LogUtils.print("mStartTouchPos = "+mStartTouchPos);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handleTouchMoveEvent(arg1.getX() - mStartTouchPos);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    resetImageMatrix();
                    break;
            }
            return true;
        }
        return false;
    }

    private void handleTouchMoveEvent(float movePos) {
        // TODO Auto-generated method stub
        // LogUtils.print("movePos = "+movePos);
        int mYB = 8;
        float mYGap = mImageHeight / 2;
        mStepGapX = Math.abs((movePos * mImageWidth / LogUtils.getScreenW()));
        mStepGapY = Math.abs((movePos * mYGap / LogUtils.getScreenW()));
        // LogUtils.print("mStepGapX="+mStepGapX+" mStepGapY="+mStepGapY);
        if (mStepGapY > mYGap / 2) {
            mStepGapY = mYGap / 2 - (mStepGapY - mYGap / 2);
        }
        float[] src = {
                0, 0,
                mImageWidth, 0,
                mImageWidth, mImageHeight,
                0, mImageHeight
        };
        if (movePos >= 0) {
            float[] dst = {
                    0 + mStepGapX, 0 + mStepGapY / mYB,
                    mImageWidth - mStepGapX, mStepGapY,
                    mImageWidth - mStepGapX, mImageHeight - mStepGapY,
                    0 + mStepGapX, mImageHeight - mStepGapY / mYB
            };
            mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        } else {
            float[] dst = {
                    0 + mStepGapX, 0 + mStepGapY,
                    mImageWidth - mStepGapX, mStepGapY / mYB,
                    mImageWidth - mStepGapX, mImageHeight - mStepGapY / mYB,
                    0 + mStepGapX, mImageHeight - mStepGapY
            };
            mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
        iv2_sm.setImageMatrix(mMatrix);
    }

    private void resetImageMatrix() {
        // TODO Auto-generated method stub
        mStepGapX = 0;
        mStepGapY = 0;
        mMatrix.reset();
        iv2_sm.setImageMatrix(mMatrix);
    }

}
