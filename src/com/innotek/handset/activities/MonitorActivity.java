package com.innotek.handset.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.innotek.handset.R;
import com.innotek.handset.fragments.MonitorFragment;
import com.innotek.handset.utils.DatabaseAdapter;

public class MonitorActivity extends BaseActivity {
	private long preferRoomId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    String address = getIntent().getExtras().getString("roomAddress");
	    
	    SharedPreferences pref = getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		String userId = pref.getString("USER_ID", null);
	    
	    DatabaseAdapter adapter = new DatabaseAdapter(this);
	    adapter.open();
	    preferRoomId = adapter.findPreferRoomByUserAndAddress(userId, address);
	    adapter.close();
	    
	    setTitle("#" + address);
	}

	@Override
	protected Fragment createFragment() {
		return new MonitorFragment();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        getMenuInflater().inflate(R.menu.room_detail, menu);
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

	         case R.id.action_go_form:
	                // Hide the "empty" view since there is now at least one item in the list.
	               // findViewById(android.R.id.empty).setVisibility(View.GONE);
	                Intent intent = new Intent(this, PackingTobaccoActivity.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                intent.putExtra("ROOM_ID", preferRoomId);
	                startActivity(intent);
	                return true;
	      }

	      return super.onOptionsItemSelected(item);
	}

}
