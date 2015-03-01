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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.innotek.handset.R;
import com.innotek.handset.activities.MonitorActivity;
import com.innotek.handset.entities.Room;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.GridJSONAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;


public class RoomsFragment extends Fragment implements LoaderCallbacks<Cursor>{

	private GridView gridView;
	private GridJSONAdapter adapter;
	private LinearLayout layout;
	private ArrayList<Room> preferList = new ArrayList<Room>();
	
	private static String stationId;
	private static String user_id;
	
	private boolean isLongClicked = false;
	
	//Public KEYS
	public static final String STATION_ID = "stationId"; 
	public static final String ROOM_ID = "room_id";
	public static final String INFO_TYPE = "infoType";
	public static final String ROOM_ADDRESS = "roomAddress";
	public static final String MID_ADDRESS = "midAddress";
	private static final String TAG = "GridFragment";
	
	
	private Button mSaveButton;
	private DatabaseAdapter databaseAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences pref = getActivity().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		user_id = pref.getString("USER_ID", null);
			
		stationId = getArguments().getString("stationId");
		databaseAdapter = new DatabaseAdapter(getActivity());
		getLoaderManager().initLoader(3, null, this);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_rooms, container, false);
		layout = (LinearLayout)view.findViewById(R.id.id_layout_action_bottom);
		mSaveButton = (Button)view.findViewById(R.id.id_btn_save);
		
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				databaseAdapter.open();
				for(int i = 0; i < preferList.size(); i++){
					Room room = preferList.get(i);
					Log.i(TAG, "will update " + room.isPrefer());
					
					databaseAdapter.updateRoom(room);
				}
				databaseAdapter.close();
				
				Toast.makeText(getActivity(), "偏好已经设置成功", Toast.LENGTH_SHORT).show();
				mSaveButton.setVisibility(View.GONE);
				
			}
		});
		
		gridView = (GridView)view.findViewById(R.id.grid_rooms);
		
		//Has some bugs in messageParse
		gridView.setOnItemClickListener(new OnItemClickListener(){
			
			 public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		            Room room = (Room)adapter.getItem(position);
		            
		            if(isLongClicked){
		            	Log.i(TAG, "is Long clicked");
		            	CheckBox checkBox = (CheckBox)v.findViewById(R.id.id_checkbox_grid);
		            	setCheckBox(checkBox);
		            	
		            	room.setPrefer(checkBox.isChecked());
						if(preferList.contains(room)){
							preferList.remove(room);
						}else{
							preferList.add(room);
						}
						
						
						
						if(preferList.isEmpty()){
							mSaveButton.setEnabled(false);
						}else{
							mSaveButton.setEnabled(true);
						}
		            }else
		            	startMonitor(room);    
		        }
		});
		
		
		gridView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "dddddd");
				isLongClicked = true;
				
				
				Room room = (Room)adapter.getItem(position);
								
				layout.setVisibility(View.VISIBLE);
				//Set All CheckBox visible
				int count = parent.getChildCount();
				for(int i = 0; i < count; i++){
					RelativeLayout rl = (RelativeLayout)parent.getChildAt(i);
					CheckBox box = (CheckBox)rl.getChildAt(3);
					box.setVisibility(View.VISIBLE);
				}
				//setCheckBox(checkBox);
				//room.setPrefer(checkBox.isChecked());
				if(preferList.contains(room)){
					preferList.remove(room);
				}else{
					preferList.add(room);
				}
			
				
				if(preferList.isEmpty()){
					mSaveButton.setEnabled(false);
				}else{
					mSaveButton.setEnabled(true);
				}
				
				return true;
			}
			

		});
		
		adapter = new GridJSONAdapter(getActivity());
		
		return view;
	}
	
	
	private void setCheckBox(CheckBox checkBox){
		if(checkBox.isChecked())
    		checkBox.setChecked(false);
    	else
    		checkBox.setChecked(true);
	}
	
	
	public void startMonitor(Room room){
		  Intent intent = new Intent(getActivity(), MonitorActivity.class);
          intent.putExtra(ROOM_ID, room.getId());
          intent.putExtra(INFO_TYPE , room.getInfoType());
          intent.putExtra(ROOM_ADDRESS, room.getAddress());
          intent.putExtra(MID_ADDRESS, room.getMidAddress());
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          
          Log.i(TAG, "start to monitoring: " + room.getId());
          startActivity(intent);
          
	}
	
	public static RoomsFragment newInstance(String station_id){
		Bundle args = new Bundle();
		args.putString("stationId", station_id);
		RoomsFragment fragment = new RoomsFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new RoomsGridCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.i(TAG, "Run in background");
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
				if(c.getInt(c.getColumnIndex("is_prefer")) == 0)
					room.setPrefer(false);
				else
					room.setPrefer(true);
				results.add(room);
			}while(c.moveToNext());
			adapter.setList(results);
			gridView.setAdapter(adapter);
		}
		

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.setList(null);
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
			if(stationId != null){
				c = dbAdapter.getRoomsByStation(stationId);
			}else
				c = dbAdapter.getPreferRoomsByUser(user_id);
			dbAdapter.close();
			return c;
		}
	}
	

}
