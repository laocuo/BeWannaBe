package com.example.doanything.view.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.doanything.R;
import com.example.doanything.utils.LogUtils;
import com.example.doanything.view.CustomizedView.BezerLineView;
import com.example.doanything.view.CustomizedView.BezerPointView;

public class BezerFragment extends Fragment implements View.OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BezerPointView start,start1,end1,end;
    private FrameLayout mFrameLayout;
    private BezerLineView mBezerLineView;
    public BezerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BezerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BezerFragment newInstance(String param1, String param2) {
        BezerFragment fragment = new BezerFragment();
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
        start = new BezerPointView(getActivity(), Color.RED);
        start1 = new BezerPointView(getActivity(), Color.RED);
        end1 = new BezerPointView(getActivity(), Color.BLUE);
        end = new BezerPointView(getActivity(), Color.BLUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bezer, container, false);
        mFrameLayout = (FrameLayout) v.findViewById(R.id.bezer_content);
        mBezerLineView = (BezerLineView) v.findViewById(R.id.bezerline);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setPointsLayout();
    }

    private void setPointsLayout() {
        FrameLayout.LayoutParams startL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        startL.setMargins(LogUtils.getScreenW()/2-50, 100, 0, 0);
        mFrameLayout.addView(start, startL);

        FrameLayout.LayoutParams start1L = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        start1L.setMargins(LogUtils.getScreenW()/2-50, 300, 0, 0);
        mFrameLayout.addView(start1, start1L);

        FrameLayout.LayoutParams end1L = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        end1L.setMargins(LogUtils.getScreenW()/2-50, 500, 0, 0);
        mFrameLayout.addView(end1, end1L);

        FrameLayout.LayoutParams endL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        endL.setMargins(LogUtils.getScreenW()/2-50, 700, 0, 0);
        mFrameLayout.addView(end, endL);

        start.setOnTouchListener(this);
        start1.setOnTouchListener(this);
        end1.setOnTouchListener(this);
        end.setOnTouchListener(this);
    }

    private float downX,downY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                relayoutPoint(v, x-downX, y-downY);
                redrawBezerLine();
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void relayoutPoint(View v, float xgap, float ygap) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
        lp.leftMargin += xgap;
        lp.topMargin += ygap;
        mFrameLayout.requestLayout();
    }

    private void redrawBezerLine() {
        FrameLayout.LayoutParams startL = (FrameLayout.LayoutParams) start.getLayoutParams();
        Point pstart = new Point(startL.leftMargin + 50, startL.topMargin + 50);

        FrameLayout.LayoutParams start1L = (FrameLayout.LayoutParams) start1.getLayoutParams();
        Point pstart1 = new Point(start1L.leftMargin + 50, start1L.topMargin + 50);

        FrameLayout.LayoutParams end1L = (FrameLayout.LayoutParams) end1.getLayoutParams();
        Point pend1 = new Point(end1L.leftMargin + 50, end1L.topMargin + 50);

        FrameLayout.LayoutParams endL = (FrameLayout.LayoutParams) end.getLayoutParams();
        Point pend = new Point(endL.leftMargin + 50, endL.topMargin + 50);

        mBezerLineView.setPoints(pstart, pstart1, pend1, pend);
    }
}
