package com.innotek.handset.activities;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;

public class SearchActivity extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("统计数据查询");
		
	}

	private class SearchFragment extends ListFragment{
		
	}
}
