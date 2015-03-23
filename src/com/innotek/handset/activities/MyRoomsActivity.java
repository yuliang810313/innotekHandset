package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.RoomsGridFragment;

public class MyRoomsActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return new RoomsGridFragment();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("ÎÒµÄ¿¾·¿");
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
}
