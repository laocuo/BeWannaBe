
package com.example.tsdf.view.impl;

import com.example.tsdf.R;
import com.example.tsdf.presenter.SplashPresenter;
import com.example.tsdf.utils.LogUtils;
import com.example.tsdf.view.ISplashView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity implements ISplashView {

    private TextView mTextView;
    private ProgressBar mProgressBar;
    private SplashPresenter mPresenter = new SplashPresenter();
    private Handler mHandler;
    private Context mContext;
    private AlertDialog mAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.splash);
        mPresenter.setView(this);
        mTextView = (TextView) findViewById(R.id.splash_text);
        mProgressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);
        LogUtils.setScreenSize(getWinWidth(), getWinHeight());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mPresenter.didFinishLoading();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
            }
        };

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mPresenter.didFinishLoading();
            }
        }, LogUtils.SHOWWELLCOMETIME);
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showNoInetErrorMsg() {
        mTextView.setText("No internet");
    }

    public void moveToMainView() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void moveToSelectView() {
        LogUtils.print("moveToSelectView");
        if (mAD == null) {
            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(this);
            String[] mItemName = {
                    "MainActivity", "SubActivity"
            };
            mAlertDialog.setItems(mItemName, new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    if (arg1 == 0) {
                        startActivity(new Intent(mContext, MainActivity.class));
                    } else {
                        startActivity(new Intent(mContext, SubActivity.class));
                    }
                    finish();
                }
            });
            mAD = mAlertDialog.create();
            mAD.setCanceledOnTouchOutside(false);
        }
        if (!mAD.isShowing()) {
            mAD.show();
        }
    }

    private int getWinWidth() {
        // getWindowManager().getDefaultDisplay().getWidth()
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getWinHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
