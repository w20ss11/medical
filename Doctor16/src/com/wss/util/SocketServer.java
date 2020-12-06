package com.wss.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer
{
    private ServerSocket server;
    private Socket socket;
    private InputStream in;
    private String str=null;
    private Handler serverHandler;

    /**
     * @steps bind();绑定端口号
     * @effect 初始化服务端
     * @param port 端口号
     * */
    public SocketServer(int port,Handler handler){
        try {
            server= new ServerSocket ( port );
            serverHandler=handler;
            
        }catch (IOException e){
            e.printStackTrace ();
        }
       
    }

    /**
     * @steps listen();
     * @effect socket监听数据
     * */
    public void beginListen()
    {
        new Thread ( new Runnable ( )
        {
            @Override
            public void run()
            {
            	while(true){
            	try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	try {
                    /**
                     * accept();
                     * 接受请求
                     * */
                    socket=server.accept();
                    Log.i("TAG", "Doctor接收设备info");
	                    try {
	                        /**得到输入流*/
	                        in =socket.getInputStream();
	                        /**
	                         * 实现数据循环接收
	                         * */
	                        while (!socket.isClosed())
	                        {
	                        	Log.i("TAG", "数据循环接收ing");
	                        	Thread.sleep(1000);
	                            byte[] bt=new byte[50];
	                            in.read ( bt );
	                            str=new String ( bt,"UTF-8" );                  //编码方式  解决收到数据乱码
	                            Log.i("TAG", "接收到 ： "+str);
	                            if (str!=null)
	                            {
	                                returnMessage ( str );
	                            }else if (str==null||str=="exit"){
	                                break;                                     //跳出循环结束socket数据接收
	                            }
	                            Log.i("TAG", "收到的设备info："+str);
	                        }
	                    } catch (IOException e) {
	                        e.printStackTrace ( );
	                        socket.isClosed ();
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace ( );
	                    socket.isClosed ();
	                } catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            }
        } ).start ();
    }

    /**
     * @steps write();
     * @effect socket服务端发送信息
     * */
    public String sendMessage(final String chat)
    {
    	if(socket!=null){
	        Thread thread=new Thread ( new Runnable ( )
	        {
	            @Override
	            public void run()
	            {
	                try {
	                    PrintWriter out=new PrintWriter ( socket.getOutputStream () );
	                    out.print ( chat + "\r\n");
	                    out.flush ();
	                } catch (IOException e) {
	                    e.printStackTrace ( );
	                }
	            }
	        } );
	        thread.start ();
	        return "已发送";
        }else
        	return "连接失败";
    }

    /**
     * @steps read();
     * @effect socket服务端得到返回数据并发送到主界面
     * */
    public void returnMessage(String chat){
        Message msg=new Message ();
        msg.what=0;
        msg.obj=chat;
        serverHandler.sendMessage ( msg );
    }

}
