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
	
	private long curveId;
	private CurveLines curveLines;
	private DatabaseAdapter dbAdapter;
	private int currentStageNumber = 0;
	
	private static final int ALERT = 0xFFB71C1C;
	private static final int NORMAL = 0xFF33691E;
	static final String TAG = "Monitor Fragment";
	
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
		
		//��װ���о�����ʾ
		TextView[] alerts = {mAlert1, mAlert2, mAlert3, mAlert4, mAlert5, mAlert6};
		
		
		mTitle.setText(bundle.getString(RoomsFragment.MID_ADDRESS) + "-" + bundle.getString(RoomsFragment.ROOM_ADDRESS));

		
		curveLines = (CurveLines)view.findViewById(R.id.id_curve);
		
		
		dbAdapter.open();
		Cursor room = dbAdapter.getRoomById(bundle.getString("room_id"));
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
			mRoomStatus.setText("����");
			mRoomStatus.setTextColor(NORMAL);
			
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
		intent.putExtra("CURVE_ID", curveId);
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
				
				curveId = cursor.getLong(cursor.getColumnIndex("curve_id"));
				counter++;
				
			}while(cursor.moveToNext());
		
			currentStageNumber = stage >> 3;
			
			if(currentStageNumber % 2 == 0){
				stageTime = durationTimes[currentStageNumber / 2];
			}else{
				stageTime = stageTimes[(currentStageNumber - 1) / 2];
			}
			
			mCurrentStageTime.setText("�׶�ʱ��:" + String.valueOf(stageTime) + "Сʱ");
			mCurrentStageNumber.setText("��ǰ�׶�:" + String.valueOf(currentStageNumber + 1));
			
			mTotalStageTime.setText("��ʱ��:" + String.valueOf(stageAmountTime) + "Сʱ");
			
			mDryTarget.setText("����Ŀ���¶�:" + drys[currentStageNumber / 2] + "��");
			mWetTarget.setText("ʪ��Ŀ���¶�:" + wets[currentStageNumber / 2] + "��");
			
			curveLines.setDrys(drys);
			curveLines.setWets(wets);
			curveLines.setDurationTimes(durationTimes);
			curveLines.setStageTimes(stageTimes);
			curveLines.setCurrentStage(currentStageNumber);
			
			curveLines.setStyle(0);
		}
	}

	
	//�������ݲ�����װ����������ʪ��Ŀ���¶ȣ�ʹ��AsyncTask��ȡ���Կ�������״ֵ̬
	private  class FetchRoomTask extends AsyncTask<Void, Void, Void>{

		private String tobaccoAmount;
		
		@Override
		protected Void doInBackground(Void...params){
			String result = JSONUtils.getInformation(bundle.getString(RoomsFragment.ROOM_ADDRESS), bundle.getString(RoomsFragment.MID_ADDRESS) );
			
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
		
	
}