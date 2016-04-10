package com.example.news;

import com.example.news.fragment.ContentFragment;
import com.example.news.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.activity_main);
		initSlidingMenu();
		initFragment();
	}
	

	/*
	 *初始化侧边栏
	 */
	private void initSlidingMenu() {
		setBehindContentView(R.layout.left_menu);// 设置侧边栏
		SlidingMenu menu=getSlidingMenu();// 获取侧边栏对象
		menu.setBehindOffset(200);// 设置预留屏幕的宽度
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
	}
	
	/*
	 * 初始化fragment
	 */
	private void initFragment() {
		FragmentManager manager=getSupportFragmentManager();
		FragmentTransaction ft=manager.beginTransaction();
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment());
		ft.replace(R.id.fl_content, new ContentFragment());
		ft.commit();
	}

}
