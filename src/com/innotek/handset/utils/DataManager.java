package com.innotek.handset.utils;

import android.content.ContentValues;
import android.content.Context;

import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.entities.Room;

public class DataManager {
	private DatabaseAdapter adapter;
	
	public DataManager(Context context){
		adapter = new DatabaseAdapter(context);
	}
	
	public int modifyCurveParams(float dry, float wet, int sTime, int dTime,long curveId, int stageNo){
		ContentValues params = new ContentValues();
		
		params.put("dry_value", dry);
		params.put("wet_value", wet);
		params.put("duration_time", dTime);
		params.put("stage_time", sTime);
		
		adapter.open();
		int result = adapter.updateCurveParams(params, curveId, stageNo);
		adapter.close();
		return result;
	}
	
	//Save user prefer room 
	public long savePreferRoom(PreferRoom room){
		ContentValues values = adapter.contentValuesForPreferRoom(room);
		adapter.open();
		long result = adapter.insertOrUpdatePreferRoom(values);
		adapter.close();
		return result;
	}
	
	public int bindingRoom(String userID, String roomID, String tobaccoNo){
		ContentValues values = new ContentValues();
		values.put("room_id", roomID);
		adapter.open();
		
		int rows = adapter.bindingRoom(userID, tobaccoNo, values);
		
		adapter.close();
		return rows;
	}
	
	public void saveRoomStatus(Room room){
		adapter.open();
		adapter.insertOrUpdateRoom(room);
		saveCurveOfRoom(room);
		adapter.close();
	}
	
	public void saveCurveOfRoom(Room room){
		long curveId = adapter.insertCurve(adapter.contentValuesForCurve(room));
		adapter.saveCurveParams(curveId, room);
	}
}
	
