package com.innotek.handset.activities;

import com.innotek.handset.fragments.StationFragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;

public class StationActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return StationFragment.newInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("我的工场");
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	

}
