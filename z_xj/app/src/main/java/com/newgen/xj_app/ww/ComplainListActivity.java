package com.newgen.xj_app.ww;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.ComplainListAdapter;
import com.newgen.domain.Complain;
import com.newgen.server.UserServer;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.ww.ComplainDetailActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class ComplainListActivity extends Activity implements IXListViewListener{
	
	ImageView back,add_complain;
	XListView listView;
	int startid = 1;
	int size =10;
	List<Complain> tempList = null;
	List<Complain> newsList = new ArrayList<Complain>();
	ComplainListAdapter adapter;
	boolean isFrist = true;
	Dialog dialog;
	private long flushTime = 0;
	LoadDateTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain_list);
		
		back = (ImageView) findViewById(R.id.back);
		add_complain = (ImageView)findViewById(R.id.add_complain);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		add_complain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ComplainListActivity.this, 
						AddComplainActivity.class);
				startActivity(intent);
			}
		});
		
		listView = (XListView)findViewById(R.id.listView);
		adapter = new ComplainListAdapter(ComplainListActivity.this, newsList);
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
	
	
	private class LoadDateTask extends AsyncTask<Object, Integer, Integer> {
		
		@Override
		protected void onPreExecute() {
			listView.setEnabled(false);
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			int ret = -1;
			try {
				UserServer server = new UserServer();
				tempList = server.getComplainList(10, startid);
				
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
					MyToast.showToast(ComplainListActivity.this, "", 3);
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
	
	
	class ListItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			Log.e("position", position+"");
			Intent intent = new Intent(ComplainListActivity.this, 
					ComplainDetailActivity.class);
			intent.putExtra("Id", newsList.get(position-1).getId());
			startActivity(intent);
		}
		
	}
	
	
}
