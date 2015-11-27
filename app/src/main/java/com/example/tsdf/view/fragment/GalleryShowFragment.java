
package com.example.tsdf.view.fragment;

import java.util.ArrayList;

import com.example.tsdf.R;
import com.example.tsdf.utils.LogUtils;
import com.example.tsdf.view.CustomizedView.CustomViewPager;
import com.example.tsdf.view.CustomizedView.DragRefreshScrollView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GalleryShowFragment extends Fragment {

    @SuppressWarnings("unused")
    private int position;
    private Context mContext;
    @SuppressWarnings("unused")
    private LayoutInflater inflator;
    private LinearLayout mContainer;
    private DragRefreshScrollView mScrollView;
    private HeaderAdapter mHeaderAdapter;
    private Handler mHandler;

    private int[] mImageList = {
            R.drawable.sample_0,
            R.drawable.sample_1,
            R.drawable.sample_2,
            R.drawable.sample_3
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            position = b.getInt(LogUtils.ARG_SECTION_NUMBER);
        }
        mContext = getActivity();
        inflator = LayoutInflater.from(mContext);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable
            ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mScrollView = (DragRefreshScrollView) inflater.inflate(R.layout.gallery_show, container,
                false);
        mContainer = (LinearLayout) mScrollView.findViewById(R.id.galler_show_container);
        mScrollView.setContainer((ViewGroup) mContainer);
        initView();
        return mScrollView;
    }

    private void initView() {
        // TODO Auto-generated method stub
        CustomViewPager mViewPager = new CustomViewPager(mContext);
        mHeaderAdapter = new HeaderAdapter();
        mViewPager.setAdapter(mHeaderAdapter);
        mViewPager.setCurrentItem(mHeaderAdapter.getCount() / 2);
        LayoutParams ll = new LayoutParams(LogUtils.getScreenW(), 400);
        mContainer.addView(mViewPager, ll);
        for (int i = 0; i < mImageList.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setImageResource(mImageList[i]);
            measureView(iv);
            LayoutParams dd = new LayoutParams(iv.getMeasuredWidth(), iv.getMeasuredHeight());
            mContainer.addView(iv, dd);
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mScrollView.displayStart();
            }
        }, 500);
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    private void measureView(View child) {
        android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        }
        // int parentWidth = MeasureSpec.makeMeasureSpec(LogUtils.getScreenW(),
        // MeasureSpec.EXACTLY);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
        // LogUtils.print("child.getMeasuredWidth() = " + child.getMeasuredWidth());
        // LogUtils.print("child.getMeasuredHeight() = " + child.getMeasuredHeight());
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

    private class HeaderAdapter extends PagerAdapter {

        private int[] mIconList = {
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3
        };

        private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();

        private int countNum;

        public HeaderAdapter() {
            countNum = mIconList.length;
            for (int i = 0; i < countNum; i++) {
                ImageView mImageView = new ImageView(mContext);
                mImageView.setImageResource(mIconList[i]);
                mImageView.setScaleType(ScaleType.FIT_XY);
                mImageViewList.add(mImageView);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return countNum * 100;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(mImageViewList.get(position % countNum));
            // LogUtils.print("removeView");
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mImageViewList.get(position % countNum));
            // LogUtils.print("addView");
            return mImageViewList.get(position % countNum);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }
}
