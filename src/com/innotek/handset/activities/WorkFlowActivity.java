package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.innotek.handset.R;
import com.innotek.handset.fragments.PreferListFragment;

public class WorkFlowActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return new PreferListFragment();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("烘烤业务管理");
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        getMenuInflater().inflate(R.menu.menu_add_room, menu);
	        return true;
     }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	         case android.R.id.home:
	                NavUtils.navigateUpTo(this, new Intent(this, HomeActivity.class));
	                return true;

	         case R.id.action_add_item:
	            
	                Intent intent = new Intent(this, RoomManageActivity.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(intent);
	                return true;
	      }

	      return super.onOptionsItemSelected(item);
	}

}
