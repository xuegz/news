package com.example.news.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;
	
	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/*
	 * 上下滑   父类拦截
	 * 向右滑   位置为0时，父类拦截
	 * 想做花   位置为max时，父类拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = (int) ev.getRawX();
				startY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int endX=(int) ev.getRawX();
				int endY=(int) ev.getRawY();
				if (Math.abs(startX-endX)>Math.abs(startY-endY)){//左右滑动
					if ((endX-startX)>0&&getCurrentItem()==0){//向右滑   位置为0时，父类拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}else if ((endX-startX)<0&&(getAdapter().getCount()-1==getCurrentItem())){//向左滑   位置为max时，父类拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}else{//父类不拦截  子类处理
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				}else{//上下滑动
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				break;
		}
		//getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

}
