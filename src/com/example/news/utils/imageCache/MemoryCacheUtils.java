package com.example.news.utils.imageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * 
 * @author Kevin
 * 
 */
public class MemoryCacheUtils {

	// private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new
	// HashMap<String, SoftReference<Bitmap>>();
	private LruCache<String, Bitmap> mMemoryCache;

	public MemoryCacheUtils() {
		long maxMemory = Runtime.getRuntime().maxMemory() / 8;// 模拟器默认是16M
		mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int byteCount = value.getRowBytes() * value.getHeight();// 获取图片占用内存大小
				return byteCount;
			}
		};
	}

	/**
	 * 从内存读
	 * 
	 * @param url
	 */
	public Bitmap getBitmapFromMemory(String url) {
		// SoftReference<Bitmap> softReference = mMemoryCache.get(url);
		// if (softReference != null) {
		// Bitmap bitmap = softReference.get();
		// return bitmap;
		// }
		return mMemoryCache.get(url);
	}

	/**
	 * 写内存
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapToMemory(String url, Bitmap bitmap) {
		// SoftReference<Bitmap> softReference = new
		// SoftReference<Bitmap>(bitmap);
		// mMemoryCache.put(url, softReference);
		mMemoryCache.put(url, bitmap);
	}
}
