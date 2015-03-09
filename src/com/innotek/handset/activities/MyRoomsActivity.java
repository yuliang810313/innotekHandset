package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.innotek.handset.R;
import com.innotek.handset.fragments.RoomsGridFragment;

public class MyRoomsActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return new RoomsGridFragment();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("ÎÒµÄ¿¾·¿");
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
	                // Navigate "up" the demo structure to the launchpad activity.
	                // See http://developer.android.com/design/patterns/navigation.html for more.
	                NavUtils.navigateUpTo(this, new Intent(this, HomeActivity.class));
	                return true;

	         case R.id.action_add_item:
	                // Hide the "empty" view since there is now at least one item in the list.
	               // findViewById(android.R.id.empty).setVisibility(View.GONE);
	                Intent intent = new Intent(this, NewRoomActivity.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(intent);
	                return true;
	      }

	      return super.onOptionsItemSelected(item);
	}
	
}
