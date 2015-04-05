package com.innotek.handset.activities;

import android.util.Log;



public abstract class BaseWorkflowActivity extends ActivityWithPopupMenu {

	
	//烤房所属工场id
	protected long mStationId;
	
	public static final String ROOM_ID = "ROOM_ID";
	public static final String STATION_ID = "STATION_ID";
		
	public static final String TAG = "BaseWorkFlow";
	
	protected void getPreferRoomIdAndStationId(){
		mPreferRoomId = getIntent().getLongExtra(ROOM_ID, 0);
		mStationId = getIntent().getLongExtra(STATION_ID, 0);
		Log.i(TAG, "room_id: " + mPreferRoomId + " - station_id: " + mStationId);
	}
}
