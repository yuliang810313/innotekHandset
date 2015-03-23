package com.innotek.handset.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.innotek.handset.R;
import com.innotek.handset.fragments.CurveSettingFragment;

public class CurveSettingActivity extends Activity {
	
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private static long curveId;
	private static String mRoomId;
	static final String TAG = "CurveSettingActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ÎÂÊª¶ÈÇúÏßÉèÖÃ");	
		
		setContentView(R.layout.activity_screen_slide);
		
		curveId = getIntent().getExtras().getLong("CURVE_ID");
		mRoomId = getIntent().getExtras().getString("ROOM_ID");
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(getIntent().getExtras().getInt("CURRENT_STAGE")/2  ) ;    
       
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
	}
	
	 
	
	
	
    
    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CurveSettingFragment.newInstance(position, mRoomId, curveId);
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
	

	
}
