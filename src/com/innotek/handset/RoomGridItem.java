package com.innotek.handset;


import android.widget.CheckBox;
import android.widget.TextView;

public class RoomGridItem {
	private TextView address;
	private TextView msg;
	private CheckBox mCheckBox;
	private TextView mStage;
	
	private TransparentLayout layout;
	
	public TextView getAddress() {
		return address;
	}
	public void setAddress(TextView address) {
		this.address = address;
	}
	public TextView getMsg() {
		return msg;
	}
	public void setMsg(TextView msg) {
		this.msg = msg;
	}
	public CheckBox getmCheckBox() {
		return mCheckBox;
	}
	public void setmCheckBox(CheckBox mCheckBox) {
		this.mCheckBox = mCheckBox;
	}
	public TransparentLayout getLayout() {
		return layout;
	}
	public void setLayout(TransparentLayout layout) {
		this.layout = layout;
	}
	public TextView getmStage() {
		return mStage;
	}
	public void setmStage(TextView mStage) {
		this.mStage = mStage;
	}
	
	
	
	
}