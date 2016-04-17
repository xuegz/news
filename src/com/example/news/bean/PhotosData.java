package com.example.news.bean;

import java.util.ArrayList;

/**
 * 组图数据
 * 
 * @author Kevin
 * 
 */
public class PhotosData {

	public int retcode;
	public PhotosInfo data;

	public class PhotosInfo {
		public String title;
		public ArrayList<PhotoInfo> news;
	}

	public class PhotoInfo {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}
}
