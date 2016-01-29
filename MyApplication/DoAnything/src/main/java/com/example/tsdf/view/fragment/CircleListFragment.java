package com.example.tsdf.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tsdf.R;
import com.example.tsdf.utils.L;
import com.example.tsdf.view.CustomizedView.CircleListView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by administrator on 1/26/16.
 */
public class CircleListFragment extends Fragment {
    private CircleListView mCircleListView;
    private List<Integer> list = Arrays.asList(
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5,
            R.string.title_section6);

    private List<Integer> iconList = Arrays.asList(
            R.drawable.ic_menu_allfriends,
            R.drawable.ic_menu_cc,
            R.drawable.ic_menu_emoticons,
            R.drawable.ic_menu_friendslist,
            R.drawable.ic_menu_myplaces,
            R.drawable.ic_menu_start_conversation);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_circlelist, container, false);
        mCircleListView = (CircleListView) v.findViewById(R.id.circle_list);
        mCircleListView.setListContent(list, iconList);
        mCircleListView.setOnCircleListItemListener(new CircleListView.OnCircleListItemListener() {
            @Override
            public void OnCircleListItemClick(int pos) {
                L.d("OnCircleListItemClick pos = " + pos);
            }
        });
        return v;
    }
}
