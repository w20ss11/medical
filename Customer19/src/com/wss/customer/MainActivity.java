package com.wss.customer;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.wss.util.BitmapUtil;
import com.wss.util.BluetoothChatService;
import com.wss.util.FileUtils;
import com.wss.util.SocketClient;
import com.wss.util.TcpSendRunnableFile;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity{
	/** Called when the activity is first created. */
	private Button button_camera;
	private Button send_photo;
	private Button link_blue;
	private String temp_name;
	private TextView textView_order;
	private TextView textView_device;
	private TextView mTitle;
	private EditText eText;
	private Button up1;
	private Button up2;
	private Button up3;
	private Button up4;
	private Button up5;
	private Button down1;
	private Button down2;
	private Button down3;
	private Button down4;
	private Button down5;
	private Button key_down;
	private boolean is_stop = false;
	//-------ble------------------------
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothChatService mChatService = null;
	// 类型的消息发送从bluetoothchatservice处理程序
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// 键名字从收到的bluetoothchatservice处理程序
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	private static final boolean False = false;
	private Handler mHandler;
	private String mConnectedDeviceName = null;
	private BluetoothDevice device;
	//第一次输入加入-->变量
	private int sum =1;
	private int UTF =1;
	private String fmsg = ""; // 保存用数据缓存
	String mmsg = "";
	String mmsg2 = "";
	String pre="";
	//-------tcp------------------------
	private SocketClient client;
	private boolean is_connect_ble = False;
	private String ip;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		ip = intent.getStringExtra("ip");
		initView();
		
		Log.i("TAG", "t开启");
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//开启蓝牙
		if(!mBluetoothAdapter.isEnabled()){  
			//弹出对话框提示用户是后打开  
//			Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  
//			startActivityForResult(enabler, 0);  
			      //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。  
			      mBluetoothAdapter.enable();  
		}  
	}

	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("TAG", "onActivityResult执行resultCode:"+resultCode);
		switch (resultCode) {
		case 2://从连接蓝牙界面返回
			initView();
			Log.i("TAG", "蓝牙界面返回"+resultCode);
			//---------------------------------
			// 获得设备地址
			String address = data.getExtras().getString(
					DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			Log.i("TAG", "地址是："+address);
			// 把蓝牙设备对象
			device = mBluetoothAdapter
					.getRemoteDevice(address);
			// 试图连接到装置
			mChatService.connect(device);
			//---------------------------------
			break;
		case -1://从camera界面  成功照相返回
			initView();
			if(is_connect_ble){
				Log.i("TAG", "返回界面地址是："+device.getAddress());
				mChatService.connect(device);
			}
			
			Log.i("TAG", "camera界面返回"+resultCode);
			ImageView view = (ImageView)findViewById(R.id.imageView1);
			
			new DateFormat();
			String name = temp_name;	
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();
			File file = new File("/sdcard/Image/");
			file.mkdirs();// 创建文件夹
			String fileName = "/sdcard/Image/"+name;

//		     Bitmap bitmap = getLoacalBitmap(fileName); //从本地取图片(在cdcard中获取)  //
	        Bitmap bitmap2 = BitmapUtil.decodeBitmap(fileName);
	        try
			{
				view.setImageBitmap(bitmap2);// 将图片显示在ImageView里
			}catch(Exception e)
			{
				Log.e("error", e.getMessage());
			}
	        break;

		default:
			if(is_connect_ble){
				Log.i("TAG", "返回界面地址是："+device.getAddress());
				mChatService.connect(device);
			}
			break;
		}
		
	}
	
	private void initView() {
		Log.i("TAG","initView");
		MyListener myListener = new MyListener();
		up1 = (Button) findViewById(R.id.up_1);
		up2 = (Button) findViewById(R.id.up_2);
		up3 = (Button) findViewById(R.id.up_3);
		up4 = (Button) findViewById(R.id.up_4);
		up5 = (Button) findViewById(R.id.up_5);
		down1 = (Button) findViewById(R.id.down_1);
		down2 = (Button) findViewById(R.id.down_2);
		down3 = (Button) findViewById(R.id.down_3);
		down4 = (Button) findViewById(R.id.down_4);
		down5 = (Button) findViewById(R.id.down_5);
		key_down = (Button) findViewById(R.id.key);
		up1.setOnClickListener(myListener);
		up2.setOnClickListener(myListener);
		up3.setOnClickListener(myListener);
		up4.setOnClickListener(myListener);
		up5.setOnClickListener(myListener);
		down1.setOnClickListener(myListener);
		down2.setOnClickListener(myListener);
		down3.setOnClickListener(myListener);
		down4.setOnClickListener(myListener);
		down5.setOnClickListener(myListener);
		key_down.setOnClickListener(myListener);
		
		link_blue = (Button) findViewById(R.id.button_linkblue);
		link_blue.setOnClickListener(myListener);
		send_photo = (Button) findViewById(R.id.send_photo);
		send_photo.setOnClickListener(myListener);
		button_camera = (Button) findViewById(R.id.button_camera);
		button_camera.setOnClickListener(myListener);
		textView_order = (TextView) findViewById(R.id.order_textView);
		mTitle = (TextView) findViewById(R.id.mTitle);
		textView_device = (TextView) findViewById(R.id.textView1);
		eText = (EditText) findViewById(R.id.ip_et);
		eText.setText(ip);
		
		mHandler = new Handler(){
			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg){
				
				switch (msg.what) {
				case 0://接收指令 wifi
					String text = msg.obj.toString();
					textView_order.setText(text);
					break;

				case MESSAGE_STATE_CHANGE:
					Log.i("TAG", "MESSAGE_STATE_CHANGE: " + msg.arg1);
					switch (msg.arg1) {
					case BluetoothChatService.STATE_CONNECTED:
						
						mTitle.setText("已连接");
						mTitle.append(mConnectedDeviceName);
//						mInputEditText.setText("");
						is_connect_ble = true;
						break;
					case BluetoothChatService.STATE_CONNECTING:
						mTitle.setText("连接中。。。");
						break;
					case BluetoothChatService.STATE_LISTEN:
					case BluetoothChatService.STATE_NONE:
						mTitle.setText("尚未连接到设备");
						break;
					}
					break;
				case MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					// 构建一个字符串缓冲区
					String writeMessage = new String(writeBuf);
					sum=1;
					UTF=1;
					mmsg += writeMessage;

                    fmsg+="\n<--"+writeMessage+"\n";

					break;
				case MESSAGE_READ:
					//TODO 发送设备info到doctor
					byte[] readBuf = (byte[]) msg.obj;
					String readMessage = new String(readBuf, 0, msg.arg1);
					Log.i("TAG", "info:"+readMessage);
					if(readMessage.length()<19){
						pre = pre+readMessage;
						Log.i("TAG", "pre:"+pre);
						if(pre.length()<19)
							break;
						else{
							String judge_res = FileUtils.judge_is_info(pre);
							if(!judge_res.equals("nomatchdata")){
								Log.i("TAG", "判断为正确");
								ArrayList<String> list = FileUtils.splitInfo(judge_res);
								if(list!=null){
									textView_device.setText("压力1："+list.get(0)
									+"   压力2："+list.get(1)+"   压力3："+list.get(2)
									+"\n角度1："+list.get(3)+"   角度2："+list.get(4)+"\n");
								}
								Log.i("TAG","list 5 is : "+ list.get(5));
								if(list.get(5).equals("00")){
									textView_device.append(Html.fromHtml("<font color=\"#ff0000\">停止！</font>"));
									is_stop = true;
								}else if(list.get(5).equals("11")){
									textView_device.append(Html.fromHtml("<font color=\"#ff0000\">警告！</font>"));
									is_stop=false;
								}else {
									is_stop=false;
								}
								client.sendMsg(pre);
							}
							pre="";
						}
					}else{
						String judge_res = FileUtils.judge_is_info(readMessage);
						if(!judge_res.equals("nomatchdata")){
							Log.i("TAG", "判断为正确");
							ArrayList<String> list = FileUtils.splitInfo(judge_res);
							if(list!=null){
								textView_device.setText("压力1："+list.get(0)
								+"   压力2："+list.get(1)+"   压力3："+list.get(2)
								+"\n角度1："+list.get(3)+"   角度2："+list.get(4)+"\n");
							}
							Log.i("TAG","list 5  is : "+ list.get(5));
							if(list.get(5).equals("00")){
								textView_device.append(Html.fromHtml("<font color=\"#ff0000\">停止！</font>"));
								is_stop = true;
							}else if(list.get(5).equals("11")){
								is_stop=false;
								textView_device.append(Html.fromHtml("<font color=\"#ff0000\">警告！</font>"));
							}else {
								is_stop=false;
							}
							client.sendMsg(readMessage);
						}else {
							Log.i("TAG", "判断为错误");
						}
					}
					break;
				case MESSAGE_DEVICE_NAME:
					// 保存该连接装置的名字
					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
					Toast.makeText(getApplicationContext(),
							"已连接 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
							.show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(),
							msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
							.show();
					break;
				}
			}
		};
		
		String ip = eText.getText().toString();
		if(ip!=null){
			client = new SocketClient();
			client.clintValue(this, ip, 10049,mHandler);
			client.openClientThread();
		}
		
		// 初始化bluetoothchatservice执行蓝牙连接
		mChatService = new BluetoothChatService(this, mHandler);
		
		if (mChatService != null) {
			// 只有国家是state_none，我们知道，我们还没有开始
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// 启动蓝牙聊天服务
				mChatService.start();
			}
		}
		
	}

	private class MyListener implements View.OnClickListener {  
	    @Override  
	    public void onClick(View view) {  
	        switch (view.getId()) {
			case R.id.up_1:
				if(is_stop)
					Toast.makeText(MainActivity.this, "不可再充气！", 0).show();
				else
					sendMessage("111");   
				break;
			case R.id.up_2:
			case R.id.up_4:
				if(is_stop)
					Toast.makeText(MainActivity.this, "不可再充气！", 0).show();
				else
					sendMessage("211");  
				break;
			case R.id.up_3:
				if(is_stop)
					Toast.makeText(MainActivity.this, "不可再充气！", 0).show();
				else
					sendMessage("311");     
				break;
			case R.id.up_5:
				if(is_stop)
					Toast.makeText(MainActivity.this, "不可再充气！", 0).show();
				else
					sendMessage("411");    
				break;
			case R.id.down_1:
				sendMessage("100");   
				break;
			case R.id.down_2:
			case R.id.down_4:
				sendMessage("200");   
				break;
			case R.id.down_3:
				sendMessage("300");   
				break;
			case R.id.down_5:
				sendMessage("400");   
				break;
			case R.id.key:
				sendMessage("444");   
				break;
			case R.id.button_linkblue:
				if(is_connect_ble)
					mChatService.stop();
				Log.i("TAG", "子线程intent开启");
				Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
				startActivityForResult(intent, 0); // (int resultCode, Intent data)
				break;
			case R.id.button_camera:
				if(is_connect_ble)
					mChatService.stop();
				Intent getImageByCamera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				//----------存放数据路径-----------------------------------
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				new DateFormat();
				temp_name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))+ ".jpg";
				String name = temp_name ;	
			
				File file = new File("/sdcard/Image/");
				file.mkdirs();// 创建文件夹
				String fileName = "/sdcard/Image/"+name;

				//--------存放照片数据 开启res activity---------------------------------------
				getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
				getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//				setResult(1, getImageByCamera);//(int resultCode, Intent data)
				startActivityForResult(getImageByCamera, 1);// (int resultCode, Intent data)
				Log.i("TAG", "camera按钮执行到此");
				break;
			case R.id.send_photo:
				String ip = eText.getText().toString();
				if(ip!=null){
					TcpSendRunnableFile tcpThread = new TcpSendRunnableFile("/sdcard/Image/"+temp_name,ip);
					new Thread(tcpThread).start();
				}
				break;
			default:
				break;
			}
	    }  
	}
	
	/**
	 * 发送一个消息
	 * 
	 * @param message
	 *            一个文本字符串发送.
	 */
	private void sendMessage(String message) {
		// 检查我们实际上在任何连接
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, "未连接", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// 检查实际上有东西寄到
		if (message.length() > 0) {
			boolean bool = FileUtils.judge_is_3num(message);
			if(bool || message=="stop"){
				// 得到消息字节和告诉bluetoothchatservice写
				byte[] send = message.getBytes();
				mChatService.write(send);
			}else{
				Toast.makeText(this, "指令输入格式不正确", Toast.LENGTH_SHORT).show();
			}

		}
	}
	
}
