package com.innotek.handset.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.service.LoginService;

public class LoginFragment extends Fragment {
	
	private EditText mUserId;
	private EditText mPassword;
	private TextView mLoginHint;
	private Button mLoginButton;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		
		mLoginButton = (Button)view.findViewById(R.id.btn_login);
		mLoginHint = (TextView)view.findViewById(R.id.login_hint);
		
		mUserId= (EditText)view.findViewById(R.id.edit_user_id);
		mPassword = (EditText)view.findViewById(R.id.edit_password);
		
		TextView mVersionText = (TextView)view.findViewById(R.id.id_version);
		try{
			Context context = getActivity();
			mVersionText.setText("�汾:" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
		}catch(NameNotFoundException e){
			mVersionText.setText("δ֪�汾");
		}
		
		
		mLoginButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		
		//ע���¼״̬�㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("LOGIN_RESULT");
		getActivity().registerReceiver(loginReceiver, filter);
		
		return view;
	}
	
	
	private void login(){
		mLoginHint.setText("���ڵ�¼...");
		
		String userId = mUserId.getText().toString();
		String password = mPassword.getText().toString();
		
		Intent intent = new Intent(getActivity(), LoginService.class);
		intent.putExtra("userId", userId);
		intent.putExtra("password", password);
		
		getActivity().startService(intent);
	}
	
	
	
	//����LoginService�㲥�����µ�¼״̬
	private BroadcastReceiver loginReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			int code = intent.getExtras().getInt(LOGIN_CODE);
			switch(code){
			case 0:
				mLoginHint.setText("�û������������");
				break;
			case 100:
				mLoginHint.setText("Internal Error!");
				break;
			case 200:
				mLoginHint.setText("��¼�ɹ������ڳ�ʼ��");
				break;
			}
		}
	};
	
	
	//ע���㲥
	@Override
	public void onDestroy(){
		super.onDestroy();
		getActivity().unregisterReceiver(loginReceiver);
	}
	
	private static final String LOGIN_CODE = "loginCode";
}
