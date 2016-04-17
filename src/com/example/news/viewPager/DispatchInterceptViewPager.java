package com.example.news.viewPager;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
 * 分发拦截的viewpager
 * 事件的3种拦截机制
 * dispatchTouchEvent:分发
 * onInterceptTouchEvent:返回true表示拦截   false传给子view
 * onTouchEvent:处理触摸事件
 */

public class DispatchInterceptViewPager extends ViewPager {

	public DispatchInterceptViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DispatchInterceptViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getCurrentItem()!=0){
			getParent().requestDisallowInterceptTouchEvent(true);//用getParent()，请求父view及更上层的view不拦截
		}
		return super.dispatchTouchEvent(ev);
	}
}
