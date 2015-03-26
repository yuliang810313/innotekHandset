package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.CreateRoomFragment;

public class CreateRoomActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return CreateRoomFragment.newInstance(0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("´´½¨¿¾·¿");
		super.onCreate(savedInstanceState);
	}

	

}
