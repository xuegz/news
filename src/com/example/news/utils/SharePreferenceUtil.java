package com.example.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	
	private SharedPreferences sp;
	
	public SharePreferenceUtil(Context context) {
		sp = context.getSharedPreferences("info", context.MODE_PRIVATE);
	}
	/*
	 * 获取/保存   boolean
	 */
	public boolean getBoolean(String key,boolean defValue){
		boolean result=sp.getBoolean(key, defValue);
		return result;
	}
	
	public void saveBoolean(String key,boolean value){
		sp.edit().putBoolean(key, value).commit();
	}
	
}
