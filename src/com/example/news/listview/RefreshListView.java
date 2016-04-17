package com.example.news.listview;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.news.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class RefreshListView extends ListView implements OnScrollListener {
	//headerView
	private View headerView;
	private ImageView iv_refresh;
	private ProgressBar pb_refresh;
	private TextView tv_refresh;
	private TextView tv_refresh_date;
	
	//footerView
	private View footerView;
	private ProgressBar pb_footer;
	private TextView tv_footer_title;
	
	private int headerHeight;
	private int footerHeight;
	private int startY;
	
	private boolean isLoadingMore=false;//底部是否加载更多数据
	
	public RefreshListView(Context context) {
		super(context);
		initView();
		initAnimation();
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initAnimation();
	}
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
		initAnimation();
	}
	
	private void initView() {
		setOnScrollListener(this);
		initHeaderView();
		initFooterView();
	}
	/*
	 * initHeaderView
	 */
	private void initHeaderView() {
		// TODO Auto-generated method stub
		headerView = View.inflate(getContext(), R.layout.header_view_refresh, null);
		iv_refresh=(ImageView) headerView.findViewById(R.id.iv_refresh);
		pb_refresh=(ProgressBar) headerView.findViewById(R.id.pb_refresh);
		tv_refresh=(TextView) headerView.findViewById(R.id.tv_refresh);
		tv_refresh_date=(TextView) headerView.findViewById(R.id.tv_refresh_date);
		//一个listview可以添加多个headerview   根据添加的先后顺序从上向下排列
		this.addHeaderView(headerView);
		headerView.measure(0, 0);//强制测量
		headerHeight = headerView.getMeasuredHeight();//获取headerview的高
		headerView.setPadding(0, -headerHeight, 0, 0);//隐藏下拉刷新
	}
	/*
	 * initFooterView
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.footer_view_refresh, null);
		pb_footer=(ProgressBar) footerView.findViewById(R.id.pb_footer);
		tv_footer_title=(TextView) footerView.findViewById(R.id.tv_footer_title);
		this.addFooterView(footerView);
		
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0,-footerHeight, 0, 0);
	}
	/*
	 * 下拉刷新3中状态
	 */
	private final int PULL_REFRESH = 0;//下拉刷新的状态
	private final int RELEASE_REFRESH = 1;//松开刷新的状态
	private final int REFRESHING = 2;//正在刷新的状态
	private int currentState = PULL_REFRESH;
	
	public void refreshHeaderView(){	
		switch (currentState) {
			case PULL_REFRESH:
				tv_refresh.setText("下拉刷新");
				iv_refresh.startAnimation(downAnimation);
				break;
			case RELEASE_REFRESH:
				tv_refresh.setText("松开刷新");
				iv_refresh.startAnimation(upAnimation);
				break;
			case REFRESHING:
				tv_refresh.setText("正在刷新");
				iv_refresh.clearAnimation();//清除动画
				headerView.setPadding(0, 0, 0, 0);
				iv_refresh.setVisibility(View.INVISIBLE);
				pb_refresh.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	/*
	 * 刷新完成,初始化界面
	 */
	public void completeRefresh(){
		//下拉刷新完成
		if (!isLoadingMore){
			tv_refresh_date.setText("最后刷新时间："+getCurrentTime());
			iv_refresh.setVisibility(View.VISIBLE);
			pb_refresh.setVisibility(View.INVISIBLE);
			headerView.setPadding(0, -headerHeight, 0, 0);
			currentState=PULL_REFRESH;
		}else{//加载更多
			footerView.setPadding(0, -footerHeight, 0, 0);
			isLoadingMore=false;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getY();//开始位置
				break;
			case MotionEvent.ACTION_MOVE:
				if (currentState==REFRESHING){//如果正在刷新  直接返回
					break;
				}
				int endY=(int) ev.getY();//结束位置
				int dY=endY-startY;//移动距离
				if (dY>0&&this.getFirstVisiblePosition()==0){//下拉刷新
					int paddingTop=-headerHeight+dY;
					headerView.setPadding(0, paddingTop, 0, 0);
					if (paddingTop>=0&&currentState!=RELEASE_REFRESH){
						//从下拉刷新进入松开刷新状态
						currentState=RELEASE_REFRESH;
					}else if (paddingTop<0&&currentState!=PULL_REFRESH){
						//进入下拉刷新状态
						currentState=PULL_REFRESH;
					}
					refreshHeaderView();
					return true;//拦截TouchMove，不让listview处理该次move事件,会造成listview无法滑动
				}
				break;
			case MotionEvent.ACTION_UP:
				if (currentState==PULL_REFRESH){
					headerView.setPadding(0, -headerHeight, 0, 0);
				}else if (currentState==RELEASE_REFRESH){
					currentState=REFRESHING;
					refreshHeaderView();
					if (onRefreshListener!=null){
						onRefreshListener.onPullRefresh();
					}
				}
				break;
		}
		return super.onTouchEvent(ev);
	}
	/*
	 * 初始化旋转动画
	 */
	private RotateAnimation upAnimation,downAnimation;
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(300);
		upAnimation.setFillAfter(true);
		
		downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(300);
		downAnimation.setFillAfter(true);
	}
	
	/*
	 * 获取当前的时间
	 */
	private String getCurrentTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/*
	 * 自定义下拉刷新接口
	 */
	public onRefreshListener onRefreshListener;
	
	public void setonRefreshListener(onRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	public interface onRefreshListener{
		void onPullRefresh();//下拉刷新
		void onLoadingMore();//加载更多
	}
	
	/**
	 * SCROLL_STATE_IDLE:闲置状态，就是手指松开
	 * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
	 * SCROLL_STATE_FLING：快速滑动后松开
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState==OnScrollListener.SCROLL_STATE_IDLE||scrollState==OnScrollListener.SCROLL_STATE_FLING){
			if (this.getLastVisiblePosition()==(this.getCount()-1)&&!isLoadingMore){
				isLoadingMore=true;
				footerView.setPadding(0, 0, 0, 0);//显示出footerView
				setSelection(getCount());//让listview最后一条显示出来  改变listview的显示位置
				if(onRefreshListener!=null){
					onRefreshListener.onLoadingMore();
				}
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
}
