package com.innotek.handset.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.innotek.handset.R;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.service.SendCommandService;
import com.innotek.handset.utils.CurveLines;
import com.innotek.handset.utils.DatabaseAdapter;

public class SelectCurveFragment extends BaseFragment {
	
	public static final String TAG = "Select curve";
	
	private Spinner mBreedSpinner;
	private Spinner mPartSpinner;
	
	private Button mDone;
	private Button mSelect;
	
	private ArrayAdapter<String> mBreedAdapter;
	private ArrayAdapter<String> mPartAdapter;
	
	private long preferRoomId;
	private long stationId;
	
	private long mProCurveId;
	private long mPartId;
	
	//private DataManager dm;
	private DatabaseAdapter dbAdapter;
		
	//private boolean mCurveSelected = false;
	
	private CurveLines mCurveLines;
	
	public static SelectCurveFragment newInstance(long roomId, long stationId){
		Bundle args = new Bundle();
		args.putLong("ROOM_ID", roomId);
		args.putLong("STATION_ID", stationId);
		
		SelectCurveFragment fragment = new SelectCurveFragment();
		fragment.setArguments(args);
		return fragment;
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		//dm = new DataManager(getActivity());
		
		dbAdapter = new DatabaseAdapter(this.getActivity());
	}

	@Override
	public void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", preferRoomId);
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_select_curve, container, false);
		
		mBreedSpinner = (Spinner) view.findViewById(R.id.id_spinner_breed);
		mPartSpinner = (Spinner) view.findViewById(R.id.id_spinner_part);
		
		
		mCurveLines = (CurveLines) view.findViewById(R.id.id_curve);
		selectCurve(1,1);
		
		mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//dm.updateRoomStage(preferRoomId, 4);
				
				dbAdapter.open();
				dbAdapter.updatePreferRoomStage(preferRoomId, 4);
				Cursor c = dbAdapter.getProCurveParams(mProCurveId, mPartId);
				Cursor cursor = dbAdapter.getRoomByPreferId(preferRoomId);
				
				String address = "" ;
				String midAddress = "";
				if(cursor != null){
					address = cursor.getString(cursor.getColumnIndex("address"));
					midAddress = cursor.getString(cursor.getColumnIndex("midAddress"));
				}
				
				if(c != null){
					
					String drys = c.getString(c.getColumnIndex("dry_value"));
					String wets = c.getString(c.getColumnIndex("wet_value"));
					String stageTimes = c.getString(c.getColumnIndex("stage_time"));
					
					Log.i(TAG, "dry_value " + drys);
					
					Intent intent = new Intent(getActivity(), SendCommandService.class);
					
					intent.putExtra("DRYS", drys);
					intent.putExtra("WETS", wets);
					intent.putExtra("ADDRESS", address);
					intent.putExtra("INFO_TYPE", 12);
					intent.putExtra("MID_ADDRESS", midAddress);
					intent.putExtra("TIMES", stageTimes);
					
					getActivity().startService(intent);
		
				}
				dbAdapter.close();
				
				startNewActivity(PreferListActivity.class);
			}
		});
		
		mSelect = (Button) view.findViewById(R.id.id_select_done);
		mSelect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				selectCurve(mProCurveId, mPartId);
			}
		});
		
		String[] breeds = new String[]{"K236", "云烟87" , "湘烟3号" };
		
		mBreedAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, breeds);  
        //设置下拉列表风格  
		mBreedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将适配器添加到spinner中去  
		mBreedSpinner.setAdapter(mBreedAdapter);  
		mBreedSpinner.setVisibility(View.VISIBLE);//设置默认显示  
        mBreedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 
        	
        	@Override  
        	public void onItemSelected(AdapterView<?> arg0, View arg1,  
                int arg2, long arg3) {  
            
        		//mBreedValue = ((TextView)arg1).getText().toString(); 
        		mProCurveId = arg3 + 1;
        		//selectCurve(mProCurveId, mPartId);
        	}

        	@Override
        	public void onNothingSelected(AdapterView<?> parent) {

        	}
        });
		
		String[] parts = new String[]{"下部叶", "中部叶", "上部叶"};
		mPartAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, parts);  
        //设置下拉列表风格  
		mPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将适配器添加到spinner中去  
		mPartSpinner.setAdapter(mPartAdapter);  
		mPartSpinner.setVisibility(View.VISIBLE);//设置默认显示  
        mPartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
        	
        	@Override  
        	public void onItemSelected(AdapterView<?> arg0, View arg1,  int arg2, long arg3) {  

            		mPartId = arg3 + 1;
            		//selectCurve(mProCurveId, mPartId);
        	}

        	@Override
        	public void onNothingSelected(AdapterView<?> parent) {
        		
        	}
        });
        
        
		return view;
	}


	public void selectCurve(long curveId, long partId){
		dbAdapter.open();
		Cursor c = dbAdapter.getProCurveParams(curveId, partId);
		dbAdapter.close();
		
		String drys = c.getString(c.getColumnIndex("dry_value"));
		String wets = c.getString(c.getColumnIndex("wet_value"));
		String stageTimes = c.getString(c.getColumnIndex("stage_time"));
		
		mCurveLines.setDrys(parseTemprature(drys.split(",")));
		mCurveLines.setWets(parseTemprature(wets.split(",")));
		mCurveLines.setDurationTimes(parseTimes(0, stageTimes.split(",")));
		mCurveLines.setStageTimes(parseTimes(1, stageTimes.split(",")));
		
		mCurveLines.postInvalidate();
	}
	
	private float[] parseTemprature(String[] temps){
		float[] values = new float[temps.length];
		
		for(int i = 0 ; i < temps.length; i++){
			values[i] = Float.valueOf(temps[i]);
		}
		return values;
	}
	
	private int[] parseTimes(int type, String[] times){
		//稳温时间
		if(type == 0){
			
			int length = (times.length + 1) / 2;
			int[] values = new int[length];
			int counter = 0;
			for(int i = 0; i < times.length; i++){
				if(i % 2 == 0){
					values[counter] = Integer.valueOf(times[i]);
					counter++;
				}
			}
			//values[length] = Integer.valueOf(times[times.length-1]);
			Log.i(TAG, "length is " + values.length + " and last is " + values[counter - 1]);
			return values;
		}
		//升温时间
		else{
			int length = (times.length + 1) / 2; 
			int[] values = new int[length];
			int counter = 0;
			for(int i = 0; i < times.length; i++){
				if(i % 2 != 0){
					values[counter] = Integer.valueOf(times[i]);
					counter++;
				}
			}
			values[counter] = 0;
			return values;
		}
		
	}
	
}
