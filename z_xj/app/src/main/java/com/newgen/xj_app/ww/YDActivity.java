package com.newgen.xj_app.ww;

import java.util.ArrayList;
import java.util.List;

import com.newgen.adapter.ViewpagerAdapterYD;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class YDActivity extends Activity {
	
	private ViewPager vpPager;
	private ViewpagerAdapterYD vpAdapterYD;
	private List<View>views;
	private Button button_aaaa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yd);
		initViews();
	}
	
	private void initViews(){
		LayoutInflater inflater =LayoutInflater.from(this);
		views =new ArrayList<View>();
		views.add(inflater.inflate(R.layout.item_yd_1, null));
		views.add(inflater.inflate(R.layout.item_yd_2, null));
		views.add(inflater.inflate(R.layout.item_yd_4, null));
		// 页面滑动监听器
		vpAdapterYD =new ViewpagerAdapterYD(views, this);
		vpPager=(ViewPager) findViewById(R.id.vieapger);
		vpPager.setAdapter(vpAdapterYD);
		button_aaaa=(Button) views.get(2).findViewById(R.id.button_aaaa);
		button_aaaa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(YDActivity.this, MainFragmentActivity.class);
				startActivity(intent);	
				finish();
			}
		});
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
