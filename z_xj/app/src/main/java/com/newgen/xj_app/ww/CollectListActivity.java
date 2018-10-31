package com.newgen.xj_app.ww;

import java.util.ArrayList;
import java.util.List;

import cn.net.newgen.widget.dialog.ArtAlertDialog;

import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.CollectListAdapter;
import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SqlHleper;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;
import com.newgen.xj_app.detail.ww.EpaperArticleDetailActivity;
import com.newgen.xj_app.detail.ww.ImgNewsDetailActivity;
import com.newgen.xj_app.detail.ww.LinkDetailActivity;
import com.newgen.xj_app.detail.ww.NewsDetailActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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

public class CollectListActivity extends Activity implements IXListViewListener{
	
	ImageView back;
	XListView listView;
	int startPage = 1;
	int size =10;
	List<NewsMix> tempList = null;
	List<NewsMix> newsList = new ArrayList<NewsMix>();
	CollectListAdapter adapter;
	boolean isFrist = true;
	Dialog dialog;
	SqlHleper hleper;
	boolean isNight = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect_list);
		
		hleper = new SqlHleper();
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		listView = (XListView)findViewById(R.id.listView);
		adapter = new CollectListAdapter(CollectListActivity.this, newsList,isNight);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(new ListItemClick());
		listView.setOnItemLongClickListener(new itemLongClick());
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isFrist) {	
			onRefresh();
		}
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		startPage = 1;
		initList();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		initList();
	}
	
	
	private void initList() {
		// TODO Auto-generated method stub
		
		tempList = hleper.getNewsList(CollectListActivity.this, startPage, size);
		
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
				MyToast.showToast(CollectListActivity.this, "没有更多数据", 5);
		}else
			MyToast.showToast(CollectListActivity.this, "没有更多数据", 5);
		
		listView.stopRefresh();
		listView.stopLoadMore();
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
					intent = new Intent(CollectListActivity.this, NewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				case PublicValue.NEWS_STYLE_IMG:
					intent = new Intent(CollectListActivity.this,
							ImgNewsDetailActivity.class);
					break;
				case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
					intent = new Intent(CollectListActivity.this, LinkDetailActivity.class);
					intent.putExtra("url", news.getNewsPubExt().getUrl());
					intent.putExtra("shareimg", news.getNewsPubExt().getFaceimgpath());
					break;
				default:
					intent = new Intent(CollectListActivity.this, NewsDetailActivity.class);
					intent.putExtra("newsObject", news);
					break;
				}
				if (null != intent) {
					intent.putExtra("newsId", news.getId());
					intent.putExtra("title", news.getShorttitle());
					startActivity(intent);
				}
			}else{
				intent = new Intent(CollectListActivity.this,
						EpaperArticleDetailActivity.class);
				intent.putExtra("aid", article.getId());
				intent.putExtra("paperName", "sgrb");
				intent.putExtra("articleObject", article);
				startActivity(intent);
			}
		}
	}
	
	class itemLongClick implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {
			// TODO Auto-generated method stub
			dialog = new ArtAlertDialog(CollectListActivity.this, "确定删除这条收藏？", "提示", "确定", "取消", new ArtAlertDialog.OnArtClickListener() {
				
				@Override
				public void okButtonClick() {
					// TODO Auto-generated method stub
					
					if(newsList.get(position-1).getIsEpaper()==0)
						hleper.deleteCollectNews(newsList.get(position-1).getNewsPub().getId(),
								newsList.get(position-1).getIsEpaper(),CollectListActivity.this);
					else
						hleper.deleteCollectNews(newsList.get(position-1).getArticle().getId(),
								newsList.get(position-1).getIsEpaper(),CollectListActivity.this);
					
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
