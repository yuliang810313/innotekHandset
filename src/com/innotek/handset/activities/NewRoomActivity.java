package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.NewRoomFragment;

public class NewRoomActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return NewRoomFragment.newInstance(0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("´´½¨¿¾·¿");
		super.onCreate(savedInstanceState);
	}

	

}
