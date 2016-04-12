package com.example.news.fragment;

import java.util.ArrayList;
import java.util.Currency;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.bean.NewsData;
import com.example.news.bean.NewsData.NewsMenuData;
import com.example.news.viewPager.impl.NewsCenterPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 侧滑菜单
 */
public class LeftMenuFragment extends BaseFragment {
	public ListView lv_left_menu;
	
	private int curentPosition;
	//侧边栏数据对象的集合
	public ArrayList<NewsMenuData> menuDatas;
	private MenuListAdpater adapter;
	
	@Override
	public View initView() {
		View view=View.inflate(getActivity(), R.layout.fragment_left_menu, null);
		lv_left_menu=(ListView) view.findViewById(R.id.lv_left_menu);
		return view;
	}
	
	@Override
	public void initData() {
		lv_left_menu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				curentPosition=position;
				setCurrentMenuPager(position);
				switchLeftMenu();
				adapter.notifyDataSetChanged();
			}
		});
	}
	/*
	 * 设置当前的菜单详情页
	 */
	protected void setCurrentMenuPager(int position) {
		MainActivity mActivity=(MainActivity) getActivity();
		ContentFragment fragment=mActivity.getContentFragment();
		NewsCenterPager newsCenterPager=fragment.getNewsCenterPager();
		newsCenterPager.setCurrentMenuDetailPager(position);
	}

	class MenuListAdpater extends BaseAdapter{
		@Override
		public int getCount() {
			return menuDatas.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return menuDatas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=View.inflate(getActivity(), R.layout.list_menu_item, null);
			TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
			NewsMenuData menuData=(NewsMenuData) getItem(position);
			tv_title.setText(menuData.title);
			if (curentPosition==position){
				tv_title.setEnabled(true);
			}else{
				tv_title.setEnabled(false);
			}
			return view;
		}	
	}
	
	/*
	 * 设置侧边栏数据
	 */
	public void setLeftMenuData(NewsData newsData){
		menuDatas = newsData.data;
		adapter = new MenuListAdpater();
		lv_left_menu.setAdapter(adapter);
	}
	/*
	 * 按钮显示/关闭侧边栏
	 */
	public void switchLeftMenu(){
		MainActivity mActivity=(MainActivity)getActivity();
		SlidingMenu menu=mActivity.getSlidingMenu();
		menu.toggle();//If it is open, it will be closed, and vice versa.
	}
}
