package com.example.news.viewPager.impl;

import com.example.news.MainActivity;
import com.example.news.viewPager.BasePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		tv_title.setText("设置");//修改标题
		btn_menu.setVisibility(View.INVISIBLE);//隐藏imagebutton
		setSlidingMenuEnable(false);
	}

}
