package com.newgen.xj_app.ww;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.newgen.UI.channel.DragGrid;
import com.newgen.UI.channel.OtherGridView;
import com.newgen.adapter.channel.DragAdapter;
import com.newgen.adapter.channel.OtherAdapter;
import com.newgen.domain.AddressComponent;
import com.newgen.domain.channel.ChannelItem;
import com.newgen.domain.channel.ChannelManage;
import com.newgen.init.MyApplication;
import com.newgen.server.MainServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 频道管理
 */

public class ChannelActivity extends Activity implements OnItemClickListener{
	
	/** 用户栏目的GRIDVIEW */
	private DragGrid userGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView otherGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter userAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter otherAdapter;
	/** 其它栏目列表 */
	ArrayList<ChannelItem> otherChannelList1 = new ArrayList<ChannelItem>();
	ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
	/** 用户栏目列表 */
	ArrayList<ChannelItem> userChannelList1 = new ArrayList<ChannelItem>();
	ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	ImageView back;
	MyHandler handler; 
	boolean isFirst = true;
	boolean tag = true;
//	int size = 0;
	boolean isAddRecommend = false;
	private ChannelItem locationChannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		initView();
		initData();
		initLocationChannel();
//		size = getIntent().getExtras().getInt("size");
//		Log.i("size", size + "");
		handler = new MyHandler();
		initCategory();
	}
	
	private void initLocationChannel() {
		// TODO Auto-generated method stub
		locationChannel = new ChannelItem();
		locationChannel.setId(-1);
		locationChannel.setName(PublicValue.TRANSLOCATIONCITY);
		locationChannel.setSummary(PublicValue.TRANSLOCATIONCITY);
		locationChannel.setShowType(1);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (tag == true) {
			new LoadInitInfo().start();
			tag = false;
		} 
	}
	
	
	/** 初始化布局 */
	private void initView() {
		back = (ImageView) findViewById(R.id.imageview_back);
		userGridView = (DragGrid) findViewById(R.id.userGridView);
		otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveChannel();
				Intent intent = new Intent(ChannelActivity.this,
						MainFragmentActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	/** 初始化数据 */
	private void initData() {
		userAdapter = new DragAdapter(this, userChannelList);
		userGridView.setAdapter(userAdapter);

		otherAdapter = new OtherAdapter(this, otherChannelList);
		otherGridView.setAdapter(this.otherAdapter);
		// 设置GRIDVIEW的ITEM的点击监听
		otherGridView.setOnItemClickListener(this);
		userGridView.setOnItemClickListener(this);
	}
	
	
	// 更新两个gridview 的数据
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				userChannelList.clear();
				userChannelList.addAll(userChannelList1);
				userAdapter.notifyDataSetChanged();
				otherChannelList.clear();
				otherChannelList.addAll(otherChannelList1);
				otherAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	
	/****
	 * 加载APP初始化信息
	 * 
	 * @author suny
	 * 
	 */
	private class LoadInitInfo extends Thread {
		@Override
		public void run() {

			MainServer mserver = new MainServer();
			String resultStr = mserver.getNewsCategoryAndADPath();
			Message msg = new Message();
			Log.i("info", "++++++++++++++++***///" + resultStr);
			try {
				// Thread.sleep(2000);
				JSONObject json = new JSONObject(resultStr);
				if (json.getInt("ret") == 1) {// 成功
					// 获取栏目列表的信息
					readData(json);
				}
			} catch (Exception e) {

			}
		}
	}
	
	
	/**
	 * 获取后台的栏目
	 * 
	 * @param json
	 * @throws JsonSyntaxException
	 * @throws JSONException
	 */
	private void readData(JSONObject json) throws JsonSyntaxException,
			JSONException {
		JSONArray categorysJson = json.getJSONArray("listCategory");
		Gson gson = new Gson();
		otherChannelList.clear();
		
		for (int i = 0; i < categorysJson.length(); i++) {

			ChannelItem c = gson.fromJson(categorysJson.getString(i),
					ChannelItem.class);
			
			if (c != null&&c.getShowType()!=2&&c.getShowType()!=3) {//2 shipin   3  huodong
				int k = 0;
				for (int j = 0; j < userChannelList.size(); j++) {
					k = j;
					if (userChannelList.get(j).getId() == c.getId()) {
						break;
					}
					
				}
				if (k == (userChannelList.size() - 1)
						&& userChannelList.get(k).getId() != c.getId()) {
					otherChannelList1.add(c);
				}
			}
		}
		
		//特殊处理，区域首页栏目添加
		int b = 0;
		for (int a = 0; a < userChannelList.size(); a++) {
			b = a;
			if (userChannelList.get(a).getId()<0) {
				break;
			}
			if(b==(userChannelList.size() - 1)
					&& userChannelList.get(b).getId()>0){
				otherChannelList1.add(locationChannel);
			}
		}
		
		Message msg = new Message();
		msg.what = 1;
		handler.sendMessage(msg);
	}
	
	@SuppressWarnings("unchecked")
	private void initCategory() {
		try {
			Map<String, String> map = (Map<String, String>) SharedPreferencesTools
					.getValue(this, PublicValue.WORD_NEWS_CATEGORY_FILE_WW_ED);
			String category = map.get(PublicValue.WORD_NEWS_CATEGORY_FILE_WW_ED);
			Log.i("pdgl", category.toString());
			if (null == category || "".equals(category)) {
				category = Tools.getFromAssets("category.json", this);
			}
			Gson gson = new Gson();
			JSONArray jsonArray = new JSONArray(category);
			for (int i = 0; i < jsonArray.length(); i++) {
				ChannelItem c = gson.fromJson(jsonArray.getString(i),
						ChannelItem.class);
				if (c != null && c.getShowType()!= 2 && c.getShowType()!=3)
					userChannelList1.add(c);
			}
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "数据异常！", Toast.LENGTH_LONG)
					.show();
		}
	}


	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view,
			final int position, long id) {
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		if (isMove) {
			return;
		}
		switch (parent.getId()) {
		case R.id.userGridView:
			// position为 0的不可以进行任何操作
			if (position != 0) {
				final ImageView moveImageView = getView(view);
				if (moveImageView != null) {
					TextView newTextView = (TextView) view
							.findViewById(R.id.text_item);
					final int[] startLocation = new int[3];
					newTextView.getLocationInWindow(startLocation);
					final ChannelItem channel = ((DragAdapter) parent
							.getAdapter()).getItem(position);// 获取点击的频道内容
					otherAdapter.setVisible(false);
					// 添加到最后一个
					otherAdapter.addItem(channel);
					// userChannelList.remove(channel);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							try {
								int[] endLocation = new int[6];
								// 获取终点的坐标
								otherGridView.getChildAt(
										otherGridView.getLastVisiblePosition())
										.getLocationInWindow(endLocation);
								MoveAnim(moveImageView, startLocation,
										endLocation, channel, userGridView);
								userAdapter.setRemove(position);
								String summary = userAdapter.getItem(position)
										.getName();
								Log.i("info", summary);

							} catch (Exception localException) {
							}
						}
					}, 50L);
				}
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view
						.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
						.getItem(position);
				userAdapter.setVisible(false);
				// 添加到最后一个
				userAdapter.addItem(channel);
				List<ChannelItem> channnelLst = otherAdapter.getChannnelLst();
				Log.i("****", channnelLst.toString());
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							userGridView.getChildAt(
									userGridView.getLastVisiblePosition())
									.getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation,
									channel, otherGridView);
							otherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 点击ITEM移动动画
	 * 
	 * @param moveView
	 * @param startLocation
	 * @param endLocation
	 * @param moveChannel
	 * @param clickGridView
	 */
	private void MoveAnim(View moveView, int[] startLocation,
			int[] endLocation, final ChannelItem moveChannel,
			final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView,
				initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(
				startLocation[0], endLocation[0], startLocation[1],
				endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					otherAdapter.setVisible(true);
					otherAdapter.notifyDataSetChanged();
					userAdapter.remove();
				} else {
					userAdapter.setVisible(true);
					userAdapter.notifyDataSetChanged();
					otherAdapter.remove();
				}
				isMove = false;
			}
		});
	}
	
	
	/**
	 * 获取移动的VIEW，放入对应ViewGroup布局容器
	 * 
	 * @param viewGroup
	 * @param view
	 * @param initLocation
	 * @return
	 */
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	/**
	 * 创建移动的ITEM对应的ViewGroup布局容器
	 */
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	/**
	 * 获取点击的Item的对应View，
	 * 
	 * @param view
	 * @return
	 */
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}
	
	
	/** 退出时候保存选择后数据库的设置 */
	private void saveChannel() {
		ChannelManage.getManage(MyApplication.getInstance().getSQLHelper())
				.deleteAllChannel();
		Gson gson1 = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		// 保存用户选中
		map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_WW_ED,
				gson1.toJson(userAdapter.getChannnelLst()));
		Log.i("tostring", gson1.toJson(userAdapter.getChannnelLst()));
		SharedPreferencesTools.setValue(ChannelActivity.this, map,
				PublicValue.WORD_NEWS_CATEGORY_FILE_WW_ED);
	}
	
	
	@Override  
	public Resources getResources() {  
	    Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res;  
	}  

}
