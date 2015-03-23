package com.innotek.handset.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.innotek.handset.R;
import com.innotek.handset.fragments.DryTobaccoFragment;

public class DryTobaccoActivity extends BaseActivity implements OnMenuItemClickListener{
	
	private long roomId;
	private long stationId;
	
	@Override
	protected Fragment createFragment() {
		roomId = getIntent().getExtras().getLong("ROOM_ID");
		stationId = getIntent().getExtras().getLong("STATION_ID");
		return DryTobaccoFragment.newInstance(roomId, stationId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setTitle("øæ∫Û—Ã“∂–≈œ¢");
		super.onCreate(savedInstanceState);
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
        	startNewActivity(PreferListActivity.class);
            return true;
        case R.id.stage_5:
        	startNewActivity(DryTobaccoActivity.class);
            return true;
        case R.id.stage_6:
        	startNewActivity(ArbitrateActivity.class);
            return true;
            
        default:
            return false;
		}
	}
	
	//Start new activity
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", roomId);
		startActivity(intent);
	}
}
