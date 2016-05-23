package com.laocuo.glassball.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.laocuo.glassball.R;
import com.laocuo.glassball.utils.L;
import com.laocuo.glassball.view.GlassGameView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GlassGameFragment.GlassGameInterface} interface
 * to handle interaction events.
 * Use the {@link GlassGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlassGameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GlassGameInterface mListener;

    private GlassGameView mGlassGameView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GlassGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GlassGameFragment newInstance(String param1, String param2) {
        GlassGameFragment fragment = new GlassGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GlassGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_glass_game, container, false);
        mGlassGameView = (GlassGameView) v.findViewById(R.id.glassgamview);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        mGlassGameView.startUpdateBall();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_glass_ball_sub_game,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mGlassGameView.startUpdateBall();
            return true;
        } else if (id == R.id.action_add) {
            mGlassGameView.addBrick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GlassGameInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mGlassGameView.stopUpdateBall();
    }

    public interface GlassGameInterface {
        // TODO: Update argument type and name
        public void onBackPress();
    }

}
