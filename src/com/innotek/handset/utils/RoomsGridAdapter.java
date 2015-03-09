package com.innotek.handset.utils;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.RoomGridItem;
import com.innotek.handset.TransparentLayout;
import com.innotek.handset.entities.Room;

/**
 * 
 * @author david
 *
 */

public class RoomsGridAdapter extends BaseAdapter {

	private ArrayList<Room> list;
	private Context context;
	
	public RoomsGridAdapter(Context context){
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
			View v = LayoutInflater.from(context).inflate(R.layout.custom_grid_item, parent, false);
			
			item = new RoomGridItem();
			item.setmStage((TextView)v.findViewById(R.id.id_room_stage));
			item.setmRoomId((TextView)v.findViewById(R.id.id_room_id));
			item.setmRoomCode((TextView)v.findViewById(R.id.id_room_code));
			item.setmStatus((TextView)v.findViewById(R.id.id_room_status));
			item.setmIsPrefer((CheckBox)v.findViewById(R.id.id_checkbox_prefer));
						
			v.setTag(item);
			convertView = v;
	
		}else{
			item = (RoomGridItem)convertView.getTag();
		}
		
		Room room = (Room)getItem(position);
		
		item.getmIsPrefer().setChecked(room.isPrefer());
		item.getmRoomId().setText("烤房" + room.getRoomNo());
		item.getmRoomCode().setText("烟基号" + room.getTobaccoNo());
		item.getmStage().setText((room.getCurrentStage()>>3) * 100 / 19  + "%");
		
		switch(room.getInfoType()){
		case 0:
			item.getmStatus().setText("警报");
			convertView.setBackgroundResource(R.drawable.border);
			break;
		case 2:
			item.getmStatus().setText("正常");
			break;
		}
		setStageBackgroundColor(item, room.getDryAct());
		return convertView;
	}

	private void setStageBackgroundColor(RoomGridItem item , float temperature){
		//Toast.makeText(context, "dry is " + temperature  , Toast.LENGTH_LONG).show();
		if(temperature < 36)
		    item.getmStage().setBackgroundColor(TransparentLayout.NORMAL_BACK);
		if(36 <=  temperature && temperature < 41)
			item.getmStage().setBackgroundColor(TransparentLayout.SOLID_GEEN);
		if(41 <= temperature && temperature < 46)
			item.getmStage().setBackgroundColor(TransparentLayout.SOLID_YELLOW);
		if(46 <= temperature && temperature < 53)
			item.getmStage().setBackgroundColor(TransparentLayout.YELLOW);
		if(53 <= temperature)
			item.getmStage().setBackgroundColor(TransparentLayout.DEEP_YELLOW);
	}
	
	public ArrayList<Room> getList() {
		return list;
	}

	public void setList(ArrayList<Room> list) {
		this.list = list;
	}
	
	public static final String TAG = "Grid Adapter";

}
