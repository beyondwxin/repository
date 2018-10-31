package com.newgen.xj_app.hw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newgen.UI.MyDialog;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.SearchResultdapter;
import com.newgen.domain.NewsPub;
import com.newgen.server.NewsServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.hw.HImgNewsDetailActivity;
import com.newgen.xj_app.detail.hw.HLinkDetailActivity;
import com.newgen.xj_app.detail.hw.HNewsDetailActivity;


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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class HSearchResultActivity extends Activity implements IXListViewListener{

	private ImageView back;
	private XListView mListView;
	
	Dialog dialog = null;
	
	private long flushTime;
	private boolean isFrist = true;
	private String keyValue;
	private int startid = -1, count = 10;	
	private List<NewsPub> tempList= new ArrayList<NewsPub>();
	private List<NewsPub> mList = new ArrayList<NewsPub>();
	private SearchResultdapter mAdapter;
	private NewsServer newsServer = new NewsServer();
	boolean isNight = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result_h);
		
		keyValue = getIntent().getExtras().getString("key");
		back = (ImageView) findViewById(R.id.back);
		mListView = (XListView) findViewById(R.id.search_result_list);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		
		mListView.setOnItemClickListener(new itemClick());
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		mAdapter = new SearchResultdapter(HSearchResultActivity.this,mList,keyValue,isNight);
		mListView.setAdapter(mAdapter);
	}

	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
			try {
				dialog = MyDialog.createLoadingDialog(HSearchResultActivity.this,
						"数据加载中..");
				dialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			flushTime = new Date().getTime();
			onRefresh();
		}
		isFrist = false;
	}

	
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		startid = -1;
		long currentTime = new Date().getTime();
		String timeStr = Tools.getTimeInterval(flushTime, currentTime);
		mListView.setRefreshTime(timeStr);
		new LoadData().execute(100);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new LoadData().execute(100);
	}
	
	
	private class LoadData extends AsyncTask<Object, Integer, Integer> {

		
		@Override
		protected void onPreExecute() {
			mListView.setEnabled(false);
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			int ret = 0;
			tempList = newsServer.newsSearch(keyValue, startid, count);
			if (null == tempList)
				ret = -1;
			else if (tempList.size() <= 0)
				ret = 0;
			else
				ret = 1;
			return ret;
		}
		
		
		
		@Override
		protected void onPostExecute(Integer result) {
			if (null != dialog)
				dialog.cancel();
			switch (result) {
			case -1:
				Toast.makeText(getApplicationContext(), R.string.getDataFial,
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(),"没有更多数据",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				mListView.setVisibility(View.VISIBLE);
				if (startid <= 0) {
					mList.clear();
					mAdapter.notifyDataSetChanged();
				}
				mList.addAll(tempList);
				mAdapter.notifyDataSetChanged();
				break;
			}
			if (null != mList && mList.size() > 0)
				startid = mList.get(mList.size() - 1).getId();
			stopLoad();
		}
		
	}
	
	public void stopLoad() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setEnabled(true);
	}
	
	class itemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			NewsPub news = mList.get(position - 1);
			Intent intent = null;
			if (news.getNewsPubExt().getInfotype() == 3) {//推广
				if (news.getNewsPubExt().getUrl() != null
						&& !news.getNewsPubExt().getUrl().equals("")) {
					intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(news.getNewsPubExt().getUrl());
					intent.setData(content_url);
				} else {
					intent = new Intent(HSearchResultActivity.this, HNewsDetailActivity.class);
				}
			}else
				switch (news.getNewsPubExt().getType()) {
				case PublicValue.NEWS_STYLE_WORD:
					intent = new Intent(HSearchResultActivity.this, HNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				case PublicValue.NEWS_STYLE_IMG:
					intent = new Intent(HSearchResultActivity.this,
							HImgNewsDetailActivity.class);
					break;
				case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
					intent = new Intent(HSearchResultActivity.this, HLinkDetailActivity.class);
					intent.putExtra("url", news.getNewsPubExt().getUrl());
					intent.putExtra("shareimg", news.getNewsPubExt()
							.getFaceimgpath()
							+ PublicValue.IMG_SIZE_M
							+ news.getNewsPubExt().getFaceimgname());
					break;
				default:
					intent = new Intent(HSearchResultActivity.this, HNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				}
			if (null != intent) {
				intent.putExtra("newsId", news.getId());
				intent.putExtra("title", news.getShorttitle());
				startActivity(intent);
			}
			
		}
		
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
