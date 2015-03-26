package com.innotek.handset.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
	
	private EditText mDryValue;
	private EditText mWetValue;
	private EditText mDurationTime;
	private EditText mStageTime;
	private TextView mStageNumber;
	
	private Button mSaveButton;
	
	private DatabaseAdapter dbAdapter;
	
	private static int mPageNumber;
	private static long mCurveId;
	private String mRoomId;
	private CurveLines curveLines;
	private int j;
	private CommandInformation cf;
	
	private String address;
	private String midAddress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPageNumber = getArguments().getInt(ARG_PAGE);
		mCurveId = getArguments().getLong(CURVE_ID);
		mRoomId = getArguments().getString("ROOM_ID");
		
		dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();
		Cursor cursor = dbAdapter.getRoomById(mRoomId);
		address = cursor.getString(cursor.getColumnIndex("address"));
		midAddress = cursor.getString(cursor.getColumnIndex("midAddress"));
		dbAdapter.close();
		
		cf = new CommandInformation(midAddress, address);
		
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_scrollview_curve_params, container, false);

		mDryValue = (EditText) view.findViewById(R.id.edit_dry_temperature);
		mWetValue = (EditText) view.findViewById(R.id.edit_wet_temperature);
		mDurationTime = (EditText) view.findViewById(R.id.edit_duration_time);
		mStageTime = (EditText) view.findViewById(R.id.edit_stage_time);
		
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
			mDryValue.setText(String.valueOf(dry));
			mWetValue.setText(String.valueOf(wet));
			mStageTime.setText(String.valueOf(sTime));
			mDurationTime.setText(String.valueOf(dTime));
		}
		
		ImageView mGotoStage = (ImageView) view.findViewById(R.id.id_button_goto_stage);
		ImageView mGotoDuration = (ImageView) view.findViewById(R.id.id_button_goto_duration);
		
		mGotoStage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jump((j-1) * 2);
			}
		});
		
		mGotoDuration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jump((j-1)*2 + 1);
			}
		});
		
		return view;
	}
	
	public void jump(int value){
		Log.i(TAG, "llaldlldl");
		Intent intent = new Intent(getActivity(), SendCommandService.class);
		
		intent.putExtra("JUMP_TO", value);
		intent.putExtra("ADDRESS", address);
		intent.putExtra("INFO_TYPE", 16);
		intent.putExtra("MID_ADDRESS", midAddress);
		
		getActivity().startService(intent);
		
	}
	
	private void saveCurveParams(){
		float dry = Float.valueOf(mDryValue.getText().toString());
		float wet = Float.valueOf(mWetValue.getText().toString());
		int sTime = Integer.valueOf(mStageTime.getText().toString());
		int dTime = Integer.valueOf(mDurationTime.getText().toString());
		
		DataManager curveManager = new DataManager(getActivity());
		
		int result = curveManager.modifyCurveParams(dry, wet, sTime, dTime, mCurveId, j);
		if(result > 0){
			//Send curve params
			
			dbAdapter.open();

			
			Cursor curve = dbAdapter.getCurveParamsByCurve(mCurveId);
			dbAdapter.close();
			
			cf = new CommandInformation(midAddress, address);
			cf.createCurveCommand(curve, 12);
			
			Intent intent = new Intent(getActivity(), SendCommandService.class);
			
			intent.putExtra("DRYS", cf.getmDryValues());
			intent.putExtra("WETS", cf.getmWetValues());
			intent.putExtra("ADDRESS", address);
			intent.putExtra("INFO_TYPE", cf.getmInfoType());
			intent.putExtra("MID_ADDRESS", midAddress);
			intent.putExtra("TIMES", cf.getmTimeLine());
			
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
				curveLines.setStage(getActivity().getIntent().getExtras().getInt("CURRENT_STAGE"));
				
				//curveLines.setStyle(0);
			}
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
