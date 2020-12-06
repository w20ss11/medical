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
     * @steps bind();�󶨶˿ں�
     * @effect ��ʼ�������
     * @param port �˿ں�
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
     * @effect socket��������
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
                     * ��������
                     * */
                    socket=server.accept();
                    Log.i("TAG", "Doctor�����豸info");
	                    try {
	                        /**�õ�������*/
	                        in =socket.getInputStream();
	                        /**
	                         * ʵ������ѭ������
	                         * */
	                        while (!socket.isClosed())
	                        {
	                        	Log.i("TAG", "����ѭ������ing");
	                        	Thread.sleep(1000);
	                            byte[] bt=new byte[50];
	                            in.read ( bt );
	                            str=new String ( bt,"UTF-8" );                  //���뷽ʽ  ����յ���������
	                            Log.i("TAG", "���յ� �� "+str);
	                            if (str!=null)
	                            {
	                                returnMessage ( str );
	                            }else if (str==null||str=="exit"){
	                                break;                                     //����ѭ������socket���ݽ���
	                            }
	                            Log.i("TAG", "�յ����豸info��"+str);
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
     * @effect socket����˷�����Ϣ
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
	        return "�ѷ���";
        }else
        	return "����ʧ��";
    }

    /**
     * @steps read();
     * @effect socket����˵õ��������ݲ����͵�������
     * */
    public void returnMessage(String chat){
        Message msg=new Message ();
        msg.what=0;
        msg.obj=chat;
        serverHandler.sendMessage ( msg );
    }

}
