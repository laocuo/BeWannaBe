package com.laocuo.glassball.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.laocuo.glassball.R;

/**
 * Created by Laocuo on 2016/4/26.
 */
public class GlassMenuFragment extends Fragment implements View.OnClickListener {
    private Button mStart;
    public interface GlassMenuInterface{
        void startGame();
    }

    private GlassMenuInterface mListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_glass_menu, container, false);
        mStart = (Button) v.findViewById(R.id.start);
        mStart.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GlassMenuInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                if (mListener != null) {
                    mListener.startGame();
                }
                break;
            default:
                break;
        }
    }
}
