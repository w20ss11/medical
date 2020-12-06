package com.wss.doctor;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.wss.util.BitmapUtil;
import com.wss.util.FileUtils;
import com.wss.util.ReceiveImageRunnable;
import com.wss.util.SocketServer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private ImageView view;
	private Bitmap bitmap;
	private TextView textView;
	private Button send_order;
	private EditText editText;
	public String save_path;
	public TextView tv;
	public TextView tv2;
	public EditText et_ip;
	private Handler mHandler;
	private SocketServer server;
	private String ip;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		ip = intent.getStringExtra("ip");
		view= (ImageView)findViewById(R.id.imageView1);
		view.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.textview);
		send_order= (Button) findViewById(R.id.send_order);
		send_order.setOnClickListener(this);
		editText = (EditText) findViewById(R.id.editText);
		tv = (TextView) findViewById(R.id.tv);
		tv.setText("尚未发送指令......");
		tv2 = (TextView) findViewById(R.id.tv2);
		et_ip = (EditText) findViewById(R.id.ip_et);
		et_ip.setText(ip);
		
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==1){
					save_path = msg.obj.toString();
					if(save_path == null)
						System.out.println("bitmap 是 空");
					bitmap = BitmapUtil.decodeBitmap(save_path);
					view.setImageBitmap(bitmap);
					textView.setText("已接收到图片：");
					server.beginListen();
				}else if(msg.what==0){
					String str = (String) msg.obj;
					String res = FileUtils.judge_is_info(str);
					if(res!="nomatchdata")
						str =res;
					Log.i("TAG", str);
					ArrayList<String> list = FileUtils.splitInfo(str);
					if(list!=null){
						tv2.setText("压力1："+list.get(0)
						+"   压力2："+list.get(1)+"   压力3："+list.get(2)
						+"\n角度1："+list.get(3)+"   角度2："+list.get(4));
					}
				}
			}
		};
		new Thread(new ReceiveImageRunnable(mHandler)).start();
		server = new SocketServer (10049,mHandler);
		server.beginListen();
		
		
	}

	@SuppressLint({ "SdCardPath", "ShowToast" })
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.send_order:
			String ip = et_ip.getText().toString();
			if(ip!=null){
				String time = (String) DateFormat.format("yyyy年MM月dd日 hh:mm:ss",Calendar.getInstance(Locale.CHINA));
				String order = time+" : "+editText.getText().toString();
				FileUtils.write2txt("/sdcard/DoctorConfig/","orders", order);
				tv.append("\r\n"+order);
				
				String str = server.sendMessage(editText.getText().toString());
				Toast.makeText(this, str,0).show();
			}
			break;

		case R.id.imageView1:
			Intent intent=new Intent();
		    intent.setClass(MainActivity.this,ImageActivity.class);
		    intent.putExtra("path", save_path);
		    startActivity(intent);
			break;
		default:
			break;
		}
		
	}

}
