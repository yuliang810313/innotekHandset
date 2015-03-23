package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.innotek.handset.R;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.utils.DataManager;

public class ArbitrateFragment extends Fragment{

	private long preferRoomId;
	private long stationId;
	
	private DataManager dm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		dm = new DataManager(getActivity());
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_arbitration, container, false);
		Button mArbitrateEnd = (Button) view.findViewById(R.id.id_button_arbitrate);
		
		mArbitrateEnd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dm.updateRoomStage(preferRoomId, 7);
				startNewActivity(PreferListActivity.class);
			}
		});
		
		return view;
	}
	
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", preferRoomId);
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}
	
	public static ArbitrateFragment newInstance(long roomId, long stationId){
		Bundle args = new Bundle();
		args.putLong("ROOM_ID", roomId);
		args.putLong("STATION_ID", stationId);
		ArbitrateFragment af = new ArbitrateFragment();
		af.setArguments(args);
		return af;
	}
}
