package com.example.demo_zhy_23_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity
{
	protected abstract Fragment createFragment();
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
	
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment =fm.findFragmentById(R.id.id_fragment_container);
		
		if(fragment == null )
		{
			fragment = createFragment() ;
			
			fm.beginTransaction().add(R.id.id_fragment_container,fragment).commit();
		}
	}
	
}
