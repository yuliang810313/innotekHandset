package com.innotek.handset.activities;

import android.app.Fragment;

import com.innotek.handset.fragments.HomeFragment;

public class HomeActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		this.setTitle("רҵ���濾���");
		return new HomeFragment();
	}
	
	

}
