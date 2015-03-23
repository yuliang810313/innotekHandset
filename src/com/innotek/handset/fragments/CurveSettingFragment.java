package com.innotek.handset.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.service.SendCommandService;
import com.innotek.handset.utils.CommandInformation;
import com.innotek.handset.utils.CurveLines;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;


public class CurveSettingFragment extends Fragment{
	
	public static final String ARG_PAGE = "page";
	public static final String CURVE_ID = "curve_id";
	public static final String TAG = "CurveParamsFragment";
	
	private EditText mDry;
	private EditText mWet;
	private EditText mDTime;
	private EditText mSTime;
	private TextView mStageNumber;
	private Button mSaveButton;
	
	private DatabaseAdapter dbAdapter;
	
	private static int mPageNumber;
	private static long mCurveId;
	private String mRoomId;
	private CurveLines curveLines;
	private int j;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPageNumber = getArguments().getInt(ARG_PAGE);
		mCurveId = getArguments().getLong(CURVE_ID);
		mRoomId = getArguments().getString("ROOM_ID");
		
		dbAdapter = new DatabaseAdapter(getActivity());
	}

	private void saveCurveParams(){
		float dry = Float.valueOf(mDry.getText().toString());
		float wet = Float.valueOf(mWet.getText().toString());
		int sTime = Integer.valueOf(mSTime.getText().toString());
		int dTime = Integer.valueOf(mDTime.getText().toString());
		
		DataManager curveManager = new DataManager(getActivity());
		
		int result = curveManager.modifyCurveParams(dry, wet, sTime, dTime, mCurveId, j);
		if(result > 0){
			//Send curve params
			
			dbAdapter.open();
			Cursor cursor = dbAdapter.getRoomById(mRoomId);
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String midAddress = cursor.getString(cursor.getColumnIndex("midAddress"));
			
			Cursor curve = dbAdapter.getCurveParamsByCurve(mCurveId);
			dbAdapter.close();
			
			CommandInformation cf = new CommandInformation(midAddress, address);
			cf.createCommandInformation(curve, 0);
			
			Intent intent = new Intent(getActivity(), SendCommandService.class);
			
			intent.putExtra("DRYS", cf.getDrys());
			intent.putExtra("WETS", cf.getWets());
			intent.putExtra("ADDRESS", address);
			intent.putExtra("MID_ADDRESS", midAddress);
			intent.putExtra("TIMES", cf.getTimes());
			
			getActivity().startService(intent);
		}else{
			
		}
	}
	
	private void confirmFireMissiles() {
	    DialogFragment newFragment = new FireMissilesDialogFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
	
	
	//检索出该自控仪温湿度控制曲线，将值传递到曲线绘制类
		private void prepareLineDatas(Cursor cursor){
			
			int stage = 0;
			int stageTime = 0;
			int stageAmountTime = 0;
			int currentStageNumber = 0 ;
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
					stageAmountTime += durationTimes[counter] + stageTimes[counter];
					
					mCurveId = cursor.getLong(cursor.getColumnIndex("curve_id"));
					counter++;
					
				}while(cursor.moveToNext());
			
				currentStageNumber = stage >> 5;
				
				if(currentStageNumber % 2 == 0){
					stageTime = durationTimes[currentStageNumber / 2];
				}else{
					stageTime = stageTimes[(currentStageNumber - 1) / 2];
				}
				
			
				
				curveLines.setDrys(drys);
				curveLines.setWets(wets);
				curveLines.setDurationTimes(durationTimes);
				curveLines.setStageTimes(stageTimes);
				curveLines.setStage(this.getActivity().getIntent().getExtras().getInt("CURRENT_STAGE"));
				
				//curveLines.setStyle(0);
			}
		}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,											
			Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_scrollview_curve_params, container, false);

		
		mDry = (EditText) view.findViewById(R.id.edit_dry_temperature);
		mWet = (EditText) view.findViewById(R.id.edit_wet_temperature);
		mDTime = (EditText) view.findViewById(R.id.edit_duration_time);
		mSTime = (EditText) view.findViewById(R.id.edit_stage_time);
		
		curveLines = (CurveLines) view.findViewById(R.id.id_curve);
		
		j = mPageNumber+1;
		
		dbAdapter.open();
		Cursor room = dbAdapter.getRoomById(mRoomId);
		
		Cursor curve = dbAdapter.getCurveParamsByRoom(room.getString(room.getColumnIndex("id")));
		
		dbAdapter.close();
		curveLines.setCurrentStage(j-1);
		curveLines.setStyle(1);
		prepareLineDatas(curve);
		
		
		mSaveButton = (Button)view.findViewById(R.id.id_save_btn);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirmFireMissiles();
			}
		});
		
		
		mStageNumber = (TextView)view.findViewById(R.id.id_stage_number);
		mStageNumber.setText("阶段 " + (j));
		
		
		dbAdapter.open();
		Cursor data =  dbAdapter.getCurveParamsByStageAndCurve(mPageNumber+1, mCurveId);
		dbAdapter.close();
		
		if(data != null){
			float dry;
			float wet;
			int sTime;
			int dTime;
			do{
				dry = data.getFloat(data.getColumnIndex("dry_value"));
				wet = data.getFloat(data.getColumnIndex("wet_value"));
				sTime = data.getInt(data.getColumnIndex("stage_time"));
				dTime = data.getInt(data.getColumnIndex("duration_time"));
				
				
			}while(data.moveToNext());
			mDry.setText(String.valueOf(dry));
			mWet.setText(String.valueOf(wet));
			mSTime.setText(String.valueOf(sTime));
			mDTime.setText(String.valueOf(dTime));
		}
		
		ImageView mGotoStage = (ImageView) view.findViewById(R.id.id_button_goto_stage);
		ImageView mGotoDuration = (ImageView) view.findViewById(R.id.id_button_goto_duration);
		
		mGotoStage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		mGotoDuration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		return view;
	}
	
    public static CurveSettingFragment newInstance(int stage, String roomId, long curveId) {
    	CurveSettingFragment fragment = new CurveSettingFragment();
    	
        Bundle args = new Bundle();
        
        args.putInt(ARG_PAGE, stage);
        args.putString("ROOM_ID", roomId);
        args.putLong(CURVE_ID, curveId);
        fragment.setArguments(args);
        
        return fragment;
    }
	
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private class FireMissilesDialogFragment extends DialogFragment {
    	@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                   .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   saveCurveParams();
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
}
