package com.innotek.handset.activities;

import android.app.Fragment;

import com.innotek.handset.fragments.HomeFragment;

public class HomeActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		this.setTitle("专业化烘烤监管");
		return new HomeFragment();
	}
	
	

}
