package com.innotek.handset.entities;

import java.util.Date;

public class PreferRoom {
	
	private long id;
	private long stationId;
	
	private String userId;
	private String roomNo;
	private String tobaccoNo;
	private String roomType;
	private String fanType;
	private String heatingEquipment;
	private String personInCharge;
	private String roomUser;
	private String phone;
	private String roomID;
	
	private String groupName;
	private int acState;
	private int fanState;
	private int blowerState;
	private int heatingState;
	private int airState;
	private String kettleState;
	private String other;
	
	private int roomStage;
	private Date createdAt;
	private long roomStageId;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getStationId() {
		return stationId;
	}
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getTobaccoNo() {
		return tobaccoNo;
	}
	public void setTobaccoNo(String tobaccoNo) {
		this.tobaccoNo = tobaccoNo;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getFanType() {
		return fanType;
	}
	public void setFanType(String fanType) {
		this.fanType = fanType;
	}
	public String getHeatingEquipment() {
		return heatingEquipment;
	}
	public void setHeatingEquipment(String heatingEquipment) {
		this.heatingEquipment = heatingEquipment;
	}
	public String getPersonInCharge() {
		return personInCharge;
	}
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	public String getRoomUser() {
		return roomUser;
	}
	public void setRoomUser(String roomUser) {
		this.roomUser = roomUser;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getAcState() {
		return acState;
	}
	public void setAcState(int acState) {
		this.acState = acState;
	}
	public int getFanState() {
		return fanState;
	}
	public void setFanState(int fanState) {
		this.fanState = fanState;
	}
	public int getBlowerState() {
		return blowerState;
	}
	public void setBlowerState(int blowerState) {
		this.blowerState = blowerState;
	}
	public int getHeatingState() {
		return heatingState;
	}
	public void setHeatingState(int heatingState) {
		this.heatingState = heatingState;
	}
	public int getAirState() {
		return airState;
	}
	public void setAirState(int airState) {
		this.airState = airState;
	}
	public String getKettleState() {
		return kettleState;
	}
	public void setKettleState(String kettleState) {
		this.kettleState = kettleState;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public int getRoomStage() {
		return roomStage;
	}
	public void setRoomStage(int roomStage) {
		this.roomStage = roomStage;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public long getRoomStageId() {
		return roomStageId;
	}
	public void setRoomStageId(long roomStageId) {
		this.roomStageId = roomStageId;
	}
	
	
	
}
