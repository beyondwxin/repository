package com.newgen.xj_app.ww;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.ActiveListAdapter;
import com.newgen.domain.Active;
import com.newgen.server.NewsServer;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;
import com.newgen.xj_app.detail.ww.ActiveDetailActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActiveActivity extends Activity implements IXListViewListener{
	
	ActiveListAdapter adapter;
	ImageView back;
	XListView listView;
	int startid = -1;
	int size =10;
	List<Active> tempList = null;
	List<Active> newsList = new ArrayList<Active>();
	boolean isFrist = true;
	boolean isNight = false;
	private long flushTime = 0;
	LoadDateTask task;
	JSONArray jsonArray;
	String category = null;
	Gson gson = new Gson();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active);
		
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		initCid();
		
		listView = (XListView)findViewById(R.id.listView);
		adapter = new ActiveListAdapter(ActiveActivity.this, newsList,isNight);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new ListItemClick());
		listView.setAdapter(adapter);
		
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
			flushTime = new Date().getTime();
			onRefresh();
			isFrist = false;
		}
	}
	
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		startid = -1;
		long tempTime = new Date().getTime();
		String notice = Tools.getTimeInterval(flushTime, tempTime);
		listView.setRefreshTime(notice);
		flushTime = tempTime;
		task = new LoadDateTask();
		task.execute();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (null != newsList && newsList.size() > 0)
			startid = newsList.get(newsList.size() - 1).getId();
		task = new LoadDateTask();
		task.execute();
	}
	
	class ListItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			
			Active active = newsList.get(position - 1);
			if(active.getFlag()==0){
				Toast.makeText(ActiveActivity.this,
						"活动还未开始哦", 5).show();
			}else {
				Intent intent = new Intent(ActiveActivity.this, ActiveDetailActivity.class);
				intent.putExtra("type", active.getType());
				intent.putExtra("relationid", active.getRelationid());
				intent.putExtra("title", active.getTitle());
				intent.putExtra("shareImg",active.getImgpath()+"/M_"+active.getImgname());
				
				startActivity(intent);
			}
			
		}
		
	}
	
	
	private class LoadDateTask extends AsyncTask<Object, Integer, Integer> {
		
		@Override
		protected void onPreExecute() {
			JCVideoPlayer.releaseAllVideos();
			listView.setEnabled(false);
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			int ret = -1;
			try {
				NewsServer server = new NewsServer();
				tempList = server.getActiveList(10, startid);
				
				if (tempList == null) {
					ret = -1;
				} else if (tempList.size() <= 0) {
					ret = 0;
				} else {
					ret = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			try {
				switch (result) {
				case 1:
					if (startid <= 0) {// 刷新动作
						newsList.clear();
						adapter.notifyDataSetChanged();
					}
					newsList.addAll(tempList);
					adapter.notifyDataSetChanged();
					tempList.clear();
					tempList = null;
					startid = newsList.get(newsList.size() - 1).getId();
					break;
				case -1:
					break;
				case 0:
					MyToast.showToast(ActiveActivity.this, "没有更多数据", 3);
					break;
				}
				stopLoadOrRefresh();
				listView.setEnabled(true);
			} catch (Exception e) {

			}
			
		}
		
		@Override
		protected void onCancelled() {
			stopLoadOrRefresh();
			listView.setEnabled(true);
		}
		
	}
	
	/**
	 * 视频停止、音频停止
	 */
	public void stopLoadOrRefresh() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}
	
	
//	@SuppressWarnings("unchecked")
//	private void initCid() {
//		// TODO Auto-generated method stub
//		Map<String, String> map1 = (Map<String, String>) SharedPreferencesTools
//				.getValue(ActiveActivity.this, PublicValue.WORD_NEWS_CATEGORY_FILE);
//		category = map1.get(PublicValue.WORD_NEWS_CATEGORY_FILE);
//		
//		try {
//			jsonArray = new JSONArray(category);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try {
//			Tools.log(category);
//			jsonArray = new JSONArray(category);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				Category c = gson.fromJson(jsonArray.getString(i),
//						Category.class);
//				if (c != null&& c.getShowType()==3)
//					cid = c.getId();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Toast.makeText(getApplicationContext(), "数据异常！", Toast.LENGTH_LONG)
//					.show();
//		}
//	}
	
	
	@Override  
	public Resources getResources() {  
	    Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res;  
	}  
	
	
}
