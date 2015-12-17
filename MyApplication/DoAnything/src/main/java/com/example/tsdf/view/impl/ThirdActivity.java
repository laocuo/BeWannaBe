package com.example.tsdf.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;
import com.example.tsdf.view.CustomizedView.ViewPagerIndicator;
import java.util.Arrays;
import java.util.List;

public class ThirdActivity extends FragmentActivity implements ViewPagerIndicator.PageChangeListener {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int[] mTitles = LogUtils.getStringList();
    private ViewPagerIndicator mPagerIndicator;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_third);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_third);
        mPagerIndicator = (ViewPagerIndicator) findViewById(R.id.pagerindicator_third);
        mViewPager.setAdapter(new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Class<?>[] classList = LogUtils.getClassList();
                Fragment mFragment = null;
                try {
                    mFragment = (Fragment) classList[position].newInstance();//getConstructor()
                    Bundle args = new Bundle();
                    args.putInt(LogUtils.ARG_SECTION_NUMBER, position + 1);
                    mFragment.setArguments(args);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return mFragment;
            }

            @Override
            public int getCount() {
                return LogUtils.getClassList().length;
            }
        });
        List<String> list = Arrays.asList("Title1", "Title2", "Title3", "Title4", "Title5");
//        for (int i=0;i<mTitles.length;i++) {
//            list.add(mContext.getResources().getString(mTitles[i]));
//        }
        mPagerIndicator.setOnPageChangeListener(this);
        mPagerIndicator.setTabItemTitles(list);
        mPagerIndicator.setViewPager(mViewPager, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
