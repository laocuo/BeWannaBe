package com.example.doanything.view.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.doanything.R;
import com.example.doanything.utils.LogUtils;
import com.example.doanything.view.CustomizedView.ColorfulPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThirdActivity extends FragmentActivity{
    private FragmentManager mFragmentManager;
    private ColorfulPagerIndicator mPagerIndicator;
    private ViewPager mViewPager;
    private List<String> list = new ArrayList<>();
    private List<Integer> iconList = Arrays.asList(R.drawable.ic_menu_allfriends,
            R.drawable.ic_menu_cc,
            R.drawable.ic_menu_emoticons,
            R.drawable.ic_menu_friendslist,
            R.drawable.ic_menu_myplaces,
            R.drawable.ic_menu_duration,
            R.drawable.ic_menu_contact,
            R.drawable.ic_menu_start_conversation);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_third);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_third);
        mPagerIndicator = (ColorfulPagerIndicator) findViewById(R.id.pagerindicator_third);
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
        int[] stringids = LogUtils.getStringList();
        list.clear();
        for(int i=0;i<stringids.length;i++) {
            list.add(getString(stringids[i]));
        }
        mPagerIndicator.setTabItemTitles(list, iconList);
        mPagerIndicator.setViewPager(mViewPager, 0);
    }
}
