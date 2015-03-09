package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.innotek.handset.activities.CurveSettingActivity;

public class SettingsFragment extends ListFragment{
	private static final String TAG = "Settings_Fragment";
	
	private ArrayList<String> settings = new ArrayList<String>();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		settings.add("干湿球温度在线设定");
		settings.add("装烟量设定");
		
		setListAdapter(new ArrayAdapter<String>(this.getActivity(),
										android.R.layout.simple_list_item_1, 
										settings));
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		Log.i(TAG, "the positon is: " + position + " and settings is: " + settings.get(position));
		switch(position){
		case 0:
			Intent intent = new Intent(getActivity(), CurveSettingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().startActivity(intent);
			break;
		case 1:
			// 装烟量设定
			break;
		}
		
		
		
	}

}
