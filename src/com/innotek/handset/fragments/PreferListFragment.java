package com.innotek.handset.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.ArbitrateActivity;
import com.innotek.handset.activities.DryTobaccoActivity;
import com.innotek.handset.activities.FreshTobaccoActivity;
import com.innotek.handset.activities.PackingTobaccoActivity;
import com.innotek.handset.activities.RoomManageActivity;
import com.innotek.handset.activities.SelectCurveActivity;
import com.innotek.handset.entities.PreferRoom;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class PreferListFragment extends ListFragment implements LoaderCallbacks<Cursor>{

	public static final String TAG = "Prefer List Fragment";
	private PreferRoom mRoom;
	private DialogFragment newFragment;
	private static long stationId;
	private TextView mHeader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_listview, container, false);
		mHeader = (TextView) view.findViewById(R.id.id_list_header);
		
		if(stationId == 1)
			mHeader.setText("云田烤烟工场");
		else
			mHeader.setText("夏层铺烤烟工场");

		
		return view;
	}


	public static PreferListFragment newInstance(long station_Id){
		Bundle args = new Bundle();
		args.putLong("STATION_ID", station_Id);
		PreferListFragment fragment = new PreferListFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stationId = getArguments().getLong("STATION_ID",1);
		getLoaderManager().initLoader(19, null, this);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		
		mRoom = (PreferRoom)getListAdapter().getItem(position);
				
		switch(mRoom.getRoomStage()){
				
			case 1:
				//启动鲜烟管理
				startNewActivity(FreshTobaccoActivity.class);
				break;
			case 2:
				//启动装（编）烟信息
				startNewActivity(PackingTobaccoActivity.class);
				break;
				//选择烘烤曲线
			case 3:
				startNewActivity(SelectCurveActivity.class);
				break;
			case 4:
				//应该进入烘烤监控阶段，由于测试，直接启动干烟管理
				//startNewActivity(MonitorActivity.class);
				startNewActivity(DryTobaccoActivity.class);
				break;
			case 5:
				//启动干烟管理
				startNewActivity(DryTobaccoActivity.class);
				break;
			case 6:
				//启动仲裁
				startNewActivity(ArbitrateActivity.class);
				break;
			case 7:
				//开启新一轮烘烤
				//startNewActivity(RoomManageActivity.class);
				 newFragment = new FireMissilesDialogFragment();
				 newFragment.show(getFragmentManager(), "newBaking");
				break;
			case 8:
				//开启新一轮烘烤
				//startNewActivity(RoomManageActivity.class);
				 newFragment = new FireMissilesDialogFragment();
				 newFragment.show(getFragmentManager(), "newBaking");
				break;
				
		}
		
	}
	
	//启动Activity
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", mRoom.getId());
		intent.putExtra("STATION_ID", stationId);
		Log.i(TAG, "room id:" + mRoom.getId() + " and station id: " + stationId);
		getActivity().startActivity(intent);
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new PreferListCursorLoader(getActivity());
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		
		ArrayList<PreferRoom> results = new ArrayList<PreferRoom>();
		if(c != null){
			do{
				PreferRoom room = new PreferRoom();
				room.setId(c.getLong(c.getColumnIndex("_id")));
				room.setRoomNo(c.getString(c.getColumnIndex("room_no")));
				room.setTobaccoNo(c.getString(c.getColumnIndex("tobacco_no")));
				room.setRoomID(c.getString(c.getColumnIndex("room_id")));
				room.setRoomStage(c.getInt(c.getColumnIndex("room_stage_id")));
				
				results.add(room);
			}while(c.moveToNext());
		}
		
		PreferListAdapter adapter = new PreferListAdapter(getActivity(), R.layout.prefer_list_item);
		adapter.setList(results);
		setListAdapter(adapter);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		setListAdapter(null);
		
	}


	private static class PreferListCursorLoader extends SQLiteCursorLoader{
		
		SharedPreferences sp = getContext().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		String userID = sp.getString("USER_ID", null);
		
		public PreferListCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			Cursor c =  dbAdapter.getPreferRooms(userID, stationId);		
			dbAdapter.close();
			return c;
		}
	}
	
	
	 private class FireMissilesDialogFragment extends DialogFragment {
	    	@Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	            // Use the Builder class for convenient dialog construction
	            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	            builder.setMessage(R.string.dialog_create_new_baking)
	                   .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                    	   startNewActivity(RoomManageActivity.class);
	                       }
	                   })
	                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                           // User cancelled the dialog
	                       }
	                   });
	            // Create the AlertDialog object and return it
	            return builder.create();
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
			TextView mStage = (TextView) convertView.findViewById(R.id.id_room_stage);
			
			mRoomNo.setText("烤房 " + room.getRoomNo());
			mTobaccoNo.setText("烟基号 " + room.getTobaccoNo());
			
			String binding = room.getRoomID() == null ? "未匹配" : "已匹配";
			mBinding.setText(binding);
			
			switch(room.getRoomStage()){
			case 1:
				mStage.setText("烤前检查已完毕");
				break;
			case 2:
				mStage.setText("鲜烟管理已完毕");
				break;
			case 3:
				mStage.setText("编（夹）已完毕");
				break;
			case 4:
				mStage.setText("曲线选择已完毕");
				break;
			case 5:
				mStage.setText("正在烘烤");
				break;
				
			case 6:
				mStage.setText("干烟管理已完毕");
				break;
			case 7:
				mStage.setText("正在仲裁");
				mStage.setTextColor(Color.RED);
				break;
			case 8:
				mStage.setText("烘烤结束");
				break;
			}
			
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
