package com.newgen.xj_app.detail.mw;

import com.newgen.xj_app.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class MServiceDetailActivity extends Activity {
	
	private WebView web_content;
	private ImageView back;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_detail);
		
		web_content = (WebView) findViewById(R.id.webview);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new Click());
		url = getIntent().getStringExtra("url");
		initView();
		
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initView() {
		// TODO Auto-generated method stub
		try {
			WebSettings setting = web_content.getSettings();
			setting.setJavaScriptEnabled(true);
			setting.setAppCacheEnabled(true);
			String appCacheDir = this.getApplicationContext()
					.getDir("cache", Context.MODE_PRIVATE).getPath();
			setting.setAppCachePath(appCacheDir);
			
			web_content.loadUrl(url);
			web_content.addJavascriptInterface(new JSInterface(), "jsObj");
			
			xWebViewClientent wvClient = new xWebViewClientent();
			xWebChromeClient wcClient = new xWebChromeClient();
			web_content.setWebViewClient(wvClient);
			web_content.setWebChromeClient(wcClient);
			
		} catch (Exception e) {

		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			web_content.destroy();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	class Click implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==back){
				web_content.destroy();
				finish();
			}
		}
		
	}
	
	/***
	 * js 调用接口类
	 * 
	 * @author suny
	 * 
	 */
	private class JSInterface {
		
	}
	
	
	public class xWebViewClientent extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			Intent intent = new Intent(MServiceDetailActivity.this,
					MLinkDetailActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			return true;
		}
		
	}
	
	public class xWebChromeClient extends WebChromeClient  {
		
		 @Override   
		 public boolean onJsAlert(WebView view, String url, String message, JsResult result) {   
			 Toast.makeText(MServiceDetailActivity.this, message, Toast.LENGTH_SHORT).show();   
			 result.cancel();   
			 return true;   
         }   
		
	}
	
}
