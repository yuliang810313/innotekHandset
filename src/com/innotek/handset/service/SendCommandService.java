package com.innotek.handset.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.innotek.handset.utils.JSONUtils;

public class SendCommandService extends IntentService {
	public static final String COMMAND_URL = "http://223.4.21.219:8080/commands";
	private static final String TAG = "COMMAND";
	
	
	public SendCommandService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		NameValuePair drys = new BasicNameValuePair("dry",
				intent.getStringExtra("DRYS"));

		NameValuePair wets = new BasicNameValuePair("wet", 
				intent.getStringExtra("WETS"));
		
		NameValuePair times = new BasicNameValuePair("sTime", 
				intent.getExtras().getString("TIMES"));
		
		NameValuePair midAddress = new BasicNameValuePair("midAddress", 
				intent.getStringExtra("MID_ADDRESS"));
		
		NameValuePair address = new BasicNameValuePair("address", 
				intent.getStringExtra("ADDRESS"));
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(drys);
		params.add(wets);
		params.add(times);
		params.add(midAddress);
		params.add(address);
		
		String result = JSONUtils.postJSON(COMMAND_URL, params);
		Log.i(TAG, result);
		

	}
	
	

}
