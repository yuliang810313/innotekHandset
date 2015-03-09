package com.innotek.handset.activities;

import android.app.Fragment;

import com.innotek.handset.fragments.NewRoomFragment;

public class ModifyPreferRoomActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		
		return NewRoomFragment.newInstance(getIntent().getExtras().getLong("ID"));
	}

}
