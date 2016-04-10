package com.example.news;

import com.example.news.utils.SharePreferenceUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.view.Menu;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/*
 * 闪屏页
 */
public class SplashActivity extends Activity {
	private ImageView iv_splash;
	private SharePreferenceUtil spUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		init();
	}

	private void init() {
		iv_splash=(ImageView) findViewById(R.id.iv_splash);
		splashAnimation();
	}
	/*
	 *闪屏页 动画
	 */
	private void splashAnimation() {
		AnimationSet set=new AnimationSet(false);
		//旋转
		RotateAnimation rAnimation=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rAnimation.setDuration(1000);
		rAnimation.setFillAfter(true);
		
		//缩放1
		ScaleAnimation sAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sAnimation.setDuration(1000);
		sAnimation.setFillAfter(true);
		
		//渐变
		AlphaAnimation aAnimation=new AlphaAnimation(0.3f, 1);
		aAnimation.setDuration(1000);
		aAnimation.setFillAfter(true);
		
		set.addAnimation(rAnimation);
		set.addAnimation(sAnimation);
		set.addAnimation(aAnimation);
		
		//设置监听  动画结束进入新手指导页
		set.setAnimationListener(new AnimationListener() {	
			@Override
			public void onAnimationStart(Animation animation) {				
			}			
			@Override
			public void onAnimationRepeat(Animation animation) {	
			}	
			@Override
			public void onAnimationEnd(Animation animation) {
				jumpNextActivity();
			}
		});
		iv_splash.startAnimation(set);
	}
	/*
	 * 跳转到下一个页面
	 */
	public void jumpNextActivity(){
		//利用sharepreference  让新手页只显示一次
		spUtil=new SharePreferenceUtil(SplashActivity.this);
		boolean saved=spUtil.getBoolean("guideAcitvity", false);
		if (saved){
			startActivity(new Intent(SplashActivity.this, MainActivity.class));//MainActivity
		}else{
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));//GuideActivity
			spUtil.saveBoolean("guideAcitvity", true);
		}
		finish();
	}
}
