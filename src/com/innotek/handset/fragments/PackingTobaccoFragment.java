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
import com.innotek.handset.activities.SelectCurveActivity;
import com.innotek.handset.entities.Packing;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class PackingTobaccoFragment extends Fragment{

	private long mRoomId;
	private long stationId;
	
	private DataManager dm;
	//
	private RadioGroup mGroupCategory;
	private String mCategory;
	
	private EditText mPackingAmount;
	
	private RadioGroup mGroupCategoryState;
	private String mCategoryState;
	
	private RadioGroup mGroupUniformity;
	private String mUniformity;
	
	private RadioGroup mGroupPackingType;
	private String mPackingType;
	
	private EditText mOther;
	private DatabaseAdapter adapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mRoomId = this.getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		dm = new DataManager(getActivity());
		adapter = new DatabaseAdapter(getActivity());
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_packing_tobacco, container, false);
		
		mOther = (EditText) view.findViewById(R.id.id_edit_other);
		
		mGroupCategory = (RadioGroup) view.findViewById(R.id.id_group_category);
		mGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mCategory = getValueOnSelect(group, checkedId);
			}
		});
		
		
		mGroupCategoryState = (RadioGroup) view.findViewById(R.id.id_group_category_state);
		mGroupCategoryState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mCategoryState = getValueOnSelect(group, checkedId);
			}
		});
		
		
		mGroupUniformity = (RadioGroup) view.findViewById(R.id.id_group_uniformity);
		mGroupUniformity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mUniformity = getValueOnSelect(group, checkedId);
			}
		});
		
		mGroupPackingType = (RadioGroup) view.findViewById(R.id.id_group_packing_type);
		mGroupPackingType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mPackingType = getValueOnSelect(group, checkedId);
			}
		});
		
		mPackingAmount = (EditText) view.findViewById(R.id.id_packing_amount);
		
		
		Button mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("Packing", "Amount :" + mPackingAmount);
				Packing packing = new Packing();
				packing.setCategoryState(mCategoryState);
				packing.setCategory(mCategory);
				packing.setPackingAmount(Float.valueOf(mPackingAmount.getText().toString()));
				packing.setPackingType(mPackingType);
				packing.setPreferRoomId(mRoomId);
				packing.setUniformity(mUniformity);
				packing.setOther(mOther.getText().toString());
				dm.savePacking(packing);
				startNewActivity();
				
			}
		});
		
		adapter.open();
		Cursor c = adapter.findPacking(mRoomId);
		adapter.close();
		if(c != null){
			String category = c.getString(c.getColumnIndex("category"));
			if(category.equals("同杆/夹同质")){
				mGroupCategory.check(R.id.radio0);
			}else if(category.equals("混编")){
				mGroupCategory.check(R.id.radio1);
			}else if(category.equals("同竿/夹均匀")){
				mGroupCategory.check(R.id.radio2);
			}else if(category.equals("同竿/夹不匀")){
				mGroupCategory.check(R.id.radio3);
			}
			
			String packageType = c.getString(c.getColumnIndex("packing_type"));
			if(packageType.equals("各竿/夹量基本一致")){
				mGroupPackingType.check(R.id.radio4);
			}else if(packageType.equals("各竿/夹量不一致")){
				mGroupPackingType.check(R.id.radio5);
			}
			
			String packageState = c.getString(c.getColumnIndex("category_state"));
			if(packageState.equals("同层同质")){
				mGroupCategoryState.check(R.id.radio9);
			}else if(packageState.equals("按气流方式正确分层")){
				mGroupCategoryState.check(R.id.radio10);
			}
			
			String uniformity = c.getString(c.getColumnIndex("uniformity"));
			if(uniformity.equals("前密后稀")){
				mGroupUniformity.check(R.id.radio11);
			}else if(uniformity.equals("前稀后密")){
				mGroupUniformity.check(R.id.radio12);
			}else if(uniformity.equals("均匀")){
				mGroupUniformity.check(R.id.radio13);
			}
			
			mPackingAmount.setText(String.valueOf(c.getFloat(c.getColumnIndex("packing_amount"))));
			mOther.setText(c.getString(c.getColumnIndex("other")));
		}
		
		return view;
	}
	
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
	}
	
	public static PackingTobaccoFragment newInstance(long roomId, long stationId){
		Bundle bundle = new Bundle();
		bundle.putLong("ROOM_ID", roomId);
		bundle.putLong("STATION_ID", stationId);
		
		PackingTobaccoFragment fragment = new PackingTobaccoFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public void startNewActivity(){
		Intent intent = new Intent(getActivity(), SelectCurveActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", mRoomId);
		intent.putExtra("STATION_ID", stationId);
		
		getActivity().startActivity(intent);
	}

}
