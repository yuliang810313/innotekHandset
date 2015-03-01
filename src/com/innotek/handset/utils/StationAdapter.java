package com.innotek.handset.utils;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.innotek.handset.entities.Room;

public class StationAdapter extends ArrayAdapter<Room> {

	
	public StationAdapter(Context context, int resource, int textViewResourceId, List<Room> objects) {
		super(context, resource, textViewResourceId, objects);
		
	}
	

}
