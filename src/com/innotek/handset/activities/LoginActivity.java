package com.innotek.handset.activities;

import com.innotek.handset.fragments.LoginFragment;

import android.app.Fragment;

public class LoginActivity extends BaseActivity {

    @Override
    protected Fragment createFragment(){
    	return new LoginFragment();
    }

}
