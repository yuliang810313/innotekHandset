package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.R;
import com.innotek.handset.fragments.MonitorFragment;

public class MonitorActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    setTitle("#" + getIntent().getExtras().getString("roomAddress"));
	}

	@Override
	protected Fragment createFragment() {
		return new MonitorFragment();
	}

}
