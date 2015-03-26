package com.innotek.handset.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.innotek.handset.entities.CurveParams;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.entities.Room;

public class DatabaseAdapter {
	
	private static final String TAG = "Database Adapter";
	private static final String DB_NAME = "handset";
	private static final int DATABASE_VERSION = 3;
	
	private SQLiteDatabase sqlite;
	private DatabaseHelper dbHelper;
	
	public DatabaseAdapter(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	

	//解析JSON数据，创建User ContentValues
	public ContentValues contentValuesForUser(JSONObject jObject){
		ContentValues user = new ContentValues();
		try{
			user.put("id", jObject.getString("_id"));
			user.put("userId", jObject.getString("userId"));
			user.put("firstName", jObject.getString("firstName"));
			user.put("lastName", jObject.getString("lastName"));
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		return user;
	}
		
	
	//ContentValue for prefer room
	public ContentValues contentValuesForPreferRoom(PreferRoom room){
		ContentValues values = new ContentValues();
		
		values.put("user_id", room.getUserId());
		values.put("room_no", room.getRoomNo());
		values.put("station_id", room.getStationId());
		values.put("room_id", room.getRoomID());
		values.put("tobacco_no", room.getTobaccoNo());
		values.put("room_type", room.getRoomType());
		values.put("fan_type", room.getFanType());
		values.put("heating_equipment", room.getHeatingEquipment());
		values.put("person_in_charge", room.getPersonInCharge());
		values.put("room_user", room.getRoomUser());
		values.put("phone", room.getPhone());
		values.put("group_name", room.getGroupName());
		values.put("ac_state", room.getAcState());
		values.put("fan_state", room.getFanState());
		values.put("blower_state", room.getBlowerState());
		values.put("heating_state", room.getHeatingState());
		values.put("air_inlet_state", room.getAirState());
		values.put("kettle_state", room.getKettleState());
		values.put("other", room.getOther());
		values.put("created_at", room.getCreatedAt().toString());
		values.put("room_stage_id", room.getRoomStageId());
		return values;
	}
	

	
	/**
	 * 
	 * @param title
	 * @return
	 */
	public ContentValues contentValuesForCurve(Room room){
		ContentValues curve = new ContentValues();
		curve.put("room_id", room.getId());
		curve.put("title", room.getMidAddress() + "-" + room.getAddress() + "-" + new Date());
		return curve;
	}
	
   /**
   * 
   * @param curve_id
   * @param dry
   * @param wet
   * @param stageTime
   * @param durationTime
   * @param stage
   * @return
   */
	public ContentValues contentValuesForCurveParams(CurveParams curveParams){
		ContentValues params = new ContentValues();
		params.put("curve_id", curveParams.getCurveId());
		params.put("stage", curveParams.getStage());
		params.put("dry_value", curveParams.getDry());
		params.put("wet_value", curveParams.getWet());
		params.put("duration_time", curveParams.getDurationTime());
		params.put("stage_time", curveParams.getStageTime());
		params.put("stage_no", curveParams.getStageNo());
		return params;
	}
	
	
	
	
	
	/**
	 * Save user and user's stations
	 * @param jObject
	 */
	public void initUserData(Context context, JSONObject jObject){
		open();
	    insertUser(contentValuesForUser(jObject));
		close();
	}
	
	
	//定时更新用户烤房烘烤数据
	public void updateDatabase(String userId){
		open();
		Cursor cursor = getAllRoomsByUser(userId);
		if(cursor != null)
			do{
				String roomId = cursor.getString(cursor.getColumnIndex("address"));
				String result = JSONUtils.getRoomById(roomId);
				
				try{
					JSONObject jsonObject = new JSONObject(result);
					JSONObject obj = jsonObject.getJSONObject("room");
					Room room = JSONUtils.createRoom(obj);
					insertOrUpdateRoom(room);
					
					//更新专家曲线
					
				}catch(JSONException e){
					e.printStackTrace();
				}
			}while(cursor.moveToNext());
		close();
	}
	
	public int bindingRoom(String userID, String tobaccoNo, ContentValues values){
		return sqlite.update("PREFER_ROOMS", values, "user_id = ? and tobacco_no = ?" , new String[]{userID, tobaccoNo});
	}
	
	
	public void saveCurveParams(long curveId, Room room){

		ArrayList<CurveParams> curveParamsList = JSONUtils.getCurveByRoom(room);
		if(curveParamsList.size() > 0){
			for(int k = 0; k < curveParamsList.size(); k++){
				CurveParams curveParams = curveParamsList.get(k);
				curveParams.setCurveId(curveId);
				Log.i(TAG, "insert curve " + curveId);
				insertCurveParams(contentValuesForCurveParams(curveParams));
				
				int currentStage = curveParams.getStage();
				//room.setCurrentStage(currentStage);
				ContentValues values = new ContentValues();
				values.put("current_stage", currentStage);
				sqlite.update("ROOMS", values, "id = ?", new String[]{room.getId()});
				
			}
		}
	}
	
	public Cursor getCurveParamsByRoomId(String id){
		Cursor cursor = sqlite.rawQuery("select curVE_PARAMS.dry_value," +
										"curvE_PARAMS.wet_value," +
										"curVE_PARAMS.stage," +
										"curVE_PARAMS.curve_id, " +
										"curVE_PARAMS.stage_time," +
										"curVE_PARAMS.duration_time " +
										"from curvE_PARAMS, curVES, rooms where rooms.id = curveS.room_id and rooms.id =? ;",
										new String[]{id});
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	/**
	 * 
	 * @param user_id
	 * @return
	 */
	public boolean isUserExist(String user_id){
		boolean exist = false;
		Cursor cursor = sqlite.query(true, "USERS", null,  
			       					"id = ?", new String[]{user_id},
			       					 null, null, null, null, null);
	
		exist = cursor.moveToFirst();
		
		return exist;
	}
	
	public Cursor getUser(String user_id){
		Cursor cursor = sqlite.query(true, "USERS", null,  
			       "id = ?", new String[]{user_id}, null, null, null, null, null);
	
		if(cursor!= null)
			cursor.moveToFirst();
		return cursor;
	}
	
	
	public Cursor getRoomById(String id){
		Cursor cursor = sqlite.query(true, "ROOMS", null, "id=?", new String[]{id}, null, null, null, null, null);
		
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	public Cursor getPreferRoomsByUser(String user_id){

		Cursor cursor = sqlite.rawQuery("select * from PREFER_ROOMS, ROOMS where PREFER_ROOMS.room_id = ROOMS.address " +
										"and PREFER_ROOMS.user_id = ? ;",
										new String[]{user_id});
		if(cursor.moveToFirst())
			return cursor;
		return cursor;
	}
	
	public Cursor getAllRoomsByUser(String user_id){
		Cursor cursor = sqlite.query(true, "ROOMS", null, "user_id = ?", new String[]{user_id}, null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	
	public Cursor getCurveParamsByStageAndCurve(int stage, long curve_id){
		Log.i(TAG, "stage is:" + stage + " and curve_id is " + curve_id);
		Cursor cursor = sqlite.query(true, "CURVE_PARAMS", null, 
									 "stage_no = "+ stage +" and curve_id = " + curve_id, 
									 null, null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	
	public Cursor getCurveParamsByCurve(long curve_id){

		Cursor cursor = sqlite.query(true, "CURVE_PARAMS", null, 
									"curve_id = " + curve_id, null,
									null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Cursor getCurveParamsByRoom(String room){
		Cursor cursor = sqlite.query(true, "CURVES", null, "room_id=?" , new String[]{room}, null, null, null, null, null);
		cursor.moveToFirst();
		long curve_id = cursor.getLong(cursor.getColumnIndex("_id"));
		
	    cursor = sqlite.query(true, "CURVE_PARAMS", null, "curve_id=" + curve_id , null, null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	//Get all prefer rooms that created by user
	public Cursor getPreferRooms(String userId, long stationId){
		Cursor cursor = sqlite.query(true, "PREFER_ROOMS", null, 
									 "user_id = ? and room_id is not null and station_id = " + 
									stationId, new String[]{userId},
				null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	
	public Cursor getPreferRoomById(long id){
		Log.i(TAG, "ID is " + id);
		Cursor cursor = sqlite.query(true, "PREFER_ROOMS", null, "_id = " + id, null,
				null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	public Cursor getFreshTobaccoByPreferId(long preferRoomId){
		Cursor cursor = sqlite.query(true, "NEW_TOBACCO", null, "prefer_room_id = " + preferRoomId, null,
				null, null, null, null, null);
//		Cursor cursor = sqlite.rawQuery("select * from new_TOBACCO t left outer join photoS p on p.prefer_room_id = t.prefer_room_id " +
//				"where t.prefer_room_id = " + preferRoomId +";",
//				null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	
	public Cursor getPhotosByTypeAndPreferRoomId(int type, long preferRoomId){
		Cursor cursor = sqlite.query(true, "PHOTOS", null, "photo_type = " + type +" and prefer_room_id = " + preferRoomId,
				null, null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	//Save user
	public long insertUser(ContentValues user){
		return sqlite.insert("USERS", null, user);
	}
	
	//Save Photo
	public long insertPhoto(ContentValues values){
		return sqlite.insert("PHOTOS", null, values);
	}
	
//	//Save states
//	public long insertState(ContentValues state){
//		return sqlite.insert("STATES", null, state);
//	}
//	
//	//Save stations
//	public long insertStation(ContentValues station){
//		return sqlite.insert("STATIONS", null, station);
//	}
	
	
	public long findPreferRoomByUserAndAddress(String userId, String address){
		Cursor c = sqlite.rawQuery("select * from PREFER_ROOMS where user_id =? and room_id =?", new String[]{userId, address});
		if(c.moveToFirst())
			return c.getLong(c.getColumnIndex("_id"));
		return 0;
	}
	
	
	
	//Save prefer room
	public long insertOrUpdatePreferRoom(ContentValues room){
		String tobaccoNo = room.getAsString("tobacco_no");
		Cursor c = sqlite.query(true, "PREFER_ROOMS", null, "tobacco_no = ?", 
				new String[]{tobaccoNo}, null, null, null, null, null);
		if(c.moveToFirst()){
			return sqlite.update("PREFER_ROOMS", room, "tobacco_no = ?", new String[]{tobaccoNo});
		}
		return sqlite.insert("PREFER_ROOMS", null, room);
	}
	
	public long insertPreferRoom(ContentValues preferRoom){
		
		String userId = preferRoom.getAsString("user_id");
		String tobaccoNo = preferRoom.getAsString("tobacco_no");
		
		Cursor c = sqlite.rawQuery("select * from prefer_rooms where tobacco_no =?;", new String[]{tobaccoNo});
		if(c.moveToFirst()){
			
			return sqlite.update("PREFER_ROOMS", preferRoom, "user_id = ? and tobacco_no = ?", new String[]{userId, tobaccoNo});
			
			
		}else{
			return sqlite.insert("PREFER_ROOMS", null, preferRoom);
		}		
		
		
	}
	
 	public int updatePreferRoom(ContentValues preferRoom){
		String tobaccoNo = preferRoom.getAsString("tobacco_no");
		Cursor c = sqlite.query(true, "PREFER_ROOMS", null, "tobacco_no = ?", 
				new String[]{tobaccoNo}, null, null, null, null, null);
		if(c.moveToFirst()){
			return sqlite.update("PREFER_ROOMS", preferRoom, "tobacco_no = ?", new String[]{tobaccoNo});
		}
		return 0;
	}
	
	
	
	public Cursor findStations(){
		Cursor c = sqlite.rawQuery("select * from STATIONS", null);
		if(c.moveToFirst())
			return c;
		return null;
	}
	
	
	
	
	
	
	//更新烤房阶段
	public int updatePreferRoom(ContentValues values, long roomId){
		return sqlite.update("PREFER_ROOMS", values, "_id = " + roomId, null);
	}
	
	//保存编烟管理
	public long savePacking(ContentValues values){
		return sqlite.insert("PACKINGS", null, values);
	}
	
	public Cursor findDryTobacco(long preferId){
		Cursor cursor = sqlite.rawQuery("select * from AFTER_BAKING where prefer_room_id = " + preferId, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	public Cursor findPacking(long preferId){
		Cursor cursor = sqlite.rawQuery("select * from PACKINGS where prefer_room_id = " + preferId	, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
//	//Updates
//	public long updateState(ContentValues state, String state_id){
//		return sqlite.update("STATES", state, "id= ? ", new String[]{state_id});
//	}
//	
//	public long updateStation(ContentValues station, String station_id){
//		return sqlite.update("STATIONS", station, "id = ?",	new String[]{station_id});
//	}
	
	
	//Save or update room
	public void insertOrUpdateRoom(Room room){
		
		ContentValues contentValues = new ContentValues();

		Cursor cursor = sqlite.query(true, "ROOMS", null, "id = ?" , new String[]{room.getId()}, null, null, null, null, null);
			//更新room
			if(cursor.moveToFirst()){
				contentValues.put("updatedAt", room.getUpdatedAt());
				contentValues.put("dry_act", room.getDryAct());
				contentValues.put("wet_act", room.getWetAct());
							
				sqlite.update("ROOMS", contentValues, "id = ?" ,new String[]{ room.getId()} );
				Log.i(TAG, "update room " + room.getId());
			}else{
				//插入新room
				contentValues.put("id", room.getId());
				contentValues.put("station_id", room.getStation_id());
				contentValues.put("user_id", room.getUser_id());
				contentValues.put("address", room.getAddress());
				contentValues.put("midAddress", room.getMidAddress());
				contentValues.put("isBelow", room.getIsBelow());
				contentValues.put("info_type", room.getInfoType());
				contentValues.put("updatedAt", room.getUpdatedAt());
				contentValues.put("amount", room.getAmount());
				contentValues.put("dry_act", room.getDryAct());
				contentValues.put("dry_target", room.getDryTarget());
				contentValues.put("wet_act", room.getWetAct());
				contentValues.put("wet_target", room.getWetTarget());
				contentValues.put("stage_time", room.getStageTime());
				contentValues.put("status", room.getStatus());
				
				sqlite.insert("ROOMS", null, contentValues);
				Log.i(TAG, "insert new room " + room.getId());
			}
	}
	
		
	//Save Curve
	public long insertCurve(ContentValues curve){
		return sqlite.insert("CURVES", null, curve);
	}
	
	//Save Curve Params
	public long insertCurveParams(ContentValues params){
		return sqlite.insert("CURVE_PARAMS", null, params);
	}
	
	
	//Update curve params
	public int updateCurveParams(ContentValues params, long curveId, int stageNo){
		Log.i(TAG, "update curve params");
		return sqlite.update("CURVE_PARAMS", params, "curve_id = " + curveId + " and stage_no= " + stageNo	, null);
	}
	
	
	//Clean database
	public boolean cleanStates(){
		return sqlite.delete("STATES", null, null) > 0;
	}
	
	public boolean cleanUsers(){
		return sqlite.delete("USERS", null, null) > 0;
	}
	
	public boolean cleanCurves(){
		return sqlite.delete("CURVES", null, null) > 0;
	}
	
	public boolean cleanCurveParams(){
		return sqlite.delete("CURVE_PARAMS", null, null) > 0;
	}
	
	public boolean cleanStations(){
		return sqlite.delete("STATIONS", null, null) > 0;
	}

	public boolean cleanRooms(){
		return sqlite.delete("ROOMS", null, null) > 0;
	}
	
	//鲜烟管理
	public long saveNewTobacco(ContentValues values){
		long preferId = values.getAsLong("prefer_room_id");
		Cursor cursor = sqlite.rawQuery("select * from NEW_TOBACCO where prefer_room_id =" + preferId, null);
		if(cursor.moveToFirst()){
			return sqlite.update("NEW_TOBACCO", values, "prefer_room_id = " + preferId, null);
		}else
			return sqlite.insert("NEW_TOBACCO", null, values);
	}
	
	//烘烤后管理
	public long saveDryTobacco(ContentValues values){
		long preferId = values.getAsLong("prefer_room_id");
		Cursor cursor = sqlite.rawQuery("select * from AFTER_BAKING where prefer_room_id = " + preferId, null);
		if(cursor.moveToFirst())
			return sqlite.update("AFTER_BAKING", values, "prefer_room_id = " + preferId, null);
		else
			return sqlite.insert("AFTER_BAKING", null, values);
	}
	
	

	// Database helper init methods
	public static void resetDatabase(Context context){
		try{
			String path = "/data/data/" + context.getPackageName() + "/databases";
			File file = new File(path);

			if(file.exists())
				file.delete();
			
			file.mkdirs();
			file.createNewFile();
			copyFile(context.getAssets().open("handset"), new FileOutputStream(path + "/handset"));
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException{
		//Copy 1k bytes at a time
		byte[] buffer = new byte[1024];
		int length = 0;
		while((length = in.read(buffer)) > 0){
			out.write(buffer, 0 , length);
		}
		in.close();
		out.close();
		Log.i(TAG, "DATABASE copyed success");
		
	}
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context context){
			super(context, DB_NAME, null, DATABASE_VERSION);
			//copyDatabaseFile(context);
		}
		
		@Override
		public void onCreate(SQLiteDatabase sqliteDatabase){
			
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(TAG, "Upgrading database form version " + oldVersion + " to " +
						newVersion + ", which will destroy all old data");
			db.execSQL("DROP DATABASE IF EXISTS");
		}
	}
	
	public DatabaseAdapter open(){
		sqlite = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public static interface ISaveStatus{
		void onSaved(String saveStatus);
	}
}
