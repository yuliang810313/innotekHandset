package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.Intent;

public class BaseFragment extends Fragment {
	
	public static final String ROOM_ID = "ROOM_ID";
	public static final String STATION_ID = "STATION_ID";
	
	
	public void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
	}
	
		
}
