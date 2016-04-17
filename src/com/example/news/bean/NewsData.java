package com.example.news.bean;

import java.util.ArrayList;

/**
 * 网络分类信息的封装
 * 
 * 字段名字必须和服务器返回的字段名一致, 方便gson解析
 * 
 * @author Kevin
 * 
 */
public class NewsData {
	public int retcode;
	public ArrayList<NewsMenuData> data;
	
	@Override
	public String toString() {
		return "NewsData [data=" + data + "]";
	}

	//侧边栏数据对象
	public class NewsMenuData{
		public String id;
		public String title;
		public String type;
		public String url;
		public ArrayList<NewsTabData> children;
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}	
	}
	// 新闻页面下11个子页签的数据对象
	public class NewsTabData{
		public String id;
		public String title;
		public int type;
		public String url;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}
	
}
