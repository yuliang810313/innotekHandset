package com.innotek.handset.utils;

import android.content.ContentValues;
import android.content.Context;

public class CurveManager {
	private DatabaseAdapter adapter;
	
	public CurveManager(Context context){
		adapter = new DatabaseAdapter(context);
	}
	public int modifyCurveParams(float dry, float wet, int sTime, int dTime,long curveId, int stageNo){
		ContentValues params = new ContentValues();
		
		params.put("dry_value", dry);
		params.put("wet_value", wet);
		params.put("duration_time", dTime);
		params.put("stage_time", sTime);
		
		adapter.open();
		int result = adapter.updateCurveParams(params, curveId, stageNo);
		adapter.close();
		return result;
	}
}
