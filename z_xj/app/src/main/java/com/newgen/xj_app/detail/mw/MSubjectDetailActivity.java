package com.newgen.xj_app.detail.mw;

import java.util.ArrayList;
import java.util.List;

import com.newgen.UI.HorizontalListView;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.PullToRefreshView;
import com.newgen.UI.PullToRefreshView.OnFooterRefreshListener;
import com.newgen.UI.PullToRefreshView.OnHeaderRefreshListener;
import com.newgen.adapter.MPublicNewsAdapter;
import com.newgen.domain.NewsPub;
import com.newgen.domain.NewsPubExt;
import com.newgen.server.NewsServer;
import com.newgen.share.ShareTools;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MSubjectDetailActivity extends Activity implements OnHeaderRefreshListener,
OnFooterRefreshListener{
	
	private String subjectFaceImage;// 专题封面图
	private String subjectSummary;// 专题摘要
	private String titleValue;
	private MPublicNewsAdapter adapter = null;
	
	private boolean isFrist = true;
	private Button backBtn, share;
	private PullToRefreshView mPullDownScrollView;
	private MongolTextView title;
	private HorizontalListView mListView;
	private int sid, startid = -1, count = 10;
	private List<NewsPub> mList = new ArrayList<NewsPub>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject_detail_m);
		
		if (savedInstanceState != null) {
			sid = savedInstanceState.getInt("newsId");
			titleValue = savedInstanceState.getString("title");
			subjectFaceImage = savedInstanceState.getString("faceImage");
			subjectSummary = savedInstanceState.getString("summary");
		} else {
			sid = getIntent().getExtras().getInt("newsId", 0);
			titleValue = getIntent().getExtras().getString("title");
			subjectFaceImage = getIntent().getExtras().getString("faceImage");
			subjectSummary = getIntent().getExtras().getString("summary");
		}
		
		backBtn = (Button) findViewById(R.id.back);
		share = (Button) findViewById(R.id.share);
		title = (MongolTextView) findViewById(R.id.title);
		title.setText(titleValue);
		mListView = (HorizontalListView) findViewById(R.id.listView);
		mPullDownScrollView = (PullToRefreshView)findViewById(R.id.refresh_root);
		mPullDownScrollView.setEnablePullTorefresh(true);
		mPullDownScrollView.setEnablePullLoadMoreDataStatus(true);
		mPullDownScrollView.setOnHeaderRefreshListener(this);
		mPullDownScrollView.setOnFooterRefreshListener(this);
		
		adapter = new MPublicNewsAdapter(MSubjectDetailActivity.this, mList,true);
		// mAdapter = new Adapter();
		mListView.setAdapter(adapter);
		this.initEvent();
		
	}
	
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		new LoadData().execute(100);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		startid = -1;
		new LoadData().execute(100);
	}

	private void initEvent() {
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		/**
		 * 专题分享
		 */
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ShareTools share = new ShareTools();

				share.ShareSubject(false, null, MSubjectDetailActivity.this,
						sid, titleValue, subjectFaceImage);

			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					NewsPub news = mList.get(position);
					if (news.getNewsPubExt().getType() < 0)
						return;
					NewsPubExt ext = news.getNewsPubExt();
					Intent intent = null;
					if (ext.getInfotype() == PublicValue.NEWS_TAG_SUBJECT) {// 当点击新闻为专题时
						intent = new Intent(MSubjectDetailActivity.this,
								MSubjectDetailActivity.class);
						intent.putExtra("title", news.getTitle());
					} else if (ext.getInfotype() == PublicValue.NEWS_TAG_LIVE) {// 当点击新闻为直播时
						intent = new Intent(MSubjectDetailActivity.this,
								MLiveDetailActivity.class);
						intent.putExtra("title", news.getTitle());
						intent.putExtra("liveid", news.getNewsPubExt().getLiveId());
						intent.putExtra("createtime", "");
					}  else if (ext.getType() == PublicValue.NEWS_STYLE_IMG) {// 当点击新闻为图片新闻时
						intent = new Intent(MSubjectDetailActivity.this,
								MImgNewsDetailActivity.class);
					} else if (ext.getType() == PublicValue.NEWS_STYLE_VIDEO) {// 当点击新闻为视频新闻时
						intent = new Intent(MSubjectDetailActivity.this,
								MNewsDetailActivity.class);
					}else if (ext.getType() == PublicValue.NEWS_STYLE_LINK) {// 当点击新闻为超链
						intent = new Intent(MSubjectDetailActivity.this,
								MLinkDetailActivity.class);
						intent.putExtra("title", news.getTitle());
						intent.putExtra("url", news.getNewsPubExt().getUrl());
						intent.putExtra("shareimg", news.getNewsPubExt()
								.getFaceimgpath()
								+ PublicValue.IMG_SIZE_M
								+ news.getNewsPubExt().getFaceimgname());
						intent.putExtra("isshow", 1);
					} else {// 为不知道类型时
						intent = new Intent(MSubjectDetailActivity.this,
								MNewsDetailActivity.class);
					}
					intent.putExtra("newsId", news.getId());
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
			onHeaderRefresh(null);
		}
		isFrist = false;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("sid", sid);
		outState.putString("title", titleValue);
		outState.putString("faceImage", subjectFaceImage);
		outState.putString("summary", subjectSummary);
	}
	
	private class LoadData extends AsyncTask<Object, Integer, Integer> {
		private List<NewsPub> tempList;

		@Override
		protected void onPreExecute() {
			mListView.setEnabled(false);
		}	
		
		@Override
		protected Integer doInBackground(Object... params) {
			// TODO Auto-generated method stub
			int ret = 0;
			tempList = new NewsServer().getNewsListBySubjectId(sid, count,
					startid);
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
			switch (result) {
			case -1:
				Toast.makeText(getApplicationContext(), R.string.getDataFial,
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				Toast.makeText(getApplicationContext(), "没有更多数据",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				if (startid <= 0) {
					mList.clear();
					adapter.notifyDataSetChanged();
//					NewsPub news = new NewsPub();
//					news.getNewsPubExt().setFaceimgname("");
//					news.getNewsPubExt().setFaceimgpath(subjectFaceImage);
//					news.getNewsPubExt().setType(-3);
//					news.setBody(subjectSummary);
//					mList.add(news);
				}
				mList.addAll(tempList);
				adapter.notifyDataSetChanged();
				break;
			}
			if (null != mList && mList.size() > 0)
				startid = mList.get(mList.size() - 1).getId();
			stopLoad();
			mListView.setEnabled(true);
		}
	}
	
	public void stopLoad() {
		mPullDownScrollView.onHeaderRefreshComplete();
		mPullDownScrollView.onFooterRefreshComplete();
	}

	
	
}
