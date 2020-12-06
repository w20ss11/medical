package com.wss.customer;

import java.util.Set;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends Activity implements OnClickListener{
	
	// 返回别的意图
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// 适配器
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 指定窗口样式
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		
		Button scan_device = (Button) findViewById(R.id.button_scan);
		scan_device.setOnClickListener(this);
		
		//一个新发现的设备
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		
		//寻找和建立配对设备列表
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// 寻找和建立为新发现的设备列表
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);
		
		// 注册时发送广播给设备
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// 广播时发现已完成注册
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// 获取本地蓝牙适配器
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// 得到一套目前配对设备
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter
		if (pairedDevices.size() > 0)
		{
			findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices)
			{
				mPairedDevicesArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress());
			}
		}
		else
		{
			String noDevices = ("没有已配对设备");
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}
	
	// 该broadcastreceiver监听设备和
	// 变化的标题时，发现完成
	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();

			// 当发现设备
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				//把蓝牙设备对象的意图
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果它已经配对，跳过它，因为它的上市
				// 早已
				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
				{
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				//当发现后，改变活动名称
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				setProgressBarIndeterminateVisibility(false);
				setTitle("选择一个设备连接");
				if (mNewDevicesArrayAdapter.getCount() == 0)
				{
					String noDevices = ("没有已配对设备");
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}
	};
	
	// 点击听众的所有设备在listviews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
		{
			// 因为它是浪费的，取消发现我们的连接
			mBtAdapter.cancelDiscovery();

			// 获得设备地址，这是近17字的
			//视图
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			//创建结果意图和包括地址
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			//结果，完成这项活动
			setResult(2, intent);//(int resultCode, Intent data)
			finish();
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_scan:
			doDiscovery();
			v.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		
	}

	private void doDiscovery() {
		// 显示扫描的称号
		setProgressBarIndeterminateVisibility(true);
		setTitle("开始扫描");

		// 打开新设备的字幕
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// 如果我们已经发现，阻止它
		if (mBtAdapter.isDiscovering())
		{
			mBtAdapter.cancelDiscovery();
		}

		// 要求从bluetoothadapter发现
		mBtAdapter.startDiscovery();
		
	}
}
