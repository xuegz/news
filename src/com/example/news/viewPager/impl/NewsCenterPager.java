package com.example.news.viewPager.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.news.MainActivity;
import com.example.news.bean.NewsData;
import com.example.news.fragment.LeftMenuFragment;
import com.example.news.global.GlobalContants;
import com.example.news.utils.CacheUtil;
import com.example.news.viewPager.BaseMenuDetailPager;
import com.example.news.viewPager.BasePager;
import com.example.news.viewPager.menudetail.InteractMenuDetailPager;
import com.example.news.viewPager.menudetail.NewsMenuDetailPager;
import com.example.news.viewPager.menudetail.PhotoMenuDetailPager;
import com.example.news.viewPager.menudetail.TopicMenuDetailPager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;


/*
 * 新闻中心页
 */
public class NewsCenterPager extends BasePager {
	public List<BaseMenuDetailPager> list;
	public NewsData newsData;
	
	public NewsCenterPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		fl_content.removeAllViews();//清除之前的布局
		tv_title.setText("新闻");//修改标题
		btn_menu.setVisibility(View.VISIBLE);//隐藏imagebutton
		setSlidingMenuEnable(true);
		String cache=CacheUtil.getCache(GlobalContants.CATEGORIES_URL);//获取缓存
		System.out.println(cache);
		if(!TextUtils.isEmpty(cache)){
			parseData(cache);
		}
		getDataFromServer();// 从服务器获取数据
		
	}
	
	/*
	 * 从服务器获取数据
	 */
	public void getDataFromServer(){
		HttpUtils utils=new HttpUtils();
		//用xutils发送请求
		utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {
			//成功
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//获取数据
				String result=responseInfo.result;
				CacheUtil.setCache(GlobalContants.CATEGORIES_URL,result);
				//解析json
				parseData(result);
			}
			//失败
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(activity, "请求失败", 0).show();
				error.fillInStackTrace();
			}
		});
	}

	protected void parseData(String result) {
		 Gson gson = new Gson();
		 newsData = gson.fromJson(result,NewsData.class);
		 
		 //把数据设置给侧边栏
		 MainActivity mActivity=(MainActivity) activity;
		 LeftMenuFragment fragment=mActivity.getLeftMenuFragment();//获取LeftMenuFragment
		 fragment.setLeftMenuData(newsData);//数据设置给侧边栏
		 
		 // 准备4个菜单详情页
		 list=new ArrayList<BaseMenuDetailPager>();
		 list.add(new NewsMenuDetailPager(activity));
		 list.add(new TopicMenuDetailPager(activity));
		 list.add(new PhotoMenuDetailPager(activity,btn_display));
		 list.add(new InteractMenuDetailPager(activity));
		 
		 fl_content.addView(list.get(0).mRootview);//默认设置   NewsMenuDetailPager
		 //list.get(0).initData();//初始化数据NewsMenuDetailPager
	}
	/*
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPager(int position){
		BaseMenuDetailPager pager=list.get(position);//获取当前的菜单详情页
		fl_content.removeAllViews();//清除之前的布局
		fl_content.addView(pager.mRootview);//将菜单详情页的布局设置给帧布局	
		tv_title.setText(newsData.data.get(position).title);	
		
		//pager.initData();//初始化数据
	}
	
	

}
