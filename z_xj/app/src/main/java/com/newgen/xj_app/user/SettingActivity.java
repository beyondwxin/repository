package com.newgen.xj_app.user;

import java.util.HashMap;
import java.util.Map;

import cn.net.newgen.widget.dialog.ArtAlertDialog;

import com.igexin.sdk.PushManager;
import com.newgen.tools.DataCleanManager;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.array;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;
import com.newgen.xj_app.ww.AboutUsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {
	
	Dialog dialog;
	ImageView back;
	SeekBar light_seekBar;
	TextView message_push_open,message_push_close;
	TextView font_size,clear_cache,check_version,about_us,screen_brightness;
	boolean pushOpen = true;// 推送功能是否打开
	DataCleanManager manager = new DataCleanManager();
	String version;
	private int defalutValue = 75;  
	private OnSeekBarChangeListener seekBarChange;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		seekBarChange = new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
				// TODO Auto-generated method stub
				setScreenLight(progress);  
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.e("onStopTrackingTouch", "拖动停止");
				light_seekBar.setVisibility(View.GONE);
			}  
		};
		
		initView();
		initListener();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		version = Tools.getVision(SettingActivity.this);
		
		back = (ImageView)findViewById(R.id.back);
		message_push_open = (TextView)findViewById(R.id.message_push_open);
		message_push_close = (TextView)findViewById(R.id.message_push_close);
		font_size = (TextView)findViewById(R.id.font_size);
		clear_cache = (TextView)findViewById(R.id.clear_cache);
		check_version = (TextView)findViewById(R.id.check_version);
		about_us = (TextView)findViewById(R.id.about_us);
		screen_brightness = (TextView)findViewById(R.id.screen_brightness);
		light_seekBar = (SeekBar)findViewById(R.id.light_seekBar);
		
		// 推送初始化
		String isPush = SharedPreferencesTools.getValue(SettingActivity.this,
				SharedPreferencesTools.KEY_PUSH_CTRL,
				SharedPreferencesTools.KEY_PUSH_CTRL);
		if (null != isPush && !"".equals(isPush)) {
			pushOpen = "true".equals(isPush);
		} else
			pushOpen = true;
		
		if (pushOpen){
			message_push_open.setTextColor(Color.parseColor("#1d1a1a"));
			message_push_close.setTextColor(Color.parseColor("#aab1b3"));
		}else{
			message_push_open.setTextColor(Color.parseColor("#aab1b3"));
			message_push_close.setTextColor(Color.parseColor("#1d1a1a"));
		}
		
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new Click());
		message_push_open.setOnClickListener(new Click());
		message_push_close.setOnClickListener(new Click());
		font_size.setOnClickListener(new Click());
		clear_cache.setOnClickListener(new Click());
		check_version.setOnClickListener(new Click());
		about_us.setOnClickListener(new Click());
		screen_brightness.setOnClickListener(new Click());
		light_seekBar.setOnSeekBarChangeListener(seekBarChange);
	}
	
	
	class Click implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==back){
				finish();
			}else if(v==message_push_open){
				
				message_push_open.setTextColor(Color.parseColor("#1d1a1a"));
				message_push_close.setTextColor(Color.parseColor("#aab1b3"));
				// com.getui.demo.DemoPushService 为第三方自定义推送服务
		        PushManager.getInstance().initialize(SettingActivity.this.getApplicationContext(), com.newgen.service.PushService.class);
		        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
		        PushManager.getInstance().registerPushIntentService(SettingActivity.this.getApplicationContext(), com.newgen.service.IntentService.class);
				pushOpen = true;
				SharedPreferencesTools
				.setValue(SettingActivity.this,
						SharedPreferencesTools.KEY_PUSH_CTRL,
						pushOpen ? "true" : "false",
						SharedPreferencesTools.KEY_PUSH_CTRL);
			}else if(v==message_push_close){
				
				message_push_open.setTextColor(Color.parseColor("#aab1b3"));
				message_push_close.setTextColor(Color.parseColor("#1d1a1a"));
				
				PushManager.getInstance().stopService(SettingActivity.this);
				pushOpen = false;
				SharedPreferencesTools
				.setValue(SettingActivity.this,
						SharedPreferencesTools.KEY_PUSH_CTRL,
						pushOpen ? "true" : "false",
						SharedPreferencesTools.KEY_PUSH_CTRL);
			}else if(v==font_size){
				setFontSize();
			}else if(v==clear_cache){
				// 清理缓存
				dialog = new ArtAlertDialog(SettingActivity.this, "بېكىتىلگەن بۇففىرنى ئۆچۈرۈش. بۇففىرنى ئۆچۈرۈش بارمۇ ؟", "بۇفېرلىق ساقلىغۇچ ئېنىقلاش", "ئېنىق", "ئەمەلدىن قالدۇرماق", new ArtAlertDialog.OnArtClickListener() {
					
					@Override
					public void okButtonClick() {
						// TODO Auto-generated method stub
						// 为用户体验效果，先清除部分提示清理完成，后台清除其他缓存
						manager.cleanFiles(SettingActivity.this);

						Toast.makeText(SettingActivity.this, "بۇفېرلىق ساقلىغۇچ ئېنىقلاش ، مۇۋەپپەقىيەت",
								Toast.LENGTH_SHORT).show();

						manager.cleanInternalCache(SettingActivity.this);
						manager.cleanExternalCache(SettingActivity.this);
					}
					
					@Override
					public void cancelButtonClick() {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
			}else if(v==check_version){
				if(version.equals(PublicValue.lastVersion))
					Toast.makeText(SettingActivity.this, "نۆۋەتتىكى ئەڭ يېڭى نەشىرى", 5).show();
				else
					Toast.makeText(SettingActivity.this, "يېڭى نۇسخا", 5).show();
				
			}else if(v==about_us){
				Intent intent = new Intent(SettingActivity.this,
						AboutUsActivity.class);
				startActivity(intent);
			}else if(v==screen_brightness){
				light_seekBar.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	
	/**
	 * 设置字体
	 */
	public void setFontSize() {
		final String[] items = getResources().getStringArray(R.array.font_size);
		new AlertDialog.Builder(this).setTitle("تېكىست خەت شەكلى")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 设置字体
						switch (which) {
						case 0:// 特大号
							SharedPreferencesTools.NORMALSIZE_W = 0;
							SharedPreferencesTools.SIZENAME_W = "چوڭ";
							break;
						case 1:// 大号
							SharedPreferencesTools.NORMALSIZE_W = 1;
							SharedPreferencesTools.SIZENAME_W = "ئوتتۇرا ";
							break;
						case 2:// 中号
							SharedPreferencesTools.NORMALSIZE_W = 2;
							SharedPreferencesTools.SIZENAME_W = "كىچىك";
							break;
						}
						/****
						 * 保存设置
						 */
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", SharedPreferencesTools.SIZENAME_W);
						map.put("size", SharedPreferencesTools.NORMALSIZE_W + "");
						SharedPreferencesTools.setValue(SettingActivity.this, map,
								SharedPreferencesTools.KEY_FONT_SIZE);
					}
				}).show();
	}
	
	
	public void setScreenLight(int progress) {  
        if (progress < 1) {  
            progress = 1;  
        } else if (progress > 255) {  
            progress = 255;  
        }  
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();  
        attrs.screenBrightness = progress / 255f;  
        getWindow().setAttributes(attrs);  
        defalutValue = progress;  
    }  
	
	
}
