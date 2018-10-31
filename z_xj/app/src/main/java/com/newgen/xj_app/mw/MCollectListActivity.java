package com.newgen.xj_app.mw;

import java.util.ArrayList;
import java.util.List;

import cn.net.newgen.widget.dialog.ArtAlertDialog;

import com.newgen.UI.HorizontalListView;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyToast;
import com.newgen.UI.PullToRefreshView;
import com.newgen.UI.XListView;
import com.newgen.UI.PullToRefreshView.OnFooterRefreshListener;
import com.newgen.UI.PullToRefreshView.OnHeaderRefreshListener;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.CollectListAdapter;
import com.newgen.adapter.MCollectListAdapter;
import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SqlHleper;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.mw.MImgNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MLinkDetailActivity;
import com.newgen.xj_app.detail.mw.MNewsDetailActivity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

public class MCollectListActivity extends Activity implements OnHeaderRefreshListener,
OnFooterRefreshListener{
	
	ImageView back;
	HorizontalListView listView;
	int startPage = 1;
	int size =10;
	List<NewsMix> tempList = null;
	List<NewsMix> newsList = new ArrayList<NewsMix>();
	MCollectListAdapter adapter;
	boolean isFrist = true;
	Dialog dialog;
	SqlHleper hleper;
	boolean isNight = false;
	private PullToRefreshView mPullDownScrollView;
	MongolTextView collect_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect_list_m);
		
		hleper = new SqlHleper();
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
		collect_title = (MongolTextView)findViewById(R.id.collect_title);
		collect_title.setText("ᠬᠤᠷᠢᠶᠠᠨ ᠬᠠᠳᠠᠭᠠᠯᠠᠬᠤ");
		listView = (HorizontalListView)findViewById(R.id.listView);
		adapter = new MCollectListAdapter(MCollectListActivity.this, newsList,isNight);
		listView.setOnItemClickListener(new ListItemClick());
		listView.setOnItemLongClickListener(new itemLongClick());
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {	
			onHeaderRefresh(null);
			isFrist = false;
		}
	}
	
	
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		startPage = 1;
		initList();
	}  
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		initList();
	}
	
	private void initList() {
		// TODO Auto-generated method stub
		
		tempList = hleper.getNewsList(MCollectListActivity.this, startPage, size);
		
		if(tempList!=null){
			if(tempList.size()>0){
				if (startPage == 1) {// 刷新动作
					newsList.clear();
					adapter.notifyDataSetChanged();
				}
				newsList.addAll(tempList);
				adapter.notifyDataSetChanged();
				startPage ++;
			}else if(tempList.size()==0)
				MyToast.showToast(MCollectListActivity.this, "没有更多数据", 5);
		}else
			MyToast.showToast(MCollectListActivity.this, "没有更多数据", 5);
		
		mPullDownScrollView.onHeaderRefreshComplete();
		mPullDownScrollView.onFooterRefreshComplete();
	}

	
	class ListItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			NewsMix newsMix = newsList.get(position-1);
			Article article = newsMix.getArticle();
			NewsPub news = newsMix.getNewsPub();
			Intent intent = null;
			
			if(newsMix.getIsEpaper()==0){
				switch (news.getNewsPubExt().getType()) {
				case PublicValue.NEWS_STYLE_WORD:
					intent = new Intent(MCollectListActivity.this, MNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				case PublicValue.NEWS_STYLE_IMG:
					intent = new Intent(MCollectListActivity.this,
							MImgNewsDetailActivity.class);
					break;
				case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
					intent = new Intent(MCollectListActivity.this, MLinkDetailActivity.class);
					intent.putExtra("url", news.getNewsPubExt().getUrl());
					intent.putExtra("shareimg", news.getNewsPubExt().getFaceimgpath());
					break;
				default:
					intent = new Intent(MCollectListActivity.this, MNewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				}
				if (null != intent) {
					intent.putExtra("newsId", news.getId());
					intent.putExtra("title", news.getShorttitle());
					startActivity(intent);
				}
			}else{
//				intent = new Intent(MCollectListActivity.this,
//						EpaperArticleDetailActivity.class);
//				intent.putExtra("aid", article.getId());
//				intent.putExtra("paperName", "sgrb");
//				intent.putExtra("articleObject", article);
//				startActivity(intent);
			}
		}
	}
	
	class itemLongClick implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {
			// TODO Auto-generated method stub
			dialog = new ArtAlertDialog(MCollectListActivity.this, "确定删除这条收藏？", "提示", "确定", "取消", new ArtAlertDialog.OnArtClickListener() {
				
				@Override
				public void okButtonClick() {
					// TODO Auto-generated method stub
					
					if(newsList.get(position-1).getIsEpaper()==0)
						hleper.deleteCollectNews(newsList.get(position-1).getNewsPub().getId(),
								newsList.get(position-1).getIsEpaper(),MCollectListActivity.this);
					else
						hleper.deleteCollectNews(newsList.get(position-1).getArticle().getId(),
								newsList.get(position-1).getIsEpaper(),MCollectListActivity.this);
					
					newsList.remove(position-1);
					adapter.notifyDataSetChanged();
				}
				
				@Override
				public void cancelButtonClick() {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();
			return true;
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
