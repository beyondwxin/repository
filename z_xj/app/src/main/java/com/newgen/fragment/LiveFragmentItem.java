package com.newgen.fragment;

import java.util.ArrayList;
import java.util.List;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.LiveAdapter;
import com.newgen.domain.Image;
import com.newgen.domain.Member;
import com.newgen.domain.NewsPub;
import com.newgen.server.NewsServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

public class LiveFragmentItem extends Fragment implements IXListViewListener{
	
	XListView listView;
	int startid = -1;
	List<NewsPub> newsList = new ArrayList<NewsPub>();
	Dialog dialog;
	int cid, sType;
	String cname;
	List<Image> runImages;
	NewsPub runImageNews;
	Handler handler = null;
	LiveAdapter adapter;
	boolean isFrist = true;
	int index = 0;
	boolean isLoading = false;
	boolean zan = false;
	List<NewsPub> tempList = null;
	LoadDateTask task;
	private long flushTime = 0;
	private final String cate = "cate_";
	int liveid;
	Member umMember;
//	Callback callback;
	
	public void setCategory(int cid, int sType, int index, String cname) {
		this.cid = cid;
		this.sType = sType;
		this.index = index;
		this.cname = cname;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_live_item, container,
				false);
		
		liveid=getArguments().getInt("newsid");
		
		adapter = new LiveAdapter(getActivity(), newsList);
		
		listView = (XListView) v.findViewById(R.id.live_fragemntlistView);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new ListItemClick());
		umMember=Tools.getUserInfo(getActivity());
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			this.cid = savedInstanceState.getInt("cid");
			this.sType = savedInstanceState.getInt("sType");
			this.index = savedInstanceState.getInt("index");
			this.cname = savedInstanceState.getString("cname");
			this.liveid = savedInstanceState.getInt("newsid");
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
//			getCacheData();// 获取缓存数据
			onRefresh();
			isFrist = false;
		}
	}

	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		startid = -1;
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
			int ret = -1;
			try {
				NewsServer server = new NewsServer();
				if (umMember == null || umMember.equals("")) {
					tempList = (server.getLiveDetails(liveid, 10, startid,0,PublicValue.HARDID,Tools.getVision(getActivity())));
				}else {
					tempList = (server.getLiveDetails(liveid, 10, startid,umMember.getId(),PublicValue.HARDID,Tools.getVision(getActivity())));
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
//					Gson gson = new Gson();
//					if (startid <= 0) {// 当刷新数据是，做数据缓存
//						String jStr = gson.toJson(newsList);
//						SharedPreferencesTools.setValue(getActivity(), cate
//								+ cid, jStr, SharedPreferencesTools.CACHEDATA);
//					}
//					startid = newsList.get(newsList.size() - 1).getId();
					break;
				case -1:
					break;
				case 0:
					MyToast.showToast(getActivity(), "没有更多数据", 3);
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
	
	
	public void stopLoadOrRefresh() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("cid", cid);
		outState.putInt("sType", sType);
		outState.putInt("index", index);
		outState.putString("cname", cname);
		outState.putInt("newsid", liveid);
	}
	

}
