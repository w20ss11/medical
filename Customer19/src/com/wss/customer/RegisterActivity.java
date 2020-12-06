package com.wss.customer;

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

@SuppressLint({ "SdCardPath", "ShowToast" })
public class RegisterActivity extends Activity implements OnClickListener{
	private EditText adult_name1;
	private EditText adult_age1;
	private EditText adult_blood1;
	private EditText adult_name2;
	private EditText adult_age2;
	private EditText adult_blood2;
	
	private EditText child_name;
	private EditText child_age;
	private EditText child_sex;
	private EditText child_blood;
	private EditText cure_start;
	private EditText cure_end;
	private EditText address;
	private EditText history;
	private EditText account;
	private EditText psw;
	private Button submit;
	private HashMap<String, String> map = new HashMap<String, String>();
	private String[] strings = {"��������","��������","����Ѫ��","ĸ������","ĸ������","ĸ��Ѫ��","��������","������������","�����Ա�",
			"����Ѫ��","���ƿ�ʼʱ��","���ƽ���ʱ��","��ַ","������ʷ","�˺�","����"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		adult_name1 = (EditText) findViewById(R.id.adult_name1);
		adult_age1 = (EditText) findViewById(R.id.adult_age1);
		adult_blood1 = (EditText) findViewById(R.id.adult_blood1);
		adult_name2 = (EditText) findViewById(R.id.adult_name2);
		adult_age2 = (EditText) findViewById(R.id.adult_age2);
		adult_blood2 = (EditText) findViewById(R.id.adult_blood2);
		child_name = (EditText) findViewById(R.id.child_name);
		child_age = (EditText) findViewById(R.id.child_age);
		child_sex = (EditText) findViewById(R.id.child_sex);
		child_blood = (EditText) findViewById(R.id.child_blood);
		cure_start = (EditText) findViewById(R.id.cure_start);
		cure_end = (EditText) findViewById(R.id.cure_end);
		address = (EditText) findViewById(R.id.address);
		history = (EditText) findViewById(R.id.history);
		account = (EditText) findViewById(R.id.account);
		psw = (EditText) findViewById(R.id.psw);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		
		FileUtils.creatAccount();
	}

	@Override
	public void onClick(View arg0) {
		// �ж��Ƿ�ȫ�� ����txt ��¼������ȡ�����˺ź����뵽һ����������ƥ��
		String check = "";
		switch (arg0.getId()) {
		case R.id.submit:
			check = check+save("adult_name1",adult_name1);
			check = check+save("adult_age1",adult_age1);
			check = check+save("adult_blood1",adult_blood1);
			check = check+save("adult_name2",adult_name2);
			check = check+save("adult_age2",adult_age2);
			check = check+save("adult_blood2",adult_blood2);
			check = check+save("child_name",child_name);
			check = check+save("child_age",child_age);
			check = check+save("child_sex",child_sex);
			check = check+save("child_blood",child_blood);
			check = check+save("cure_start",cure_start);
			check = check+save("cure_end",cure_end);
			check = check+save("address",address);
			check = check+save("history",history);
			check = check+save("account",account);
			check = check+save("psw",psw);
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
		FileUtils.write2txt("/sdcard/CumtomerConfig/", "account", map.get("account")+"#"+map.get("psw"));
	}

	@SuppressLint("SdCardPath")
	private void saveMap2txt(HashMap<String, String> map2) {
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[0]+" : "+map2.get("adult_name1"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[1]+" : "+map2.get("adult_age1"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[2]+" : "+map2.get("adult_blood1"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[3]+" : "+map2.get("adult_name2"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[4]+" : "+map2.get("adult_age2"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[5]+" : "+map2.get("adult_blood2"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[6]+" : "+map2.get("child_name"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[7]+" : "+map2.get("child_age"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[8]+" : "+map2.get("child_sex"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[9]+" : "+map2.get("child_blood"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[10]+" : "+map2.get("cure_start"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[11]+" : "+map2.get("cure_end"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[12]+" : "+map2.get("address"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[13]+" : "+map2.get("history"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[14]+" : "+map2.get("account"));
		FileUtils.write2txt("/sdcard/CumtomerConfig/", map2.get("account"),strings[15]+" : "+map2.get("psw"));
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
