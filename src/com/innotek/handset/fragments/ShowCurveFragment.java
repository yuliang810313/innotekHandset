package com.innotek.handset.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innotek.handset.R;
import com.innotek.handset.utils.CurveLines;
import com.innotek.handset.utils.DatabaseAdapter;

public class ShowCurveFragment extends Fragment {
	private DatabaseAdapter dbAdapter;
	private Bundle bundle;

	private int currentStageNumber;
	private long mCurveId;
	private CurveLines curveLines;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		bundle = getActivity().getIntent().getExtras();
		dbAdapter = new DatabaseAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_curve, container, false);
		
		curveLines = (CurveLines)v.findViewById(R.id.id_curve);
		
		dbAdapter.open();
		
		//Cursor room = dbAdapter.getRoomById(bundle.getString("room_id"));
		Cursor curve = dbAdapter.getCurveParamsByRoomId(bundle.getString("room_id"));
		
		dbAdapter.close();
		prepareLineDatas(curve);
		
		return v;
	}
	
	//检索出该自控仪温湿度控制曲线，将值传递到曲线绘制类
		private void prepareLineDatas(Cursor cursor){
			
			int stage = 0;
		
			
			if(cursor != null){
				int counter = 0;
				int length = cursor.getCount();
				
				float drys[] = new float[length];
				float wets[] = new float[length];
				
				//持续升温时间
				int durationTimes[] = new int[length];
				
				//阶段维持时间
				int stageTimes[] = new int[length];
				
				do{
					drys[counter] = cursor.getFloat(cursor.getColumnIndex("dry_value"));
					wets[counter] = cursor.getFloat(cursor.getColumnIndex("wet_value"));
					
					durationTimes[counter] = cursor.getInt(cursor.getColumnIndex("duration_time"));
					stageTimes[counter] = cursor.getInt(cursor.getColumnIndex("stage_time"));
					
					stage = cursor.getInt(cursor.getColumnIndex("stage"));
					
					
					mCurveId = cursor.getLong(cursor.getColumnIndex("curve_id"));
					counter++;
					
				}while(cursor.moveToNext());
			
				currentStageNumber = stage >> 3;
				
				
				
				
				curveLines.setDrys(drys);
				curveLines.setWets(wets);
				curveLines.setDurationTimes(durationTimes);
				curveLines.setStageTimes(stageTimes);
				curveLines.setCurrentStage(currentStageNumber);
				
				//curveLines.setStyle(0);
			}
	
		}
}
