package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.innotek.handset.R;
import com.innotek.handset.activities.StationActivity;
import com.innotek.handset.entities.State;
import com.innotek.handset.service.UpdateMessageService;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class StatesListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	
	public static final String TAG = "MainFragment";
		
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {

		State state = (State)getListAdapter().getItem(position);
				
		Intent intent = new Intent(getActivity(), StationActivity.class);
		intent.putExtra("STATE_ID", state.getId());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
		
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
		
		Intent intent = new Intent(getActivity(), UpdateMessageService.class);
		getActivity().startService(intent);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview, container, false);
				
		return view;
	}

	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new StateListCursorLoader(getActivity());
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PRE_USER", Context.MODE_PRIVATE);
		String user_id = sharedPreferences.getString("USER_ID", null);
		
		ArrayList<State> results = new ArrayList<State>();
		do{
			String name = c.getString(c.getColumnIndex("name"));
			String id = c.getString(c.getColumnIndex("id"));
			State state = new State(id, user_id, name);
			results.add(state);
		}while(c.moveToNext());
		
		ArrayAdapter<State> adapter = new ArrayAdapter<State>(getActivity(), android.R.layout.simple_list_item_1, results);
		setListAdapter(adapter);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		setListAdapter(null);
	}


	private static class StateListCursorLoader extends SQLiteCursorLoader{
		
		public StateListCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			Cursor c =  dbAdapter.getStates();			
			dbAdapter.close();
			return c;
		}
	}
}
