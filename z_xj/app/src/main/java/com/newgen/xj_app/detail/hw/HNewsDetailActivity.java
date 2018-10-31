package com.newgen.xj_app.detail.hw;

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

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.newgen.UI.MyDialog;
import com.newgen.domain.Member;
import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.server.NewsCommentServer;
import com.newgen.server.NewsServer;
import com.newgen.share.ShareTools;
import com.newgen.tools.NewsDetailActivityFactory;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SqlHleper;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.user.LoginActivity;
import com.newgen.xj_app.ww.ShowImageActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

public class HNewsDetailActivity extends Activity {

	ImageView backButton;
	ImageView shareButton;
	ImageView comment_news,collect,thumb_up;
	LinearLayout reply;
	TextView comment_num;
	NewsPub news,tempnews;
	NewsMix newsMix;
	Article article;
	
	private String newsJson = null;
	
	Dialog loadDialog = null;
	Dialog replyDailog;
	RelativeLayout footer_rlayout;
	
	private WebView web_content;
	
	private int newsId = 0;
	private int fontSize = 1;// 中号字
	private Integer clickedReplyId = null;
	
	int isEpaper = 0;// 电子报的详情是1  ，普通是0
	
	String uuid="";//
	
	private boolean isSupported = false; // 新闻点赞
	private boolean isCollected = false; // 新闻收藏
	
