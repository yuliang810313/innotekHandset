package com.innotek.handset.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.innotek.handset.fragments.BindingRoomFragment;

public class BindingRoomActivity extends BaseActivityWithMenu {
	
	private String roomNo;
	private String tobaccoNo;
	private long roomId;
	private long stationId;
	
	@Override
	protected Fragment createFragment() {
		roomId = getIntent().getExtras().getLong("ROOM_ID");
		stationId = getIntent().getExtras().getLong("STATION_ID");
		
		super.setRoomId(roomId);
		return BindingRoomFragment.newInstance(roomNo, tobaccoNo, stationId);
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("¿¾·¿Æ¥Åä");
		
		Intent intent = getIntent();
		roomNo = intent.getExtras().getString("ROOM_NO");
		tobaccoNo = intent.getExtras().getString("TOBACCO_NO");
		
		super.onCreate(savedInstanceState);
	}
	
}
