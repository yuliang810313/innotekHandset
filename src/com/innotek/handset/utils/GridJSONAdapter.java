package com.innotek.handset.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.innotek.handset.RoomGridItem;
import com.innotek.handset.R;
import com.innotek.handset.TransparentLayout;
import com.innotek.handset.entities.Room;

/**
 * 
 * @author david
 *
 */

public class GridJSONAdapter extends BaseAdapter {

	private ArrayList<Room> list;
	private Context context;
	
	public GridJSONAdapter(Context context){
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RoomGridItem item;
		
		if(convertView == null){
			View v = LayoutInflater.from(context).inflate(R.layout.custom_gridview, parent, false);
			item = new RoomGridItem();
			
			item.setmStage((TextView)v.findViewById(R.id.id_current_stage));
			
//			TransparentLayout layout = (TransparentLayout)v.findViewById(R.id.id_tl);
//			int currentStage = (list.get(position).getCurrentStage() >> 3) + 1;
//			int width = 350 * currentStage / 19;
//			
//			layout.setRectHiehgt(100);
//			layout.setRectWidth(width);
//			
//			layout.setLayoutParams(new LayoutParams(width, 100));
//			layout.setInfoType(list.get(position).getInfoType());
//			
//			
//			item.setLayout(layout);
			
			item.setAddress((TextView)v.findViewById(R.id.address));
			item.setMsg((TextView)v.findViewById(R.id.msg));
			item.setmCheckBox((CheckBox)v.findViewById(R.id.id_checkbox_grid));
			
			v.setTag(item);
			convertView = v;
			
			
		}else{
			item = (RoomGridItem)convertView.getTag();
		}
		
		String address = "#"+list.get(position).getAddress();
		
		item.getmCheckBox().setChecked(list.get(position).isPrefer());
		
		item.getmStage().setText((list.get(position).getCurrentStage()>>3) * 100/19  + "%");
		item.getAddress().setText(address);
		item.getMsg().setTextColor(Color.BLACK);
		
		switch(list.get(position).getInfoType()){
		case 0:
			convertView.setBackgroundColor(TransparentLayout.ALERT_BACK);
			item.getMsg().setText("警报");
			break;
		case 2:
			float temp = list.get(position).getDryAct();
			Log.i(TAG, "the act is " + temp);
			//convertView.setBackgroundColor(TransparentLayout.NORMAL_BACK);	
			if(temp < 36)
			    convertView.setBackgroundColor(TransparentLayout.NORMAL_BACK);
			if(36 <=  temp && temp < 41)
				convertView.setBackgroundColor(TransparentLayout.SOLID_GEEN);
			if(41 <= temp && temp < 46)
				convertView.setBackgroundColor(TransparentLayout.SOLID_YELLOW);
			if(46 <= temp && temp < 53)
				convertView.setBackgroundColor(TransparentLayout.YELLOW);
			if(53 <= temp)
				convertView.setBackgroundColor(TransparentLayout.DEEP_YELLOW);
			item.getMsg().setText("正常");
			
			break;
		case 3:
			convertView.setBackgroundColor(0xFFFDD835);	
			item.getAddress().setTextColor(Color.BLACK);
			item.getMsg().setText("未知");
		}
		return convertView;
	}

	public ArrayList<Room> getList() {
		return list;
	}

	public void setList(ArrayList<Room> list) {
		this.list = list;
	}
	
	private static final String TAG = "Grid Adapter";

}
