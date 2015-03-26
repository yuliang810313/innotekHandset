package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.PackingTobaccoFragment;

public class PackingTobaccoActivity extends BaseWorkflowActivity{
	
	
	@Override
	protected Fragment createFragment() {
//		long roomId = getIntent().getExtras().getLong("ROOM_ID");
//		long stationId = getIntent().getExtras().getLong("STATION_ID");
		
		return PackingTobaccoFragment.newInstance(mPreferRoomId, mStationId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		setTitle("±à(×°)ÑÌÐÅÏ¢");
		super.onCreate(savedInstanceState);
	}
	
}
