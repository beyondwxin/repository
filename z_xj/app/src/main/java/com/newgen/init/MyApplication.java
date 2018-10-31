package com.newgen.init;

import com.newgen.db.SQLHelper;
import com.newgen.tools.ImageCacheUtil;
import com.newgen.tools.Tools;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

public class MyApplication extends Application {

	private static MyApplication instance;
	private SQLHelper sqlHelper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		/****
		 * 图片加载组件初始化
		 */
		ImageCacheUtil.init(this);
		instance = this;
		
		// 加载系统默认设置，字体不随用户设置变化
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        
	}
	
	public static MyApplication getInstance() {
		return instance;
	}
	
	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(instance);
		return sqlHelper;
	}
	
}