package com.wss.doctor;

import java.util.HashMap;

import com.wss.util.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class RegisterActivity extends Activity implements OnClickListener{
	private EditText hospital;
	private EditText department;
	private EditText name;
	private EditText sex;
	private EditText age;
	private EditText user;
	private EditText password;
	private Button submit;
	private HashMap<String, String> map = new HashMap<String, String>();
	private String[] strings = {"ҽԺ","����","����","�Ա�","����","�˺�","����"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		hospital = (EditText) findViewById(R.id.hospital);
		department = (EditText) findViewById(R.id.department);
		name = (EditText) findViewById(R.id.name);
		sex = (EditText) findViewById(R.id.sex);
		age = (EditText) findViewById(R.id.age);
		user = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.psw);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		
		FileUtils.creatAccount();
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View arg0) {
		String check = "";
		switch (arg0.getId()) {
		case R.id.submit:
			check = check+save("hospital",hospital);
			check = check+save("department",department);
			check = check+save("name",name);
			check = check+save("sex",sex);
			check = check+save("age",age);
			check = check+save("account",user);
			check = check+save("psw",password);
			Log.i("TAG", check);
			
			if(check.contains("0")){
				int index = check.indexOf("0");
				Toast.makeText(this, "����д"+strings[index], 0).show();
			}else{
				//TODO ���ע����˺��Ƿ��Ѵ���
				check = "";
				saveMap2txt(map);
				saveAccount2txt(map.get("account"),map.get("psw"));
				Toast.makeText(this, "ע��ɹ�", 0).show();
			}
			//�������
			break;
		}
	}
	
	private void saveAccount2txt(String string, String string2) {
		FileUtils.write2txt("/sdcard/DoctorConfig/", "account", map.get("account")+"#"+map.get("psw"));
	}

	@SuppressLint("SdCardPath")
	private void saveMap2txt(HashMap<String, String> map2) {
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[0]+" : "+map2.get("hospital"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[1]+" : "+map2.get("department"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[2]+" : "+map2.get("name"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[3]+" : "+map2.get("sex"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[4]+" : "+map2.get("age"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[5]+" : "+map2.get("account"));
		FileUtils.write2txt("/sdcard/DoctorConfig/", map2.get("account"),strings[6]+" : "+map2.get("psw"));
	}

	private String save(String key, EditText editText) {
		String value = editText.getText().toString();
		if(value.isEmpty()){
			return "0";
		}else{
			map.put(key, value);
			return "1";
		}
	}

}
