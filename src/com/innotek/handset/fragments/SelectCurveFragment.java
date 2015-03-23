package com.innotek.handset.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.utils.DataManager;

public class SelectCurveFragment extends BaseFragment {
	
	public static final String TAG = "Select curve";
	
	private Spinner mBreedSpinner;
	private Spinner mPartSpinner;
	private Button mDone;
	
	private ArrayAdapter<String> mBreedAdapter;
	private ArrayAdapter<String> mPartAdapter;
	
	private String mBreedValue;
	private String mPartValue;
	
	private long preferRoomId;
	private long stationId;
	
	private DataManager dm;
	
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
		dm = new DataManager(getActivity());
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
		
		mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, mBreedValue + " : " + mPartValue);
				dm.updateRoomStage(preferRoomId, 4);
				startNewActivity(PreferListActivity.class);
			}
		});
		
		
		String[] breeds = new String[]{"K236", "云烟87" , "湘烟3号" , "特殊烟叶", "自设"};
		
		mBreedAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, breeds);  
        //设置下拉列表风格  
		mBreedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将适配器添加到spinner中去  
		mBreedSpinner.setAdapter(mBreedAdapter);  
		mBreedSpinner.setVisibility(View.VISIBLE);//设置默认显示  
        mBreedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() { 
        	
        @Override  
        public void onItemSelected(AdapterView<?> arg0, View arg1,  
                int arg2, long arg3) {  
            
        	mBreedValue = ((TextView)arg1).getText().toString(); 
              
        }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
        });
		
		String[] parts = new String[]{"下部叶", "中部叶", "上部叶"};
		mPartAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, parts);  
        //设置下拉列表风格  
		mPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将适配器添加到spinner中去  
		mPartSpinner.setAdapter(mPartAdapter);  
		mPartSpinner.setVisibility(View.VISIBLE);//设置默认显示  
        mPartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
        @Override  
        public void onItemSelected(AdapterView<?> arg0, View arg1,  
                int arg2, long arg3) {  
            
            	mPartValue = ((TextView)arg1).getText().toString(); 
              
              
        }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
        });
        
        
		return view;
	}


	
	
	
	
}
