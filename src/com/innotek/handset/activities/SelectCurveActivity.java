package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.SelectCurveFragment;

public class SelectCurveActivity extends BaseWorkflowActivity{


	
	@Override
	protected Fragment createFragment() {
		getPreferRoomIdAndStationId();
		return SelectCurveFragment.newInstance(mPreferRoomId, mStationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("ÇúÏßÑ¡Ôñ");
		super.onCreate(savedInstanceState);
	}

}
