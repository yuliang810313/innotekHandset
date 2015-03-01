package com.innotek.handset.utils;

import android.database.Cursor;
import android.util.Log;

public class CommandInformation {

	private String drys = "";
	private String wets = "";
	private String sTimes = "";
	private String dTimes = "";
	private String midAddress;
	private String address;
	
	private static final String TAG = "CommandInformation";
	
	public CommandInformation(String midAddress, String address){
		this.midAddress = midAddress;
		this.address = address;
	}
	
	public void createCommandInformation(Cursor c, int stage){

		do{
			drys += c.getFloat(c.getColumnIndex("dry_value")) + ",";
			wets += c.getFloat(c.getColumnIndex("wet_value")) + ",";
			sTimes += c.getInt(c.getColumnIndex("stage_time")) + ",";
			dTimes += c.getInt(c.getColumnIndex("duration_time")) + ",";
		}while(c.moveToNext());
		Log.i(TAG, drys);
	}

	public String getMidAddress() {
		return midAddress;
	}

	public void setMidAddress(String midAddress) {
		this.midAddress = midAddress;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDrys() {
		return drys;
	}

	public void setDrys(String drys) {
		this.drys = drys;
	}

	public String getWets() {
		return wets;
	}

	public void setWets(String wets) {
		this.wets = wets;
	}

	public String getsTimes() {
		return sTimes;
	}

	public void setsTimes(String sTimes) {
		this.sTimes = sTimes;
	}

	public String getdTimes() {
		return dTimes;
	}

	public void setdTimes(String dTimes) {
		this.dTimes = dTimes;
	}


	
	
	
	
}