	private MHandler mHandler;
	
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail_h);
		
		PublicValue.USER = Tools.getUserInfo(this);
		
		if (savedInstanceState != null) {
			newsId = savedInstanceState.getInt("newsId");
		} else{
			newsId = getIntent().getIntExtra("newsId", 0);
		}
		
		uuid = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.KEY_DEVICE_ID,
				SharedPreferencesTools.KEY_DEVICE_ID);
		
		mHandler = new MHandler(this);
		
		backButton = (ImageView) findViewById(R.id.back);
		shareButton = (ImageView) findViewById(R.id.share);
		web_content = (WebView) findViewById(R.id.webview);
		reply = (LinearLayout)findViewById(R.id.reply);
		comment_news = (ImageView) findViewById(R.id.comment_news);
		collect = (ImageView) findViewById(R.id.collect);
		thumb_up = (ImageView) findViewById(R.id.thumb_up);
		comment_num = (TextView)findViewById(R.id.comment_num);
		footer_rlayout = (RelativeLayout)findViewById(R.id.footer_rlayout);
		
		initialEnv();
		initSDK();
		this.initSysoutFontSize();
		new LoadHtml().execute();
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
	
	/**
	 * 进行webView的初始化
	 */
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initWebView() {
		try {
			WebSettings setting = web_content.getSettings();
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
				web_content.loadUrl(PublicValue.BASEURL+"getNewsDetailsById.do?newsid="+
						newsId+"&memberid="+PublicValue.USER.getId()+"&device="+uuid);
			else 
				web_content.loadUrl(PublicValue.BASEURL+"getNewsDetailsById.do?newsid="+
						newsId+"&memberid=-1"+"&device="+uuid);
			
			web_content.addJavascriptInterface(new JSInterface(), "jsObj");
			
			xWebViewClientent wvClient = new xWebViewClientent();
			web_content.setWebViewClient(wvClient);
		} catch (Exception e) {

		}
	}
	
	
	class OnClick implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			 if (view == backButton) {
				finish();
			} else if(view == reply){
				clickedReplyId = null;
				alertInputDialog();
			}else if(view ==shareButton){
				ShareTools share = new ShareTools();
				share.showShare(false, null, HNewsDetailActivity.this, news);
			}else if(view==comment_news){
				Intent intent = new Intent(HNewsDetailActivity.this,
						HNewsReviewActivity.class);
				intent.putExtra("newsId", newsId);
				intent.putExtra("title", news.getShorttitle());
				intent.putExtra("isEpaper", false);
				startActivity(intent);
			}else if(view == collect){
				SqlHleper hleper = new SqlHleper();
				if(isCollected){
					int result = hleper.deleteCollectNews(newsId,isEpaper,HNewsDetailActivity.this);
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
					hleper.addCollect(newsMix, HNewsDetailActivity.this);
					Toast.makeText(getApplicationContext(), "收藏成功",
							Toast.LENGTH_SHORT).show();
				}
				
			}else if(view == thumb_up){
				SqlHleper hleper = new SqlHleper();
				if(isSupported){
					int result = hleper.deleteSupportNews(newsId, HNewsDetailActivity.this);
					if(result>0){
						isSupported = false;
						thumb_up.setBackgroundResource(R.drawable.thumb_up_black);
						Toast.makeText(getApplicationContext(), "取消点赞",
								Toast.LENGTH_SHORT).show();
						Log.e("1111", hleper.getSupportNewsList(HNewsDetailActivity.this, 1, 10)+"");
					}else
						Toast.makeText(getApplicationContext(), "取消失败",
								Toast.LENGTH_SHORT).show();
				}else{
					isSupported = true; 
					thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
					hleper.addSupport(news, HNewsDetailActivity.this);
					Toast.makeText(getApplicationContext(), "点赞成功",
							Toast.LENGTH_SHORT).show();
					
					Log.e("2222", hleper.getSupportNewsList(HNewsDetailActivity.this, 1, 10)+"");
				}
			}
		}
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
	
	public class xWebViewClientent extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			Intent intent = new Intent(HNewsDetailActivity.this,
					HLinkDetailActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			HNewsDetailActivity.this.finish();
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			
			SqlHleper hleper = new SqlHleper();
			isSupported = hleper.findSupportByNewsId(newsId, HNewsDetailActivity.this);
			isCollected = hleper.findCollectByNewsId(newsId, isEpaper, HNewsDetailActivity.this);
		}
		
		 @Override  
         public void onPageFinished(WebView view,String url)  
         {  
			 if(loadDialog!=null)
				 loadDialog.cancel();
			 
			 if(isNight)
					web_content.loadUrl("javascript:useNightModel()");
			 
			 comment_news.setOnClickListener(new OnClick());
			 shareButton.setOnClickListener(new OnClick());
			 collect.setOnClickListener(new OnClick());
			 reply.setOnClickListener(new OnClick());
			 thumb_up.setOnClickListener(new OnClick());
			
			 if(isSupported)
				 thumb_up.setBackgroundResource(R.drawable.thumb_up_red);
			 else 
				thumb_up.setBackgroundResource(R.drawable.thumb_up_black);
			 
			 if(isCollected)
				 collect.setBackgroundResource(R.drawable.collected);
			 else 
				 collect.setBackgroundResource(R.drawable.collect_black);
			 
			 if(news!=null){
				 if (news.getNewsPubExt().getReviewcount() > 0&&news.getNewsPubExt().getReviewcount() < 99) {
						comment_num.setVisibility(View.VISIBLE);
						comment_num.setText(news.getNewsPubExt().getReviewcount()
								+ "");
					 }else if(news.getNewsPubExt().getReviewcount() > 99){
						comment_num.setVisibility(View.VISIBLE);
						comment_num.setText("99+");
					 }
			 }
			 
			 footer_rlayout.setVisibility(View.VISIBLE);
         }  

	}
	
	// 获取用户信息
	private String getUserInfo() {
		Member member = PublicValue.USER;
		Gson gson = new Gson();
		if (member == null) {
			member = new Member();
			member.setMemcode("sgrb");
			return gson.toJson(member);
		} else
			return gson.toJson(member);
	}
	
	private static class MHandler extends Handler {
		
		private final int ACTION_POST_COMMENT = 1;// 提交评论
		private final int ACTION_POST_VOTE = 2;// 提交投票
		private final int ACTION_SHOW_BIG_PIC = 3;// 展示大图
	
		WeakReference<HNewsDetailActivity> mActivity;
	
		MHandler(HNewsDetailActivity activity) {
			mActivity = new WeakReference<HNewsDetailActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_POST_COMMENT:
				boolean flag = msg.getData().getBoolean("flag");
				if (flag)
					Toast.makeText(mActivity.get(), "评论成功", Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(mActivity.get(), "评论失败", Toast.LENGTH_SHORT)
							.show();
				break;
			case ACTION_POST_VOTE:
				Toast.makeText(mActivity.get(), msg.getData().getString("msg"),
						Toast.LENGTH_SHORT).show();
				break;
			case ACTION_SHOW_BIG_PIC:
				Bundle data = msg.getData();
				String url = data.getString("imgSrc");
				Intent intent = new Intent(mActivity.get(),
						ShowImageActivity.class);
				intent.putExtra("imgSrc", url);
				mActivity.get().startActivity(intent);
				break;
			}
		}
	}
	
	/***
	 * js 调用接口类
	 * 
	 * @author yilang
	 * 
	 */
	private class JSInterface {
//		Share share = new Share(NewsDetailActivity.this);

		@JavascriptInterface
		public void showBigPic(String url) {// 当点击图片时，可取到当前图
			Message msg = new Message();
			msg.what = mHandler.ACTION_SHOW_BIG_PIC;
			Bundle data = new Bundle();
			data.putString("imgSrc", url);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

		@JavascriptInterface
		public void playVideo(String url) {// 当点击播放按钮时，调用本地播放器播放视频
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url), "video/mp4");
			Log.i("info", url);
			startActivity(intent);
		}
		
		
		@JavascriptInterface
		public void showHotNews(int newsId, int type, int infoType,
				String shorttitle,String digest,String faceimgpath,
				String faceimgname,int reviewcount) {
			NewsPub news = new NewsPub();
			news.setId(newsId);
			news.getNewsPubExt().setType(type);
			news.getNewsPubExt().setInfotype(infoType);
			news.setShorttitle(shorttitle);
			news.setDigest(digest);
			news.getNewsPubExt().setFaceimgpath(faceimgpath);
			news.getNewsPubExt().setFaceimgname(faceimgname);
			news.getNewsPubExt().setReviewcount(reviewcount);
			try {
				NewsDetailActivityFactory.openActivity(news,
						HNewsDetailActivity.this);
				// /finish();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

		@JavascriptInterface
		public String userinfo() { // 返回用户信息
			return getUserInfo();
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
							HNewsDetailActivity.this.newsId, clickedReplyId,
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
//        printEngineInfo();
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
        web_content.destroy();
        web_content = null;
        super.onDestroy();
    }
    
    
    private void JavaSpeak(){
//		String temp = "";
//		
//		if(news_speak_num!=0)
//			news_text = news_text.substring(20);
//		temp = news_text.substring(0, 20);
//		news_speak_num++;
//		int result = mSpeechSynthesizer.speak(temp);
		
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
				textArray[j] = textArray[j].replace("　　", "。 ");
				news_text = news_text.substring(positions.get(j));
			}
		}
		
		List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
		for (int m = 0; m< textArray.length; m++) {
			bags.add(getSpeechSynthesizeBag(textArray[m], "0"));
		}
		
		int result = mSpeechSynthesizer.batchSpeak(bags);
		
	}
    
    
    
    /***
	 * 加载数据
	 */
	private class LoadHtml extends AsyncTask<Object, Object, Integer> {

		
		@Override
		protected void onPreExecute() {
			if (loadDialog == null)
				loadDialog = MyDialog.createLoadingDialog(
						HNewsDetailActivity.this, "新闻加载中……");
			loadDialog.show();
		}
		
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {
				NewsServer newsServer = new NewsServer();
				int memberid = -1;
				if (PublicValue.USER != null)
					memberid = PublicValue.USER.getId();
				newsJson = newsServer.getNewsDetail(newsId, memberid,
						PublicValue.HARDID);
				JSONObject json = new JSONObject(newsJson);
				if (json.getInt("ret") == 1) {
					tempnews = new Gson().fromJson(json.getString("data"),
							NewsPub.class);
					Thread.sleep(500);
					return 1;
				} else
					return 0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			if (1 == result) {
				// 新闻获取成功后才能进行评论等操作
				news = tempnews;
				// 在收藏列表中 用于判断是否是电子报的文章， 进入不同的detail
				newsMix = new NewsMix();
				article = new Article();
				newsMix.setIsEpaper(isEpaper);
				newsMix.setArticle(article);
				newsMix.setNewsPub(news);
				
				initWebView();
			} else {
				loadDialog.cancel();
				Toast.makeText(getApplicationContext(), "新闻获取失败",
						Toast.LENGTH_SHORT).show();
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
