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
import com.innotek.handset.entities.State;
import com.innotek.handset.entities.Station;

public class DatabaseAdapter {
	
	private static final String TAG = "Database Adapter";
	private static final String DB_NAME = "handset";
	private static final int DATABASE_VERSION = 3;
	//private static final String USER_ID = "user_id";
	
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
	
	//
	public ContentValues contentValuesForState(State state){
		ContentValues values = new ContentValues();
		
		values.put("id", state.getId());
		values.put("name", state.getName());
		values.put("user_id", state.getName());
		
		return values;
	}
	
	
	//ContentValue for prefer room
	public ContentValues contentValuesForPreferRoom(PreferRoom room){
		ContentValues values = new ContentValues();
		
		values.put("user_id", room.getUserId());
		values.put("room_no", room.getRoomNo());
		values.put("tobacco_no", room.getTobaccoNo());
		values.put("room_type", room.getRoomType());
		values.put("fan_type", room.getFanType());
		values.put("heating_equipment", room.getHeatingEquipment());
		values.put("person_in_charge", room.getPersonInCharge());
		values.put("room_user", room.getRoomUser());
		values.put("phone", room.getPhone());
		
		return values;
	}
	
	/**
	 * 
	 * @param state_id
	 * @param jObject
	 * @return
	 */
	public ContentValues contentValuesForStation(Station station){
		ContentValues contentValues = new ContentValues();
	
		contentValues.put("id", station.getId());
		contentValues.put("name", station.getName());
		contentValues.put("latitude", station.getLatitude());
		contentValues.put("longitude", station.getLongitude());
		contentValues.put("state_id", station.getState_id());
		
		return contentValues;
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
//		String userID;
		open();
			
	    insertUser(contentValuesForUser(jObject));
//		try{
//			userID = jObject.getString("_id");
//			//Save states that user managed
//			JSONArray states = jObject.getJSONArray("states");
//			int statesLength = states.length();
//			
//			if(statesLength > 0){
//				
//				for(int i = 0; i < statesLength; i++){
//		
//					JSONObject obj = states.getJSONObject(i);
//					State state = new State(obj.getString("_id"), userID, obj.getString("name"));
//					
//					insertState(contentValuesForState(state));
//					
//					saveStations(JSONUtils.getStations(state.getId()), userID);
//				}
//			}
//			JSONUtils.getPreferRooms(userID);
//			
//		}catch(JSONException e){
//			e.printStackTrace();
//		}
		
		close();
	}
	

	public void updateDatabase(String user_id){
		open();
		//update states
		ArrayList<State> states = JSONUtils.getStates(user_id);
		for(int i = 0; i < states.size(); i++){
			State state = states.get(i);
			Log.i(TAG, "Updating states");
			updateState(contentValuesForState(state), state.getId());
			
			ArrayList<Station> stations = JSONUtils.getStations(state.getId());
			for(int j = 0; j < stations.size(); j++){
				Station station = stations.get(j);
				Log.i(TAG, "Updating stations");
				updateStation(contentValuesForStation(station), station.getId());
				
				ArrayList<Room> rooms = JSONUtils.getRooms(station.getId(), 
														  "http://223.4.21.219:8080/stations/" + station.getId() + "/rooms");
				
				for(int k = 0; k < rooms.size(); k++){
					Room room = rooms.get(k);
					Log.i(TAG, "Updating room");
					this.insertOrUpdateRoom(room);
				}
			}
			
		}
		
		
		close();
	}
	
	public int bindingRoom(String userID, String tobaccoNo, ContentValues values){
		return sqlite.update("PREFER_ROOMS", values, "user_id = ? and tobacco_no = ?" , new String[]{userID, tobaccoNo});
	}
	
