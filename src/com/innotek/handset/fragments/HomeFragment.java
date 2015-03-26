package com.innotek.handset.fragments;


import android.app.Fragment;
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
import android.widget.Button;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.ConsultationActivity;
import com.innotek.handset.activities.MyRoomsActivity;
import com.innotek.handset.activities.StationActivity;
import com.innotek.handset.service.UpdateMessageService;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.SQLiteCursorLoader;

public class HomeFragment extends Fragment implements LoaderCallbacks<Cursor>{

	private TextView mUserFullName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = getActivity(); 
		Intent intent = new Intent(context, UpdateMessageService.class);
		context.startService(intent);
		
		getLoaderManager().initLoader(22, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		//ImageView spaceshipImage = (ImageView)view.findViewById(R.id.id_user_avatar);
		//Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.hyperspace_jump);
		//spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		
		mUserFullName = (TextView)view.findViewById(R.id.id_user_title);
		
		Button mWorkFlowButton = (Button) view.findViewById(R.id.id_workflow);
		mWorkFlowButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(StationActivity.class);
			}
		});
		
		Button mRoomMonitor = (Button) view.findViewById(R.id.id_room_monitor);
		mRoomMonitor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(MyRoomsActivity.class);
			}
		});
		
		
		
		Button mMyRooms = (Button)view.findViewById(R.id.id_button_zixun);
		mMyRooms.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startNewActivity(ConsultationActivity.class);
			}
		});
	
		return view;
	}
	

	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		getActivity().startActivity(intent);
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new StateListCursorLoader(getActivity());
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Cursor c = data;
		String fullName = "";
		do{
			fullName = c.getString(c.getColumnIndex("firstName")) + c.getString(c.getColumnIndex("lastName"));
			
		}while(c.moveToNext());

		mUserFullName.setText(fullName);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mUserFullName.setText("");
	}


	private static class StateListCursorLoader extends SQLiteCursorLoader{
		
		public StateListCursorLoader(Context context){
			super(context);
		}
		
		@Override
		protected Cursor loadCursor(){
			
			DatabaseAdapter dbAdapter = new DatabaseAdapter(getContext());
			dbAdapter.open();
			
			SharedPreferences pref = getContext().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
			String user_id = pref.getString("USER_ID", null);
			
			Cursor c =  dbAdapter.getUser(user_id);			
			dbAdapter.close();
			return c;
		}
	}
}
