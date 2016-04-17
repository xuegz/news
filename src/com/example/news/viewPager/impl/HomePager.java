package com.example.news.viewPager.impl;

import com.example.news.viewPager.BasePager;

import android.app.Activity;
import android.view.View;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		System.out.println("初始化首页");
		tv_title.setText("智慧北京");//修改标题
		btn_menu.setVisibility(View.INVISIBLE);//隐藏imagebutton
		setSlidingMenuEnable(false);
	}
	
}
