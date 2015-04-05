package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.ArbitrateFragment;

public class ArbitrateActivity extends BaseWorkflowActivity{

	@Override
	protected Fragment createFragment() {
		getPreferRoomIdAndStationId();
		return ArbitrateFragment.newInstance(mPreferRoomId, mStationId);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("’˘“È÷Ÿ≤√");
		super.onCreate(savedInstanceState);		
	}

}
