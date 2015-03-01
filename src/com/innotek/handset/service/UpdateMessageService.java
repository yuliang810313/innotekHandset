package com.innotek.handset.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.innotek.handset.utils.DatabaseAdapter;

public class UpdateMessageService extends IntentService {

	private static final String TAG = "UpdateMessageService";
	
	public UpdateMessageService(){
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {	
		DatabaseAdapter adapter = new DatabaseAdapter(this);
		
		SharedPreferences pref = this.getSharedPreferences("PREF_USER", MODE_PRIVATE);
		Log.i(TAG, pref.getString("USER_ID", null));
		adapter.updateDatabase(pref.getString("USER_ID", null));
		//start(this);
	}
	
	
	public static void start(Context context){
		Long updateFrequent =  1L * 3000 * 10;		
		boolean isAutoUpdate = true ;
	
		
		AlarmManager alarmManager  = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Intent updateIntent = new Intent(context, UpdateMessageService.class);
		PendingIntent pi = PendingIntent.getService(context, 9, updateIntent, 8);
		
		if(isAutoUpdate){
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime() + updateFrequent, 
									  updateFrequent, 
									  pi);
			
			Log.i(TAG, "Updating in backgroud...");
		}else{
			alarmManager.cancel(pi);
			pi.cancel();
		}
		
		
	}

}
