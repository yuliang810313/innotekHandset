package com.innotek.handset.utils;

import android.content.ContentValues;
import android.content.Context;

import com.innotek.handset.entities.DryTobacco;
import com.innotek.handset.entities.Packing;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.entities.Room;
import com.innotek.handset.entities.Tobacco;

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
		//long result = adapter.insertOrUpdatePreferRoom(values);
		long result = adapter.insertPreferRoom(values);
		adapter.close();
		return result;
	}
	
	//绑定成功后更新此烤房阶段为烤前检查
	public int bindingRoom(String userID, String roomID, String tobaccoNo){
		ContentValues values = new ContentValues();
		values.put("room_id", roomID);
		values.put("room_stage_id", 1);
		adapter.open();
		
		int rows = adapter.bindingRoom(userID, tobaccoNo, values);
		
		adapter.close();
		return rows;
	}
	
	//更新烤房阶段
	public void updateRoomStage(long roomId, int stage){
		ContentValues values = new ContentValues();
		values.put("room_stage_id", stage);
		
		adapter.open();
		adapter.updatePreferRoom(values, roomId);
		adapter.close();
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
	
	//保存鲜烟管理
	public void saveNewTobacco(Tobacco tobacco){
		ContentValues values = new ContentValues();
		values.put("tobacco_type", tobacco.getTobaccoType());
		values.put("part", tobacco.getPart());
		values.put("water_content", tobacco.getWaterContent());
		values.put("maturity", tobacco.getMaturity());
		values.put("breed", tobacco.getBreed());
		values.put("quality", tobacco.getQuality());
		values.put("prefer_room_id", tobacco.getPreferRoomId());
		
		adapter.open();
		adapter.saveNewTobacco(values);
		adapter.close();
		
		updateRoomStage(tobacco.getPreferRoomId(), 2);
	}
	
	//保存编烟管理
	public void savePacking(Packing packing){
		ContentValues values = new ContentValues();
		values.put("prefer_room_id", packing.getPreferRoomId());
		values.put("category", packing.getCategory());
		values.put("category_state", packing.getCategoryState());
		values.put("packing_type", packing.getPackingType());
		values.put("packing_amount", packing.getPackingAmount());
		values.put("uniformity", packing.getUniformity());
		
		adapter.open();
		adapter.savePacking(values);
		adapter.close();
		
		updateRoomStage(packing.getPreferRoomId(), 3);
	}
	
	//烘烤后管理
	public void saveTobaccoAfterBaking(DryTobacco tobacco){
		ContentValues values = new ContentValues();
		values.put("prefer_room_id", tobacco.getPreferRoomId());
		values.put("dry_weight", tobacco.getDryWeight());
		values.put("quality", tobacco.getQuality());
		values.put("issue", tobacco.getIssue());
		
		adapter.open();
		adapter.saveDryTobacco(values);
		adapter.close();
		
		updateRoomStage(tobacco.getPreferRoomId(), 6);
		
	}
}
	
