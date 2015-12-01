package com.example.tsdf.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tsdf.R;
import com.example.tsdf.view.CustomizedView.AudioControlView;

/**
 * Created by administrator on 12/1/15.
 */
public class AudioControlFragment extends Fragment {
    private AudioControlView mAudioControlView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.audio_control, container, false);
        mAudioControlView = (AudioControlView)rootView.findViewById(R.id.audio_control_view);
        return rootView;
    }
}
