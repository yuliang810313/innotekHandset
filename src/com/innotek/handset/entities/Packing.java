package com.innotek.handset.entities;

public class Packing {
	private long id;
	private long preferRoomId;
	private String category;
	private String packingType;
	private float packingAmount;
	private String uniformity;
	private String categoryState;
	private String other;
	
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPackingType() {
		return packingType;
	}
	public void setPackingType(String packingType) {
		this.packingType = packingType;
	}
	public float getPackingAmount() {
		return packingAmount;
	}
	public void setPackingAmount(float packingAmount) {
		this.packingAmount = packingAmount;
	}
	public String getUniformity() {
		return uniformity;
	}
	public void setUniformity(String uniformity) {
		this.uniformity = uniformity;
	}
	public String getCategoryState() {
		return categoryState;
	}
	public void setCategoryState(String categoryState) {
		this.categoryState = categoryState;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	
	
}
