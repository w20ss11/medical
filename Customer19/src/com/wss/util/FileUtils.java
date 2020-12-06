package com.wss.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.bool;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Nancy on 2017/5/11.
 */
public class FileUtils {

	public static void write2txt(String filePath,String filename, String insertContent) {

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

            File txt = new File(filePath+filename+".txt");
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

	public String readTxt1(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		try {
			FileInputStream fis = new FileInputStream(file);
			int c;
			while ((c = fis.read()) != -1) {
				sb.append((char) c);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			File file1 = new File(fileName);
	        if (!file1.exists()) {
	            file1.mkdirs();
	        }
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static ArrayList<String> readTxt(String filename) {  
        //将读出来的一行行数据使用Map存储  
        ArrayList<String> list = new ArrayList<String>();
        try {  
            File file = new File(filename);  
            if (file.isFile() && file.exists()) {       //文件存在的前提  
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));  
                BufferedReader br = new BufferedReader(isr);  
                String lineTxt = null;  
                while ((lineTxt = br.readLine()) != null) {     //  
                    if (!"".equals(lineTxt)) {  
                        String reds = lineTxt.split("\\+")[0];      //java 正则表达式  
                        list.add(reds);
                    }  
                }  
                isr.close();  
                br.close();  
            }else {  
            	File file1 = new File(filename);
    	        if (!file1.exists()) {
    	            file1.mkdirs();
    	        }
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return list;  
    }  
	
	public static ArrayList<String> splitInfo(String string){
		ArrayList<String> list = new ArrayList<String>();
		if(string.length()>= 15){
			Log.i("TAG", "split");
			list.add(string.substring(1,3));
			list.add(string.substring(4,6));
			list.add(string.substring(7,9));
			list.add(string.substring(10,13));
			list.add(string.substring(14,16));
			list.add(string.substring(17));
			System.out.println(string.substring(1,3));
			return list;
		}
		return null;
	}

	public static String judge_is_info(String string){
		String pattern = "\\d{3}2\\d{2}3\\d{2}4\\d{3}5\\d{2}1\\d{2}";
		 
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	 
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(string);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         return m.group(0);
	      } else {
	         System.out.println("NO MATCH");
	         return "nomatchdata";
	      }
	}
	public static boolean judge_is_3num(String str){
//		String string = "23";
		String pattern = "[0-9]{3}";
	    // 创建 Pattern 对象
	    Pattern r = Pattern.compile(pattern);
	    // 现在创建 matcher 对象
	    Matcher m = r.matcher(str);
		boolean bool = m.matches();
		return bool;
	}
	
	
	@SuppressLint("SdCardPath")
	public static void creatAccount() {
        try {
        	String sdStatus = Environment.getExternalStorageState();
        	if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TAG",
						"SD card is not avaiable/writeable right now.");
				return;
			}
            File file = new File("/sdcard/CumtomerConfig/");
            if (!file.exists()) {
                file.mkdirs();
            }

            File txt = new File("/sdcard/CumtomerConfig/account.txt");
            if (!txt.exists()) {
                txt.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
