
package com.example.tsdf.utils;

import com.example.tsdf.R;
import com.example.tsdf.view.fragment.ContentFragment;
//import com.example.tsdf.view.fragment.ContentFragment;
import com.example.tsdf.view.fragment.GalleryShowFragment;
import com.example.tsdf.view.fragment.PowerCalculatorFragment;
import com.example.tsdf.view.fragment.ShowMatrixFragment;
import com.example.tsdf.view.fragment.ShowMatrixGalleryFragment;

import android.util.Log;

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
            R.string.title_section4
    };

    private static int[] mIconList = {
            R.drawable.ic_menu_contact,
            R.drawable.ic_menu_delete_played,
            R.drawable.ic_menu_duration
    };

    private static Class<?>[] mClassList = {
            ShowMatrixGalleryFragment.class,
            GalleryShowFragment.class,
            PowerCalculatorFragment.class
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