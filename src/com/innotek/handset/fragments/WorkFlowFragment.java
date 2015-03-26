package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.innotek.handset.R;
import com.innotek.handset.activities.DryTobaccoActivity;
import com.innotek.handset.activities.EquipmentManageActivity;
import com.innotek.handset.activities.FreshTobaccoActivity;
import com.innotek.handset.activities.RoomManageActivity;
import com.innotek.handset.activities.UserManageActivity;

public class WorkFlowFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_workflow, container, false);
		Button mRoomManage = (Button) view.findViewById(R.id.id_room_manage);
		mRoomManage.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				startNewActivity(RoomManageActivity.class);
			}
		});
		
		Button mDryManage = (Button) view.findViewById(R.id.id_dry_tobacco);
		mDryManage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(DryTobaccoActivity.class);
			}
		});
		
		Button mNewTobacco = (Button) view.findViewById(R.id.id_new_tobacco);
		mNewTobacco.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(FreshTobaccoActivity.class);
			}
		});
		
		
		Button mEquipment = (Button) view.findViewById(R.id.id_equipment_manage);
		mEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(EquipmentManageActivity.class);
			}
		});
		
		Button mUser = (Button) view.findViewById(R.id.id_user_manage);
		mUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(UserManageActivity.class);
			}
		});
		
		Button mSearch = (Button) view.findViewById(R.id.id_search);
		mSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(UserManageActivity.class);
			}
		});
		
		return view;
	}

	
	
	//Start new activity
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
	}
}
