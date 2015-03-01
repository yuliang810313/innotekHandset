package com.innotek.handset.activities;

import android.app.Fragment;
import com.innotek.handset.fragments.SettingsFragment;

public class SettingsActivity extends BaseActivity {
	
	@Override
	protected Fragment createFragment() {
		return new SettingsFragment();
	}
	
	
}
