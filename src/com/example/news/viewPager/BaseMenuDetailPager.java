package com.example.news.viewPager;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	public Activity activity;
	public View mRootview;
	
	public BaseMenuDetailPager(Activity activity) {
		super();
		this.activity = activity;
		mRootview=initViews();
		initData();
	}
	
	public abstract View initViews();
	
	public void initData(){
	}
	
}
