package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.R;
import com.innotek.handset.fragments.RoomsGridFragment;

public class RoomsGridActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		String station_id = getIntent().getExtras().getString("STATION_ID");
		
		return RoomsGridFragment.newInstance(station_id);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("����״̬");
		setContentView(R.layout.container);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	}

}
