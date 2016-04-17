package com.example.news.viewPager.menudetail;

import java.util.ArrayList;

import com.example.news.R;
import com.example.news.bean.PhotosData;
import com.example.news.bean.PhotosData.PhotoInfo;
import com.example.news.global.GlobalContants;
import com.example.news.utils.SharePreferenceUtil;
import com.example.news.viewPager.BaseMenuDetailPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 菜单详情页-组图
 * 
 * @author Kevin
 * 
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {
	private ListView lv_photo;
	private GridView gv_photo;
	private PhotosListAdapter adapter;
	private ArrayList<PhotoInfo> photoInfos;
	private ImageButton btn_display;
	private boolean isList;
	
	public PhotoMenuDetailPager(Activity activity) {
		super(activity);
	}
	
	public PhotoMenuDetailPager(Activity activity, final ImageButton btn_display) {
		super(activity);
		this.btn_display = btn_display;
		
		isList = SharePreferenceUtil.getBoolean("photo_isList", true);
		btn_display.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				changeDisplay();
			}
		});
	}
	/*
	 * 切换组图展现方式
	 */
	protected void changeDisplay() {
		if (isList){
			isList=false;
			lv_photo.setVisibility(View.GONE);
			gv_photo.setVisibility(View.VISIBLE);
			SharePreferenceUtil.saveBoolean("photo_isList", isList);
			btn_display.setImageResource(R.drawable.icon_pic_list_type);
		}else{
			isList=true;
			lv_photo.setVisibility(View.VISIBLE);
			gv_photo.setVisibility(View.GONE);
			SharePreferenceUtil.saveBoolean("photo_isList", isList);
			btn_display.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}

	@Override
	public View initViews() {
		View view=View.inflate(activity, R.layout.menu_photo_pager, null);
		lv_photo=(ListView) view.findViewById(R.id.lv_photo);
		gv_photo=(GridView) view.findViewById(R.id.gv_photo);
		return view;
	}
	
	@Override
	public void initData() {
		getDataFromServer();
	}
	
	
	private void getDataFromServer(){
		HttpUtils utils=new HttpUtils();
		String url=GlobalContants.PHOTO_MENU_URL;
		utils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				parseData(result);
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "连接失败", 0).show();
			}
		});
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		PhotosData data=gson.fromJson(result, PhotosData.class);
		photoInfos = data.data.news;
		adapter = new PhotosListAdapter();
		lv_photo.setAdapter(adapter);
		gv_photo.setAdapter(adapter);
		if (isList){
			lv_photo.setVisibility(View.VISIBLE);
			gv_photo.setVisibility(View.GONE);
			btn_display.setImageResource(R.drawable.icon_pic_list_type);
		}else{
			lv_photo.setVisibility(View.GONE);
			gv_photo.setVisibility(View.VISIBLE);
			btn_display.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
	
	class PhotosListAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return photoInfos.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView==null){
				holder=new ViewHolder();
				convertView=View.inflate(activity, R.layout.list_photo_item, null);
				holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
				holder.iv_pic=(ImageView) convertView.findViewById(R.id.iv_pic);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			PhotoInfo photoInfo=photoInfos.get(position);
			holder.tv_title.setText(photoInfo.title);
			BitmapUtils bitmapUtils=new BitmapUtils(activity);
			bitmapUtils.display(holder.iv_pic,photoInfo.listimage);
			return convertView;
		}
	}
	static class ViewHolder{
		public TextView tv_title;
		public ImageView iv_pic;
	}

}
