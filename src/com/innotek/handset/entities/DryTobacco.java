package com.innotek.handset.entities;

public class DryTobacco {
	private long id;
	private long preferRoomId;
	private float dryWeight;
	private String quality;
	private String issue;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPreferRoomId() {
		return preferRoomId;
	}
	public void setPreferRoomId(long preferRoomId) {
		this.preferRoomId = preferRoomId;
	}
	public float getDryWeight() {
		return dryWeight;
	}
	public void setDryWeight(float dryWeight) {
		this.dryWeight = dryWeight;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	
	
}
