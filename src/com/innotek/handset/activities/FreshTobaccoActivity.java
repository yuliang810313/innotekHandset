package com.innotek.handset.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.innotek.handset.fragments.FreshTobaccoFragment;

/**
 * ���̹���
 *
 */
public class FreshTobaccoActivity extends ActivityWithPopupMenu{
	
	private long roomId;
	private long stationId;
	
	
	@Override
	protected Fragment createFragment() {
		//super.setRoomId(roomId);
		return FreshTobaccoFragment.newInstance(roomId, stationId);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		setTitle("���̹���");
		Intent intent = this.getIntent();
		roomId = intent.getExtras().getLong("ROOM_ID");
		stationId = intent.getExtras().getLong("STATION_ID");

		super.onCreate(savedInstanceState);
	}

}
