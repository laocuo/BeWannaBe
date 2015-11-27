
package com.example.tsdf.view.adapter;

import com.example.tsdf.utils.LogUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ContentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        // TODO Auto-generated constructor stub
    }

    public ContentPagerAdapter(Context mContext,
            FragmentManager fragmentManager) {
        super(fragmentManager);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        int[] stringList = LogUtils.getStringList();
        return mContext.getResources().getText(stringList[position]);
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        Class<?>[] classList = LogUtils.getClassList();
        Fragment mFragment = null;
        try {
            mFragment = (Fragment) classList[position].getConstructor().newInstance();
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
        // TODO Auto-generated method stub
        return LogUtils.getClassList().length;
    }

}
