package com.newgen.xj_app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList; 	
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.net.newgen.widget.dialog.ArtAlertDialog;
import cn.net.newgen.widget.dialog.ArtAlertDialog.OnArtClickListener;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.igexin.sdk.PushManager;
import com.newgen.domain.AddressComponent;
import com.newgen.domain.Category;
import com.newgen.server.MainServer;
import com.newgen.tools.LocationUtils;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.detail.ww.ImgNewsDetailActivity;
import com.newgen.xj_app.detail.ww.LinkDetailActivity;
import com.newgen.xj_app.detail.ww.NewsDetailActivity;
import com.newgen.xj_app.ww.YDActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.commonsdk.UMConfigure;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {
	
	MyHandler handler;
	Timer timer;
	int time = 0;//用于计时
	String version;//版本
	String downloadUrl;
	String description;// 更新显示的内容
	
	String adImgUrl;
	ImageView image;
	ImageLoader loader;
	DisplayImageOptions options;
	AnimateFirstDisplayListener displayListenter = new AnimateFirstDisplayListener();
	Location location;
	String address;
	
	int newsID = 0;
	int type = 0;
	int infoType = 0;
	int liveId = 0;
	String url = null;
	String contentTitle = null;
	String contentText = null;
	String faceImgName = null;
	String faceImgPath = null;
	String faceImage = null;
	
	private static final int A = 1001;
	private static final int B = 1002;
	private boolean C = false;
	boolean isMust = false;
	
	
	ThreadPoolExecutor threadPool;
	
    @Override	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        location = LocationUtils.getInstance( MainActivity.this ).showLocation();
        if (location != null) {
        	address =  location.getLatitude() + "," + location.getLongitude();
        	Log.e( "FLY.LocationUtils", address );
        }
        
        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), com.newgen.service.PushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.newgen.service.IntentService.class);
        //友盟统计初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        
        setDriveWidthAndHeight4StaticValue();
        
        handler = new MyHandler();
        initImageLoadAndDisPlayImageOptions();
       
        
        image = (ImageView) findViewById(R.id.imageView1);
