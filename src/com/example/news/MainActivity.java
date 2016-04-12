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
import android.view.MotionEvent;

public class MainActivity extends SlidingFragmentActivity {

	private SlidingMenu menu;
	//标记  通过它可获取相应的对象
	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";
	private FragmentManager manager;

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
		menu = getSlidingMenu();
		menu.setBehindOffset(200);// 设置预留屏幕的宽度
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
	}
	
	/*
	 * 初始化fragment
	 */
	private void initFragment() {
		manager = getSupportFragmentManager();
		FragmentTransaction ft=manager.beginTransaction();
		ft.replace(R.id.fl_left_menu, new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
		ft.replace(R.id.fl_content, new ContentFragment(),FRAGMENT_CONTENT);
		ft.commit();
	}
	
	/*
	 * 获取侧边栏对象
	 */
	public LeftMenuFragment getLeftMenuFragment(){
		LeftMenuFragment leftFragment=(LeftMenuFragment) manager.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return leftFragment;
	}
	
	/*
	 * 获取
	 */
	public ContentFragment getContentFragment(){
		ContentFragment contentFragment=(ContentFragment) manager.findFragmentByTag(FRAGMENT_CONTENT);
		return contentFragment;
	}
	
}
