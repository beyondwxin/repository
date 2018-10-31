package com.newgen.xj_app.mw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.newgen.UI.HorizontalListView;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyToast;
import com.newgen.UI.PullToRefreshView;
import com.newgen.UI.PullToRefreshView.OnFooterRefreshListener;
import com.newgen.UI.PullToRefreshView.OnHeaderRefreshListener;
import com.newgen.adapter.MPublicNewsAdapter;
import com.newgen.adapter.PublicNewsAdapter;
import com.newgen.domain.NewsPub;
import com.newgen.server.NewsServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.mw.MImgNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MLinkDetailActivity;
import com.newgen.xj_app.detail.mw.MNewsDetailActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class MMessageActivity extends Activity implements OnHeaderRefreshListener,
OnFooterRefreshListener{

	ImageView back;
	HorizontalListView listView;
	int startid = -1;
	int size =10;
	List<NewsPub> tempList = null;
	List<NewsPub> newsList = new ArrayList<NewsPub>();
	MPublicNewsAdapter adapter;
	boolean isFrist = true;
	Dialog dialog;
	boolean isNight = false;
	private long flushTime = 0;
	LoadDateTask task;
	MongolTextView message_title;
	private PullToRefreshView mPullDownScrollView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_m);
		
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mPullDownScrollView = (PullToRefreshView)findViewById(R.id.refresh_root);
		mPullDownScrollView.setEnablePullTorefresh(true);
		mPullDownScrollView.setEnablePullLoadMoreDataStatus(true);
		mPullDownScrollView.setOnHeaderRefreshListener(this);
		mPullDownScrollView.setOnFooterRefreshListener(this);
		listView = (HorizontalListView)findViewById(R.id.listView);
		message_title = (MongolTextView)findViewById(R.id.message_title);
		message_title.setText("ᠮᠡᠳᠡᠭᠡ");//我的消息
		adapter = new MPublicNewsAdapter(MMessageActivity.this, newsList,true);
		listView.setOnItemClickListener(new ListItemClick());
		listView.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
			flushTime = new Date().getTime();
			onHeaderRefresh(null);
			isFrist = false;
		}
	}
	

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		startid = -1;
		task = new LoadDateTask();
		task.execute();
	}  
	
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
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
			
			NewsPub news = newsList.get(position);
			Intent intent = null;
			if (news.getNewsPubExt().getInfotype() == 3) {//推广
				if (news.getNewsPubExt().getUrl() != null
						&& !news.getNewsPubExt().getUrl().equals("")) {
					intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(news.getNewsPubExt().getUrl());
					intent.setData(content_url);
				} else {
					intent = new Intent(MMessageActivity.this, MNewsDetailActivity.class);
				}
			}else
				switch (news.getNewsPubExt().getType()) {
				case PublicValue.NEWS_STYLE_WORD:
					intent = new Intent(MMessageActivity.this, MNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				case PublicValue.NEWS_STYLE_IMG:
					intent = new Intent(MMessageActivity.this,
							MImgNewsDetailActivity.class);
					break;
				case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
					intent = new Intent(MMessageActivity.this, MLinkDetailActivity.class);
					intent.putExtra("url", news.getNewsPubExt().getUrl());
					intent.putExtra("shareimg", news.getNewsPubExt()
							.getFaceimgpath()
							+ PublicValue.IMG_SIZE_M
							+ news.getNewsPubExt().getFaceimgname());
					break;
				default:
					intent = new Intent(MMessageActivity.this, MNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				}
			if (null != intent) {
				intent.putExtra("newsId", news.getId());
				intent.putExtra("title", news.getShorttitle());
				JCVideoPlayer.releaseAllVideos();
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
				tempList = server.getMessageList(10, startid);
				
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
					MyToast.showToast(MMessageActivity.this, "تېخىمۇ كۆپ سانلىق مەلۇمات يوق", 3);
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
//		listView.stopRefresh();
//		listView.stopLoadMore();
		mPullDownScrollView.onHeaderRefreshComplete();
		mPullDownScrollView.onFooterRefreshComplete();
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
