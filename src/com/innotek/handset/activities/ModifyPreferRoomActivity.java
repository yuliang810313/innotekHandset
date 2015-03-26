package com.innotek.handset.activities;

import android.app.Fragment;

import com.innotek.handset.fragments.CreateRoomFragment;

public class ModifyPreferRoomActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		
		return CreateRoomFragment.newInstance(getIntent().getExtras().getLong("ID"));
	}

}
