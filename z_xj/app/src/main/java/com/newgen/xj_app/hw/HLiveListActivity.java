package com.newgen.xj_app.hw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.LiveNewsListAdapter;
import com.newgen.domain.Image;
import com.newgen.domain.Member;
import com.newgen.domain.NewsPub;
import com.newgen.server.NewsServer;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.layout;
import com.newgen.xj_app.detail.ww.LiveDetailActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class HLiveListActivity extends Activity implements IXListViewListener{

	XListView listView;
	int startid = -1;
	List<NewsPub> newsList = new ArrayList<NewsPub>();
	Dialog dialog;
	Thread loadDataThread;
	int cid, sType;
	String cname;
	List<Image> runImages;
	NewsPub runImageNews;
	Handler handler = null;
	LiveNewsListAdapter adapter;
	boolean isFrist = true;
	int index = 0;
	boolean isLoading = false;
	List<NewsPub> tempList = null;
	LoadDateTask task;
	private long flushTime = 0;
	private final String cate = "cate_";
	Member uMember;
	Button back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_list_h);
		initView();
	}

	private void initView(){
		
		back=(Button) findViewById(R.id.back);
		adapter = new LiveNewsListAdapter(HLiveListActivity.this, newsList);
		listView = (XListView) findViewById(R.id.live_listView);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new ListItemClick());
		listView.setAdapter(adapter);
		uMember = Tools.getUserInfo(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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
	
	
	
	class ListItemClick implements AdapterView.OnItemClickListener {
		@SuppressWarnings("static-access")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			NewsPub news = newsList.get(position - 1);
			
			if (news.getLivestate() == 0) {
				String publishtime = news.getPublishTime();
				Toast.makeText(HLiveListActivity.this,
						"直播开始的时间为：" + publishtime, Toast.LENGTH_SHORT).show();
			} else if (news.getLivestate() == 1) {
				Intent intent = new Intent(HLiveListActivity.this,
						LiveDetailActivity.class);
				intent.putExtra("title", news.getTitle());
				intent.putExtra("liveid", news.getId());
				intent.putExtra("createtime", "");
				startActivity(intent);
			} else {
				Intent intent = new Intent(HLiveListActivity.this,
						LiveDetailActivity.class);
				intent.putExtra("title", news.getTitle());
				intent.putExtra("liveid", news.getId());
				intent.putExtra("createtime", "");
				intent.putExtra("livestate", 2);
				startActivity(intent);
				// Toast.makeText(getActivity(), "直播已结束",
				// Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("cid", cid);
		outState.putInt("sType", sType);
		outState.putInt("index", index);
		outState.putString("cname", cname);
	}
	
	
	private class LoadDateTask extends AsyncTask<Object, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			listView.setEnabled(false);
		}

		@Override
		protected Integer doInBackground(Object... arg0) {
			int ret = -1;
			try {
				String mac = Tools.getMac(HLiveListActivity.this);
				String vision = Tools.getVision(HLiveListActivity.this);
				NewsServer server = new NewsServer();
				tempList = (server.getLivenewsList(10, startid, 0, mac, vision));
				if (uMember == null || uMember.equals("")) {
					tempList = (server.getLivenewsList(10, startid, 0, mac,
							vision));
				} else {
					Integer id = uMember.getId();
					tempList = (server.getLivenewsList(10, startid, id, mac,
							vision));
				}
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
					Gson gson = new Gson();
//					if (startid <= 0) {// 当刷新数据是，做数据缓存
//						String jStr = gson.toJson(newsList);
//						SharedPreferencesTools.setValue(LiveListActivity.this,
//								cate + cid, jStr,
//								SharedPreferencesTools.CACHEDATA);
//					}
					startid = newsList.get(newsList.size() - 1).getId();
					break;
				case -1:
					break;
				case 0:
					MyToast.showToast(HLiveListActivity.this,
						"没有更多数据", 3);
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

	public void stopLoadOrRefresh() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (null != task && !task.isCancelled()) {
				task.cancel(true);
			}
		} catch (Exception e) {
			Tools.debugLog(e.getMessage());
		}
	}
	
	
}
