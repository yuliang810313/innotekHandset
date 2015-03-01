package com.innotek.handset.entities;

public class Station {
	
	private String id;
	private String name;
	private double latitude;
	private double longitude;
	private String state_id;
	
	
	public Station(String id, String name, double latitude, double longitude) {

		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		//this.state_id = state_id;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getState_id() {
		return state_id;
	}
	public void setState_id(String state_id) {
		this.state_id = state_id;
	}
	
	public String toString(){
		return this.name;
	}
	
	
	
}
