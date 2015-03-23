package com.innotek.handset.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.CurveSettingActivity;
import com.innotek.handset.entities.Room;
import com.innotek.handset.utils.CurveLines;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.JSONUtils;

public class MonitorFragment extends Fragment {
	
	private TextView mTitle;
	private TextView mRoomStatus;
	private TextView mCurrentStageNumber;
	private TextView mCurrentStageTime;
	
	//�ܺ濾ʱ��
	private TextView mTotalStageTime;
	
	//����Ŀ��ֵ
	private TextView mDryTarget;
	
	//����ʵ��ֵ
	private TextView mDryAct;
	
	//ʪ��Ŀ��ֵ
	private TextView mWetTarget;
	
	//ʪ��ʵ��ֵ
	private TextView mWetAct;
	
	//ʧˮĿ������
	private TextView mLostTarget;
	
	//ʧˮʵ������
	private TextView mLostAct;

	//װ����
	private TextView mTobaccoCount;
	
	private TextView mAlert1;
	private TextView mAlert2;
	private TextView mAlert3;
	private TextView mAlert4;
	private TextView mAlert5;
	private TextView mAlert6;
	
	private Button mSettingButton;
	
	private Bundle bundle;
	
	private long mCurveId;
	private String mRoomId;
	
	private CurveLines curveLines;
	private DatabaseAdapter dbAdapter;
	
	private int currentStageNumber = 0 ;

	private boolean visible = true;
	
	private static final int ALERT = 0xFFB71C1C;
	private static final int NORMAL = 0xFF33691E;
	
	public static final String TAG = "Monitor Fragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		bundle = getActivity().getIntent().getExtras();
		dbAdapter = new DatabaseAdapter(getActivity());
		
