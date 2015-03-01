package com.innotek.handset.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;

import com.innotek.handset.R;
import com.innotek.handset.fragments.StationFragment;


public class StationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("ялу╬ап╠М");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.container);
		
		FragmentManager fm = getFragmentManager();
	    Fragment fragment = fm.findFragmentById(R.id.container);
	    
	    if(fragment == null){
	    	fragment = new StationFragment();
	    	fm.beginTransaction().add(R.id.container, fragment).commit();
	    }
	}


	
}
