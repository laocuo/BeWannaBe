package com.example.doanything.view.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doanything.R;
import com.example.doanything.utils.L;
import com.example.doanything.view.CustomizedView.CircleListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by administrator on 1/26/16.
 */
public class CircleListFragment extends Fragment {
    private CircleListView mCircleListView;
    private List<Integer> titleList_id = Arrays.asList(
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5,
            R.string.title_section6);

    private List<Integer> iconList_id = Arrays.asList(
            R.drawable.ic_menu_allfriends,
            R.drawable.ic_menu_cc,
            R.drawable.ic_menu_emoticons,
            R.drawable.ic_menu_friendslist,
            R.drawable.ic_menu_myplaces,
            R.drawable.ic_menu_start_conversation);
    private ArrayList<String> titleList = new ArrayList<String>();
    private ArrayList<Bitmap> iconList = new ArrayList<Bitmap>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_circlelist, container, false);
        mCircleListView = (CircleListView) v.findViewById(R.id.circle_list);
        titleList.clear();
        for(int i=0;i<titleList_id.size();i++) {
            titleList.add(getResources().getString(titleList_id.get(i)));
        }
        iconList.clear();
        for(int i=0;i<iconList_id.size();i++) {
            Drawable icon = getResources().getDrawable(iconList_id.get(i));
            BitmapDrawable bd = (BitmapDrawable) icon;
            iconList.add(bd.getBitmap());
        }
        mCircleListView.setListContent(titleList, iconList);
        mCircleListView.setOnCircleListItemListener(new CircleListView.OnCircleListItemListener() {
            @Override
            public void OnCircleListItemClick(int pos) {
                L.d("OnCircleListItemClick pos = " + pos);
            }
        });
        return v;
    }
}
