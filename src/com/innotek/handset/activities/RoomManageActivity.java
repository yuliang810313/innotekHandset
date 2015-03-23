package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.RoomManageFragment;

public class RoomManageActivity extends BaseActivityWithMenu {
	private long roomId;
	private long stationId;
	@Override
	protected Fragment createFragment() {
		roomId = getIntent().getLongExtra("ROOM_ID", 0);
		stationId = getIntent().getExtras().getLong("STATION_ID");
		
		super.setRoomId(roomId);
		return RoomManageFragment.newInstance(roomId, stationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("øæ∑øπ‹¿Ì");
		super.onCreate(savedInstanceState);
	}
	
}
