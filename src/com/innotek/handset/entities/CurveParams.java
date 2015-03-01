package com.innotek.handset.entities;


public class CurveParams {
	private long curveId;
	private double dry;
	private double wet;
	private int stageTime;
	private int durationTime;
	private int stage;
	private int stageNo;
	
	
	public CurveParams(){}
	
	public CurveParams(long curveId, double dry, double wet, int stageTime,
			int durationTime, int stage) {
		
		this.curveId = curveId;
		this.dry = dry;
		this.wet = wet;
		this.stageTime = stageTime;
		this.durationTime = durationTime;
		this.stage = stage;
	}
	
	
	
	public int getStageNo() {
		return stageNo;
	}

	public void setStageNo(int stageNo) {
		this.stageNo = stageNo;
	}

	public long getCurveId() {
		return curveId;
	}
	public void setCurveId(long curveId) {
		this.curveId = curveId;
	}
	public double getDry() {
		return dry;
	}
	public void setDry(double dry) {
		this.dry = dry;
	}
	public double getWet() {
		return wet;
	}
	public void setWet(double wet) {
		this.wet = wet;
	}
	public int getStageTime() {
		return stageTime;
	}
	public void setStageTime(int stageTime) {
		this.stageTime = stageTime;
	}
	public int getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	@Override
	public String toString(){
		return  " 干球: " + this.getDry() + " 湿球: " + this.getWet() + " 阶段时间: " + this.getStageTime() +
				" 持续时间: " + this.getDurationTime();
	}
	
	
	
}
