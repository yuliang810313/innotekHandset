package com.innotek.handset.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.innotek.handset.activities.HomeActivity;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.JSONUtils;

public class LoginService extends IntentService {

	public LoginService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
	
		NameValuePair userId = new BasicNameValuePair("userId", intent.getExtras().getString("userId"));
		
		NameValuePair password = new BasicNameValuePair("password", intent.getExtras().getString("password"));
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(userId);
		params.add(password);
		
		String result = JSONUtils.postJSON(LOGIN_URL, params);
		
		//�洢��¼״̬
		Intent loginIntent = new Intent();
		loginIntent.setAction("LOGIN_RESULT");
		
		try{
			JSONObject jObject = new JSONObject(result);
			JSONObject user = jObject.getJSONObject("user");
			if(user != null){
				//��¼�ɹ������û��������վ��Ϣ�洢�����ݿ�, 200��ʾ�ɹ���¼
				loginIntent.putExtra(LOGIN_CODE, 200);
				
				SharedPreferences pref = this.getSharedPreferences("PREF_USER", MODE_PRIVATE);
				String userID = pref.getString("USER_ID", null);
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
				
				//ʹ�� SharedPreferences�����Ƿ��г�ʼ�������ݿ�, ��user_id == null��ʼ�����ݿ�
				if(userID == null){
					Log.i(TAG, "��ʼ���������ݿ��ļ�");
					pref.edit().putString("USER_ID", user.getString("_id")).commit();
					DatabaseAdapter.resetDatabase(this);
					databaseAdapter.initUserData(this, user);
				}else{
					//�Ѿ���¼���û��Ƿ�͵�ǰ�û�һ��,�������ݿ���û�и��û���¼
					
					if(!user.getString("_id").equals(userID) && !databaseAdapter.isUserExist(user.getString("_id"))){
						Log.i(TAG, "��ʼ���û�����");
						pref.edit().clear().commit();
						pref.edit().putString("USER_ID", user.getString("_id")).commit();
						databaseAdapter.initUserData(this, user);
					}
				}
				
				
				
				//����Ӧ��������
				Intent aIntent = new Intent(this, HomeActivity.class);
				aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(aIntent);
					
			}else
				//��¼ʧ��
				loginIntent.putExtra("loginCode", 0);
			
		}catch(Exception e){
			Log.i(TAG, "Internal Error: " + e.getMessage());
			loginIntent.putExtra("loginCode", 0);
		}finally{
			sendBroadcast(loginIntent);
		}
		
	}
	
	private static final String LOGIN_CODE = "loginCode";
	//private static final String BACK_TASKS = "backTasks";
	private static final String LOGIN_URL = "http://223.4.21.219:8080/login";
	private static final String TAG = "LOGIN_SERVICE";

}
