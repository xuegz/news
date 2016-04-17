package com.example.news.utils;

/*
 * 缓存
 */
public class CacheUtil {
	/*
	 * 设置缓存  key为url
	 * 			value 为json
	 */
	public static void setCache(String key,String value){
		SharePreferenceUtil.saveString(key, value);
	}
	/*
	 * 获得缓存
	 */
	public static String getCache(String key){
		return SharePreferenceUtil.getString(key, "");
	}
	
}
