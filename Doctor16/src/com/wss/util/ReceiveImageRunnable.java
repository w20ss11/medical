package com.wss.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;


public class ReceiveImageRunnable implements Runnable{
	public Socket socket;
	private Handler handler;
	
	private static ServerSocket tcpSocket = null;
	private static final int socket_port = 10046;
	
	
	public ReceiveImageRunnable(Handler mHandler){
		this.handler = mHandler;
	}

	@SuppressLint("SdCardPath")
	@Override
	public void run() {
		try {
			tcpSocket = new ServerSocket(socket_port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(true){
			//接收客户端的数据并计算 获取返回值
			try {

				Thread.sleep(1000);
				Socket data = tcpSocket.accept();
				Log.i("TAG", "收到photo");
				InputStream dataStream = data.getInputStream();
				//生成文件夹
				File file = new File("/sdcard/Image/");
				file.mkdirs();// 创建文件夹
				String savePath = "/sdcard/Image/"+DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))+ ".jpg";
				FileOutputStream fileOutputStream = new FileOutputStream(savePath, false);
				byte[] buffer = new byte[1024];
				int size = -1;
				while ((size = dataStream.read(buffer)) != -1){
					fileOutputStream.write(buffer, 0 ,size);
				}
				fileOutputStream.close();
				dataStream.close();
				data.close();
		        
				Message message = new Message();
		        message.obj = savePath;
		        message.what = 1;
		        handler.sendMessage(message);
			
			} catch (SocketTimeoutException e1) {
	            System.out.println("网络连接超时！！");
	        }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
