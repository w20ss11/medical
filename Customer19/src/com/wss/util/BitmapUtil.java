package com.wss.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class BitmapUtil {
	
	public static void write2txt(String filePath,String user, String insertContent) {

        String strContent = insertContent + "\r\n";
        try {
        	String sdStatus = Environment.getExternalStorageState();
        	if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
				Log.i("TAG",
						"SD card is not avaiable/writeable right now.");
				return;
			}
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            File txt = new File(filePath+user+".txt");
            if (!txt.exists()) {
                txt.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(txt, "rwd");
            raf.seek(txt.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	public static Bitmap getLoacalBitmap(String url) {
        try {
             FileInputStream fis = new FileInputStream(url);
             return BitmapFactory.decodeStream(fis);  ///����ת��ΪBitmapͼƬ        

          } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        }
   }
	
	 public static Bitmap decodeBitmap(String path)  
	    {  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inJustDecodeBounds = true;  
	        // ͨ�����bitmap��ȡͼƬ�Ŀ�͸�         
	        Bitmap bitmap = BitmapFactory.decodeFile(path, options);  
	        if (bitmap == null)  
	        {  
	            System.out.println("bitmapΪ��");  
	        }  
	        float realWidth = options.outWidth;  
	        float realHeight = options.outHeight;  
	        System.out.println("��ʵͼƬ�߶ȣ�" + realHeight + "���:" + realWidth);  
	        // �������ű�         
	        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);  
	        if (scale <= 0)  
	        {  
	            scale = 1;  
	        }  
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        // ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false,���ͼƬ��Ҫ��ȡ�����ġ�        
	        bitmap = BitmapFactory.decodeFile(path, options);  
	        int w = bitmap.getWidth();  
	        int h = bitmap.getHeight();  
	        System.out.println("����ͼ�߶ȣ�" + h + "���:" + w);  
	        return bitmap;
	    }
}
