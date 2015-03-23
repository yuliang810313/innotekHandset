package com.innotek.handset.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.innotek.handset.R;

public class ConsultationActivity extends BaseActivity {

	@Override
	protected Fragment createFragment() {
		return new ConsultationFragment();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("ºæ¿¾¼¼Êõ×ÉÑ¯");
		super.onCreate(savedInstanceState);
	}


	private class ConsultationFragment extends Fragment{

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View view = inflater.inflate(R.layout.fragment_web, container, false);
			
			WebView myWebView = (WebView)view.findViewById(R.id.webview);
			myWebView.loadUrl("http://223.4.21.219:3000/documents");
			
			return view;
		}
		
	}
}
