package com.example.doanything.view.CustomizedView;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by administrator on 1/22/16.
 */
public class FoldDrawerLayout extends DrawerLayout {
    public FoldDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoldDrawerLayout(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        int count = getChildCount();
        for(int i=0;i<count;i++) {
            View child = getChildAt(i);
            if (isDrawerView_New(child)) {
                removeView(child);
                FolderLayout foldlayout = new FolderLayout(
                        getContext());
                foldlayout.addView(child);
                ViewGroup.LayoutParams layPar = child.getLayoutParams();
                addView(foldlayout, i, layPar);
            }
        }
        setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView instanceof FolderLayout) {
                    FolderLayout foldLayout = ((FolderLayout) drawerView);
                    foldLayout.setScale(slideOffset);
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

    boolean isDrawerView_New(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        return (absGravity & (Gravity.LEFT | Gravity.RIGHT | Gravity.START | Gravity.END)) != 0;
    }
}
