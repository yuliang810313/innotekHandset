package com.innotek.handset.fragments;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.BindingRoomActivity;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class RoomManageFragment extends BaseWorkflowFragment {
	
	private PreferRoom room;
	private String roomID;
	
	private TextView mGroupName;
	private EditText mRoomNo;
	private EditText mTobaccoNo;
	private EditText mOther;
	
	//检查风机状态
	private RadioGroup mFanGroup;
	private RadioButton mFanNormal;
	private RadioButton mFanDamage;
	private int mFanState;
	
	//检查自控仪状态
	private RadioGroup mAcGroup;
	private RadioButton mAcNormal;
	private RadioButton mAcDamage;
	private int mAcState;
	
	//检查鼓风机状态
	private RadioGroup mBlowerGroup;
	private RadioButton mBlowerNormal;
	private RadioButton mBlowerDamage;
	private int mBlowerState;
	
	//检查加热设备
	private RadioGroup mHeatingGroup;
	private RadioButton mHeatingNormal;
	private RadioButton mHeatingDamage;
	private int mHeatingState;
	
	//检查进风门
	private RadioGroup mAirGroup;
	private RadioButton mAirNormal;
	private RadioButton mAirDamage;
	private int mAirState;
	
	//检查水壶
	private RadioGroup mKettleGroup;
	private RadioButton mKettleNormal;
	private RadioButton mKettleDamage;
	private RadioButton mKettleClean;
	private RadioButton mKettleDirty;
	private String mKettleState;
	
	public static final String TAG = "RoomManage";
	
	//private DatabaseAdapter dbAdapter;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mStationId = getArguments().getLong("STATION_ID");
//		mPreferRoomId = getArguments().getLong("ROOM_ID");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_room_manage, container, false);
		
	    mGroupName = (TextView) view.findViewById(R.id.id_group_name);
	   
		mRoomNo = (EditText) view.findViewById(R.id.id_room_no);
		mTobaccoNo = (EditText) view.findViewById(R.id.id_tobacco_no);
		mOther = (EditText) view.findViewById(R.id.id_edit_other);
		
		//风机是否正常
		mFanGroup = (RadioGroup) view.findViewById(R.id.id_fan_group);
		mFanNormal = (RadioButton) view.findViewById(R.id.radio0);
		mFanDamage = (RadioButton) view.findViewById(R.id.radio1);
		mFanGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mFanNormal.getId()){
					mFanState = 1;
				}else if(checkedId == mFanDamage.getId())
					mFanState = 0;
			}
		});
		
		//自控仪是否正常
		mAcGroup = (RadioGroup) view.findViewById(R.id.id_ac_group);
		mAcNormal = (RadioButton) view.findViewById(R.id.radio3);
		mAcDamage = (RadioButton) view.findViewById(R.id.radio4);
		mAcGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mAcNormal.getId())
					mAcState = 1;
				else if(checkedId == mAcDamage.getId())
					mAcState = 0;
			}
		});
		
		//鼓风机是否正常
		mBlowerGroup = (RadioGroup) view.findViewById(R.id.id_blower_group);
		mBlowerNormal = (RadioButton) view.findViewById(R.id.radio5);
		mBlowerDamage = (RadioButton) view.findViewById(R.id.radio6);
		mBlowerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mBlowerNormal.getId())
					mBlowerState = 1;
				else if(checkedId == mBlowerDamage.getId())
					mBlowerState = 0;
			}
		});
		
		//加热设备是否正常
		mHeatingGroup = (RadioGroup) view.findViewById(R.id.id_heating_group);
		mHeatingNormal = (RadioButton) view.findViewById(R.id.radio7);
		mHeatingDamage = (RadioButton) view.findViewById(R.id.radio8);
		mHeatingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mHeatingNormal.getId())
					mHeatingState = 1;
				else if(checkedId == mHeatingDamage.getId())
					mHeatingState = 0;
			}
		});
		
		//进风门是否正常
		mAirGroup = (RadioGroup) view.findViewById(R.id.id_air_group);
		mAirNormal = (RadioButton)view.findViewById(R.id.radio9);
		mAirDamage = (RadioButton)view.findViewById(R.id.radio10);
		mAirGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mAirNormal.getId())
					mAirState = 1;
				else if(checkedId == mAirDamage.getId())
					mAirState = 0;

			}
		});
		
		//水壶是否正常
		mKettleGroup = (RadioGroup) view.findViewById(R.id.id_kettle_group);
		mKettleNormal = (RadioButton) view.findViewById(R.id.radio11);
		mKettleDamage = (RadioButton) view.findViewById(R.id.radio12);
		mKettleClean = (RadioButton) view.findViewById(R.id.radio13);
		mKettleDirty = (RadioButton) view.findViewById(R.id.radio14);
		mKettleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == mKettleNormal.getId())
					mKettleState = "正常";
				else if(checkedId == mKettleDamage.getId())
					mKettleState = "漏水";
				else if(checkedId == mKettleClean.getId())
					mKettleState = "纱干净";
				else if(checkedId == mKettleDirty.getId())
					mKettleState = "纱不净";
			}
		});
		
		//保存烤房，如果保存成功将进行烤房匹配
		Button mSave = (Button) view.findViewById(R.id.id_button_done);
		mSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				room = new PreferRoom();
				room.setStationId(mStationId);
				
				if(mPreferRoomId > 0){
				
					room.setRoomID(roomID);
					room.setRoomStageId(1);
					savePreferRoom();
					
					Intent intent = new Intent(getActivity(), PreferListActivity.class);
			    	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			    	intent.putExtra("STATION_ID", mStationId);
			    	getActivity().startActivity(intent);
			    	
				}else{
					savePreferRoom();
					startNewActivity(BindingRoomActivity.class);
				}
				
					
			}
		});
		
		fillForm();
		
		return view;
	}
	
	private void fillForm(){
		//如果preferID大于0，则是在原烤房重新开启烘烤进程
				if(mPreferRoomId > 0){
					dbAdapter = new DatabaseAdapter(getActivity());
					dbAdapter.open();
					Cursor cursor = dbAdapter.getPreferRoomById(mPreferRoomId);
					dbAdapter.close();
					
					do{
						mGroupName.setText(cursor.getString(cursor.getColumnIndex("group_name")));
						mRoomNo.setText(cursor.getString(cursor.getColumnIndex("room_no")));
						mTobaccoNo.setText(cursor.getString(cursor.getColumnIndex("tobacco_no")));
						roomID = cursor.getString(cursor.getColumnIndex("room_id"));
						
						int acValue = cursor.getInt(cursor.getColumnIndex("ac_state"));
						if(acValue == 1){
							mAcGroup.check(mAcNormal.getId());
						}else
							mAcGroup.check(mAcDamage.getId());
						
						int fanValue = cursor.getInt(cursor.getColumnIndex("fan_state"));
						if(fanValue == 1)
							mFanGroup.check(mFanNormal.getId());
						else
							mFanGroup.check(mFanDamage.getId());
						
						int airValue = cursor.getInt(cursor.getColumnIndex("air_inlet_state"));
						if(airValue == 1)
							mAirGroup.check(mAirNormal.getId());
						else
							mAirGroup.check(mAirDamage.getId());
						
						int blowerValue = cursor.getInt(cursor.getColumnIndex("blower_state"));
						if(blowerValue == 1)
							mBlowerGroup.check(mBlowerNormal.getId());
						else
							mBlowerGroup.check(mBlowerDamage.getId());
						
						int heatingState = cursor.getInt(cursor.getColumnIndex("heating_state"));
						if(heatingState == 1)
							mHeatingGroup.check(mHeatingNormal.getId());
						else
							mHeatingGroup.check(mHeatingDamage.getId());
						
						String kettleState = cursor.getString(cursor.getColumnIndex("kettle_state"));
						
						if(kettleState.equals("正常")){
							mKettleGroup.check(mKettleNormal.getId());
						}else if(kettleState.equals("漏水")){
							mKettleGroup.check(mKettleDamage.getId());
						}else if(kettleState.equals("纱干净")){
							mKettleGroup.check(mKettleClean.getId());
						}else if(kettleState.equals("纱不净")){
							mKettleGroup.check(mKettleDirty.getId());
						}
						
					}while(cursor.moveToNext());
					
					
				}else{
					  if(mStationId == 1)
					    	mGroupName.setText("云田烤烟工场");
					    else
					    	mGroupName.setText("夏层铺烤烟工场");
				}
	}
	
	
	
	@Override
	public void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("ROOM_ID", mPreferRoomId);
		intent.putExtra("ROOM_NO", mRoomNo.getText().toString());
		intent.putExtra("TOBACCO_NO", mTobaccoNo.getText().toString());
		intent.putExtra("STATION_ID", mStationId);
		getActivity().startActivity(intent);
	}
	
	public static RoomManageFragment newInstance(long roomId, long stationId){
		Bundle args = new Bundle();
		args.putLong("ROOM_ID", roomId);
		args.putLong("STATION_ID", stationId);
		RoomManageFragment fragment = new RoomManageFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private long savePreferRoom(){
		DataManager dm = new DataManager(getActivity());	
		
		SharedPreferences sp = getActivity().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		String userId = sp.getString("USER_ID", null);

		room.setUserId(userId);
		room.setRoomNo(mRoomNo.getText().toString());
		room.setTobaccoNo(mTobaccoNo.getText().toString());
		room.setOther(mOther.getText().toString());
		room.setGroupName(mGroupName.getText().toString());
		
		room.setFanState(mFanState);
		room.setAcState(mAcState);
		room.setAirState(mAirState);
		room.setKettleState(mKettleState);
		room.setBlowerState(mBlowerState);
		room.setHeatingState(mHeatingState);
		room.setCreatedAt(new Date());
		return dm.savePreferRoom(room);
		
	}
	
	
}
