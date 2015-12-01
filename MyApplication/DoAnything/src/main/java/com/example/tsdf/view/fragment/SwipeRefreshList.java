package com.example.tsdf.view.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.tsdf.R;
import com.example.tsdf.adapterhelper.CommonAdapter;
import com.example.tsdf.adapterhelper.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by administrator on 12/1/15.
 */
public class SwipeRefreshList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int REFRESH_COMPLETE = 1;
    private LayoutInflater mLayoutInflater;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private BaseAdapter mAdapter;
    private Handler mHandler;
    private Context mContext;
    private ArrayList<String> mContentList = new ArrayList<String>(Arrays.asList(new String[]{
            "henory",
            "arsenal",
            "champion",
            "highbury"}));
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View rooView = inflater.inflate(R.layout.swipe_refresh_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rooView.findViewById(R.id.swiperefresh_container);
        mListView = (ListView) rooView.findViewById(R.id.swiperefresh_list);
        init();
        return rooView;
    }

    private void init() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new CommonAdapter<String>(mContext, mContentList, R.layout.swipe_refresh_list_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.setText(R.id.swipe_refresh_list_item_text, item);
            }
        };
        mListView.setAdapter(mAdapter);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        mSwipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        //TODO update content and refresh UI
        mContentList.add("onRefresh");
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
    }
}
