package com.wss.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient
{
    private Socket client;
    private Context context;
    private int port;           //IP
    private String site;            //�˿�
    private Thread thread;
    public Handler mHandler;
    private boolean isClient=false;
    private PrintWriter out;
    private InputStream in;
    private String str;


    /**
     * @effect �����߳̽������ӿ����ͻ���
     * */
    public void openClientThread(){
        thread=new Thread ( new Runnable ( )
        {
            @Override
            public void run()
            {

                try {
                    /**
                     *  connect()����
                     * */
                    client=new Socket ( site,port );

//                    client.setSoTimeout ( 5000 );//���ó�ʱʱ��
                    if (client!=null)
                    {
                        isClient=true;
                        forOut();
                        forIn ();
                    }else {
                        isClient=false;
                        Toast.makeText ( context,"��������ʧ��", Toast.LENGTH_LONG ).show ();
                    }
                    Log.i ( "hahah","site="+site+" ,port="+port );
                }catch (UnknownHostException e) {
                    e.printStackTrace ();
                    Log.i ( "socket","6" );
                }catch (IOException e) {
                    e.printStackTrace ();
                    Log.i ( "socket","7" );
                }

            }
        } );
        thread.start ();
    }

    /**
     * ����ʱ�����ﴫֵ
     * */
    public void clintValue(Context context, String site, int port,Handler handler)
    {
        this.context=context;
        this.site=site;
        this.port=port;
        this.mHandler=handler;
    }

    /**
     * @effect �õ�����ַ���
     * */
    public void forOut()
    {
        try {
            out=new PrintWriter ( client.getOutputStream () );
        }catch (IOException e){
            e.printStackTrace ();
            Log.i ( "socket","8" );
        }
    }

    /**
     * @steps read();
     * @effect �õ������ַ���
     * */
    public void forIn(){

        while (isClient) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
                in=client.getInputStream ();

                /**�õ�����16����������Ҫ���н���*/
                byte[] bt = new byte[50];
                in.read ( bt );
                str=new String ( bt,"UTF-8" );
                } catch (IOException e) {}
           if (str!=null) {
               Message msg = new Message ( );
               msg.obj =str ;
               msg.what = 0;
               mHandler.sendMessage ( msg );
            }

        }
    }

    /**
     * @steps write();
     * @effect ������Ϣ
     * */
    public void sendMsg(final String str)
    {
        new Thread ( new Runnable ( )
        {
            @Override
            public void run()
            {
                if (client!=null)
                {
                	Log.i("TAG", "�����豸info"+str+"��Doctor");
                    out.print ( str + "\r\n");
                    out.flush ();
                    Log.i ( "outtt",out+"" );
                }else
                {
                	Log.i("TAG", "client �ǿ�");
                    isClient=false;
                }
            }
        } ).start ();

    }
}