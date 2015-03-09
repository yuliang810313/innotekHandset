package com.innotek.handset.fragments;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.innotek.handset.R;
import com.innotek.handset.activities.BindingRoomActivity;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class NewRoomFragment extends Fragment implements LoaderCallbacks<Cursor>{

	private EditText mRoomNo;
	private EditText mTobaccoNo;
	private EditText mRoomType;
	private EditText mFanType;
	private EditText mHeatingEquipment;
	private EditText mPersonInCharge;
	private EditText mRoomUser;
	private EditText mPhone;
	
	private Button mResetButton;
	private Button mSaveButton;
	
	private long id;
	private PreferRoom room;
	
	
	public static NewRoomFragment newInstance(long ID){
		Bundle bundle = new Bundle();
		bundle.putLong("ID", ID);
		NewRoomFragment fragment = new NewRoomFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = getArguments().getLong("ID");
		if(id > 0){
			getLoaderManager().initLoader(6, null, this);
		}else{
			
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_room, container, false);
		
		mRoomNo = (EditText) view.findViewById(R.id.id_room_no);
		mTobaccoNo = (EditText) view.findViewById(R.id.id_tobacco_no);
		mRoomType = (EditText) view.findViewById(R.id.id_room_type);
		mFanType = (EditText) view.findViewById(R.id.id_fan_type);
		mHeatingEquipment = (EditText) view.findViewById(R.id.id_heating_equipment);
		mPersonInCharge = (EditText) view.findViewById(R.id.id_person_in_charge);
		mRoomUser = (EditText) view.findViewById(R.id.id_room_user);
		mPhone = (EditText) view.findViewById(R.id.id_phone);
		
		mResetButton = (Button) view.findViewById(R.id.id_reset);
		mSaveButton = (Button) view.findViewById(R.id.id_save);
		
		mResetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//如果创建成功，应该转到匹配页面
				if(savePreferRoom() > 0){
					bindingRoom();
				}else{
					
				}
				
			}
		});
		
		return view;
	}
		
	private void bindingRoom(){
		Intent intent = new Intent(this.getActivity(), BindingRoomActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_NO", mRoomNo.getText().toString());
		intent.putExtra("TOBACCO_NO", mTobaccoNo.getText().toString());
		this.getActivity().startActivity(intent);
	}
	
	private long savePreferRoom(){
		DataManager dm = new DataManager(getActivity());	
		
		SharedPreferences sp = this.getActivity().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		String userId = sp.getString("USER_ID", null);
		
		room = new PreferRoom();
		room.setUserId(userId);
		room.setRoomNo(mRoomNo.getText().toString());
		room.setTobaccoNo(mTobaccoNo.getText().toString());
		room.setRoomType(mRoomType.getText().toString());
		room.setFanType(mFanType.getText().toString());
		room.setHeatingEquipment(mHeatingEquipment.getText().toString());
		room.setPersonInCharge(mPersonInCharge.getText().toString());
		room.setRoomUser(mRoomUser.getText().toString());
		room.setPhone(mPhone.getText().toString());
		
		return dm.savePreferRoom(room);
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		PreferRoomCursorLoader loader = new PreferRoomCursorLoader(getActivity());
		loader.setPreferRoomID(id);
		return loader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
				
		room = new PreferRoom();
		do{
			
			room.setRoomNo(c.getString(c.getColumnIndex("room_no")));
			room.setTobaccoNo(c.getString(c.getColumnIndex("tobacco_no")));
			room.setRoomType(c.getString(c.getColumnIndex("room_type")));
			room.setFanType(c.getString(c.getColumnIndex("fan_type")));
			room.setHeatingEquipment(c.getString(c.getColumnIndex("heating_equipment")));
			room.setPersonInCharge(c.getString(c.getColumnIndex("person_in_charge")));
			room.setRoomUser(c.getString(c.getColumnIndex("room_user")));
			room.setPhone(c.getString(c.getColumnIndex("phone")));
			
		}while(c.moveToNext());
		mRoomNo.setText(room.getRoomNo()); 
		mTobaccoNo.setText(room.getTobaccoNo());
		mRoomType.setText(room.getRoomType());
		mFanType.setText(room.getFanType()); 
		mHeatingEquipment.setText(room.getHeatingEquipment()); 
		mPersonInCharge.setText(room.getPersonInCharge()); 
		mRoomUser.setText(room.getRoomUser());
		mPhone.setText(room.getPhone()); 
		
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
	}


	private static class PreferRoomCursorLoader extends SQLiteCursorLoader{
		
		private long preferRoomID;
		
		public void setPreferRoomID(long id){
			this.preferRoomID = id;
		}
		
		public PreferRoomCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			Cursor c =  dbAdapter.getPreferRoomById(preferRoomID);		
			dbAdapter.close();
			return c;
		}
	}
	
}
