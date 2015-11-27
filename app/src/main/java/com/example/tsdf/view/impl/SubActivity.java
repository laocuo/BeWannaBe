
package com.example.tsdf.view.impl;

import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;
import com.example.tsdf.view.CustomizedView.CustomBottomBar;
import com.example.tsdf.view.CustomizedView.CustomBottomBar.ICustomBottomBar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SubActivity extends FragmentActivity implements ICustomBottomBar {
    private Context mContext;
    private CustomBottomBar mCustomBottomBar;
    private LayoutInflater mLayoutInflater;
    private FragmentManager mFragmentManager;
    private int[] mTitles = {
            R.string.sub_title_section1,
            R.string.sub_title_section2,
            R.string.sub_title_section3
    };

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mContext = this;
        mLayoutInflater = LayoutInflater.from(mContext);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_sub);
        init(mContext);
    }

    private void init(Context mContext2) {
        // TODO Auto-generated method stub
        mCustomBottomBar = (CustomBottomBar) findViewById(R.id.bottom_bar);
        mCustomBottomBar.setCustomBottomBar(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    @Override
    public Class<?>[] getItemList() {
        // TODO Auto-generated method stub
        return LogUtils.getClassList();
    }

    @Override
    public View getItemView(int i) {
        // TODO Auto-generated method stub
        View v = mLayoutInflater.inflate(R.layout.sub_btn, null, false);
        TextView btn = (TextView) v.findViewById(R.id.sub_btn_title);
        btn.setText(mTitles[i]);
        return v;
    }

    @Override
    public void itemSelect(View v) {
        // TODO Auto-generated method stub
        TextView btn = (TextView) v.findViewById(R.id.sub_btn_title);
        btn.setTextColor(getResources().getColor(R.color.sub_text_color_selected));
    }

    @Override
    public void itemUnSelect(View v) {
        // TODO Auto-generated method stub
        TextView btn = (TextView) v.findViewById(R.id.sub_btn_title);
        btn.setTextColor(getResources().getColor(R.color.sub_text_color));
    }

    @Override
    public void switchItem(Fragment fragment) {
        // TODO Auto-generated method stub
        mFragmentManager.beginTransaction().replace(R.id.subtest_content, fragment).commit();
    }

}
