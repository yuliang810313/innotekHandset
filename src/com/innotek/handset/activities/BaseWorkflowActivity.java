package com.innotek.handset.activities;



public abstract class BaseWorkflowActivity extends ActivityWithPopupMenu {

	//����id
	//protected long mPreferRoomId;
	
	//������������id
	protected long mStationId;
	
	public static final String ROOM_ID = "ROOM_ID";
	public static final String STATION_ID = "STATION_ID";
		
	protected void initPreferRoomIdAndStationId(){
		mPreferRoomId = getIntent().getLongExtra(ROOM_ID, 0);
		mStationId = getIntent().getLongExtra(STATION_ID, 0);
	}
}
