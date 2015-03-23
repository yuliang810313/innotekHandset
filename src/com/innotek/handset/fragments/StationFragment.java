package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class StationFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	
	public static StationFragment newInstance(){
		StationFragment fragment = new StationFragment();
		return fragment;
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(23, null, this);
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		long stationId = 0;
		switch(position){
		case 0:
			stationId = 1;
			break;
		case 1:
			stationId = 2;
			break;
		}
		Intent intent = new Intent(this.getActivity(), PreferListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("STATION_ID", stationId);
		this.getActivity().startActivity(intent);
	}



	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new StationsCursorLoader(this.getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		ArrayList<String> list = new ArrayList<String>();
		
		if(c != null){
			do{
				list.add(c.getString(c.getColumnIndex("title")));
			}while(c.moveToNext());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		
		setListAdapter(adapter);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}
	
	private static class StationsCursorLoader extends SQLiteCursorLoader{
			
		public StationsCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			Cursor c =  dbAdapter.findStations();		
			dbAdapter.close();
			return c;
		}
	}
	
}
