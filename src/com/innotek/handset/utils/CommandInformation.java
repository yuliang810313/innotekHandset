package com.innotek.handset.utils;

import java.util.HashMap;

import android.database.Cursor;

public class CommandInformation {

	private int mInfoType;
	private String mDryValues = "";
	private String mWetValues = "";
	private String mStageTimes = "";
	private String mDurationTimes = "";
	private String mMidAddress;
	private String mAddress;
	private String mTimeLine;
	
	public static final String TAG = "CommandInformation";

	public CommandInformation(String midAddress, String address){
		this.mMidAddress = midAddress;
		this.mAddress = address;
	}
	
	public void createCurveCommand(Cursor c, int infoType){
		this.mInfoType = infoType;
		
		mTimeLine = "";
		do{
			mDryValues += c.getFloat(c.getColumnIndex("dry_value")) + ",";
			mWetValues += c.getFloat(c.getColumnIndex("wet_value")) + ",";
			mDurationTimes = c.getInt(c.getColumnIndex("duration_time")) + ",";
			mTimeLine += mDurationTimes;
			
			if(c.getInt(c.getColumnIndex("stage_time")) != 0){
				mStageTimes = c.getInt(c.getColumnIndex("stage_time")) + ",";
				mTimeLine += mStageTimes;
			}
		}while(c.moveToNext());
	}
	
	
	public HashMap<Integer, Integer> jumpToSpecStage(int type, int targetValue){
		HashMap<Integer, Integer> value = new HashMap<Integer, Integer>();
		value.put(type, targetValue);
		return value;
	}

	public int getmInfoType() {
		return mInfoType;
	}

	public void setmInfoType(int mInfoType) {
		this.mInfoType = mInfoType;
	}

	public String getmDryValues() {
		return mDryValues;
	}

	public void setmDryValues(String mDryValues) {
		this.mDryValues = mDryValues;
	}

	public String getmWetValues() {
		return mWetValues;
	}

	public void setmWetValues(String mWetValues) {
		this.mWetValues = mWetValues;
	}

	public String getmMidAddress() {
		return mMidAddress;
	}

	public void setmMidAddress(String mMidAddress) {
		this.mMidAddress = mMidAddress;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmTimeLine() {
		return mTimeLine;
	}

	public void setmTimeLine(String mTimeLine) {
		this.mTimeLine = mTimeLine;
	}

	
}
