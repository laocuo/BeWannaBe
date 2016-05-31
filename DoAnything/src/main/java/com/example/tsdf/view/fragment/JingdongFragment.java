package com.example.tsdf.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.tsdf.R;
import com.example.tsdf.utils.DensityUtils;
import com.example.tsdf.utils.L;
import com.example.tsdf.view.CustomizedView.DragRefreshScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JingdongFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout jd_top_bar;
    private Drawable jd_top_bar_bg_drawable;
    private DragRefreshScrollView jd_content_container;
    private LinearLayout jd_content;
    private ViewPager jd_top_gallery;
    private TopGalleryAdapter jd_top_gallery_adapter;
    private LinearLayout jd_top_gallery_tips_group;
    private int jd_top_gallery_current_selected = 0;
    private ArrayList<ImageView> jd_top_gallery_tipviewlist = new ArrayList<ImageView>();

    private HorizontalScrollView jd_miaosha;
    private LinearLayout jd_miaosha_container;
    private List<Integer> jd_miaosha_iconList = Arrays.asList(
            R.drawable.ccc,
            R.drawable.ddd,
            R.drawable.eee,
            R.drawable.ccc,
            R.drawable.ddd,
            R.drawable.eee);

    private Context mContext;
    public JingdongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JingdongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JingdongFragment newInstance(String param1, String param2) {
        JingdongFragment fragment = new JingdongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_jingdong, container, false);
        initView(v);
        initData();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        jd_top_gallery_tipviewlist.clear();
        jd_top_gallery_tipviewlist = null;
    }

    private void initView(View v) {
        jd_top_bar = (LinearLayout) v.findViewById(R.id.jd_top_bar);
        jd_top_bar_bg_drawable = new ColorDrawable(Color.RED);
        jd_top_bar.setBackground(jd_top_bar_bg_drawable);
        jd_top_bar_bg_drawable.setAlpha(0);
        jd_content_container = (DragRefreshScrollView) v.findViewById(R.id.jd_content_container);
        jd_content = (LinearLayout) v.findViewById(R.id.jd_content);
        jd_top_gallery = (ViewPager) v.findViewById(R.id.jd_top_gallery);
        jd_top_gallery_tips_group = (LinearLayout) v.findViewById(R.id.jd_top_gallery_tips_group);
        jd_miaosha = (HorizontalScrollView) v.findViewById(R.id.jd_miaoshao_scrollview);
        jd_miaosha_container = (LinearLayout) v.findViewById(R.id.jd_miaoshao_scrollview_container);
    }

    private void initData() {
        jd_content_container.setOverScorlledListener(new DragRefreshScrollView.OverScorlledListener() {
            @Override
            public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
                int alpha = 150;
                int headHeight = jd_content_container.getHeadHeight();
                if (scrollY < headHeight) {
                    jd_top_bar.setVisibility(View.INVISIBLE);
                } else {
                    jd_top_bar.setVisibility(View.VISIBLE);
                    if (scrollY - headHeight <= 150) {
                        alpha = scrollY - headHeight;
                    }
                    jd_top_bar_bg_drawable.setAlpha(alpha);
                }
            }
        });
        jd_content_container.setContainer(jd_content);
        jd_content_container.setVerticalScrollBarEnabled(false);
        jd_top_gallery_adapter = new TopGalleryAdapter();
        jd_top_gallery.setAdapter(jd_top_gallery_adapter);
        if (jd_top_gallery_tipviewlist == null) {
            jd_top_gallery_tipviewlist = new ArrayList<ImageView>();
        }
        for (int i = 0; i < jd_top_gallery_adapter.getCount(); i++) {
            ImageView m = new ImageView(mContext);
            if (i == jd_top_gallery_current_selected) {
                m.setImageResource(R.drawable.selected_tip);
            } else {
                m.setImageResource(R.drawable.unselected_tip);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.setMargins(10, 10, 10, 0);
            m.setLayoutParams(params);
            jd_top_gallery_tips_group.addView(m);
            jd_top_gallery_tipviewlist.add(m);
        }
        jd_top_gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != jd_top_gallery_current_selected) {
                    jd_top_gallery_tipviewlist.get(position).setImageResource(R.drawable.selected_tip);
                    jd_top_gallery_tipviewlist.get(jd_top_gallery_current_selected).setImageResource(R.drawable.unselected_tip);
                    jd_top_gallery_current_selected = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        jd_miaosha.setHorizontalScrollBarEnabled(false);
        int miaosha_items = jd_miaosha_iconList.size();
        for (int i=0;i<miaosha_items;i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.jd_miaosha_item, null, false);
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(DensityUtils.dp2px(mContext, 160.0f),
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
//            int lpWidth = params.width;
//            int childWidthSpec = 0;
//            if (lpWidth > 0) {
//                childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpWidth, View.MeasureSpec.EXACTLY);
//            }
//            L.d("lpWidth:"+lpWidth+"\nchildWidthSpec:"+childWidthSpec);
//            int lpHeight = params.height;
//            int childHeightSpec = 0;
//            if (lpHeight > 0) {
//                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
//            }
//            L.d("lpHeight:"+lpHeight+"\nchildHeightSpec:"+childHeightSpec);
//            v.measure(childWidthSpec, childHeightSpec);
            ImageView iv = (ImageView)v.findViewById(R.id.jd_miaosha_item_image);
            iv.setImageResource(jd_miaosha_iconList.get(i));
            jd_miaosha_container.addView(v, params);
        }
    }
    private class TopGalleryAdapter extends PagerAdapter {

        private int[] mIconList = {
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3
        };
        private ArrayList<ImageView> mImageViewList = new ArrayList<ImageView>();
        private int countNum;

        public TopGalleryAdapter() {
            countNum = mIconList.length;
            for (int i = 0; i < countNum; i++) {
                ImageView mImageView = new ImageView(mContext);
                mImageView.setImageResource(mIconList[i]);
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageViewList.add(mImageView);
            }
        }
        @Override
        public int getCount() {
            return mIconList.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(mImageViewList.get(position % countNum));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mImageViewList.get(position % countNum));
            return mImageViewList.get(position % countNum);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
