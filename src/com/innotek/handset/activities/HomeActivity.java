package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.innotek.handset.fragments.HomeFragment;

public class HomeActivity extends BaseActivity {

	private long mExitTime = 0;  
	
    @Override
    protected Fragment createFragment(){
    	return new HomeFragment();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
        	{  
               if((System.currentTimeMillis()- mExitTime) > 2000){  
                   Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();                                  
                   mExitTime = System.currentTimeMillis();  
               }else {  
                 		finish();  
                        System.exit(0);  
                     }  
                                   
               return true;  
             }  
         return super.onKeyDown(keyCode, event);  
    }  
}
