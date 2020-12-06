package com.wss.doctor;

import java.io.File;
import java.util.ArrayList;

import com.wss.util.BitmapUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageActivity extends Activity implements OnClickListener{
	
	private ArrayList<String> paths;
	private ImageView iv;
	private int len;
	private int id;
	private int reqWidth = 400;
	private int reqHeight = 400;

	@SuppressLint({ "ShowToast", "SdCardPath" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		iv = (ImageView) findViewById(R.id.imageview);
		Button button_pre = (Button) findViewById(R.id.button_pre);
		button_pre.setOnClickListener(this);
		Button button_next = (Button) findViewById(R.id.button_next);
		button_next.setOnClickListener(this);
		
		Intent intent=getIntent();
		String path=intent.getStringExtra("path");
		Toast.makeText(this, path, 0).show();
		
		//判断文件夹是否存在
		paths = getFileDir("/sdcard/Image/");
		len = paths.size()-1;
		id = 0;
		Log.i("TAG", paths.get(id));
		if(path == null){
			path = paths.get(id);
		}
		iv.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFilePath(path,reqWidth,reqHeight));
	}
	
	 public  ArrayList<String> getFileDir(String filePath) {  
		 ArrayList<String> paths = null;
         try{  
        	 paths = new ArrayList<String>();  
             
             File f = new File(filePath);  
             File[] files = f.listFiles();// 列出所有文件  
             // 将所有文件存入list中  
             if(files != null){  
                 int count = files.length;// 文件个数  
                 for (int i = 0; i < count; i++) {  
                     File file = files[i];  
                     paths.add(file.getPath());  
                 }  
             } 
             Log.i("TAG", "paths:"+paths.toString());
   
         }catch(Exception ex){  
             ex.printStackTrace();  
         }  
         return paths;
     }

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button_pre:
			iv.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFilePath(paths.get(id),reqWidth,reqHeight));
			if(id==0)
				id = len;
			else
				id--;
			break;
		case R.id.button_next:
			iv.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFilePath(paths.get(id),reqWidth,reqHeight));
			if(id == len)
				id = 0;
			else
				id++;
			break;
		default:
			break;
		}
		
	}  

}
