package com.newgen.xj_app.detail.ww;


import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class NewsReviewActivity extends Activity {
	
	private int newsID;
	private WebView web_content;
	ImageView backButton;
	private boolean isEpaper;
	boolean isNight = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String isNightString = SharedPreferencesTools.getValue(NewsReviewActivity.this, SharedPreferencesTools.KEY_NIGHT,
				SharedPreferencesTools.KEY_NIGHT);
		isNight = isNightString.equals("true");
		
		if(isNight)
			setContentView(R.layout.activity_news_review_night);
		else
			setContentView(R.layout.activity_news_review);
		
		PublicValue.USER = Tools.getUserInfo(this);
		if (savedInstanceState != null) {
			newsID = savedInstanceState.getInt("newsId");
			isEpaper = savedInstanceState.getBoolean("isEpaper");
		} else{
			newsID = getIntent().getIntExtra("newsId", 0);
			isEpaper = getIntent().getBooleanExtra("isEpaper", false);
		}
			
		
		backButton = (ImageView) findViewById(R.id.back);
		web_content = (WebView) findViewById(R.id.web_content);
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		initWebView();
		
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
			
			int memberid = -1;
			if (PublicValue.USER != null)
				memberid = PublicValue.USER.getId();

			if(isEpaper)
				web_content.loadUrl(PublicValue.BASEURL+"getArticleReviewPage.do?articleid="+
						newsID+"&memberid="+memberid);
			else 
				web_content.loadUrl(PublicValue.BASEURL+"getNewsReviewPage.do?newsid="+
						newsID+"&memberid="+memberid);
			
			
			xWebViewClientent wvClient = new xWebViewClientent();
			web_content.setWebViewClient(wvClient);
		} catch (Exception e) {

		}
	}
	
	public class xWebViewClientent extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			
		}
		
		@Override  
        public void onPageFinished(WebView view,String url){
			if(isNight)
				web_content.loadUrl("javascript:useNightModel()");
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
