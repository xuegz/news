package com.example.news.viewPager.menudetail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.bean.TabData;
import com.example.news.bean.NewsData.NewsTabData;
import com.example.news.bean.TabData.TabDetail;
import com.example.news.bean.TabData.TopNewsData;
import com.example.news.fragment.ContentFragment;
import com.example.news.global.GlobalContants;
import com.example.news.viewPager.BaseMenuDetailPager;
import com.example.news.viewPager.impl.GovAffairsPager;
import com.example.news.viewPager.impl.NewsCenterPager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.TabPageIndicator;

/*
 * 菜单新闻页
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {
	
	private ViewPager vp_menu_detail;
	private TabPageIndicator indicator;
	
	private List<TabDetailPager> list;
	private List<NewsTabData> newsTabDatas;
	
	
	public NewsMenuDetailPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View initViews() {
		View view=View.inflate(activity, R.layout.news_menu_detail, null);
		vp_menu_detail = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		//ViewPagerIndicator   初始化自定义控件
		indicator = (TabPageIndicator)view.findViewById(R.id.indicator);//须在TabPageIndicator中加入dispatchTouchEvent拦截
		return view;
	}
	
	public void initData(){
		newsTabDatas=getNewsTabData();
		list = new ArrayList<TabDetailPager>();
		for (int i=0;i<newsTabDatas.size();i++){
			list.add(new TabDetailPager(activity));
		}
		MenuDetailPager adapter=new MenuDetailPager();
		vp_menu_detail.setAdapter(new MenuDetailPager());
		
		//ViewPagerIndicator  必须在vivepager设置完adapter后调用
		indicator.setViewPager(vp_menu_detail);
		
	}
	
	
	/*
	 * pagerAdapter
	 */
	class MenuDetailPager extends PagerAdapter{
		//ViewPagerIndicator需实现该方法
		@Override
		public CharSequence getPageTitle(int position) {
			return newsTabDatas.get(position).title;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			NewsTabData newsTabData=newsTabDatas.get(position);
			TabDetailPager pager=list.get(position);//获取TabDetailPager--tab_detail_pager.xml
			pager.initData(position);
			container.addView(pager.mRootview);
			return pager.mRootview;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}
	/*
	 * 获取NewsTabData的集合
	 */
	public List<NewsTabData> getNewsTabData(){
		MainActivity mActivity=(MainActivity) activity;
		ContentFragment contentFragment=mActivity.getContentFragment();
		NewsCenterPager newsCenterPager=(NewsCenterPager) contentFragment.list.get(1);
		List<NewsTabData> children=newsCenterPager.newsData.data.get(0).children;
		return children;
	}
	
}
