package com.newgen.xj_app.detail.ww;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.newgen.UI.MyDialog;
import com.newgen.base.BaseActivity;
import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.server.NewsCommentServer;
import com.newgen.server.PaperServer;
import com.newgen.share.ShareTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SqlHleper;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.user.LoginActivity;
import com.newgen.xj_app.ww.ShowImageActivity;

public class EpaperArticleDetailActivity extends BaseActivity{
	private int aid;// 数字报文章ID
	private String articleJsonData;// 数字报文章数据
	private String paperName;// 报纸简称

	private WebView webview;
	private ImageView backBtn;
	private Dialog dialog;
	private MHandler mHandler;
	private ImageView shareBtn;
	
	private ImageView comment_news,collect,thumb_up;
	private TextView comment_num;
	private LinearLayout reply;
	
	private int fontSize = 1;// 中号字

	private Article article,temparticle;
	NewsPub news;
	NewsMix newsMix;
	
	int isEpaper = 1;// 电子报的详情是1  ，普通是0
	
	Dialog loadDialog = null;
	Dialog replyDailog;
	
	RelativeLayout footer_rlayout;
	
	private boolean isSupported = false; // 新闻点赞
	private boolean isCollected = false; // 新闻收藏
	
	private SpeechSynthesizer mSpeechSynthesizer;
	private SpeechSynthesizeBag speechSynthesizeBag;
	private String mSampleDirPath;
	private static final String SAMPLE_DIR_NAME = "hljrb_TTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private String news_text;
    boolean isNight = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_article_detail);
		
		if (savedInstanceState != null) {
			aid = savedInstanceState.getInt("aid");
			articleJsonData = savedInstanceState.getString("data");
			paperName = savedInstanceState.getString("paperName");
		} else {
			Bundle datas = getIntent().getExtras();
			aid = datas.getInt("aid");
			paperName = datas.getString("paperName");
		}
		
		
		
		mHandler = new MHandler(this);
		this.initSysoutFontSize();
		initialEnv();
		initSDK();
		initView();
//		initWebView();
		new LoadData().execute();
		setNeedBackGesture(true);
	}

	private void initView() {
		backBtn = (ImageView) findViewById(R.id.back);
		backBtn.setOnClickListener(new onClick());

		shareBtn = (ImageView) findViewById(R.id.share);
		shareBtn.setOnClickListener(new onClick());
		webview = (WebView) findViewById(R.id.webview);
		
		comment_news = (ImageView) findViewById(R.id.comment_news);
		comment_num = (TextView)findViewById(R.id.comment_num);
		reply = (LinearLayout)findViewById(R.id.reply);
		collect = (ImageView) findViewById(R.id.collect);
		thumb_up = (ImageView) findViewById(R.id.thumb_up);
		footer_rlayout = (RelativeLayout)findViewById(R.id.footer_rlayout);
	}
	
	/***
	 * 初始化字号大小
	 */
	private void initSysoutFontSize() {
		// 字号初始化
		Map<String, ?> map = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.KEY_FONT_SIZE);
		try {
			if (null != map)
				fontSize = Integer.parseInt((String) map.get("size"));
			else
				fontSize = 1;
		} catch (Exception e) {
			fontSize = 1;
		}
	}

	/**
	 * 进行webView的初始化
	 */
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initWebView() {
		try {
			WebSettings setting = webview.getSettings();
			setting.setJavaScriptEnabled(true);
			setting.setAppCacheEnabled(true);
			String appCacheDir = this.getApplicationContext()
					.getDir("cache", Context.MODE_PRIVATE).getPath();
			setting.setAppCachePath(appCacheDir);
			
			if(fontSize == 0)
				setting.setTextSize(WebSettings.TextSize.LARGER);
			else if(fontSize == 1)
				setting.setTextSize(WebSettings.TextSize.NORMAL);
			else if(fontSize == 2)
				setting.setTextSize(WebSettings.TextSize.SMALLER);
			
			if(PublicValue.USER!=null)
				webview.loadUrl(PublicValue.BASEURL+"getArticleDetails.do?aid="+
						aid+"&memberid="+PublicValue.USER.getId()+"&pSName="+paperName);
			else 
				webview.loadUrl(PublicValue.BASEURL+"getArticleDetails.do?aid="+
						aid+"&memberid=-1"+"&pSName="+paperName);
			
			webview.addJavascriptInterface(new JSInterface(), "jsObj");
			webview.setWebViewClient(new xWebViewClientent());
		} catch (Exception e) {

		}
	}

	public class xWebViewClientent extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			return false;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
