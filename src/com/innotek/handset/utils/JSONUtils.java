package com.innotek.handset.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.innotek.handset.entities.CurveParams;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.entities.Room;
import com.innotek.handset.entities.State;
import com.innotek.handset.entities.Station;


public class JSONUtils {
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String getJSON(String url){
		
		String result = "";
		HttpGet httpRequest = new HttpGet(url);
		try{
		    HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		    if(httpResponse.getStatusLine().getStatusCode() == 200){
		    	HttpEntity httpEntity = httpResponse.getEntity();
		    	result = EntityUtils.toString(httpEntity);
		    	result.replaceAll("/r", "");
		    }else
		    	httpRequest.abort();
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result = e.getMessage();
		}catch(IOException e){
			e.printStackTrace();
			result = e.getLocalizedMessage();
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postJSON(String url, List<NameValuePair> params){
		String result = "";
		
		HttpPost httpRequest = new HttpPost(url);
		Log.i(TAG, "Send post to api");
		try{
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);
			}else{
				Log.i(TAG, "Request about");
				httpRequest.abort();
			}
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			result = e.getMessage();
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result = e.getMessage();
		}catch(IOException e){
			e.printStackTrace();
			result = e.getMessage();
		}
		Log.i(TAG, result);
		return result;
	}
	
	
	public static ArrayList<Station> getStations(String stateId){
		ArrayList<Station> list = new ArrayList<Station>();
		String result = getJSON("http://223.4.21.219:8080/states/" + stateId + "/stations");
		if(result != null){
			try{
				JSONObject jObject = new JSONObject(result);
				JSONArray stations = jObject.getJSONArray("stations");
				for(int i = 0; i < stations.length(); i++){
					JSONObject jObj = stations.getJSONObject(i);
					Station station = new Station(jObj.getString("_id"),
												  jObj.getString("name"),
												  jObj.getDouble("latitude"),
												  jObj.getDouble("longitude"));
					station.setState_id(stateId);
					list.add(station);
				}
			}catch(Exception e){
				Log.e("JSON_TAG", e.getMessage());
			}
		}
		return list;
	}
	
	
	/**
	 * 
	 * @param stationId
	 * @param station_id
	 * @param flag
	 * @return
	 */
	public static ArrayList<Room> getRooms(String stationId, String url){
		ArrayList<Room> list = new ArrayList<Room>();
		String results = getJSON(url);
		
		if(results != null){
			try{
				
				JSONObject jObj = new JSONObject(results);
				JSONArray array = jObj.getJSONArray("rooms");
				for(int i = 0 ; i < array.length(); i++){
					Room room = createRoom(array.getJSONObject(i));
					
					list.add(room);
				}
			}catch(Exception e){
				Log.e("JSON_TAG", e.getMessage());
			}
		}
		Log.i(TAG, "The result is " + list);
		return list;
	}
	
