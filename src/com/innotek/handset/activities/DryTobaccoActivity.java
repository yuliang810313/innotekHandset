package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.DryTobaccoFragment;

public class DryTobaccoActivity extends BaseWorkflowActivity{
	
	
	@Override
	protected Fragment createFragment() {
		return DryTobaccoFragment.newInstance(mPreferRoomId, mStationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTitle("øæ∫Û—Ã“∂–≈œ¢");
		super.onCreate(savedInstanceState);
	}

}
