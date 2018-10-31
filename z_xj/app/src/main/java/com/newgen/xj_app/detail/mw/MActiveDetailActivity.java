package com.newgen.xj_app.detail.mw;

import com.newgen.share.ShareTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
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

public class MActiveDetailActivity extends Activity {
	
	private int type,relationid;
	private WebView web_content;
	private ImageView share;
	private ImageView back;
	String uuid="";//
	String shareUrl = "";
	String title,shareImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_detail);
		
		uuid = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.KEY_DEVICE_ID,
				SharedPreferencesTools.KEY_DEVICE_ID);
		
		type = getIntent().getIntExtra("type", 0);
		relationid = getIntent().getIntExtra("relationid", 0);
		title = getIntent().getStringExtra("title");
		shareImg = getIntent().getStringExtra("shareImg");
		web_content = (WebView) findViewById(R.id.webview);
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		back.setOnClickListener(new Click());
		share.setOnClickListener(new Click());
		
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
			
			shareUrl = PublicValue.BASEURL+"getActiveDetails.do?type="+
					type+"&relationid="+relationid;
			
			
			if(PublicValue.USER!=null)
				web_content.loadUrl(PublicValue.BASEURL+"getActiveDetails.do?type="+
						type+"&relationid="+relationid+"&memberid="+PublicValue.USER.getId()+"&device="+uuid);
			else 
				web_content.loadUrl(PublicValue.BASEURL+"getActiveDetails.do?type="+
						type+"&relationid="+relationid+"&memberid=-1"+"&device="+uuid);
			
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
			}else if(v==share){
				ShareTools share = new ShareTools();
				share.showSharelink(false, null, MActiveDetailActivity.this,title,
						shareImg, shareUrl);
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
			Intent intent = new Intent(MActiveDetailActivity.this,
					MLinkDetailActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			return true;
		}
		
	}
	
	public class xWebChromeClient extends WebChromeClient  {
		
		 @Override   
		 public boolean onJsAlert(WebView view, String url, String message, JsResult result) {   
			 Toast.makeText(MActiveDetailActivity.this, message, Toast.LENGTH_SHORT).show();   
			 result.cancel();   
			 return true;   
         }   
		
	}
	
}