	public static Room createRoom(JSONObject jObject){
		//JSONObject jObject = array.getJSONObject(i);
		Room room = new Room();	
		try{
			JSONArray a = jObject.getJSONArray("status");
		
			int [] datas = new int[a.length()];
			for(int j = 0; j < a.length(); j++){
				datas[j] = a.getInt(j);
			}
		
			int infoType = jObject.getInt("infoType");
		
			String [] status = Room.getMsgsContent(infoType, datas);
			Log.i(TAG,"Status: " + Arrays.toString(status));
		
			
		
			switch(infoType){
			case 0:
			
				room.setId(jObject.getString("_id"));
				room.setInfoType(jObject.getInt("infoType"));
				room.setAddress(jObject.getString("address"));
				room.setMidAddress(jObject.getString("midAddress"));
				room.setUpdatedAt(jObject.getString("updatedAt"));
				room.setDryAct(Float.parseFloat(status[0]));
				room.setWetAct(Float.parseFloat(status[1]));
				room.setStatus(Arrays.toString(status));
				//room.setStation_id(stationId);
				break;
			case 2:
				
				room.setId(jObject.getString("_id"));
				room.setInfoType(jObject.getInt("infoType"));
				room.setAddress(jObject.getString("address"));
				room.setMidAddress(jObject.getString("midAddress"));
				room.setUpdatedAt(jObject.getString("updatedAt"));
				room.setDryTarget(Float.parseFloat(status[0]));
				room.setDryAct(Float.parseFloat(status[2]));
				room.setWetAct(Float.parseFloat(status[3]));
				room.setWetTarget(Float.parseFloat(status[1]));
				room.setAmount(Float.parseFloat(status[4]));
				room.setStatus(Arrays.toString(status));
				//room.setStation_id(stationId);
				break;
			};
		}catch(JSONException e){
			e.printStackTrace();	
		}
		return room;
	}
	
	
	public static ArrayList<CurveParams> getCurveByRoom(Room room){
		ArrayList<CurveParams> list = new ArrayList<CurveParams>();
		String results = getJSON("http://223.4.21.219:8080/curves/" + room.getAddress() + "/" + room.getMidAddress());
		if(results != null){
			try{
				JSONObject object = new JSONObject(results);
				Log.i(TAG, "object is " + object);
				JSONObject curve = object.getJSONObject("curve");
				JSONArray params = curve.getJSONArray("curves");
				int stage = params.getInt(0);
				Log.i(TAG, "the curve stage is " + stage);
				JSONArray drys = params.getJSONArray(1);
				JSONArray wets = params.getJSONArray(2);
				JSONArray times = params.getJSONArray(3);
				

				for(int i = 0; i < drys.length(); i++ ){
					CurveParams cParams = new CurveParams();
					cParams.setStage(stage);
					cParams.setDry(drys.getDouble(i));
					cParams.setWet(wets.getDouble(i));
					cParams.setDurationTime(times.getInt(i*2));
					cParams.setStageNo(i+1);
					if(2 * i + 1 < 19)
						cParams.setStageTime(times.getInt(2*i+1));
					list.add(cParams);
				}
				
			}catch(Exception e){
				Log.e(TAG, e.getMessage());
			}
		}
		return list;
	}
	
	public static ArrayList<State> getStates(String user_id){
		ArrayList<State> list = new ArrayList<State>();
		String results = getJSON("http://223.4.21.219:8080/users/" + user_id + "/states");
		if(results != null){
			try{
				JSONObject obj = new JSONObject(results);
				JSONArray states = obj.getJSONArray("states");
				for(int i = 0; i < states.length(); i++){
					JSONObject object = states.getJSONObject(i);
					State state = new State(object.getString("_id"), user_id, object.getString("name"));
					list.add(state);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	//获取用户偏好烤房
	public static ArrayList<PreferRoom> getPreferRooms(String userID){
		ArrayList<PreferRoom> list = new ArrayList<PreferRoom>();
		String result = getJSON("https://223.4.21.219:8080/users/" + userID + "/prefers");
		if(result != null){
			try{
				JSONObject results = new JSONObject(result);
				JSONArray prefers = results.getJSONArray("prefers");
				for(int i = 0; i < prefers.length(); i++){
					JSONObject object = prefers.getJSONObject(i);
					PreferRoom room = new PreferRoom();
					room.setUserId(userID);
					room.setRoomNo(object.getString("room_no"));
					room.setTobaccoNo(object.getString("tobacco_no"));
					room.setRoomUser(object.getString("room_user"));
					room.setRoomType(object.getString("room_type"));
					room.setFanType(object.getString("fan_type"));
					room.setHeatingEquipment(object.getString("heating_equipment"));
					room.setPersonInCharge(object.getString("person_in_charge"));
					room.setPhone(object.getString("phone"));
					room.setRoomID(object.getString("room_id"));
					
					list.add(room);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	public static String getInformation(String address, String midAddress){
		return JSONUtils.getJSON("http://223.4.21.219:8080/informations/" + address + "/" + midAddress);
	}
	
	public static String getRoomById(String roomID){
		return JSONUtils.getJSON("http://223.4.21.219:8080/rooms/" + roomID);
	}
	
	private static final String TAG = "JSON_UTIL";
}
