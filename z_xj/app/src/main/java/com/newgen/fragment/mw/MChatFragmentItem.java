package com.newgen.fragment.mw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.newgen.UI.FontTextView;
import com.newgen.UI.MyToast;
import com.newgen.UI.XListView;
import com.newgen.UI.XListView.IXListViewListener;
import com.newgen.adapter.ChatAdapter;
import com.newgen.domain.Member;
import com.newgen.domain.NewsReview;
import com.newgen.server.NewsCommentServer;
import com.newgen.server.NewsServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.user.LoginActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MChatFragmentItem extends Fragment implements IXListViewListener{
	
	int cid, sType;
	String cname;
	int index = 0;
	int startid = -1;
	XListView listView;
	List<NewsReview> newsList = new ArrayList<NewsReview>();
	List<NewsReview> tempList = null;
	ChatAdapter adapter;
	private int newsId, livestate;
	private String title, imag_url;
	private MHandler mHandler;
	FontTextView comment_layout;
	RelativeLayout comment_layout11;
	boolean isFrist = true;
	private long flushTime = 0;
	LoadDateTask task;
	Member umMember;
	
	public void setCategory(int cid, int sType, int index, String cname) {
		this.cid = cid;
		this.sType = sType;
		this.index = index;
		this.cname = cname;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.chatfragment_item_m, container, false);
		if (savedInstanceState != null) {
			this.cid = savedInstanceState.getInt("cid");
			this.sType = savedInstanceState.getInt("sType");
			this.index = savedInstanceState.getInt("index");
			this.cname = savedInstanceState.getString("cname");
		}
		
		adapter = new ChatAdapter(getActivity(), newsList, null);
		newsId = getArguments().getInt("newsid");
		livestate = getArguments().getInt("livestate");
		title = getArguments().getString("title");
		imag_url = getArguments().getString("ima_url");
		
		mHandler = new MHandler();
		comment_layout = (FontTextView) v.findViewById(R.id.comment_layout);
		listView = (XListView) v.findViewById(R.id.chat_fragemntlistView);
		comment_layout11 = (RelativeLayout) v
				.findViewById(R.id.comment_layout11);
		if (livestate == 2) {
			comment_layout11.setVisibility(View.GONE);
		} else {
			comment_layout11.setVisibility(View.VISIBLE);
		}
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);

		listView.setAdapter(adapter);
		comment_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// clickedReplyId = null;
				alertInputDialog();
			}
		});
		umMember = Tools.getUserInfo(getActivity());
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
			this.newsId = savedInstanceState.getInt("newsid");
			this.title = savedInstanceState.getString("title");
			this.imag_url = savedInstanceState.getString("ima_url");
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isFrist) {
//			getCacheData();// 获取缓存数据
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
	
	
	private class MHandler extends Handler {
		private final int ACTION_POST_COMMENT = 1;// 提交评论

		// private final int ACTION_POST_VOTE = 2;// 提交投票
		// private final int ACTION_SHOW_BIG_PIC = 3;// 展示大图

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_POST_COMMENT:
				boolean flag = msg.getData().getBoolean("flag");
				if (flag) {
					Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
		}
	}
	
	
	Dialog replyDailog;

	/***
	 * 弹出评论框
	 */
	private void alertInputDialog() {
		if (null == PublicValue.USER) {
			callLoginActivity();
			return;
		}
		replyDailog = new Dialog(getActivity());
		replyDailog.show();
		Window window = replyDailog.getWindow();
		window.setContentView(R.layout.reply_layout);
		window.setBackgroundDrawable(new ColorDrawable(0));
		final EditText editText = (EditText) window.findViewById(R.id.edit);
		TextView cancel = (TextView) window.findViewById(R.id.cancel);
		TextView save = (TextView) window.findViewById(R.id.save);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				replyDailog.cancel();
			}
		});

		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String commentStr = editText.getText().toString();
				if (null == commentStr || "".equals(commentStr)) {
					Toast.makeText(getActivity(), "请填写评论回内", Toast.LENGTH_SHORT)
							.show();
					return;
				} else if (commentStr.length() > 200) {
					Toast.makeText(getActivity(), "评论字数应小于200字",
							Toast.LENGTH_SHORT).show();
					return;
				}

				Member umMember = Tools.getUserInfo(getActivity());
				if (umMember == null || umMember.equals("")) {
					LiveCommentThread liveCommentThread = new LiveCommentThread(
							editText.getText().toString(), umMember
									.getNickname(), 0, newsId);
					liveCommentThread.start();
				} else {
					LiveCommentThread liveCommentThread = new LiveCommentThread(
							editText.getText().toString(), umMember
									.getNickname(), umMember.getId(), newsId);
					liveCommentThread.start();
				}
				replyDailog.cancel();
			}
		});
	}
	
	/***
	 * 提交评论
	 * 
	 * 
	 */
	class LiveCommentThread extends Thread {
		private Integer userId, liveId;
		private String body, userName;

		public LiveCommentThread(String body, String userName, int userId,
				int liveId) {
			this.userId = userId;
			this.liveId = liveId;
			this.body = body;
			this.userName = userName;
		}

		@Override
		public void run() {
			NewsCommentServer server = new NewsCommentServer();
			boolean flag = server.liveComment(body, userName, userId, liveId);
			Message msg = new Message();
			msg.what = mHandler.ACTION_POST_COMMENT;
			Bundle data = new Bundle();
			data.putBoolean("flag", flag);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
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
					tempList = (server.getChatNewsList(newsId, 10, startid, 0,
							PublicValue.HARDID, Tools.getVision(getActivity())));
				} else {
					tempList = (server.getChatNewsList(newsId, 10, startid,
							umMember.getId(), PublicValue.HARDID,
							Tools.getVision(getActivity())));
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
//						SharedPreferencesTools.setValue(getActivity(), cate
//								+ newsId, jStr,
//								SharedPreferencesTools.CACHEDATA);
//					}
					startid = newsList.get(newsList.size() - 1).getId();
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
	
	public void stopLoadOrRefresh() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}
	

	/***
	 * 呼出登录界面
	 */
	private void callLoginActivity() {
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("cid", cid);
		outState.putInt("sType", sType);
		outState.putInt("index", index);
		outState.putString("cname", cname);
		outState.putInt("newsid", newsId);
		outState.putString("title", title);
		outState.putString("ima_url", imag_url);
	}

}
