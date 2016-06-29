
package com.example.doanything.utils;

import android.util.Log;

import com.example.doanything.R;
import com.example.doanything.view.fragment.AudioControlFragment;
import com.example.doanything.view.fragment.BezerFragment;
import com.example.doanything.view.fragment.CircleListFragment;
import com.example.doanything.view.fragment.JingdongFragment;
import com.example.doanything.view.fragment.NetEasyFragment;
import com.example.doanything.view.fragment.ViewDragFragment;
import com.example.doanything.view.fragment.ZoomViewFragment;

//import com.example.doanything.view.fragment.ContentFragment;

public class LogUtils {
    final static String TAG = "zhaocheng";

    public final static String ARG_SECTION_NUMBER = "section_number";

    public static int SHOWWELLCOMETIME = 1000;

    private static int screenW = 0;
    private static int screenH = 0;

    private static int[] mStringList = {
            R.string.title_section1,
            R.string.title_section2,
            R.string.title_section3,
            R.string.title_section4,
            R.string.title_section5,
            R.string.title_section6,
            R.string.title_section7
    };

    private static int[] mIconList = {
            R.drawable.ic_menu_contact,
            R.drawable.ic_menu_delete_played,
            R.drawable.ic_menu_duration
    };

    //            Class.forName("com.example.doanything.view.fragment.SwipeRefreshList"),
    private static Class<?>[] mClassList = {
            JingdongFragment.class,
            NetEasyFragment.class,
            AudioControlFragment.class,
            ViewDragFragment.class,
            ZoomViewFragment.class,
            CircleListFragment.class,
            BezerFragment.class
    };

    public static int[] getStringList() {
        return mStringList;
    }

    public static int[] getIconList() {
        return mIconList;
    }

    public static Class<?>[] getClassList() {
        return mClassList;
    }

    public static void print(String msg) {
        Log.d(TAG, msg);
    }

    public static void setScreenSize(int w, int h) {
        screenW = w;
        screenH = h;
    }

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }
}
