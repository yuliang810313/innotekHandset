package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innotek.handset.R;
import com.innotek.handset.activities.PackingTobaccoActivity;
import com.innotek.handset.entities.Tobacco;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class NewTobaccoFragment extends Fragment {
	
	public static final String TAG = "New Tobacco";
	
	//烟叶品种
	private RadioGroup mGroupBreed;
	private String mBreed;
	
	//部位
	private RadioGroup mGroupPart;
	private String mPart;
	
	//含水量
	private RadioGroup mGroupWater;
	private String mWater;
	
	//烟叶质地
	private RadioGroup mGroupQuality;
	private String mQuality;
	
	//烟叶类型
	private RadioGroup mGroupType;
	private String mType;
	
	//烟叶成熟度
	private EditText maturityNormal;
	private EditText maturityBelow;
	private EditText maturityAbove;
	
	
	private DataManager dm;
	private long mPreferRoomId;
	private long stationId;
	private DatabaseAdapter dbAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dm = new DataManager(getActivity());
		dbAdapter = new DatabaseAdapter(getActivity());
		mPreferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_new_tobacco, container, false);
		
		//选择品种
		mGroupBreed = (RadioGroup) view.findViewById(R.id.id_group_breed);
		mGroupBreed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mBreed = getValueOnSelect(group, checkedId);
			}
		});
		
		//选择部位
		mGroupPart = (RadioGroup) view.findViewById(R.id.id_group_part);
		mGroupPart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mPart = getValueOnSelect(group, checkedId);
			}
		});
		
		//含水量
		mGroupWater = (RadioGroup) view.findViewById(R.id.id_group_water);
		mGroupWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mWater = getValueOnSelect(group, checkedId);
			}
		});
		
		//烟叶质地
		mGroupQuality = (RadioGroup) view.findViewById(R.id.id_group_quality);
		mGroupQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mQuality = getValueOnSelect(group, checkedId);
			}
		});
		
		
		//烟叶类型
		mGroupType = (RadioGroup) view.findViewById(R.id.id_group_type);
		mGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mType = getValueOnSelect(group, checkedId);
			}
		});
		
		maturityNormal = (EditText) view.findViewById(R.id.id_maturity_normal);
		maturityBelow = (EditText) view.findViewById(R.id.id_maturity_below);
		maturityAbove = (EditText) view.findViewById(R.id.id_maturity_above);
		
		
		Button mSave = (Button) view.findViewById(R.id.id_button_done);
		mSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tobacco mTobacco = new Tobacco();
				mTobacco.setBreed(mBreed);
				mTobacco.setMaturity(maturityNormal.getText().toString() + "/" +
									 maturityBelow.getText().toString() + "/" +
									 maturityAbove.getText().toString());
				
				mTobacco.setPart(mPart);
				mTobacco.setQuality(mQuality);
				mTobacco.setTobaccoType(mType);
				mTobacco.setWaterContent(mWater);
				mTobacco.setPreferRoomId(mPreferRoomId);
				Log.i(TAG, mTobacco.getBreed());
				dm.saveNewTobacco(mTobacco);
				
				startActivity();
			}
		});
		
		
		
		dbAdapter.open();
		Cursor cursor = dbAdapter.getFreshTobaccoByPreferId(mPreferRoomId);
		dbAdapter.close();
		if(cursor != null){
			String breed = cursor.getString(cursor.getColumnIndex("breed"));
			if(breed.equals("云烟87")){
				mGroupBreed.check(R.id.radio0);
			}else if(breed.equals("湘烟3号")){
				mGroupBreed.check(R.id.radio1);
			}else if(breed.toLowerCase().equals("k236")){
				mGroupBreed.check(R.id.radio2);
			}else if(breed.equals("其他")){
				mGroupBreed.check(R.id.radio3);
			}
			
			String part = cursor.getString(cursor.getColumnIndex("part"));
			if(part.equals("下部叶")){
				mGroupPart.check(R.id.radio4);
			}else if(part.equals("中部叶")){
				mGroupPart.check(R.id.radio5);
			}else if(part.equals("上部叶")){
				mGroupPart.check(R.id.radio6);
			}
			
			String water = cursor.getString(cursor.getColumnIndex("water_content"));
			if(water.equals("较大")){
				mGroupWater.check(R.id.radio7);
			}else if(water.equals("适中")){
				mGroupWater.check(R.id.radio8);
			}else if(water.equals("较小")){
				mGroupWater.check(R.id.radio9);
			}
			
			String quality = cursor.getString(cursor.getColumnIndex("quality"));
			if(quality.equals("手握弹性好")){
				mGroupQuality.check(R.id.radio10);
			}else if(quality.equals("手握易破碎")){
				mGroupQuality.check(R.id.radio11);
			}
			
//			String type = cursor.getString(cursor.getColumnIndex("tobacco_type"));
//			if(type.equals("正常")){
//				mGroupType.check(R.id.radio12);
//			}else if(type.equals("反青")){
//				mGroupType.check(R.id.radio13);
//			}else if(type.equals("干旱")){
//				mGroupType.check(R.id.radio14);
//			}else if(type.equals("黑暴")){
//				mGroupType.check(R.id.radio15);
//			}else if(type.equals("后发")){
//				mGroupType.check(R.id.radio16);
//			}
			
			String maturity = cursor.getString(cursor.getColumnIndex("maturity"));
			String[] ms = maturity.split("/");
			maturityNormal.setText(ms[0]);
			maturityBelow.setText(ms[1]);
			maturityAbove.setText(ms[2]);
		}

		
		return view;
	}

	private void startActivity(){
		Intent intent = new Intent(this.getActivity(), PackingTobaccoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", mPreferRoomId);
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}
	
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
	}
	
	public static NewTobaccoFragment newInstance(long roomId, long stationId){
		Bundle bundle = new Bundle();
		bundle.putLong("ROOM_ID", roomId);
		bundle.putLong("STATION_ID", stationId);
		
		NewTobaccoFragment fragment = new NewTobaccoFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
}
