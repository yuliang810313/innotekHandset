package com.innotek.handset.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.innotek.handset.R;


public abstract class BaseActivityWithMenu extends BaseActivity implements OnMenuItemClickListener{

	private long roomId;
	
	public void setRoomId(long roomId){
		this.roomId = roomId;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_overflow:
	        	View view = this.findViewById(R.id.action_overflow);
	            showPopup(view);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showPopup(View v) {
		
			PopupMenu popup = new PopupMenu(this, v);
			popup.setOnMenuItemClickListener(this);
			MenuInflater inflater = popup.getMenuInflater();
			inflater.inflate(R.menu.stages, popup.getMenu());
			popup.show();
		
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.stage_1:
        	startNewActivity(RoomManageActivity.class);
            return true;
        case R.id.stage_2:
        	startNewActivity(NewTobaccoActivity.class);
            return true;
        case R.id.stage_3:
        	startNewActivity(PackingTobaccoActivity.class);
            return true;
        case R.id.stage_4:
        	startNewActivity(SelectCurveActivity.class);
            return true;
        case R.id.stage_5:
        	startNewActivity(MyRoomsActivity.class);
            return true;
        case R.id.stage_6:
        	startNewActivity(DryTobaccoActivity.class);
            return true;
        case R.id.stage_7:
        	startNewActivity(ArbitrateActivity.class);
            return true;
            
        default:
            return false;
		}
	}
	
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", roomId);
		startActivity(intent);
	}

}
