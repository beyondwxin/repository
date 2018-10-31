package com.newgen.xj_app.ww;

import com.newgen.share.ShareTools;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class AboutUsActivity extends Activity {
	
	ImageView back,share;
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		
		
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.share);
		webView = (WebView) findViewById(R.id.share_webview);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareTools share = new ShareTools();
				share.showSharelink(false, null, AboutUsActivity.this,
						"今日首钢","",PublicValue.SHAREUSPATH );
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		webView.loadUrl(PublicValue.SHAREUSPATH);
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