//			if (loadDialog == null)
//				loadDialog = MyDialog.createLoadingDialog(
//						EpaperArticleDetailActivity.this, "新闻加载中……");
//			loadDialog.show();
			
			SqlHleper hleper = new SqlHleper();
			isSupported = hleper.findESupportByNewsId(aid, EpaperArticleDetailActivity.this);
			isCollected = hleper.findCollectByNewsId(aid, isEpaper, EpaperArticleDetailActivity.this);
		}
		
		 @Override  
         public void onPageFinished(WebView view,String url)  
         {  
			 if (dialog != null)
				 dialog.cancel();  
			 
			 if(isNight)
				 webview.loadUrl("javascript:useNightModel()");
			 
			 comment_news.setOnClickListener(new onClick());
			 shareBtn.setOnClickListener(new onClick());
			 collect.setOnClickListener(new onClick());
			 reply.setOnClickListener(new onClick());
			 thumb_up.setOnClickListener(new onClick());
			 
			 if (article.getReviewCount() > 0&&article.getReviewCount() < 99) {
				comment_num.setVisibility(View.VISIBLE);
				comment_num.setText(article.getReviewCount()+"");
			 }else if(article.getReviewCount() > 99){
				comment_num.setVisibility(View.VISIBLE);
				comment_num.setText("99+");
			 }
			
			 if(isSupported)
				 thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
			 else 
				thumb_up.setBackgroundResource(R.drawable.thumb_up_black);
			 
			 if(isCollected)
				 collect.setBackgroundResource(R.drawable.collected);
			 else 
				 collect.setBackgroundResource(R.drawable.collect_black);
			 
			 footer_rlayout.setVisibility(View.VISIBLE);
			 
         }  
	}

	/***
	 * js 调用接口类
	 * 
	 * @author yilang
	 * 
	 */
	private class JSInterface {
		
//		Share share = new Share(EpaperArticleDetailActivity.this);

		@JavascriptInterface
		public void checkBigPic(String url) {
			Tools.log(url);
			Message msg = new Message();
			msg.what = mHandler.ACTION_SHOW_BIG_PIC;
			Bundle data = new Bundle();
			data.putString("imgSrc", url);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

		@JavascriptInterface
		public void share(String platform) {
//			share.share(article.getTitle(),
//					article.getContent().length() >= 50 ? article.getContent()
//							.substring(0, 50) : article.getContent(),
//					PublicValue.BASEURL + "logo.png", 
//					PublicValue.BASEURL + "lookHtmlFromTemplet.do?aid=" + article.getId(),
//					platform);
		}
		
		@JavascriptInterface
		public void pauseDetail() {//暂停播放
			 mSpeechSynthesizer.pause();
		}
		
		@JavascriptInterface
		public void resumeDetail() {//恢复播放
			mSpeechSynthesizer.resume();
		}
		
		
		@JavascriptInterface
		public void speakDetail(String text) {//读报
			
			news_text = text.replace("，","。");
			news_text = news_text.replace("    ","。");
			news_text = news_text.replace("   ","。");
			news_text = news_text.replace("  ","。");
			news_text = news_text.replace(" ","。");
			news_text = news_text+"。";
			news_text.length();
			JavaSpeak();
		}
		
		
		@JavascriptInterface
		public void showText(String text){
//			Toast.makeText(NewsDetailActivity.this, text, 5).show();
			Log.e("读报执行?", text);
		}
		
		
		
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("aid", aid);
		outState.putString("data", articleJsonData);
		outState.putString("paperName", "paperName");
		super.onSaveInstanceState(outState);
	}

	private class LoadData extends AsyncTask<Object, Object, Integer> {

		@Override
		protected void onPreExecute() {
			if (null == dialog)
				dialog = MyDialog.createLoadingDialog(
						EpaperArticleDetailActivity.this,
						EpaperArticleDetailActivity.this
								.getString(R.string.get_news_detail));
			dialog.show();
		}

		@Override
		protected Integer doInBackground(Object... params) {
			PaperServer server = new PaperServer();
			articleJsonData = server.getArticle(aid, paperName);
			try {
				JSONObject json = new JSONObject(articleJsonData);
				if (json.getInt("ret") == 1) {
					Gson gson = new Gson();
					temparticle = gson.fromJson(json.getString("data"),
							Article.class);
					return 1;
				}else {
					return 0;	
				}
			} catch (Exception e) {
				return 0;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			
			if (1 == result) {
				// 新闻获取成功后才能进行评论等操作
				article = temparticle;
				// 在收藏列表中 用于判断是否是电子报的文章， 进入不同的detail
				newsMix = new NewsMix();
				news = new NewsPub();
				newsMix.setIsEpaper(isEpaper);
				newsMix.setArticle(article);
				newsMix.setNewsPub(news);
				
				initWebView();
			} else {
				if (null == dialog)
					dialog.cancel();
				Toast.makeText(getApplicationContext(), "新闻获取失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class onClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == backBtn)
				finish();
			else if (v == shareBtn) {
				ShareTools share = new ShareTools();
				share.showEpaperShare(false, null, EpaperArticleDetailActivity.this, article);
			}else if(v == reply){
				alertInputDialog();
			}else if(v==comment_news){
				Intent intent = new Intent(EpaperArticleDetailActivity.this,
						NewsReviewActivity.class);
				intent.putExtra("newsId", aid);
				intent.putExtra("title", news.getShorttitle());
				intent.putExtra("isEpaper", true);
				startActivity(intent);
			}else if(v==collect){
				SqlHleper hleper = new SqlHleper();
				if(isCollected){
					int result = hleper.deleteCollectNews(aid,isEpaper,EpaperArticleDetailActivity.this);
					if(result>0){
						isCollected = false;
						collect.setBackgroundResource(R.drawable.collect_black);
						Toast.makeText(getApplicationContext(), "取消收藏",
								Toast.LENGTH_SHORT).show();
					}else
						Toast.makeText(getApplicationContext(), "取消失败",
								Toast.LENGTH_SHORT).show();
				}else{
					isCollected = true; 
					collect.setBackgroundResource(R.drawable.collected);
					hleper.addCollect(newsMix, EpaperArticleDetailActivity.this);
					Toast.makeText(getApplicationContext(), "收藏成功",
							Toast.LENGTH_SHORT).show();
				}
				
			}else if(v==thumb_up){
				SqlHleper hleper = new SqlHleper();
				if(isSupported){
					int result = hleper.deleteESupportNews(aid, EpaperArticleDetailActivity.this);
					if(result>0){
						isSupported = false;
						thumb_up.setBackgroundResource(R.drawable.thumb_up_black);
						Toast.makeText(getApplicationContext(), "取消点赞",
								Toast.LENGTH_SHORT).show();
					}else
						Toast.makeText(getApplicationContext(), "取消失败",
								Toast.LENGTH_SHORT).show();
				}else{
					isSupported = true; 
					thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
					hleper.addESupport(article, EpaperArticleDetailActivity.this);
					Toast.makeText(getApplicationContext(), "点赞成功",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	}

	private static class MHandler extends Handler {

		WeakReference<EpaperArticleDetailActivity> mActivity;

		public final int ACTION_SHOW_BIG_PIC = 0;
		private final int ACTION_POST_COMMENT = 1;// 提交评论

		public MHandler(EpaperArticleDetailActivity activity) {
			this.mActivity = new WeakReference<EpaperArticleDetailActivity>(
					activity);
		}

		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			switch (msg.what) {
			case ACTION_SHOW_BIG_PIC:
				String url = data.getString("imgSrc");
				Intent intent = new Intent(mActivity.get(),
						ShowImageActivity.class);
				intent.putExtra("imgSrc", url);
				mActivity.get().startActivity(intent);
				break;
			case ACTION_POST_COMMENT:
				boolean flag = msg.getData().getBoolean("flag");
				if (flag)
					Toast.makeText(mActivity.get(), "评论成功", Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(mActivity.get(), "评论失败", Toast.LENGTH_SHORT)
							.show();
				break;
			default:
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
							aid, PublicValue.USER.getId(), editText.getText()
									.toString());
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
		private Integer newsId, memberid;
		private String txt;

		public PostCommentThread(Integer newsId,
				Integer memberId, String txt) {
			this.memberid = memberId;
			this.newsId = newsId;
			this.txt = txt;
		}

		@Override
		public void run() {
			NewsCommentServer server = new NewsCommentServer();
			boolean flag = server.postEComment(txt, newsId, memberid);
			Message msg = new Message();
			msg.what = mHandler.ACTION_POST_COMMENT;
			Bundle data = new Bundle();
			data.putBoolean("flag", flag);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}
	
	
	
	//读报功能开始
		private void initialEnv() {
	        if (mSampleDirPath == null) {
	            String sdcardPath = Environment.getExternalStorageDirectory().toString();
	            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
	        }
	        makeDir(mSampleDirPath);
	        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
	        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
	        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
	        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
	        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
	                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
	        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
	                + ENGLISH_SPEECH_MALE_MODEL_NAME);
	        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
	                + ENGLISH_TEXT_MODEL_NAME);
	    }
		
		private void makeDir(String dirPath) {
	        File file = new File(dirPath);
	        if (!file.exists()) {
	            file.mkdirs();
	        }
	    }
		
		
		 /**
	     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
	     * 
	     * @param isCover 是否覆盖已存在的目标文件
	     * @param source
	     * @param dest
	     */
	    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
	        File file = new File(dest);
	        if (isCover || (!isCover && !file.exists())) {
	            InputStream is = null;
	            FileOutputStream fos = null;
	            try {
	                is = getResources().getAssets().open(source);
	                String path = dest;
	                fos = new FileOutputStream(path);
	                byte[] buffer = new byte[1024];
	                int size = 0;
	                while ((size = is.read(buffer, 0, 1024)) >= 0) {
	                    fos.write(buffer, 0, size);
	                }
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } finally {
	                if (fos != null) {
	                    try {
	                        fos.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	                try {
	                    if (is != null) {
	                        is.close();
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    
	    private void initSDK() {
			// TODO Auto-generated method stub
			this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
	        this.mSpeechSynthesizer.setContext(this);
	        this.mSpeechSynthesizer.setSpeechSynthesizerListener(new ttsListener());
	        // 文本模型文件路径 (离线引擎使用)
	        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
	                + TEXT_MODEL_NAME);
	        // 声学模型文件路径 (离线引擎使用)
	        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
	                + SPEECH_FEMALE_MODEL_NAME);
	        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
	        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
	        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
	                + LICENSE_FILE_NAME);
	        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
	        this.mSpeechSynthesizer.setAppId("6353821"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
	        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
	        this.mSpeechSynthesizer.setApiKey("B479rm7D45XaZOSf5kBCyHWs",
	                "WDq4udqHe4SnuyGX6CGqyRzkKIFl3MPh"/*  这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
	        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
	        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "1");
	        // 设置Mix模式的合成策略
	        //MIX_MODE_HIGH_SPEED_NETWORK：WIFI 4G 3G 2G下在线优先， 其它网络状况离线合成。 如果在线连接百度服务器失败或者超时6s，那么切换成离线合成。
	        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
	        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
	        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
	        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);

	        if (authInfo.isSuccess()) { 
	        } else {
	            String errorMsg = authInfo.getTtsError().getDetailMessage();
	        }

	        // 初始化tts
	        // 初始化离在线混合模式，如果只需要在线合成功能，使用 TtsMode.ONLINE
	        mSpeechSynthesizer.initTts(TtsMode.MIX);
	        // 加载离线英文资源（提供离线英文合成功能）
	        int result =
	                mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
	                        + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);

	        //打印引擎信息和model基本信息
//	        printEngineInfo();
		}
	    
	    
	    class ttsListener implements SpeechSynthesizerListener{

			@Override
			public void onError(String arg0, SpeechError arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeechFinish(String arg0) {
				// TODO Auto-generated method stub
				
			}

			/**
		     * 播放进度回调接口，分多次回调
		     *
		     * @param utteranceId
		     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
		     */
			@Override
			public void onSpeechProgressChanged(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeechStart(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			/**
		     * 合成数据和进度的回调接口，分多次回调
		     *
		     * @param utteranceId
		     * @param data 合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
		     * @param progress 文本按字符划分的进度，比如:你好啊 进度是0-3
		     */
			@Override
			public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSynthesizeFinish(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSynthesizeStart(String arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    }
	    
	    private SpeechSynthesizeBag getSpeechSynthesizeBag(String text, String utteranceId) {
	        SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
	        //需要合成的文本text的长度不能超过1024个GBK字节。
	        speechSynthesizeBag.setText(text);
	        speechSynthesizeBag.setUtteranceId(utteranceId);
	        return speechSynthesizeBag;
	    }
	    
	    @Override
	    protected void onDestroy() {
	        this.mSpeechSynthesizer.release();
	        super.onDestroy();
	    }
	    
	    private void JavaSpeak(){
//			String temp = "";
//			
//			if(news_speak_num!=0)
//				news_text = news_text.substring(20);
//			temp = news_text.substring(0, 20);
//			news_speak_num++;
//			int result = mSpeechSynthesizer.speak(temp);
			
			List<Integer> positions = new ArrayList<Integer>();
			String[] textArray = new String[99];
			
			
			int position = 0;
			String temp_news_text ="";
			temp_news_text = news_text;
			
			for(int i = 0;i<99;i++){
				int end = 0;
				int last_end = -1;
				
				while(end<500&&last_end!=end&&end>=0){
					last_end = end;
					if(position+end+1<news_text.length())
						end = temp_news_text.indexOf("。", position+end+1);
				}
				position = last_end;
				temp_news_text = temp_news_text.substring(position);
				positions.add(last_end);
				
			}
			
			for(int j = 0; j<positions.size();j++){
				if(positions.get(j)!=0){
					textArray[j] = news_text.substring(0,positions.get(j));
					news_text = news_text.substring(positions.get(j));
				}
			}
			
			List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
			for (int m = 0; m< textArray.length; m++) {
				bags.add(getSpeechSynthesizeBag(textArray[m], "0"));
			}
			
			int result = mSpeechSynthesizer.batchSpeak(bags);
			
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
