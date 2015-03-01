package com.innotek.handset.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.innotek.handset.R;

public abstract class BaseActivity extends Activity {
	
	protected abstract Fragment createFragment();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.container);
	    
	    FragmentManager fm = getFragmentManager();
	    Fragment fragment = fm.findFragmentById(R.id.container);
	    
	    if(fragment == null){
	    	fragment = createFragment();
	    	fm.beginTransaction().add(R.id.container, fragment).commit();
	    }
	}
	
}
