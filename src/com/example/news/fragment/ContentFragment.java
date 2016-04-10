package com.example.news.fragment;

import com.example.news.R;

import android.view.View;

public class ContentFragment extends BaseFragment {

	@Override
	public View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_content, null);
		return view;
	}

}
