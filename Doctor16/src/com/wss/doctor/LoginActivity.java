package com.wss.doctor;

import java.util.ArrayList;

import com.wss.util.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class LoginActivity extends Activity implements OnClickListener{
	private Button login;
	private Button register;
	private EditText user;
	private EditText password;
	private EditText ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.pass);
		ip = (EditText) findViewById(R.id.ip_login);
		ip.setText("192.168.1.100");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login:
			Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			intent.putExtra("ip", ip.getText()+"");
			String account = user.getText().toString();
			String psw = password.getText().toString();
			Boolean res = judge(account,psw);
			if(res){
				startActivity(intent);
			}else{
				Toast.makeText(this, " ‰»Îµƒ’À∫≈ªÚ√ÿ√‹¥ÌŒÛ£°", 0).show();
			}
			break;

		case R.id.register:
			Intent intent2 = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent2);
			break;
		}
		
	}
	
	@SuppressLint("SdCardPath")
	private Boolean judge(String account, String psw) {
		// TODO 
		ArrayList<String> list = FileUtils.readTxt("/sdcard/DoctorConfig/account.txt");
		Log.i("TAG", list.toString());
		if(list.contains(account+"#"+psw)){
			return true;
		}else{
			return false;
		}
	}

}
