package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;

import com.innotek.handset.fragments.HomeFragment;

public class HomeActivity extends BaseActivity {

	
    @Override
    protected Fragment createFragment(){
    	return new HomeFragment();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
    
    
	

}
