package com.example.news.viewPager;

import com.example.news.MainActivity;
import com.example.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/*
 * 5个viewpager的基类
 */
public class BasePager {
	
	public Activity activity;
	public TextView tv_title;
	public ImageButton btn_menu;
	public FrameLayout fl_content;
	public View view;
	public ImageButton btn_display;
	
	public BasePager(Activity activity) {
		super();
		this.activity = activity;
		initUI();
	}
	/*
	 * 初始化界面
	 */
	public void initUI(){
		view = View.inflate(activity, R.layout.base_pager, null);
		tv_title=(TextView) view.findViewById(R.id.tv_title);
		btn_menu=(ImageButton) view.findViewById(R.id.btn_menu);
		fl_content=(FrameLayout) view.findViewById(R.id.fl_content);
		btn_display=(ImageButton) view.findViewById(R.id.btn_display);
		btn_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchLeftMenu();
			}
		});
	}
	/*
	 * 初始化数据
	 */
	public void initData(){
	}
	
	/*
	 * 侧边栏
	 */
	public void setSlidingMenuEnable(boolean enable){
		MainActivity mActivity=(MainActivity) activity;
		SlidingMenu menu=mActivity.getSlidingMenu();
		if (enable){
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	/*
	 * 按钮显示/关闭侧边栏
	 */
	public void switchLeftMenu(){
		MainActivity mActivity=(MainActivity) activity;
		SlidingMenu menu=mActivity.getSlidingMenu();
		menu.toggle();//If it is open, it will be closed, and vice versa.
	}

}
