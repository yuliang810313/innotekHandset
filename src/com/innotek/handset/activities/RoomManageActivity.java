package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.RoomManageFragment;

public class RoomManageActivity extends BaseWorkflowActivity {
	
	@Override
	protected Fragment createFragment() {
		initPreferRoomIdAndStationId();
		return RoomManageFragment.newInstance(mPreferRoomId, mStationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("øæ∑øπ‹¿Ì");
		super.onCreate(savedInstanceState);
	}
	
}
