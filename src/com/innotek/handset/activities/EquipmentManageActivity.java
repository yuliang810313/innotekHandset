package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innotek.handset.R;

public class EquipmentManageActivity extends BaseActivity{

	@Override
	protected Fragment createFragment() {
		return new EquipmentManageFragment();
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTitle("烘烤物资管理");
		super.onCreate(savedInstanceState);
	}



	private class EquipmentManageFragment extends Fragment{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_equipment, container, false);
			
			return view;
		}
		
		
		
	}
}
