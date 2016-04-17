package com.example.news;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.news.utils.SharePreferenceUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class NewsDetailActivity extends Activity {
	private ImageButton ib_back;
	private ImageButton ib_share;
	private ImageButton ib_textsize;
	private WebView wv_news;
	private WebSettings settings;
	private SharePreferenceUtil sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		setContentView(R.layout.activity_news_detail);
		ib_back=(ImageButton) findViewById(R.id.ib_back);
		ib_share=(ImageButton) findViewById(R.id.ib_share);
		ib_textsize=(ImageButton) findViewById(R.id.ib_textsize);
		wv_news=(WebView) findViewById(R.id.wv_news);
	}

	private void initData() {
		setWebView();
		setTextSize();
		share();//分享
		back();//返回
	}
	
	private void back() {
		ib_back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	//设置字体大小
	private void setTextSize() {
		sp = new SharePreferenceUtil(this);
		final int textSize=sp.getInt("textSize",2);//默认正常字体
		getTextSize(textSize);//获取保存的字体大小
		
		ib_textsize.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				String[] items=new String[]{
						"超大号字体","大号字体","正常字体","小号字体","超小号字体"
				};
				AlertDialog.Builder builder=new Builder(NewsDetailActivity.this);
				builder.setTitle("设置字体大小");
				builder.setSingleChoiceItems(items, textSize, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						saveTextSize(which);//设置并保存字体大小		
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
	}
	//设置并保存字体大小
	private void saveTextSize(int which){
		switch (which) {
			case 0:
				settings.setTextSize(TextSize.LARGEST);
				sp.saveInt("textSize", 0);
				break;
			case 1:
				settings.setTextSize(TextSize.LARGER);
				sp.saveInt("textSize", 1);
				break;
			case 2:
				settings.setTextSize(TextSize.NORMAL);
				sp.saveInt("textSize",2);
				break;
			case 3:
				settings.setTextSize(TextSize.SMALLER);
				sp.saveInt("textSize", 3);
				break;
			case 4:
				settings.setTextSize(TextSize.SMALLEST);
				sp.saveInt("textSize", 4);
				break;				
		}
	}
	//获取保存的字体大小
	private void getTextSize(int which){
		switch (which) {
			case 0:
				settings.setTextSize(TextSize.LARGEST);
				break;
			case 1:
				settings.setTextSize(TextSize.LARGER);
				break;
			case 2:
				settings.setTextSize(TextSize.NORMAL);
				break;
			case 3:
				settings.setTextSize(TextSize.SMALLER);
				break;
			case 4:
				settings.setTextSize(TextSize.SMALLEST);
				break;				
		}
	}
	
	//分享
	private void share() {
		ib_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showShare();
			}
		});
	}
	/*
	 * shareSDK   分享
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("薛的分享");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn"); 
		// 启动分享GUI
		 oks.show(this);
		 }
	//设置WebView
	private void setWebView() {
		String newsUrl=getIntent().getStringExtra("newsUrl");
		settings = wv_news.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);//设置支持缩放 
		settings.setUseWideViewPort(true);//双击缩放
		//wv_news.goBack();wv_news.goForward();返回  前进
		wv_news.setWebViewClient(new WebViewClient(){// 打开网页时不调用系统浏览器， 而是在本WebView中显示：
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				System.out.println("网页开始加载");
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				System.out.println("网页结束加载");
				super.onPageFinished(view, url);
			}
			@Override
			//所有跳转的url都会在此方法回调
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
				//return super.shouldOverrideUrlLoading(view, url);
			}
		});
		wv_news.setWebChromeClient(new WebChromeClient(){
			@Override
			//进度改变
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println(newProgress);
				super.onProgressChanged(view, newProgress);
			}
			@Override
			//获取网页标题
			public void onReceivedTitle(WebView view, String title) {
				System.out.println(title);
				super.onReceivedTitle(view, title);
			}
		});
		wv_news.loadUrl(newsUrl);
	}
	
	/*  setJavaScriptEnabled(true);  //支持js
		setPluginsEnabled(true);  //支持插件 
		setUseWideViewPort(true);  //双击缩放
		setBuiltInZoomControls(true); //设置支持缩放 
		setSupportZoom(true);  //支持缩放 
		setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局  
		supportMultipleWindows();  //多窗口 
		setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存 
		setAllowFileAccess(true);  //设置可以访问文件 
		setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
		setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口 
		setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		setLoadsImagesAutomatically(true);  //支持自动加载图片
	 */

}
