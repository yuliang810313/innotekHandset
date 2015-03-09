package com.innotek.handset.entities;

import com.innotek.handset.utils.MessageParser;

public class Room {
	private String id;
	private String station_id;
	private String user_id;
	private int infoType;
	private String address;
	private String midAddress;
	private int isBelow;
	private String updatedAt;
	private String status;
	private float dryTarget;
	private float dryAct;
	private float wetTarget;
	private float wetAct;
	private String stageTime;
	private float amount;
	private boolean isPrefer;
	private int[] data;
	private int currentStage;
	
	private String tobaccoNo;
	private String roomNo;
	
	public int getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(int currentStage) {
		this.currentStage = currentStage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMidAddress() {
		return midAddress;
	}

	public void setMidAddress(String midAddress) {
		this.midAddress = midAddress;
	}

	public int getIsBelow() {
		return isBelow;
	}

	public void setIsBelow(int isBelow) {
		this.isBelow = isBelow;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatus() {
		return status;
		
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getDryTarget() {
		return dryTarget;
	}

	public void setDryTarget(float dryTarget) {
		this.dryTarget = dryTarget;
	}

	public float getDryAct() {
		return dryAct;
	}

	public void setDryAct(float dryAct) {
		this.dryAct = dryAct;
	}

	public float getWetTarget() {
		return wetTarget;
	}

	public void setWetTarget(float wetTarget) {
		this.wetTarget = wetTarget;
	}

	public float getWetAct() {
		return wetAct;
	}

	public void setWetAct(float wetAct) {
		this.wetAct = wetAct;
	}

	public String getStageTime() {
		return stageTime;
	}

	public void setStageTime(String stageTime) {
		this.stageTime = stageTime;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	
	
	public boolean isPrefer() {
		return isPrefer;
	}

	public void setPrefer(boolean isPrefer) {
		this.isPrefer = isPrefer;
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public static String[] getMsgsContent(int type, int[] data){
		String [] contents;
		
		switch(type){
		case 0:
			contents = MessageParser.parseAlert(data);
			break;
		case 2:
			contents =  MessageParser.parseInformations(data);
			break;
		default:
			contents = new String[]{""};
		}
		
		return contents;
	}

	public String getTobaccoNo() {
		return tobaccoNo;
	}

	public void setTobaccoNo(String tobaccoNo) {
		this.tobaccoNo = tobaccoNo;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	
}
