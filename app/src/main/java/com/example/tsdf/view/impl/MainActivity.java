
package com.example.tsdf.view.impl;

import com.example.tsdf.NavigationDrawerFragment;
import com.example.tsdf.R;
import com.example.tsdf.tabpager.ViewPagerTabs;
import com.example.tsdf.view.adapter.ContentPagerAdapter;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private ViewPager mViewPager;

    private ViewPagerTabs mViewPagerTabs;

    private int mPagerIndex;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainContent();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    private void initMainContent() {
        // TODO Auto-generated method stub
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new ContentPagerAdapter(this, getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                mViewPagerTabs.onPageScrollStateChanged(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                mViewPagerTabs.onPageScrolled(arg0, arg1, arg2);
            }

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                mViewPagerTabs.onPageSelected(arg0);
                mPagerIndex = arg0;
            }
        });

        mViewPagerTabs = (ViewPagerTabs) findViewById(R.id.pager_header);
        mViewPagerTabs.setViewPager(mViewPager);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getPagerIndex() {
        // TODO Auto-generated method stub
        return mPagerIndex;
    }

}
