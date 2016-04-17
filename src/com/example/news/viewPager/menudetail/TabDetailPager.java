package com.example.news.viewPager.menudetail;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.MainActivity;
import com.example.news.NewsDetailActivity;
import com.example.news.R;
import com.example.news.bean.TabData;
import com.example.news.bean.NewsData.NewsTabData;
import com.example.news.bean.TabData.TabNewsData;
import com.example.news.bean.TabData.TopNewsData;
import com.example.news.fragment.ContentFragment;
import com.example.news.global.GlobalContants;
import com.example.news.listview.RefreshListView;
import com.example.news.listview.RefreshListView.onRefreshListener;
import com.example.news.utils.CacheUtil;
import com.example.news.utils.SharePreferenceUtil;
import com.example.news.viewPager.BaseMenuDetailPager;
import com.example.news.viewPager.impl.NewsCenterPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

/*
 * 标签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager {
	
	public ViewPager vp_news;
	public RefreshListView lv_news;
	public TextView tv_top_news_title;
	public CirclePageIndicator indicator;
	
	public int mPosition;//页面的位置
	public TabData tabData;//页签详情页数据
	private String newsUrl;//httputils请求的路径
	private String moreUrl;
	private List<NewsTabData> newsTabDatas;//新闻页面下11个子页签的数据对象
	private ArrayList<TopNewsData> topnews;//头条新闻数据的集合
	private ArrayList<TabNewsData> news;//新闻列表集合
	private NewsListViewAdapter newsListViewAdapter;//新闻列表listviewAdapter
	
	private SharePreferenceUtil sp;
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			int currentItem=vp_news.getCurrentItem();
			if (currentItem==topnews.size()-1){
				vp_news.setCurrentItem(0);
			}else{
				vp_news.setCurrentItem(currentItem+1);
			}
			handler.sendEmptyMessageDelayed(0, 3000);
		};
	};
	//构造方法
	public TabDetailPager(Activity activity) {
		super(activity);
		sp=new SharePreferenceUtil(activity);
	}

	@Override
	public View initViews() {
		View view=View.inflate(activity, R.layout.tab_detail_pager, null);
		lv_news = (RefreshListView) view.findViewById(R.id.lv_news);
		
		View headerView=View.inflate(activity, R.layout.header_view_top_news, null);
		vp_news = (ViewPager) headerView.findViewById(R.id.vp_news);
		tv_top_news_title=(TextView) headerView.findViewById(R.id.tv_top_news_title);
		indicator=(CirclePageIndicator) headerView.findViewById(R.id.indicator);
		
		lv_news.addHeaderView(headerView);//添加头布局
		//设置下拉刷新监听
		lv_news.setonRefreshListener(new onRefreshListener() {		
			@Override
			public void onPullRefresh() {
				new Thread(){
					public void run() {
						SystemClock.sleep(2000);//休眠两秒
						activity.runOnUiThread(new Runnable() {//主线程运行
							public void run() {
								lv_news.completeRefresh();
							}
						});
					};
				}.start();
			}		
			@Override
			public void onLoadingMore() {
				new Thread(){
					public void run() {
						SystemClock.sleep(2000);//休眠两秒
						activity.runOnUiThread(new Runnable() {//主线程运行
							public void run() {
								getMoreDataFromServer();
								lv_news.completeRefresh();
							}
						});
					};
				}.start();
			}
		});
		
		//设置条目点击监听
		lv_news.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//position-2  因为有两个headerView
				TabNewsData data=news.get(position-2);
				String savedIds=sp.getString("ids", "");//保存的id
				String newId=data.id;//新闻的id
				if(!savedIds.contains(newId)){
					savedIds+=newId+",";
					sp.saveString("ids", savedIds);
					//view表示被点击的条目的对象
					TextView tv_title=(TextView) view.findViewById(R.id.tv_title);
					tv_title.setTextColor(Color.GRAY);
				}
				String newsUrl=data.url;
				//跳转页面
				Intent intent=new Intent(activity, NewsDetailActivity.class);
				intent.putExtra("newsUrl", newsUrl);
				activity.startActivity(intent);
			}
		});
		return view;
	}
	
	public void initData(int position){
		mPosition=position;
		newsTabDatas=getNewsTabData();
		String cache=CacheUtil.getCache(GlobalContants.SERVER_URL+newsTabDatas.get(mPosition).url);//获得缓存
		if (!TextUtils.isEmpty(cache)){
			parseData(cache,false);
		}
		getDataFromServer();//不管有没有缓存，都调用服务器    更新数据
		
		//setOnPageChangeListener
		vp_news.setOnPageChangeListener(new OnPageChangeListener() {	
			@Override
			public void onPageSelected(final int position) {
				tv_top_news_title.setText(topnews.get(position).title);
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {	
			}
			@Override
			public void onPageScrollStateChanged(int state) {	
			}
		});
		
		handler.sendEmptyMessageDelayed(0, 3000);
	}
	
	/*
	 * 从服务器获取数据
	 */
	public void getDataFromServer(){
		HttpUtils httpUtils=new HttpUtils();
		newsUrl=GlobalContants.SERVER_URL+newsTabDatas.get(mPosition).url;
		httpUtils.send(HttpMethod.GET, newsUrl, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//获取数据
				String result=responseInfo.result;
				CacheUtil.setCache(newsUrl, result);
				//json解析数据
				parseData(result,false);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(activity, "请求失败", 0).show();
				error.fillInStackTrace();
			}
		});
	}
	/*
	 * 从服务器获取更多数据
	 */
	public void getMoreDataFromServer(){
		HttpUtils httpUtils=new HttpUtils();
		if (!TextUtils.isEmpty(tabData.data.more)){
			moreUrl=GlobalContants.SERVER_URL+tabData.data.more;
			httpUtils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					//获取数据
					String result=responseInfo.result;
					//json解析数据
					parseData(result,true);
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(activity, "请求失败", 0).show();
					error.fillInStackTrace();
				}
			});
		}else{
			moreUrl=null;
			Toast.makeText(activity, "没有更多数据了", 0).show();
		}
		
	}
	/*
	 * json解析数据
	 */
	private void parseData(String result,boolean more){
		Gson gson=new Gson();
		tabData = gson.fromJson(result, TabData.class);
		topnews = tabData.data.topnews;
		tv_top_news_title.setText(topnews.get(0).title);
		//viewPager  设置adapter
		vp_news.setAdapter(new TopNewsPagerAdapter());
		
		if (more){//加载更多
			news.addAll(tabData.data.news);
			newsListViewAdapter.notifyDataSetChanged();//刷新
		}else{//初次加载
			//listview   设置adapter
			news=tabData.data.news;
			newsListViewAdapter = new NewsListViewAdapter();
			lv_news.setAdapter(newsListViewAdapter);			
			indicator.setViewPager(vp_news);
			indicator.setSnap(true);//快照显示
			indicator.onPageSelected(0);//让指示器重新定位到第一个
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
	/*
	 * viewPager  设置adapter
	 */
	class TopNewsPagerAdapter extends PagerAdapter{
		private BitmapUtils bitmapUtils;//BitmapUtils加载图片
		
		public TopNewsPagerAdapter(){
			bitmapUtils = new BitmapUtils(activity);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return topnews.size();
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view==object;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv=new ImageView(activity);
			iv.setScaleType(ScaleType.FIT_XY);
			// 加载网络图片
			bitmapUtils.display(iv, topnews.get(position).topimage);
			container.addView(iv);
			return iv;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	/*
	 * listview   设置adapter
	 */
	class NewsListViewAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return news.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null){
				holder=new ViewHolder();
				convertView=View.inflate(activity, R.layout.list_news_item, null);
				holder.iv_pic=(ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			TabNewsData data=news.get(position);
			
			//利用bitmapUtils加载图片
			BitmapUtils bitmapUtils=new BitmapUtils(activity);
			bitmapUtils.display(holder.iv_pic, data.listimage);
			
			//设置tv_title
			holder.tv_title.setText(data.title);
			//设置  如果被点击  字体为灰色
			String ids=sp.getString("ids", "");
			if (ids.contains(data.id)){
				holder.tv_title.setTextColor(Color.GRAY);
			}
			
			//设置tv_date
			holder.tv_date.setText(data.pubdate);
			return convertView;
		}	
	}
	static class ViewHolder{
		ImageView iv_pic;
		TextView tv_title;
		TextView tv_date;
	}

}
