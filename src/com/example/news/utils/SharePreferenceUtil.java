package com.example.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	
	private static SharedPreferences sp;
	
	public SharePreferenceUtil(Context context) {
		sp = context.getSharedPreferences("info", context.MODE_PRIVATE);
	}
	/*
	 * 获取/保存   boolean
	 */
	public static boolean getBoolean(String key,boolean defValue){
		boolean result=sp.getBoolean(key, defValue);
		return result;
	}
	
	public static void saveBoolean(String key,boolean value){
		sp.edit().putBoolean(key, value).commit();
	}
	
	/*
	 * 获取/保存  string
	 */
	public static String getString(String key,String defValue){
		return sp.getString(key, defValue);
	}
	
	public static void saveString(String key,String value){
		sp.edit().putString(key, value).commit();
	}
	
	/*
	 * 获取/保存  int
	 */
	public int getInt(String key,int defValue){
		return sp.getInt(key, defValue);
	}
	
	public void saveInt(String key,int value){
		sp.edit().putInt(key, value).commit();
	}
}