	/**
	 * 
	 * @param state_id
	 * @param stations
	 */
	private void saveStations(ArrayList<Station> stations, String user_id){
		if(stations != null && !stations.isEmpty()){
			for(int i = 0; i < stations.size(); i++){
				insertStation(contentValuesForStation(stations.get(i)));
				//saveRooms(stations.get(i).getId(), user_id);
			}
		}
	}
	
	
	public void saveRooms(String stationId, String user_id){
		ArrayList<Room> list = JSONUtils.getRooms(stationId,
												 "http://223.4.21.219:8080/stations/" + stationId + "/rooms");
		
		for(int j =0; j < list.size(); j++){
			Room room = list.get(j);
			room.setUser_id(user_id);
			insertOrUpdateRoom(room);
			long curveId = insertCurve(contentValuesForCurve(room));
			saveCurveParams(curveId, room);
			
		}
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
	
	
	
	/**
	 * 
	 * @param user_id
	 * @return
	 */
	public boolean isUserExist(String user_id){
		boolean exist = false;
		Cursor cursor = sqlite.query(true, "USERS", null,  
			       "id = ?", new String[]{user_id}, null, null, null, null, null);
	
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
	
	public Cursor getStates(){
		Cursor cursor = sqlite.query(true, "STATES", new String[]{"id", "name"},  
				       null, null, null, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor;
	}
	
	
	public Cursor getStationsByState(String state_id){
		Cursor cursor = sqlite.query(true, "STATIONS", null,  
			       "state_id = ?", new String[]{state_id}, null, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getRoomsByStation(String stationId){
		Cursor cursor = sqlite.query(true, "ROOMS", null, null, null, null, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getRoomById(String id){
		Cursor cursor = sqlite.query(true, "ROOMS", null, "id=?", new String[]{id}, null, null, null, null, null);
		
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getPreferRoomsByUser(String user_id){
//		Cursor cursor = sqlite.query(true, "ROOMS", null, "user_id = ?", new String[]{user_id}, null, null, null, null, null);
//		cursor.moveToFirst();
		Cursor cursor = sqlite.rawQuery("select * from PREFER_ROOMS, ROOMS where PREFER_ROOMS.room_id = ROOMS.address " +
				"and PREFER_ROOMS.user_id = ? ;", new String[]{user_id});
		if(cursor.moveToFirst())
			return cursor;
		return cursor;
	}
	
	public Cursor getAllRoomsByUser(String user_id){
		Cursor cursor = sqlite.query(true, "ROOMS", null, "user_id = ?", new String[]{user_id}, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public Cursor getCurveParamsByStageAndCurve(int stage, long curve_id){
		Log.i(TAG, "stage is:" + stage + " and curve_id is " + curve_id);
		Cursor cursor = sqlite.query(true, "CURVE_PARAMS", null,  "stage_no = "+ stage +" and curve_id = " + curve_id, null, null, null, null, null, null);
		if(cursor.moveToFirst())
			return cursor;
		return null;
	}
	
	public Cursor getCurveParamsByCurve(long curve_id){

		Cursor cursor = sqlite.query(true, "CURVE_PARAMS", null,  "curve_id = " + curve_id, null, null, null, null, null, null);
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
	public Cursor getPreferRooms(String userId){
		Log.i(TAG, "userId is " + userId);
		Cursor cursor = sqlite.query(true, "PREFER_ROOMS", null, "user_id = ?", new String[]{userId},
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
	
	//Save user
	public long insertUser(ContentValues user){
		return sqlite.insert("USERS", null, user);
	}
	
	//Save states
	public long insertState(ContentValues state){
		return sqlite.insert("STATES", null, state);
	}
	
	//Save stations
	public long insertStation(ContentValues station){
		return sqlite.insert("STATIONS", null, station);
	}
	
	//Save prefer room
	public long insertOrUpdatePreferRoom(ContentValues room){
		String tobaccoNo = room.getAsString("tobacco_no");
		Cursor c = sqlite.query(true, "PREFER_ROOMS", null, "tobacco_no = ?", 
				new String[]{tobaccoNo}, null, null, null, null, null);
		if(c.moveToFirst()){
			return sqlite.update("PREFER_ROOMS", room, "tobaccoNo = ?", new String[]{tobaccoNo});
		}
		return sqlite.insert("PREFER_ROOMS", null, room);
	}
	
	
	//Updates
	public long updateState(ContentValues state, String state_id){
		return sqlite.update("STATES", state, "id= ? ", new String[]{state_id});
	}
	
	public long updateStation(ContentValues station, String station_id){
		return sqlite.update("STATIONS", station, "id = ?",	new String[]{station_id});
	}
	
	
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
	
	
	public void updateRoom(Room room){
		ContentValues contentValues = new ContentValues();
		int value = room.isPrefer()?1:0;
		contentValues.put("is_prefer", value);
		sqlite.update("ROOMS", contentValues, "id = ?" ,new String[]{ room.getId()} );
		
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
