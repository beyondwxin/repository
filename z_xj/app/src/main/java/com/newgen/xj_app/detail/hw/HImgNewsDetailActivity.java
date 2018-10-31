package com.newgen.xj_app.detail.hw;

import java.lang.ref.WeakReference;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.newgen.adapter.ImgNewsDetailAdapter;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.server.NewsCommentServer;
import com.newgen.server.NewsServer;
import com.newgen.share.ShareTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SqlHleper;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.user.LoginActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HImgNewsDetailActivity extends Activity {
	
	NewsPub news;
	RelativeLayout footer_rlayout;
	ImageView backButton;
	ImageView commentButton;//进入评论列表
	ImageView shareButton;//分享
	ImageView collectButton;//收藏
	ImageView thumb_up;//点赞
	LinearLayout reply_linear;//评论新闻
	ViewPager viewPage;
	TextView comment_num;
	
	NewsMix newsMix;
	Article article;
	
	private Integer clickedReplyId = null;
	Dialog replyDailog;
	int newsID;
	
	int isEpaper = 0;// 电子报的详情是1  ，普通是0
	
	private boolean isSupported = false; // 新闻点赞
	private boolean isCollected = false; // 新闻收藏
	
	private MHandler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_news_detail_h);
		
		mHandler = new MHandler(this);
		
		if (savedInstanceState != null) {
			newsID = savedInstanceState.getInt("newsId");
			PublicValue.USER = Tools.getUserInfo(this);
		} else
			newsID = getIntent().getIntExtra("newsId", 0);
		
		Tools.getScreenWidth(this);
		
		initView();
		new GetData().execute();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		PublicValue.USER = Tools.getUserInfo(this);
		PublicValue.HARDID = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.KEY_DEVICE_ID,
				SharedPreferencesTools.KEY_DEVICE_ID);
		backButton.setOnClickListener(new OnClick());
	}

	private void initView() {
		// TODO Auto-generated method stub
		backButton = (ImageView) findViewById(R.id.back);
		shareButton = (ImageView) findViewById(R.id.share);
		reply_linear = (LinearLayout)findViewById(R.id.reply);
		commentButton = (ImageView) findViewById(R.id.comment_news);
		collectButton = (ImageView) findViewById(R.id.collect);
		thumb_up = (ImageView) findViewById(R.id.thumb_up);
		footer_rlayout = (RelativeLayout)findViewById(R.id.footer_rlayout);
		comment_num = (TextView)findViewById(R.id.comment_num);
		viewPage = (ViewPager) findViewById(R.id.pager);
	}
	
	
	
	class OnClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == backButton) {
				finish();
			}else if(v==reply_linear){
				clickedReplyId = null;
				alertInputDialog();
			}else if(v ==shareButton){
				ShareTools share = new ShareTools();
				share.showShare(false, null, HImgNewsDetailActivity.this, news);
			}else if(v==commentButton){
				Intent intent = new Intent(HImgNewsDetailActivity.this,
						HNewsReviewActivity.class);
				intent.putExtra("newsId", newsID);
				intent.putExtra("title", news.getShorttitle());
				intent.putExtra("isEpaper", false);
				startActivity(intent);
			}else if(v==collectButton){
				SqlHleper hleper = new SqlHleper();
				if(isCollected){
					int result = hleper.deleteCollectNews(news.getId(),isEpaper,HImgNewsDetailActivity.this);
					if(result>0){
						isCollected = false;
						collectButton.setBackgroundResource(R.drawable.collect);
						Toast.makeText(getApplicationContext(), "取消收藏",
								Toast.LENGTH_SHORT).show();
					}else
						Toast.makeText(getApplicationContext(), "取消失败",
								Toast.LENGTH_SHORT).show();
				}else{
					isCollected = true; 
					collectButton.setBackgroundResource(R.drawable.collected);
					hleper.addCollect(newsMix, HImgNewsDetailActivity.this);
					Toast.makeText(getApplicationContext(), "收藏成功",
							Toast.LENGTH_SHORT).show();
				}
			}else if(v==thumb_up){
				SqlHleper hleper = new SqlHleper();
				if(isSupported){
					int result = hleper.deleteSupportNews(news.getId(), HImgNewsDetailActivity.this);
					if(result>0){
						isSupported = false;
						thumb_up.setBackgroundResource(R.drawable.thumb_up);
						Toast.makeText(getApplicationContext(), "取消点赞",
								Toast.LENGTH_SHORT).show();
						Log.e("1111", hleper.getSupportNewsList(HImgNewsDetailActivity.this, 1, 10)+"");
					}else
						Toast.makeText(getApplicationContext(), "取消失败",
								Toast.LENGTH_SHORT).show();
				}else{
					isSupported = true; 
					thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
					hleper.addSupport(news, HImgNewsDetailActivity.this);
					Toast.makeText(getApplicationContext(), "点赞成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	
	
	private class GetData extends AsyncTask<Object, Object, Integer> {

		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			
			SqlHleper hleper = new SqlHleper();
			isSupported = hleper.findSupportByNewsId(newsID, HImgNewsDetailActivity.this);
			isCollected = hleper.findCollectByNewsId(newsID, isEpaper, HImgNewsDetailActivity.this);
			
			NewsServer server = new NewsServer();
			String result = server.getNewsDetail(newsID, 0, "");
			try {
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					news = new Gson().fromJson(json.getString("data"),
							NewsPub.class);
					return 1;
				} else
					return 0;
			} catch (Exception e) {
				return 0;
			}
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 1:
				newsMix = new NewsMix();
				article = new Article();
				newsMix.setIsEpaper(isEpaper);
				newsMix.setArticle(article);
				newsMix.setNewsPub(news);
				
				if (news.getListpic() != null && news.getListpic().size() > 0) {
					List<NewsFile> imgList = news.getListpic();
					ImgNewsDetailAdapter adapter = new ImgNewsDetailAdapter(
							imgList, news.getTitle(),
							HImgNewsDetailActivity.this);
					viewPage.setAdapter(adapter);
					viewPage.setCurrentItem(0);

					reply_linear.setOnClickListener(new OnClick());
					collectButton.setOnClickListener(new OnClick());
					commentButton.setOnClickListener(new OnClick());
					thumb_up.setOnClickListener(new OnClick());
					shareButton.setOnClickListener(new OnClick());
					
					footer_rlayout.setVisibility(View.VISIBLE);
					
					if(isSupported)
						 thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
					 else 
						thumb_up.setBackgroundResource(R.drawable.thumb_up);
					 
					 if(isCollected)
						 collectButton.setBackgroundResource(R.drawable.collected);
					 else 
						 collectButton.setBackgroundResource(R.drawable.collect);
					 
					 if (news.getNewsPubExt().getReviewcount() > 0&&news.getNewsPubExt().getReviewcount() < 99) {
						comment_num.setVisibility(View.VISIBLE);
						comment_num.setText(news.getNewsPubExt().getReviewcount()
								+ "");
					 }else if(news.getNewsPubExt().getReviewcount() > 99){
						comment_num.setVisibility(View.VISIBLE);
						comment_num.setText("99+");
					 }
					
				}
				break;
			default:
				Toast.makeText(getApplicationContext(), "获取新闻失败",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
		
	}
	
	
	/***
	 * 弹出评论框
	 */
	private void alertInputDialog() {
		if (null == PublicValue.USER) {
			callLoginActivity();
			return;
		}
		replyDailog = new Dialog(this);
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
					Toast.makeText(getApplicationContext(), "请填写评论，不能为空哦",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (commentStr.length() > 200) {
					Toast.makeText(getApplicationContext(), "评论字数应小于200字",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (PublicValue.USER != null) {
					PostCommentThread postCommentThread = new PostCommentThread(
							HImgNewsDetailActivity.this.newsID, clickedReplyId,
							PublicValue.USER.getId(), editText.getText()
									.toString(), "");
					postCommentThread.start();
				}
				replyDailog.cancel();
			}
		});
	}
	
	/***
	 * 呼出登录界面
	 */
	private void callLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, 0);
	}
	
	
	/***
	 * 提交评论
	 * 
	 * @author yilang
	 * 
	 */
	class PostCommentThread extends Thread {
		private Integer newsId, memberid, replyId;
		private String txt, uid;

		public PostCommentThread(Integer newsId, Integer replyId,
				Integer memberId, String txt, String uid) {
			this.memberid = memberId;
			this.newsId = newsId;
			this.replyId = replyId;
			this.txt = txt;
			this.uid = uid;
		}

		@Override
		public void run() {
			NewsCommentServer server = new NewsCommentServer();
			boolean flag = server.postComment(txt, newsId, memberid, uid,
					replyId);
			Message msg = new Message();
			msg.what = mHandler.ACTION_POST_COMMENT;
			Bundle data = new Bundle();
			data.putBoolean("flag", flag);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}
	
	private static class MHandler extends Handler {
		
		private final int ACTION_POST_COMMENT = 1;// 提交评论
	
		WeakReference<HImgNewsDetailActivity> mActivity;
		
		MHandler(HImgNewsDetailActivity activity) {
			mActivity = new WeakReference<HImgNewsDetailActivity>(activity);
		}
		
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_POST_COMMENT:
				boolean flag = msg.getData().getBoolean("flag");
				if (flag)
					Toast.makeText(mActivity.get(), "评论成功",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(mActivity.get(), "评论失败",
							Toast.LENGTH_SHORT).show();
				break;
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
