package com.example.doanything.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doanything.R;

/**
 * Created by administrator on 12/8/15.
 */
public class MyViewGroupFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_pokaviewgroup, container, false);
        return rootView;
    }
}
