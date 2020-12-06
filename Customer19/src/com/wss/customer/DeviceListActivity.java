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
	
	// ���ر����ͼ
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	// ������
	private BluetoothAdapter mBtAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ָ��������ʽ
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		
		Button scan_device = (Button) findViewById(R.id.button_scan);
		scan_device.setOnClickListener(this);
		
		//һ���·��ֵ��豸
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_name);
		
		//Ѱ�Һͽ�������豸�б�
		ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
		pairedListView.setAdapter(mPairedDevicesArrayAdapter);
		pairedListView.setOnItemClickListener(mDeviceClickListener);

		// Ѱ�Һͽ���Ϊ�·��ֵ��豸�б�
		ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
		newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
		newDevicesListView.setOnItemClickListener(mDeviceClickListener);
		
		// ע��ʱ���͹㲥���豸
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// �㲥ʱ���������ע��
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		// ��ȡ��������������
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

		// �õ�һ��Ŀǰ����豸
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
			String noDevices = ("û��������豸");
			mPairedDevicesArrayAdapter.add(noDevices);
		}
	}
	
	// ��broadcastreceiver�����豸��
	// �仯�ı���ʱ���������
	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();

			// �������豸
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				//�������豸�������ͼ
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// ������Ѿ���ԣ�����������Ϊ��������
				// ����
				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
				{
					mNewDevicesArrayAdapter.add(device.getName() + "\n"
							+ device.getAddress());
				}
				//�����ֺ󣬸ı�����
			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				setProgressBarIndeterminateVisibility(false);
				setTitle("ѡ��һ���豸����");
				if (mNewDevicesArrayAdapter.getCount() == 0)
				{
					String noDevices = ("û��������豸");
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}
	};
	
	// ������ڵ������豸��listviews
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
		{
			// ��Ϊ�����˷ѵģ�ȡ���������ǵ�����
			mBtAdapter.cancelDiscovery();

			// ����豸��ַ�����ǽ�17�ֵ�
			//��ͼ
			String info = ((TextView) v).getText().toString();
			String address = info.substring(info.length() - 17);

			//���������ͼ�Ͱ�����ַ
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			//������������
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
		// ��ʾɨ��ĳƺ�
		setProgressBarIndeterminateVisibility(true);
		setTitle("��ʼɨ��");

		// �����豸����Ļ
		findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

		// ��������Ѿ����֣���ֹ��
		if (mBtAdapter.isDiscovering())
		{
			mBtAdapter.cancelDiscovery();
		}

		// Ҫ���bluetoothadapter����
		mBtAdapter.startDiscovery();
		
	}
}
