package com.example.news.viewPager.impl;

import com.example.news.MainActivity;
import com.example.news.viewPager.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		tv_title.setText("智慧服务");//修改标题
		btn_menu.setVisibility(View.VISIBLE);//隐藏imagebutton
		setSlidingMenuEnable(true);
	}

}
