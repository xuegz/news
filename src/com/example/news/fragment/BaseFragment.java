package com.example.news.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * ContentFragment与LeftMenuFragment的基类
 */
public abstract class BaseFragment extends Fragment {
	
	// fragment创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mActivity = getActivity();
	}
	// 处理fragment的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initView();
	}
	// 依附的activity创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	public abstract View initView();
	
	// 初始化数据, 可以不实现
	public void initData() {

	}
}
