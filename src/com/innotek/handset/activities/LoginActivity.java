package com.innotek.handset.activities;

import android.app.Fragment;

import com.innotek.handset.fragments.LoginFragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected Fragment createFragment(){
    	return new LoginFragment();
    }

}