		new FetchRoomTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_monitor, container, false);
		
		mSettingButton = (Button)view.findViewById(R.id.btn_curve_setting);
		
		mRoomStatus = (TextView)view.findViewById(R.id.id_room_status);
		mCurrentStageNumber = (TextView)view.findViewById(R.id.id_stage_no);
		mCurrentStageTime = (TextView)view.findViewById(R.id.id_room_stage_time);
		mTotalStageTime = (TextView)view.findViewById(R.id.id_room_total_time);
		
		mDryTarget = (TextView)view.findViewById(R.id.id_dry_target);
		mWetTarget = (TextView)view.findViewById(R.id.id_wet_target);
		mLostTarget = (TextView)view.findViewById(R.id.id_lost_speed_target);
		mTobaccoCount = (TextView)view.findViewById(R.id.id_tobacco_count);
		
		mDryAct = (TextView)view.findViewById(R.id.id_dry_act);
		mWetAct = (TextView)view.findViewById(R.id.id_wet_act);
		mLostAct = (TextView)view.findViewById(R.id.id_lost_speed_act);
		mTitle = (TextView)view.findViewById(R.id.id_title);
		
		
		mAlert1 = (TextView)view.findViewById(R.id.id_alert_1);
		mAlert2 = (TextView)view.findViewById(R.id.id_alert_2);
		mAlert3 = (TextView)view.findViewById(R.id.id_alert_3);
		mAlert4 = (TextView)view.findViewById(R.id.id_alert_4);
		mAlert5 = (TextView)view.findViewById(R.id.id_alert_5);
		mAlert6 = (TextView)view.findViewById(R.id.id_alert_6);
		
		//���о�����ʾ
		TextView[] alerts = {mAlert1, mAlert2, mAlert3, mAlert4, mAlert5, mAlert6};
		
		mTitle.setText("����" + bundle.getString("ROOM_NO"));

		final LinearLayout mLayout = (LinearLayout) view.findViewById(R.id.id_layout_text);
		
		curveLines = (CurveLines)view.findViewById(R.id.id_curve);
		curveLines.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(visible){
					mLayout.setVisibility(View.GONE);
					
				}
				else
					mLayout.setVisibility(View.VISIBLE);

				visible = !visible;
				return false;
			}
		});
		
		
		mRoomId = bundle.getString("room_id");		
		dbAdapter.open();
		Cursor room = dbAdapter.getRoomById(mRoomId);
		
		Cursor curve = dbAdapter.getCurveParamsByRoom(room.getString(room.getColumnIndex("id")));
		dbAdapter.close();
		
		prepareLineDatas(curve);
		
		String dryAct = room.getString(room.getColumnIndex("dry_act"));
		String wetAct = room.getString(room.getColumnIndex("wet_act"));
		String tobacco = room.getString(room.getColumnIndex("amount"));
		
		switch(bundle.getInt("infoType")){
		case 0:
			mRoomStatus.setText("����");
			mRoomStatus.setTextColor(ALERT);
			
			mTitle.setTextColor(0xFFFFFFFF);
			mTitle.setBackgroundColor(ALERT);
			
			//�������״̬��Ϣ���ı���ʽ���棬�����ֳ��ַ�����
			String statusArray[] = room.getString(room.getColumnIndex("status")).split(",");
		
			for(int i = 0; i < alerts.length; i++){
				//�����ǰ������Ա�Ǹ���ʪ��ʵ���¶ȣ���ʾ��ϢҪ��������ֵ����ĳ�Ա��ȡ
				if(statusArray[i+2] != null)
					alerts[i].setText("����: " + statusArray[i+2]);
				else
					continue;
			}
			
			break;
			
		case 2:
		
			mRoomStatus.setVisibility(View.GONE);
			view.findViewById(R.id.id_layout_alert_title).setVisibility(View.GONE);
			//�������б�����ʾ��Ϣ
			for(int i = 0; i < alerts.length; i++){
				alerts[i].setVisibility(View.GONE);
			}
			
			mTobaccoCount.setText("װ����: " + tobacco + "Kg");
			break;
		};
		
		mDryAct.setText("����ʵ���¶�: " + dryAct + "��");
		mWetAct.setText("ʪ��ʵ���¶�: " + wetAct + "��");
				
		mLostTarget.setText("ʧˮĿ������: N/A");
		mLostAct.setText("ʧˮʵ������: N/A");
		
		//������ʪ�ȿ�������
		mSettingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setCurveParams(bundle);
			}
		});
		
		return view;
	}
	
	
	private void setCurveParams(Bundle bundle){
		
		String midAddress = bundle.getString("midAddress");
		String address = bundle.getString("roomAddress");
		
		Intent intent = new Intent(getActivity(), CurveSettingActivity.class);
		intent.putExtra("MID_ADDRESS", midAddress);
		intent.putExtra("ROOM_ADDRESS", address);
		intent.putExtra("CURVE_ID", mCurveId);
		intent.putExtra("ROOM_ID", mRoomId);
		intent.putExtra("CURRENT_STAGE", currentStageNumber);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		startActivity(intent);
	}
		
	//���������Կ�����ʪ�ȿ������ߣ���ֵ���ݵ����߻�����
	private void prepareLineDatas(Cursor cursor){
		
		int stage = 0;
		int stageTime = 0;
		int stageAmountTime = 0;
	
		if(cursor != null){
			int counter = 0;
			int length = cursor.getCount();
			
			float drys[] = new float[length];
			float wets[] = new float[length];
			
			//��������ʱ��
			int durationTimes[] = new int[length];
			
			//�׶�ά��ʱ��
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
			
			mCurrentStageTime.setText("�׶�ʱ��:" + String.valueOf(stageTime) + "Сʱ");
			//mCurrentStageNumber.setText("��ǰ�׶�:" + String.valueOf(currentStageNumber/2 + 1));
			mCurrentStageNumber.setText("��ǰ�׶�:" + String.valueOf(currentStageNumber + 1));
			
			mTotalStageTime.setText("��ʱ��:" + String.valueOf(stageAmountTime) + "Сʱ");
			
			mDryTarget.setText("����Ŀ���¶�:" + drys[currentStageNumber / 2] + "��");
			mWetTarget.setText("ʪ��Ŀ���¶�:" + wets[currentStageNumber / 2] + "��");
			
			curveLines.setDrys(drys);
			curveLines.setWets(wets);
			curveLines.setDurationTimes(durationTimes);
			curveLines.setStageTimes(stageTimes);
			curveLines.setCurrentStage(currentStageNumber);
			
			//curveLines.setStyle(0);
		}
	}

	
	//�������ݲ�����װ����������ʪ��Ŀ���¶ȣ�ʹ��AsyncTask��ȡ���Կ�������״ֵ̬
	private  class FetchRoomTask extends AsyncTask<Void, Void, Void>{

		private String tobaccoAmount;
		
		@Override
		protected Void doInBackground(Void...params){
			String result = JSONUtils.getInformation(bundle.getString(RoomsGridFragment.ROOM_ADDRESS), 
													 bundle.getString(RoomsGridFragment.MID_ADDRESS) );
			
			if(result != null){
				try{
					JSONObject jObject = new JSONObject(result);
					JSONObject object = jObject.getJSONObject("info");
					JSONArray a = object.getJSONArray("information");
			
					int [] datas = new int[a.length()];
					for(int j = 0; j < a.length(); j++){
						datas[j] = a.getInt(j);
					}
				
					int infoType = object.getInt("infoType");
				
					String [] status = Room.getMsgsContent(infoType, datas);

					tobaccoAmount = status[4];
				}catch(JSONException e){
					e.printStackTrace();
				}
				
			}else{

				 tobaccoAmount = "װ����: N/A";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mTobaccoCount.setText("װ����: " + tobaccoAmount + "Kg");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 private class CurveSettingsDialogFragment extends DialogFragment {
		 	private int currentStage;
		 
		 	private EditText mDry;
		 	private EditText mWet;
		 	private EditText mDTime;
		 	private EditText mSTime;
		 	private TextView stage;
		 	
		 	public CurveSettingsDialogFragment(int pointX){
			 	this.currentStage = pointX;
		 	}
		 
		 	public void initEditText (View view){
				mDry = (EditText)view.findViewById(R.id.edit_dry_temperature);
				mWet = (EditText)view.findViewById(R.id.edit_wet_temperature);
				mDTime = (EditText)view.findViewById(R.id.edit_duration_time);
				mSTime = (EditText)view.findViewById(R.id.edit_stage_time);
				stage  = (TextView) view.findViewById(R.id.id_stage_number);
		 	}
		 	
		 	
			private void saveCurveParams(){
				float dry = Float.valueOf(mDry.getText().toString());
				float wet = Float.valueOf(mWet.getText().toString());
				int sTime = Integer.valueOf(mSTime.getText().toString());
				int dTime = Integer.valueOf(mDTime.getText().toString());
				
				DataManager curveManager = new DataManager(getActivity());
				
				int result = curveManager.modifyCurveParams(dry, wet, sTime, dTime, mCurveId, currentStage);
				if(result > 0){
					//Send curve params
					Log.i(TAG, "Send curve Params to device");
				}else{
					
				}
			}
		 	
	    	@Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	            // Use the Builder class for convenient dialog construction
	            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	            
	            LayoutInflater inflater = getActivity().getLayoutInflater();
	            View view = inflater.inflate(R.layout.curve_form, null);
	            initEditText(view);
	            stage.setText(String.valueOf("�׶�: " + currentStage));
	             
	    		dbAdapter.open();
	    		
	    		Cursor data =  dbAdapter.getCurveParamsByStageAndCurve(currentStage, mCurveId);
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
	            
	            
	            builder.setView(view);
	            
	            
	            builder.setMessage(R.string.curve_settings_dialog_title)
	                   .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                    	   saveCurveParams();
	                   		dbAdapter.open();
	                		Cursor room = dbAdapter.getRoomById(bundle.getString("room_id"));
	                		Cursor curve = dbAdapter.getCurveParamsByRoom(room.getString(room.getColumnIndex("id")));
	                		dbAdapter.close();
	                		
	                		prepareLineDatas(curve);
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
		*/
	
}
