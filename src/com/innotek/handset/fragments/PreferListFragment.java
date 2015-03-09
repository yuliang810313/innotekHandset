package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;



public class PreferListFragment extends ListFragment implements LoaderCallbacks<Cursor>{

	public static final String TAG = "Prefer List Fragment";
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview, container, false);
		return view;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(19, null, this);
	}


	
	
//	@Override
//	public void onListItemClick(ListView listView, View view, int position, long id) {
//
//		PreferRoom room = (PreferRoom)getListAdapter().getItem(position);
//				
//		Intent intent = new Intent(getActivity(), ModifyPreferRoomActivity.class);
//		intent.putExtra("ID", room.getId());
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		getActivity().startActivity(intent);
//		
//	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new PreferListCursorLoader(getActivity());
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		
		ArrayList<PreferRoom> results = new ArrayList<PreferRoom>();
		
		do{
			PreferRoom room = new PreferRoom();
			room.setId(c.getLong(c.getColumnIndex("_id")));
			room.setRoomNo(c.getString(c.getColumnIndex("room_no")));
			room.setTobaccoNo(c.getString(c.getColumnIndex("tobacco_no")));
			room.setRoomID(c.getString(c.getColumnIndex("room_id")));
			
			results.add(room);
		}while(c.moveToNext());
		Log.i(TAG, "results length is" + results.size());
		PreferListAdapter adapter = new PreferListAdapter(getActivity(), R.layout.prefer_list_item);
		adapter.setList(results);
		this.setListAdapter(adapter);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		setListAdapter(null);
	}


	private static class PreferListCursorLoader extends SQLiteCursorLoader{
		
		SharedPreferences sp = this.getContext().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		String userID = sp.getString("USER_ID", null);
		
		public PreferListCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			Cursor c =  dbAdapter.getPreferRooms(userID);		
			dbAdapter.close();
			return c;
		}
	}
	
	private class PreferListAdapter extends ArrayAdapter<PreferRoom>{
		
		private ArrayList<PreferRoom> list = new ArrayList<PreferRoom>();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PreferRoom room = getItem(position);
			
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.prefer_list_item, parent, false);	 
			}
			
			TextView mRoomNo = (TextView) convertView.findViewById(R.id.id_room_no);
			TextView mTobaccoNo = (TextView) convertView.findViewById(R.id.id_tobacco_no);
			TextView mBinding = (TextView) convertView.findViewById(R.id.id_binding_status);
			
			mRoomNo.setText("¿¾·¿ " + room.getRoomNo());
			mTobaccoNo.setText("ÑÌ»ùºÅ " + room.getTobaccoNo());
			
			String binding = room.getRoomID() == null ? "Î´Æ¥Åä" : "ÒÑÆ¥Åä";
			mBinding.setText(binding);

			
			return convertView;
			
		}

		
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public PreferRoom getItem(int position) {
			return list.get(position);
		}
		

		public void setList(ArrayList<PreferRoom> list) {
			this.list = list;
		}


		public PreferListAdapter(Context context, int resource){
			super( context,  resource);
		}
	}
}
