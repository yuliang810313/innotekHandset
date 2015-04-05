package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.innotek.handset.fragments.FreshTobaccoFragment;

/**
 * 鲜烟管理
 *
 */
public class FreshTobaccoActivity extends BaseWorkflowActivity{
	
	
	@Override
	protected Fragment createFragment() {
		getPreferRoomIdAndStationId();
		return FreshTobaccoFragment.newInstance(mPreferRoomId, mStationId);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		setTitle("鲜烟管理");		
		super.onCreate(savedInstanceState);
	}

}
