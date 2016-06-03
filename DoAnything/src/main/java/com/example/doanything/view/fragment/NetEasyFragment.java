
package com.example.doanything.view.fragment;

import java.util.ArrayList;

import com.example.doanything.R;
import com.example.doanything.utils.LogUtils;
import com.example.doanything.view.CustomizedView.CustomViewPager;
import com.example.doanything.view.CustomizedView.DragRefreshListView;
import com.example.doanything.view.CustomizedView.CustomViewPager.OnPageChangeListener;
import com.example.doanything.view.CustomizedView.PowerCalculatorScrollItem;
import com.example.doanything.view.CustomizedView.PowerCalculatorScrollItem.ScrollItemClick;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class NetEasyFragment extends Fragment
        implements DragRefreshListView.OnRefreshListener {

    @SuppressWarnings("unused")
    private int position;
    private DragRefreshListView mListView;
    private Context mContext;
    private View mHeader = null;
    private CustomViewPager mViewPager;
    private LayoutInflater inflator;
    private LinearLayout tipsgroup;
    private HeaderAdapter mHeaderAdapter;
    private ArrayList<ImageView> mTipViewList = new ArrayList<ImageView>();
    private int mCurrentSelected = 0;
    private Handler mHandle = null;
    private final int GALLERY_ITEM = 0;
    private final int NORMAL_ITEM = 1;
    private OnPageChangeListener lOnPageChangeListener;

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
        mHandle = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable
            ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.power_calculator, container, false);
        mListView = (DragRefreshListView) rootView.findViewById(R.id.content_list);
        mListView.setonRefreshListener(this);
        mListView.setAdapter(new ListAdapter());
        // mListView.setOnItemClickListener(this);
        // mListView.setOnLongClickListener(this);
        mHeaderAdapter = new HeaderAdapter();
        lOnPageChangeListener = new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                int selectindex = arg0 % mHeaderAdapter.getImagesCount();
                if (selectindex != mCurrentSelected) {
                    mTipViewList.get(selectindex).setImageResource(R.drawable.selected_tip);
                    mTipViewList.get(mCurrentSelected).setImageResource(R.drawable.unselected_tip);
                    mCurrentSelected = selectindex;
                }
            }

        };
        return rootView;
    }

    private void initHeadView(View v) {
        // TODO Auto-generated method stub
        if (mHeader == null) {
            mHeader = v;// inflator.inflate(R.layout.power_calculator_list_head, mListView, false);
            tipsgroup = (LinearLayout) mHeader.findViewById(R.id.power_calculator_head_tipsgroup);
            mViewPager = (CustomViewPager) mHeader.findViewById(R.id.gallery_header);

            for (int i = 0; i < mHeaderAdapter.getImagesCount(); i++) {
                ImageView m = new ImageView(mContext);
                if (i == mCurrentSelected) {
                    m.setImageResource(R.drawable.selected_tip);
                } else {
                    m.setImageResource(R.drawable.unselected_tip);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
                params.setMargins(5, 0, 5, 10);
                m.setLayoutParams(params);
                tipsgroup.addView(m);
                mTipViewList.add(m);
            }

            mViewPager.setAdapter(mHeaderAdapter);
            mViewPager.setCurrentItem(mHeaderAdapter.getCount() / 2);
            mViewPager.setOnPageChangeListener(lOnPageChangeListener);
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        mHeader = null;
        mCurrentSelected = 0;
        if (tipsgroup != null) {
            tipsgroup.removeAllViews();
            tipsgroup = null;
        }
        if (mTipViewList != null) {
            mTipViewList.clear();
        }
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
            mViewPager.setOnPageChangeListener(null);
            mViewPager = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
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

        public int getImagesCount() {
            return countNum;
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

    private class ListAdapter extends BaseAdapter implements ScrollItemClick {

        private int[] mImageList = {
                0, // for pageview
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3,
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3
        };

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mImageList.length;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getViewTypeCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            if (position == 0) {
                return GALLERY_ITEM;
            } else {
                return NORMAL_ITEM;
            }
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            View v = null;
            if (GALLERY_ITEM == getItemViewType(arg0)) {
                if (arg1 == null) {
                    if (mHeader == null) {
                        v = inflator.inflate(R.layout.power_calculator_list_head, arg2, false);
                    } else {
                        v = mHeader;
                    }
                    // LogUtils.print("initHeadView");
                    initHeadView(v);
                } else {
                    v = arg1;
                    if (mHeader != arg1) {
                        LogUtils.print("mHeader != arg1");
                    }
                    // LogUtils.print("updateHeadView");
                }
            } else {
                if (arg1 == null) {
                    v = inflator.inflate(R.layout.power_calculator_scroll_item, arg2, false);
                } else {
                    v = arg1;
                }
                PowerCalculatorScrollItem pcsi = (PowerCalculatorScrollItem) v;
                pcsi.wrapContent(arg0, mImageList[arg0]);
                pcsi.setScrollItemClick(this);
            }
            return v;
        }

        @Override
        public void OnScrollItemClick(int index, View v) {
            // TODO Auto-generated method stub
            // LogUtils.print("OnScrollItemClick--index="+index);
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        mHandle.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mListView.onRefreshComplete();
            }
        }, 3000);
    }
}
