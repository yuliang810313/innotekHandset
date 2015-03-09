package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.BindingRoomFragment;

public class BindingRoomActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return new BindingRoomFragment();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("¿¾·¿Æ¥Åä");
		super.onCreate(savedInstanceState);
	}

}
