package com.newgen.xj_app.detail.mw;

import com.newgen.share.ShareTools;
import com.newgen.xj_app.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * 视频直播界面 没有实现全屏播放功能
 * 
 * @author suny
 * 
 */
public class MLinkDetailActivity extends Activity {
	
	private WebView browserBody;
	private String url, shareimg, title;
	private ImageView share;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link_detail);
		
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		browserBody = (WebView) findViewById(R.id.browserBody);
		Bundle bundle = getIntent().getExtras();
		url = bundle.getString("url", "");
		Log.i("info", url);
		
		shareimg = bundle.getString("shareimg", "");
		title = bundle.getString("title", "");
		
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		back.setOnClickListener(new Click());
		share.setOnClickListener(new Click());
		browserBody.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		browserBody.loadUrl(url);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			browserBody.destroy();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onStart() {
		super.onStart();
		browserBody.setWebViewClient(new xWebViewClientent());
		browserBody.setWebChromeClient(new WebChromeClient() {
			/*** 视频播放相关的方法 **/

			@Override
			public View getVideoLoadingProgressView() {
				FrameLayout frameLayout = new FrameLayout(MLinkDetailActivity.this);
				frameLayout.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return frameLayout;
			}

			@Override
			public void onShowCustomView(
					View view,
					com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback callback) {
				// TODO Auto-generated method stub
//				showCustomView(view, callback);
			}

			@Override
			public void onHideCustomView() {
//				hideCustomView();
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				
			}
		});
		
		com.tencent.smtt.sdk.WebSettings settings = browserBody.getSettings();
		
		settings.setJavaScriptEnabled(true);
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();    
		settings.setAppCachePath(appCachePath);  
		settings.setAppCacheEnabled(true); 
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setDomStorageEnabled(true); 
		settings.setUseWideViewPort(true);
		settings.setSupportZoom(true);
		settings.setAllowFileAccess(true);   
		settings.setBuiltInZoomControls(true);
		settings.setAppCacheEnabled(true);
		settings.setDefaultTextEncodingName("gbk");
	}
	
	
	public class xWebViewClientent extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Click implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == back) {
				browserBody.destroy();
				finish();
			} else if (v == share) {
				ShareTools share = new ShareTools();
				share.showSharelink(false, null, MLinkDetailActivity.this, title,
						shareimg, url);
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
