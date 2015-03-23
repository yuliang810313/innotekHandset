package com.innotek.handset.entities;

public class Tobacco {
	
	private long id;
	private long preferRoomId;
	private String tobaccoType;
	private String part;
	private String waterContent;
	private String maturity;
	private String breed;
	private String quality;
	
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
	public String getTobaccoType() {
		return tobaccoType;
	}
	public void setTobaccoType(String tobaccoType) {
		this.tobaccoType = tobaccoType;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getWaterContent() {
		return waterContent;
	}
	public void setWaterContent(String waterContent) {
		this.waterContent = waterContent;
	}
	public String getMaturity() {
		return maturity;
	}
	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	
}
