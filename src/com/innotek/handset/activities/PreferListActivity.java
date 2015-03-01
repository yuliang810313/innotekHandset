package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.R;
import com.innotek.handset.fragments.RoomsFragment;


public class PreferListActivity extends BaseActivity {
	@Override
	protected Fragment createFragment() {
		return RoomsFragment.newInstance(null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("¿¾·¿×´Ì¬");
		setContentView(R.layout.container);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	}
}
