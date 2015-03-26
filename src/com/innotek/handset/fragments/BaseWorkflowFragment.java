package com.innotek.handset.fragments;

import android.os.Bundle;

import com.innotek.handset.utils.DatabaseAdapter;

public class BaseWorkflowFragment extends BaseFragment {
	
	protected long mPreferRoomId;
	protected long mStationId;
	
	protected DatabaseAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStationId = getArguments().getLong("STATION_ID");
		mPreferRoomId = getArguments().getLong("ROOM_ID");
	}
	
	
}
