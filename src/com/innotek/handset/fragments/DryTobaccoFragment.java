package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innotek.handset.R;
import com.innotek.handset.activities.ArbitrateActivity;
import com.innotek.handset.activities.WorkFlowActivity;
import com.innotek.handset.entities.DryTobacco;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class DryTobaccoFragment extends Fragment{

	private RadioGroup mGroupQuality;
	private String mQuality;
	
	private EditText mDryWeight;
	private EditText mIssue;
		
	private long preferRoomId;
	
	private DataManager dm;
	private int bakingResult;
	private DatabaseAdapter adapter;
	private long stationId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		
		dm = new DataManager(getActivity());
		adapter = new DatabaseAdapter(this.getActivity());
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_dry_tobacco, container, false);
		
		mGroupQuality = (RadioGroup) view.findViewById(R.id.id_group_quality);
		mGroupQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mQuality = getValueOnSelect(group, checkedId);
				bakingResult = checkedId;
			}
		});
		
		mDryWeight = (EditText) view.findViewById(R.id.id_dry_weight);
		mIssue = (EditText) view.findViewById(R.id.id_edit_issue);
		
		Button mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveDryTobacco();
				if(bakingResult == R.id.radio1){
					//启动仲裁
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(ArbitrateActivity.class);
				}
				else{
					//结束烘烤流程
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(WorkFlowActivity.class);
				}
			}
		});
		
		Button mArbitrate = (Button) view.findViewById(R.id.id_button_arbitrate);
		mArbitrate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveDryTobacco();
				if(bakingResult == R.id.radio1){
					//启动仲裁
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(ArbitrateActivity.class);
				}
				else{
					//结束烘烤流程
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(WorkFlowActivity.class);
				}
				//startNewActivity(ArbitrateActivity.class);
			}
		});
		
		
		adapter.open();
		Cursor c = adapter.findDryTobacco(preferRoomId);
		adapter.close();
		if(c != null){
		
			mDryWeight.setText(c.getString(c.getColumnIndex("dry_weight")));
			mIssue.setText(c.getString(c.getColumnIndex("issue")));
			String quality = c.getString(c.getColumnIndex("quality"));
			if(quality.equals("烘烤质量正常")){
				mGroupQuality.check(R.id.radio0);
			}else if(quality.equals("烘烤质量有异议"))
				mGroupQuality.check(R.id.radio1);
		}
		
		
		
		
		return view;
	}
	
	//Factory method for create new instance
	public static DryTobaccoFragment newInstance(long roomId, long stationId){
		Bundle args = new Bundle();
		args.putLong("ROOM_ID", roomId);
		args.putLong("STATION_ID", stationId);
		DryTobaccoFragment fragment = new DryTobaccoFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", preferRoomId);
		
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}
	
	private void saveDryTobacco(){
		DryTobacco dt = new DryTobacco();
		dt.setDryWeight(Float.valueOf(mDryWeight.getText().toString()));
		dt.setPreferRoomId(preferRoomId);
		dt.setIssue(mIssue.getText().toString());
		dt.setQuality(mQuality);
		
		dm.saveTobaccoAfterBaking(dt);
	}
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
	}
}
