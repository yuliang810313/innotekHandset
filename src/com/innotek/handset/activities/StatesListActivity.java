package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.StatesListFragment;

public class StatesListActivity extends BaseActivity {
	
	@Override
	protected Fragment createFragment(){
		return new StatesListFragment();
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setTitle("地区列表");
	}
	

}
