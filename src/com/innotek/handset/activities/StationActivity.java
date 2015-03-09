package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.R;
import com.innotek.handset.fragments.StationFragment;


public class StationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("��վ�б�");
		setContentView(R.layout.container);
	}

	@Override
	protected Fragment createFragment() {
		return new StationFragment();
	}


	
}