//      image.setBackgroundResource(R.drawable.cove1);
//		image.setBackgroundResource(R.anim.welcome);
//		AnimationDrawable background = (AnimationDrawable) image
//				.getBackground();
//		background.setOneShot(true);
//		background.start();
        
		PublicValue.USER = Tools.getUserInfo(this);
		
		newsID = getIntent().getIntExtra("newsID", -1);
		type = getIntent().getIntExtra("type", -10);
		infoType = getIntent().getIntExtra("infoType", -1);
		liveId = getIntent().getIntExtra("liveId", -1);
		url = getIntent().getStringExtra("url");
		contentTitle = getIntent().getStringExtra("title");// 标题
		contentText = getIntent().getStringExtra("degist"); // 内容
		
		faceImgName = getIntent().getStringExtra("faceImgName");
		faceImgPath = getIntent().getStringExtra("faceImgPath");
		faceImage = getIntent().getStringExtra("faceImage");
		
        
        /**
		 * 应用启动时查看是否存在UUID 如果不存在则根据机器码生成一个uuid,保证一个手机只能有一个uuid
		 */
		String uuid = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.KEY_DEVICE_ID,
				SharedPreferencesTools.KEY_DEVICE_ID);
		
		if (null == uuid || "".equals(uuid)) {
			uuid = this.getUID();
			SharedPreferencesTools.setValue(this,
					SharedPreferencesTools.KEY_DEVICE_ID, uuid,
					SharedPreferencesTools.KEY_DEVICE_ID);
		}
        
    }
   
    @Override
	protected void onStart() {
		super.onStart();
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		timer = new Timer();
		timer.schedule(new DataTask(), 1000, 1000);//一秒后执行，一秒执行一次
		
		//判断是否是新安装或者第一次进APP
//		init();暂时不需要
		
		new LoadInitInfo().start();
		new GetLocation().start();
//		TypefaceFactory.getTypeface(this, "HWZS.TTF");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 判断版本信息
				if (null != version && !"".equals(version)) {
					if (version.endsWith("x")) {
						isMust = true;
						version = version.replace("x", "");
					}
					String version_old = getVersionName();
					boolean haveNewVersion = version_old.compareTo(version) < 0;
					if (haveNewVersion) {// 有新版本	
						alertUpdateInfo();
					} else {
						// 展示广告图
						loader.displayImage(adImgUrl, image, options,
								displayListenter);
					}
				} else {
					// 展示广告图
					loader.displayImage(adImgUrl, image, options,
							displayListenter);
				}
				break;
			case 2:
				callMainFragment();
				break;
			case A:
				goviewpager();
				break;
			}
		}
	}
	
	private void initImageLoadAndDisPlayImageOptions() {
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.cover1)
				.showImageForEmptyUri(R.drawable.cover1)
				.showImageOnFail(R.drawable.cover1).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0)).build();
	}
	
	
	private void init() {
		SharedPreferences pePreferences = getSharedPreferences("isFirstEnterApp",
				MODE_PRIVATE);
		C = pePreferences.getBoolean("isFirstEnterApp", true);
		Log.i("isFirstEnterApp", C + "");
		if (C) {
			if (null != timer)
				timer.cancel();// 取消计时器
			handler.sendEmptyMessageDelayed(A, 2000);
			Editor editor = pePreferences.edit();
			editor.putBoolean("isFirstEnterApp", false);
			editor.commit();
		} 
	}
	
	private void setDriveWidthAndHeight4StaticValue() {
 		Tools.getScreenWidth(this);
 	}
	
	/**
	 * 安装 或者第一次进去APP  会有一个推广
	 * **/
	private void goviewpager() {
		Log.e("goviewpager", "新安装APP,进入介绍页");
		Intent intent = new Intent(MainActivity.this, YDActivity.class);
		startActivity(intent);
		finish();
	}
	
	public String getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}
		return packInfo.versionName;
	}
	
	
	/***
	 * 唤出APP主界面
	 */
	private void callMainFragment() {
		// this.quitFullScreen();  Home_mainactivity  MainFragment
		Log.e("callMainFragment","唤出APP主界面");
		
		Intent intent = new Intent(this, SelectLanguageActivity.class);
		// 如果是通过信息推送开打应该，要保留相关信息
		startActivity(intent);
		
		Intent _intent = new Intent();
		if (newsID > 0) {
			_intent.putExtra("newsID", newsID);
			_intent.putExtra("newsId", newsID);
			_intent.putExtra("type", type);
			_intent.putExtra("infoType", infoType);
			_intent.putExtra("url",url);
			_intent.putExtra("liveid", liveId);
			_intent.putExtra("title", contentTitle);
			_intent.putExtra("faceImage", faceImage);
			_intent.putExtra("summary", contentText);
			_intent.putExtra("image_url", faceImage);
			_intent.putExtra("shareimg", faceImage);
			
			switch (type) {
			case PublicValue.NEWS_STYLE_WORD:
				_intent.setClass(MainActivity.this, NewsDetailActivity.class);
				break;
			case PublicValue.NEWS_STYLE_IMG:
				_intent.setClass(MainActivity.this, ImgNewsDetailActivity.class);
				break;
			case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
				_intent.putExtra("url", url);
				_intent.putExtra("shareimg",faceImage);
				_intent.setClass(MainActivity.this, LinkDetailActivity.class);
				break;
			default:
				_intent.setClass(MainActivity.this, NewsDetailActivity.class);
				break;
			}
			
			startActivity(_intent);
		}
		
		finish();
	}
	
	private void alertUpdateInfo() {
		if (null != timer)
			timer.cancel();// 取消计时器
		if (!isMust) {
			ArtAlertDialog artAlertDialog = new ArtAlertDialog(this,
					description, "发现有新版本，是否更新？", "更新", "继续",
					new OnArtClickListener() {
						@Override
						public void okButtonClick() {
							// TODO Auto-generated method stub
							showUpdataDialog();
						}

						@Override
						public void cancelButtonClick() {
							// TODO Auto-generated method stub
							callMainFragment();
						}
					});
			// 点击其他区域不消失
			artAlertDialog.setCanceledOnTouchOutside(false);
			artAlertDialog.show();
		} else {
			ArtAlertDialog artAlertDialog = new ArtAlertDialog(this,
					description, "发现有新版本，是否更新？", "更新", "继续",
					new OnArtClickListener() {
						@Override
						public void okButtonClick() {
							// TODO Auto-generated method stub
							showUpdataDialog();
						}

						@Override
						public void cancelButtonClick() {
							// TODO Auto-generated method stub
							callMainFragment();
						}
					});
			// 点击其他区域不消失
			artAlertDialog.setCanceledOnTouchOutside(false);
			artAlertDialog.show();
		}
	}
	
	protected void showUpdataDialog() {
		downLoadApk();
	}
	
	private class DataTask extends TimerTask {
		public DataTask() {
			time = 0;
		}

		@Override
		public void run() {
			time++;// 加一秒
			if (time >= 4) {
				handler.sendEmptyMessage(2);
				timer.cancel();
			}
		}
	}
	
	private class AnimateFirstDisplayListener extends
		SimpleImageLoadingListener {
	
		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		
		/* 加载广告 */
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	private String getUID() {
		try {
			TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			String uniqueId = deviceUuid.toString();
			return uniqueId.replace("-", "");
		} catch (Exception e) {
			return UUID.randomUUID().toString();
		}
	}
	
	/****
	 * 加载APP初始化信息
	 * 
	 * @author yilang
	 * 
	 */
	private class LoadInitInfo extends Thread {
		@Override
		public void run() {
			MainServer mserver = new MainServer();
			String resultStr = mserver.getNewsCategoryAndADPath();
			Message msg = new Message();
			Log.e("info", "++++++++++++++++" + resultStr);
			try {
				Thread.sleep(2000);
				JSONObject json = new JSONObject(resultStr);
				if (json.getInt("ret") == 1) {// 成功
					// 获取栏目列表的信息
//					readData(json);
					PublicValue.RUNIMAGE_SERVER_PATH = json.getString("picServicePath");
					JSONObject versionJson = json.getJSONObject("version");
					if (null != versionJson) {
						version = versionJson.getString("version");
						PublicValue.lastVersion = version;
						downloadUrl = versionJson.getString("url");
						description = versionJson.getString("description");
					}
					JSONObject adJson = json.getJSONObject("ad");
					if (null != adJson) {
						// 在些处可以判断网络情况而选择图片的大小
						adImgUrl = adJson.getString("filepath") + "/L_"
								+ adJson.getString("fileName");
						msg.what = 1;
					}
				}
			} catch (Exception e) {

			}
			handler.sendMessage(msg);
		}
	}
	
	private class GetLocation extends Thread {
		@Override
		public void run() {
			MainServer mserver = new MainServer();
			String resultStr = mserver.getLoacation(address);
			Message msg = new Message();
			Log.e("info", "++++++++++++++++" + resultStr);
			try {
				Thread.sleep(1000);
				JSONObject json = new JSONObject(resultStr);
				if (json.getString("status").equals("OK")) {// 成功
					JSONObject result =new JSONObject(json.getString("result")) ;
					Gson gson = new Gson();
					AddressComponent locationCity = gson.fromJson(result.getString("addressComponent"),
							AddressComponent.class);
					PublicValue.LOCATIONCITY = null;
					PublicValue.LOCATIONCITY = locationCity;
				}
			} catch (Exception e) {

			}
			handler.sendMessage(msg);
		}
	}
	
	
	
	/***
	 * 读取栏目信息
	 * 
	 * @param json
	 * @throws JsonSyntaxException
	 * @throws JSONException
	 */
	private void readData(JSONObject json) throws JsonSyntaxException,
			JSONException {
		JSONArray categorysJson = json.getJSONArray("listCategory");
		Gson gson = new Gson();
		/* 保存栏目数据到文件 */
		Map<String, String> map = new HashMap<String, String>();
		map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_MW, categorysJson.toString());
		Log.i("tostring", categorysJson.toString());
		SharedPreferencesTools.setValue(MainActivity.this, map,
				PublicValue.WORD_NEWS_CATEGORY_FILE_MW);
		
		PublicValue.CATEGORYS.clear();

		for (int i = 0; i < categorysJson.length(); i++) {

			Category c = gson.fromJson(categorysJson.getString(i),
					Category.class);
			if (c != null)
				PublicValue.CATEGORYS.add(c);
		}
	}
	
	public void downLoadApk() {

		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.setCancelable(false);
		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
			}
		});
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(downloadUrl, pd);
					sleep(1000);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					pd.dismiss(); // 结束掉进度条对话框
					Message msg = new Message();
					Bundle b = new Bundle();
					msg.what = 5;
					b.putInt("ret", 0);
					msg.setData(b);
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	
	// 从服务端下载apk文件
	public File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			// pd.setMax(conn.getContentLength());
			pd.setMax(100);
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress((total * 100) / conn.getContentLength());
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
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
