
package com.example.doanything.view.fragment;

import com.example.doanything.R;
import com.example.doanything.utils.LogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment extends Fragment {

    public interface ContentInterface {
        void updateTitle(int index);
    }

    private int position;
    private ContentInterface mContentInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            position = b.getInt(LogUtils.ARG_SECTION_NUMBER);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        if (mContentInterface != null) {
            mContentInterface.updateTitle(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable
            ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.section_label);
        tv.setText("==" + position + "==");
        return rootView;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void setContentInterface(ContentInterface ci) {
        mContentInterface = ci;
    }
}
