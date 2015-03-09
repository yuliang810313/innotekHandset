package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.innotek.handset.R;
import com.innotek.handset.activities.RoomsGridActivity;
import com.innotek.handset.entities.Station;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class StationFragment extends ListFragment implements LoaderCallbacks<Cursor>{

	private static final String TAG = "Station Fragment";
	private static String state_id;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);	
		
		Intent intent = getActivity().getIntent();
		state_id = intent.getExtras().getString("STATE_ID");
		Log.i(TAG, "The state id is " + state_id);	
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(1, null, this);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview, container, false);
				
		return view;
	}
	
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {

		Station station = (Station)getListAdapter().getItem(position);
				
		Intent intent = new Intent(getActivity(), RoomsGridActivity.class);
		intent.putExtra("STATION_ID", station.getId());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
		
	}
	

	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new StationListCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		ArrayList<Station> results = new ArrayList<Station>();
		if(c.moveToFirst()){
			do{
				String id = c.getString(c.getColumnIndex("id"));
				String name = c.getString(c.getColumnIndex("name"));
				
				Station station = new Station(id, name, 
											 c.getDouble(c.getColumnIndex("latitude")),
											 c.getDouble(c.getColumnIndex("longitude")));
				results.add(station);
			}while(c.moveToNext());	
		}
		ArrayAdapter<Station> adapter = new ArrayAdapter<Station>(getActivity(), android.R.layout.simple_list_item_1, results);
		setListAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		setListAdapter(null);
	}
	

	private static class StationListCursorLoader extends SQLiteCursorLoader{
		
		public StationListCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			Cursor c =  dbAdapter.getStationsByState(state_id);		
			dbAdapter.close();
			return c;
		}
	}
	
	
}
