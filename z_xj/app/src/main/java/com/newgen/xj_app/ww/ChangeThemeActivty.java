package com.newgen.xj_app.ww;

import java.util.Timer;
import java.util.TimerTask;

import com.newgen.xj_app.R;
import com.newgen.xj_app.R.layout;

import cn.net.newgen.widget.dialog.ArtWaitDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

public class ChangeThemeActivty extends Activity {
	
	Dialog dialog;
	Timer timer;
	int time = 3;//用于计时	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_theme_activty);
		
		dialog = new ArtWaitDialog(ChangeThemeActivty.this, "切换日/夜间模式，请稍等...");
		dialog.show();
		
		
		timer = new Timer();
		timer.schedule(new DataTask(), 0, 1000);//一秒后执行，一秒执行一次
		
	}
	
	
	 private class DataTask extends TimerTask {
			public DataTask() {
				time =3;
			}

			@Override
			public void run() {
				time--;// 加一秒
				if(time<0){
					dialog.cancel();
					timer.cancel();
					Intent intent = new Intent(ChangeThemeActivty.this,MainFragmentActivity.class);
					startActivity(intent);
					ChangeThemeActivty.this.finish();
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
