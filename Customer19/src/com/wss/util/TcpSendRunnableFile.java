package com.wss.util;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.util.Log;


public class TcpSendRunnableFile implements Runnable{
	String file;
	String ip;
	private Socket socket;
	public TcpSendRunnableFile(String file,String ip) {
		this.file = file;
		this.ip = ip;
	}

	@Override
	public void run() {
		try {
			System.out.println("代码开始");
			String ipAddress = ip;
			int port = 10046;
//			Socket socket = new Socket(ipAddress, port);
			
//			OutputStream outputStream = socket.getOutputStream();
//			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
//			BufferedWriter bufferedWriter = new BufferedWriter(outputWriter);
//			bufferedWriter.write(file);
//			bufferedWriter.close();
//			outputWriter.close();
//			outputStream.close();
//			socket.close();
			socket = new Socket(ipAddress, port);
			if(socket == null)
				Log.i("TAG", "send photo socket is null");
			Log.i("TAG", "send photo socket is not null");
			OutputStream outputData = socket.getOutputStream();
			FileInputStream fileInput = new FileInputStream(file);
			int size = -1;
			byte[] buffer = new byte[1024];
			while((size = fileInput.read(buffer, 0, 1024)) != -1){
				outputData.write(buffer, 0, size);
				System.out.println("11111");
			}
			outputData.close();
			fileInput.close();
			socket.close();
			System.out.println("代码执行完毕");
		} catch (SocketTimeoutException e1) {
            System.out.println("网络连接超时！！");
            
        }catch (Exception e) {
			System.out.println("发送出现异常");
			e.printStackTrace();
		} 
		
	}
	
}
