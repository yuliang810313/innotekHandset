package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.MainFragment;

public class MainActivity extends BaseActivity {
	
	@Override
	protected Fragment createFragment(){
		return new MainFragment();
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setTitle("地区列表");
	}
	

}
