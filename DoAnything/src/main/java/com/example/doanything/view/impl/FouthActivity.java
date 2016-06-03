package com.example.doanything.view.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.doanything.R;
import com.example.doanything.view.CustomizedView.FolderLayout;

public class FouthActivity extends Activity {
    private DrawerLayout mFolder_List_Container;
    private FolderLayout mFolder_Layout;
    private ListView mFolder_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fouth);
        mFolder_List_Container = (DrawerLayout) findViewById(R.id.folder_list_drawer);
        mFolder_Layout = (FolderLayout) findViewById(R.id.folder_list_container);
        mFolder_List = (ListView) findViewById(R.id.folder_list);
         mFolder_List.setAdapter(new ArrayAdapter<String>(
         this,
         android.R.layout.simple_list_item_activated_1,
         android.R.id.text1,
         new String[]{
         getString(R.string.title_section1),
         getString(R.string.title_section2),
         getString(R.string.title_section3),
         getString(R.string.title_section4),
         getString(R.string.title_section5),
         getString(R.string.title_section6),
         }));
        mFolder_List_Container.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView instanceof FolderLayout) {
                    mFolder_Layout.setScale(slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
}
