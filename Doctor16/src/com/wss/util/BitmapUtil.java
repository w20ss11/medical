package com.wss.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
	
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeBitmap(String path){

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

	//----------------------------------------
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap decodeSampledBitmapFromFilePath(String imagePath,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imagePath,options);
	}
	
	//----------------------------------
	
}
