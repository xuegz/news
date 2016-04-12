package com.example.news.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.news.R;
import com.example.news.viewPager.BasePager;
import com.example.news.viewPager.impl.GovAffairsPager;
import com.example.news.viewPager.impl.HomePager;
import com.example.news.viewPager.impl.NewsCenterPager;
import com.example.news.viewPager.impl.SettingPager;
import com.example.news.viewPager.impl.SmartServicePager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/*
 * 
 */
public class ContentFragment extends BaseFragment {
	
	private RadioGroup rg_content;
	private ViewPager vp_content;
	public List<BasePager> list;

	/*
	 * 初始化view
	 */
	@Override
	public View initView() {	
		View view = View.inflate(getActivity(), R.layout.fragment_content, null);
		vp_content=(ViewPager) view.findViewById(R.id.vp_content);
		rg_content = (RadioGroup) view.findViewById(R.id.rg_content);
		return view;
	}
	
	@Override
	public void initData() {
		super.initData();
		/*
		 * viewpager添加数据
		 */
		list= new ArrayList<BasePager>();
		/*for (int i=0;i<5;i++){
			lists.add(new BasePager(getActivity()));
		}*/
		list.add(new HomePager(getActivity()));
		list.add(new NewsCenterPager(getActivity()));
		list.add(new SmartServicePager(getActivity()));
		list.add(new GovAffairsPager(getActivity()));
		list.add(new SettingPager(getActivity()));
		/*
		 * 设置viewpager页面改变监听  初始化数据
		 */
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				list.get(position).initData();
			}			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}		
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
		list.get(0).initData();//手动初始化首页
		vp_content.setAdapter(new ContentPagerAdapter());
		/*
		 * 设置按钮改变监听   跳转页面
		 */
		rg_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.rb_home:
						vp_content.setCurrentItem(0, false);
						break;
					case R.id.rb_news:
						vp_content.setCurrentItem(1, false);
						
						break;
					case R.id.rb_smart:
						vp_content.setCurrentItem(2, false);
						break;
					case R.id.rb_gov:
						vp_content.setCurrentItem(3, false);
						break;
					case R.id.rb_setting:
						vp_content.setCurrentItem(4, false);
						break;
				}
			}
		});
	}
	
	/*
	 * 得到NewsCenterPager
	 */
	public NewsCenterPager getNewsCenterPager(){
		return (NewsCenterPager) list.get(1);
	}
	
	class ContentPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager=list.get(position);
			container.addView(pager.view);
			return pager.view;
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==object;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
	}
	

}
