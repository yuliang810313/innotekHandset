package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.MonitorActivity;
import com.innotek.handset.entities.Room;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.RoomsGridAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;


public class RoomsGridFragment extends Fragment implements LoaderCallbacks<Cursor>{

	private GridView gridView;
	private RoomsGridAdapter mAdapter;
	private TextView mAlertRooms;
	private TextView mNormalRooms;
	
	
	private static String stationId;
	private static String userID;
	
	
	//Public KEYS
	public static final String STATION_ID = "stationId"; 
	public static final String ROOM_ID = "room_id";
	public static final String INFO_TYPE = "infoType";
	public static final String ROOM_ADDRESS = "roomAddress";
	public static final String MID_ADDRESS = "midAddress";
	private static final String TAG = "GridFragment";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences pref = getActivity().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		
		userID = pref.getString("USER_ID", null);	
	
		getLoaderManager().initLoader(3, null, this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_rooms, container, false);
		
		mAlertRooms = (TextView) view.findViewById(R.id.id_alert_rooms);
		mNormalRooms = (TextView) view.findViewById(R.id.id_normal_rooms);
		
		gridView = (GridView) view.findViewById(R.id.grid_rooms);
		gridView.setOnItemClickListener(new OnItemClickListener(){
			
			 @Override
			 public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				 Room room = (Room) mAdapter.getItem(position);
		         startMonitor(room);    
			 }
		});
		
		
//		gridView.setOnItemLongClickListener(new OnItemLongClickListener(){
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				isLongClicked = true;
//				
//				Room room = (Room)mAdapter.getItem(position);
//				setChangeCheckBoxState(parent);
//				
//				
//				//layout.setVisibility(View.VISIBLE);
//				//Set All CheckBox visible
////				int count = parent.getChildCount();
////				for(int i = 0; i < count; i++){
////					LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
////					RelativeLayout relativeLayout = (RelativeLayout)linearLayout.getChildAt(0);
////					CheckBox checkBox = (CheckBox)relativeLayout.getChildAt(1);
////					checkBox.setVisibility(View.VISIBLE);
////				}
//				//setCheckBox(checkBox);
//				//room.setPrefer(checkBox.isChecked());
////				if(preferList.contains(room)){
////					preferList.remove(room);
////				}else{
////					preferList.add(room);
////				}
////			
////				
////				if(preferList.isEmpty()){
////					mSaveButton.setEnabled(false);
////				}else{
////					mSaveButton.setEnabled(true);
////				}
//				
//				return true;
//			}
			

//		});
		
		mAdapter = new RoomsGridAdapter(getActivity());
		
		return view;
	}
	
	
/*	private void setCheckBox(CheckBox checkBox){
		if(checkBox.isChecked())
    		checkBox.setChecked(false);
    	else
    		checkBox.setChecked(true);
	}
	
	private void setChangeCheckBoxState(AdapterView<?> parent){
		
		int count = parent.getChildCount();
		for(int i = 0; i < count; i++){
			LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
			RelativeLayout relativeLayout = (RelativeLayout)linearLayout.getChildAt(0);
			CheckBox checkBox = (CheckBox)relativeLayout.getChildAt(1);
			if(checkBox.isShown()){
				checkBox.setVisibility(View.GONE);
				isLongClicked = false;
			}
			else
				checkBox.setVisibility(View.VISIBLE);
		}
		
	}*/
	
	
	public void startMonitor(Room room){
		  Intent intent = new Intent(getActivity(), MonitorActivity.class);
		  
          intent.putExtra(ROOM_ID, room.getId());
          intent.putExtra(INFO_TYPE , room.getInfoType());
          intent.putExtra(ROOM_ADDRESS, room.getAddress());
          intent.putExtra(MID_ADDRESS, room.getMidAddress());
          intent.putExtra("ROOM_NO", room.getRoomNo());
          intent.putExtra("TOBACCO_NO", room.getTobaccoNo());
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          
          startActivity(intent);
          
	}
	
	public static RoomsGridFragment newInstance(String stationID){
		Bundle args = new Bundle();
		args.putString("stationId", stationID);
		RoomsGridFragment fragment = new RoomsGridFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	
	public int alertElements(ArrayList<Room> rooms){
		int counter = 0;
		for(int i = 0; i < rooms.size(); i++){
			if(rooms.get(i).getInfoType() == 0)
				counter++;
			continue;
		}
		return counter;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new RoomsGridCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		
		if(c.moveToFirst()){
			ArrayList<Room> results = new ArrayList<Room>();
			do{
				Room room = new Room();
				room.setId(c.getString(c.getColumnIndex("id")));
				room.setInfoType(c.getInt(c.getColumnIndex("info_type")));
				room.setAddress(c.getString(c.getColumnIndex("address")));
				room.setMidAddress(c.getString(c.getColumnIndex("midAddress")));
				room.setCurrentStage(c.getInt(c.getColumnIndex("current_stage")));
				room.setRoomNo(c.getString(c.getColumnIndex("room_no")));
				room.setTobaccoNo(c.getString(c.getColumnIndex("tobacco_no")));
				room.setDryAct(c.getFloat(c.getColumnIndex("dry_act")));
				if(c.getInt(c.getColumnIndex("is_prefer")) == 0)
					room.setPrefer(false);
				else
					room.setPrefer(true);
				results.add(room);
			}while(c.moveToNext());
			mAdapter.setList(results);
			gridView.setAdapter(mAdapter);
			
			int alerts = alertElements(results);
			int normals = results.size() - alerts;
			
			mAlertRooms.setText(String.valueOf(alerts));
			mNormalRooms.setText(String.valueOf(normals));
		}
		

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.setList(null);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(STATION_ID, stationId);
		Log.i(TAG, "station id is saved");
	}


	private static class RoomsGridCursorLoader extends SQLiteCursorLoader{
		
		public RoomsGridCursorLoader(Context context){
			super(context);
		}

		
		@Override
		protected Cursor loadCursor(){
			Cursor c ;
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			c = dbAdapter.getPreferRoomsByUser(userID);
			dbAdapter.close();
			return c;
		}
	}
	

}
