
package com.example.tsdf.view.CustomizedView;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class CustomBottomBar extends HorizontalScrollView {

    private LinearLayout mContent;
    private ArrayList<View> mBtnList;
    private CustomBottomBar mCustomBottomBar;
    private ICustomBottomBar mI;
    private ArrayList<Fragment> mFragmentList;
    private int mCurrentItem = 0;
    private float mItemWidth = 0;
    private int mTouchItem = -1;
    private int mCount;

    public interface ICustomBottomBar {
        int getScreenWidth();

        Class<?>[] getItemList();

        View getItemView(int i);

        void itemSelect(View v);

        void itemUnSelect(View v);

        void switchItem(Fragment fragment);
    }

    public CustomBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    private void init(Context context) {
        // TODO Auto-generated method stub
        mContent = new LinearLayout(context);
        mCustomBottomBar = this;
        mContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(mContent);
        mBtnList = new ArrayList<View>();
        mFragmentList = new ArrayList<Fragment>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchItem = getSelectItem(x);
                if (mTouchItem != mCurrentItem) {
                    mI.itemSelect(mBtnList.get(mTouchItem));
                } else {
                    mTouchItem = -1;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (mTouchItem > -1) {
                    if (x < mItemWidth * mTouchItem || x >= mItemWidth * (mTouchItem + 1)) {
                        mI.itemUnSelect(mBtnList.get(mTouchItem));
                        mTouchItem = -1;
                    }
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchItem > -1) {
                    SwitchItem(mTouchItem);
                    mTouchItem = -1;
                }
                return true;
        }
        return false;
    }

    private void SwitchItem(int index) {
        // TODO Auto-generated method stub
        if (index >= 0 && index < mBtnList.size()) {
            if (index == mCurrentItem) {
                mI.itemSelect(mBtnList.get(index));
            } else {
                mI.itemUnSelect(mBtnList.get(mCurrentItem));
                mI.itemSelect(mBtnList.get(index));
                mCurrentItem = index;
            }
        }
        mI.switchItem(mFragmentList.get(mCurrentItem));
    }

    public void setCustomBottomBar(ICustomBottomBar i) {
        // TODO Auto-generated method stub
        mI = i;
        initBottomBar();
        SwitchItem(0);
    }

    private void initBottomBar() {
        // TODO Auto-generated method stub
        Class<?>[] mClassList = mI.getItemList();
        mCount = mClassList.length;
        mItemWidth = mI.getScreenWidth() / mCount;
        for (int i = 0; i < mCount; i++) {
            View btnView = mI.getItemView(i);
            android.view.ViewGroup.LayoutParams parent = mCustomBottomBar.getLayoutParams();
            LayoutParams dd = new LayoutParams((int) mItemWidth, parent.height);
            mBtnList.add(btnView);
            mContent.addView(btnView, dd);
            Fragment mFragment = null;
            try {
                mFragment = (Fragment) mClassList[i].getConstructor().newInstance();
                // Bundle args = new Bundle();
                // args.putInt(LogUtils.ARG_SECTION_NUMBER, i+1);
                // mFragment.setArguments(args);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mFragmentList.add(mFragment);
        }
    }

    private int getSelectItem(float x) {
        // TODO Auto-generated method stub
        int index = 0;
        for (int i = 0; i < mCount; i++) {
            if (x >= mItemWidth * i && x < mItemWidth * (i + 1)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
