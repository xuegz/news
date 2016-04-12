package com.example.news;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager vp_guide;
	private LinearLayout ll_point_group;
	private View view_red_point;
	private Button bt_start;
	
	private GuidePagerAdapter adapter;
	
	private int width;//两个小圆点间的距离

	private int[] arr=new int[]{
		R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	private void initUI() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		vp_guide=(ViewPager) findViewById(R.id.vp_guide);
		ll_point_group=(LinearLayout) findViewById(R.id.ll_point_group);
		view_red_point=findViewById(R.id.view_red_point);
		bt_start=(Button) findViewById(R.id.bt_start);
	}
	
	private void initData() {
		//初始化引导页小圆点
		for (int i=0;i<arr.length;i++){
			View v=new View(this);
			v.setBackgroundResource(R.drawable.shape_point_gray);
			LayoutParams params=new LayoutParams(10, 10);
			if (i>0){
				params.leftMargin=10;
			}
			ll_point_group.addView(v,params);
		}
		adapter=new GuidePagerAdapter();
		vp_guide.setAdapter(adapter);
		
		//视图树getViewTreeObserver() 
		ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {//布局后
				width = ll_point_group.getChildAt(1).getLeft()-ll_point_group.getChildAt(0).getLeft();
				ll_point_group.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		//设置页面改变监听  setOnPageChangeListener
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {			
			@Override
			public void onPageSelected(int position) {
				if (position==arr.length-1){
					bt_start.setVisibility(View.VISIBLE);
				}else{
					bt_start.setVisibility(View.INVISIBLE);
				}
			}	
			//小红点的滚动
			@Override
			public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
				/*position  位置
				 *positionOffset   移动百分比（0-1）
				 *positionOffsetPixels  移动距离
				 */
				int distance=(int) (width*(positionOffset+position));
				RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) view_red_point.getLayoutParams();
				layoutParams.leftMargin=distance;
				view_red_point.setLayoutParams(layoutParams);
			}			
			@Override
			public void onPageScrollStateChanged(int state) {			}
		});
	}
	
	/*
	 * bt_start的点击事件  进入主页面
	 */
	public void enterMain(View v){
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
	
	/*
	 * GuidePagerAdapter
	 */
	class GuidePagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arr.length;
		}
		@Override
		public Object instantiateItem(android.view.ViewGroup container, int position) {
			ImageView iv=new ImageView(GuideActivity.this);
			//iv.setImageResource(arr[position]);
			iv.setBackgroundResource(arr[position]);
			container.addView(iv);
			return iv;
		};
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}	
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

}
