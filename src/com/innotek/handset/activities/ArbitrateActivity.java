package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.ArbitrateFragment;

public class ArbitrateActivity extends ActivityWithPopupMenu{

	@Override
	protected Fragment createFragment() {
		long roomId = getIntent().getExtras().getLong("ROOM_ID");
		long stationId = getIntent().getExtras().getLong("STATION_ID");
		
		//super.setRoomId(roomId);
		return ArbitrateFragment.newInstance(roomId, stationId);
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("’˘“È÷Ÿ≤√");
		super.onCreate(savedInstanceState);
		
	}





}
