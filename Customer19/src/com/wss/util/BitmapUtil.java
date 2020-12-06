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
        	if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
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
             return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

          } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        }
   }
	
	 public static Bitmap decodeBitmap(String path)  
	    {  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inJustDecodeBounds = true;  
	        // 通过这个bitmap获取图片的宽和高         
	        Bitmap bitmap = BitmapFactory.decodeFile(path, options);  
	        if (bitmap == null)  
	        {  
	            System.out.println("bitmap为空");  
	        }  
	        float realWidth = options.outWidth;  
	        float realHeight = options.outHeight;  
	        System.out.println("真实图片高度：" + realHeight + "宽度:" + realWidth);  
	        // 计算缩放比         
	        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);  
	        if (scale <= 0)  
	        {  
	            scale = 1;  
	        }  
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
	        bitmap = BitmapFactory.decodeFile(path, options);  
	        int w = bitmap.getWidth();  
	        int h = bitmap.getHeight();  
	        System.out.println("缩略图高度：" + h + "宽度:" + w);  
	        return bitmap;
	    }
}
