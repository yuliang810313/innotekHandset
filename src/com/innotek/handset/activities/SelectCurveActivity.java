package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.SelectCurveFragment;

public class SelectCurveActivity extends ActivityWithPopupMenu{

	private long roomId;
	private long stationId;
	
	@Override
	protected Fragment createFragment() {
		roomId = getIntent().getExtras().getLong("ROOM_ID");
		stationId = getIntent().getExtras().getLong("STATION_ID");
		
		//super.setRoomId(roomId);
		return SelectCurveFragment.newInstance(roomId, stationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("ÇúÏßÑ¡Ôñ");
		super.onCreate(savedInstanceState);
	}

}
