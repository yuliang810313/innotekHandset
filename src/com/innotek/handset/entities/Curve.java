package com.innotek.handset.entities;

public class Curve {
	private String title;
	private String roomId;
	
	
	public Curve(String title, String roomId) {
		super();
		this.title = title;
		this.roomId = roomId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	
}
