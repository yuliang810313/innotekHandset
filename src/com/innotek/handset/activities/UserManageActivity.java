package com.innotek.handset.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.innotek.handset.R;

public class UserManageActivity extends BaseActivity{

	@Override
	protected Fragment createFragment() {
		return new UserManageFragment();
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("人员管理");
		super.onCreate(savedInstanceState);
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
	                Intent intent = new Intent(this, UserManageActivity.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(intent);
	                return true;
	      }

	      return super.onOptionsItemSelected(item);
	}

	private class UserManageFragment extends Fragment{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View view = inflater.inflate(R.layout.fragment_user_manage, container, false);
			return view;
		}
		
	}
}
