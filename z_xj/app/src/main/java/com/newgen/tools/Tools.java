package com.newgen.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.gson.Gson;
import com.newgen.adapter.MPublicNewsAdapter;
import com.newgen.domain.Member;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class Tools {

	/**
	 * 获取手机的硬件地址
	 * 
	 * @param con
	 * @return
	 */
	public static String getMac(Context con) {
		TelephonyManager tm = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						con.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		return uniqueId.replace("-", "");
	}

	private final static boolean ISLOG = true;

	/***
	 * 从网络取得图片
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}
		return drawable;
	}

	/**
	 * http 同步请求
	 * 
	 * @param requestPath
	 * @return
	 */
	public static String executeHttpGet(String requestPath) {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(requestPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.getResponseCode();
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	/***
	 * http post 同步请求
	 * 
	 * @return
	 */
	public static String executeHttpPost(String requestPath) {
		Log.e("dubug", "executeHttpPost");
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(requestPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Charset", "utf-8");
			DataOutputStream dop = new DataOutputStream(
					connection.getOutputStream());
			dop.writeBytes("token=alexzhou");
			dop.flush();
			dop.close();

			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		if (null == context)
			return (int) dpValue;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		if (null == context)
			return (int) pxValue;
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	
	public static Bitmap getBitMBitmap(String urlpath) {  
		
		Bitmap map = null;  
		try {  
			
			URL url = new URL(urlpath);  
			URLConnection conn = url.openConnection();  
			conn.connect();  
			InputStream in;  
			in = conn.getInputStream();  
			map = BitmapFactory.decodeStream(in);
			
			// TODO Auto-generated catch block  
		} catch (IOException e) {
			e.printStackTrace();  
		}  
		return map;  
	}  

	/***
	 * 调总imageView的大小
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @return
	 */
	public static LinearLayout.LayoutParams getLayoutParams(Bitmap bitmap,
			int screenWidth) {

		float rawWidth = bitmap.getWidth();
		float rawHeight = bitmap.getHeight();

		float width = 0;
		float height = 0;

		Log.i("hello", "原始图片高度：" + rawHeight + "原始图片宽度：" + rawWidth);
		Log.i("hello", "原始高宽比：" + (rawHeight / rawWidth));

		height = (rawHeight / rawWidth) * screenWidth;
		width = screenWidth;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				(int) width, (int) height);

		return layoutParams;
	}

	public static LayoutParams getLinearLayoutParams(float screenWidth,
			float HWRate) {
		float width = 0;
		float height = 0;
		// height = (rawHeight / rawWidth) * screenWidth;
		height = screenWidth * HWRate;
		width = screenWidth;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				(int) width, (int) height);
		return layoutParams;
	}

	/**
	 * 视频栏目图片的截取
	 * 
	 * @param screenWidth
	 * @param HWRate
	 * 
	 */
	public static FrameLayout.LayoutParams getFrameLayoutParams(
			float screenWidth, float HWRate) {
		float width = 0;
		float height = 0;
		// height = (rawHeight / rawWidth) * screenWidth;
		height = screenWidth * HWRate;
		width = screenWidth;
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				(int) width, (int) height);
		return layoutParams;
	}

	public static LayoutParams getLinearLayoutParams(Bitmap bitmap,
			float screenWidth) {
		float rawWidth = bitmap.getWidth();
		float rawHeight = bitmap.getHeight();

		float width = 0;
		float height = 0;

		Log.i("hello", "原始图片高度：" + rawHeight + "原始图片宽度：" + rawWidth);
		Log.i("hello", "原始高宽比：" + (rawHeight / rawWidth));

		height = (rawHeight / rawWidth) * screenWidth;
		width = screenWidth;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				(int) width, (int) height);

		return layoutParams;
	}

	public static FrameLayout.LayoutParams getFrameLayoutParams(Bitmap bitmap,
			int screenWidth) {

		float rawWidth = bitmap.getWidth();
		float rawHeight = bitmap.getHeight();

		float width = 0;
		float height = 0;

		Log.i("hello", "原始图片高度：" + rawHeight + "原始图片宽度：" + rawWidth);
		Log.i("hello", "原始高宽比：" + (rawHeight / rawWidth));

		height = (rawHeight / rawWidth) * screenWidth;
		width = screenWidth;
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				(int) width, (int) height);

		return layoutParams;
	}

	/***
	 * 调总imageView的大小
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @return
	 */
	public static FrameLayout.LayoutParams getFrameLayoutLayoutParams(
			Bitmap bitmap, int screenWidth) {

		float rawWidth = bitmap.getWidth();
		float rawHeight = bitmap.getHeight();

		float width = 0;
		float height = 0;

		Log.i("hello", "原始图片高度：" + rawHeight + "原始图片宽度：" + rawWidth);
		Log.i("hello", "原始高宽比：" + (rawHeight / rawWidth));

		height = (rawHeight / rawWidth) * screenWidth;
		width = screenWidth;
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				(int) width, (int) height);

		return layoutParams;
	}

	/**
	 * 查看当前网络是否可用
	 * 
	 * @param act
	 * @return
	 */
	public static boolean checkNetWork(Activity act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/***
	 * 获取当前使用的网络情况，3G,WIFI...
	 * 
	 * @param act
	 * @return
	 */
	public static String getCurrentNetWorkState(Activity act) {
		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return null;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return null;
		}
		return networkinfo.getTypeName().toLowerCase();// 返回全小写
	}

	/**
	 * 仅wifi加载图片的处理 若是true则图片为开
	 */
	public static void loadPicByWifi(ImageLoader imageLoader,
			DisplayImageOptions options, String picUrl, Context context,
			ImageView imageView, SimpleImageLoadingListener animateFirstListener) {
		String wifiOpen = SharedPreferencesTools.getValue((Activity) context,
				SharedPreferencesTools.KEY_WIFI,
				SharedPreferencesTools.KEY_WIFI);
		Log.i("wifiopen", wifiOpen);
		if (wifiOpen != null && !wifiOpen.equals("")
				&& !wifiOpen.equals("null")) {
			boolean isOpen = Boolean.parseBoolean(wifiOpen);
			if (isOpen) {
				if (PublicValue.WIFIOPEND) {
					
					imageView.setBackgroundResource(R.drawable.image_load_error);
					Log.i("AAAA","11111111");
				} else {
					imageLoader.displayImage(picUrl, imageView, options,
							animateFirstListener);
					Log.i("AAAA","2222222");
				}

			} else {
//				imageLoader.displayImage(picUrl, imageView, options,
//						animateFirstListener);
				imageView.setBackgroundResource(R.drawable.image_load_error);
				Log.i("AAAA","3333333");
			}
		} else {
			imageLoader.displayImage(picUrl, imageView, options,
					animateFirstListener);
			Log.i("AAAA","4444444");
		}
	}

	/***
	 * 保存用户信息
	 */
	public static boolean saveUserInfo(Member u, Activity act) {
		SharedPreferences shared = act.getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		Gson gson = new Gson();
		try {
			gson.toJson(u);
			editor.putString("userInfo", gson.toJson(u));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			editor.putInt("id", u.getId());
			editor.putString("name", u.getNickname());
			editor.putString("loginName", u.getMemcode());
			editor.putString("password", u.getPassword());
			editor.putString("email", u.getEmail());
			editor.putString("phone", u.getPhonenumber());
			editor.putString("userpic", u.getPhoto());
			editor.putString("userword", u.getUserword());
		}
		editor.commit();
		return true;
	}

	/**
	 * 读取用户信息
	 * 
	 * @return
	 */
	public static Member getUserInfo(Activity act) {
		SharedPreferences shared = act.getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		Member u = new Member();
		String jsonStr = shared.getString("userInfo", "");
		Gson gson = new Gson();
		if ("".equals(jsonStr) || null == jsonStr) {
			u.setId(shared.getInt("id", 0));
			u.setNickname(shared.getString("name", ""));
			u.setMemcode(shared.getString("loginName", ""));
			u.setPassword(shared.getString("password", ""));
			u.setEmail(shared.getString("email", ""));
			u.setPhonenumber(shared.getString("phone", ""));
			u.setUserpic(shared.getString("userpic", ""));
			u.setUserword(shared.getString("userword", ""));
		} else {
			try {
				u = gson.fromJson(jsonStr, Member.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (u.getMemcode() == null || "".equals(u.getMemcode()))
			return null;
		return u;
	}

	/**
	 * 清空用户信息
	 * 
	 * @return
	 */
	public static boolean cleanUserInfo(Activity act) {
		PublicValue.USER = null;
		Member u = new Member();
		u.setEmail("");
		u.setId(0);
		u.setPassword("");
		u.setMemcode("");
		u.setPhonenumber("");
		u.setUserpic("");
		u.setUserword("");
		return saveUserInfo(u, act);
	}

	public static String uploadImage(Map<String, Object> params, String url) {
		HttpContext httpContext = new BasicHttpContext();
		HttpPost post = new HttpPost(url);
		Set<String> keys = params.keySet();
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (String key : keys) {
				if (key.equals("myfiles")) {
					List<String> imagesPath = (List<String>) params.get(key);
					for (String path : imagesPath) {
						File file = new File(path);
						entity.addPart(key, new FileBody(file));
					}
				} else {
					entity.addPart(
							key,
							new StringBody(URLEncoder.encode(
									(String) params.get(key), "UTF-8")));
					// new StringBody((String) params.get(key)));
				}
			}
			post.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post, httpContext);
			String reslutStr = null;
			if (response.getStatusLine().getStatusCode() == 200) {
				reslutStr = EntityUtils.toString(response.getEntity());
			}
			return reslutStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/***
	 * 保存图片到手机sd卡
	 * 
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveImage(Bitmap bitmap, String savePath, String name)
			throws IOException {
		FileOutputStream b = null;
		File file = new File(savePath);
		if (!file.exists())
			file.mkdirs();// 创建文件夹
		String fileName = savePath + name;
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static float floatPrice(float f) {
		DecimalFormat fnum = new DecimalFormat("###,###,###.##");
		return Float.parseFloat(fnum.format(f));
	}

//	public static String getNewsHtmlUrl(int newsID) {
//		String url = PublicValue.SERVERBASEURL + "html/" + newsID + ".html";
//		return url;
//	}

	/***
	 * 获取前后两次操作间隔时间（不精确）
	 * 
	 * @param last
	 * @param current
	 * @return
	 */
	public static String getTimeInterval(long last, long current) {
		long num = (current - last) / 1000;// 取得间隔多少秒钟
		num /= 60;// 化为分钟
		if (num < 1)
			return ((current - last) / 1000) + "秒前";
		else if (num >= 1 && num < 60)
			return num + "分钟前";
		else {
			return num / 60 + "小时前";
		}
	}

	/***
	 * 截取字符串
	 * 
	 * @param str
	 * @param start
	 * @param number
	 * @return
	 */
	public static String cutString(String str, int start, int number) {
		int length = str.length();
		if (length - start - 1 >= number) {
			return str.substring(start, start + number);
		} else
			return str.substring(start);
	}

	public static void getScreenWidth(Activity activty) {
		DisplayMetrics dm = new DisplayMetrics();
		activty.getWindowManager().getDefaultDisplay().getMetrics(dm);
		PublicValue.WIDTH = dm.widthPixels;
		PublicValue.HEIGHT = dm.heightPixels;
	}

	public static void debugLog(String msg) {
		if (ISLOG) {
			Log.e("debug", msg);
		}
	}

	/**
	 * 获取当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVision(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			return null;
		}
		return packInfo.versionName;
	}

	/****
	 * http Post 请求方法
	 * 
	 * @param params
	 *            参数<key, value> 对，其他可加入File 对象用于文件上传
	 * @param url
	 *            请求地址
	 * @param fileParams
	 *            如果 params 中带有File对象或File在磁盘上的地址， 请填写与key对应的值
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(Map<String, Object> params, String url,
			String... fileParams) throws Exception {
		HttpContext httpContext = new BasicHttpContext();
		HttpPost post = new HttpPost(url);
		Set<String> keys = params.keySet();
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (String key : keys) {
				if (null != fileParams && fileParams.length > 0) {// 如果有传文参数过来
					for (String fileParam : fileParams) {
						if (fileParam.equals(key)) {// 如果当前为文件
							File file = null;
							if (params.get(key).getClass() == String.class) {
								file = new File((String) params.get(key));
							} else if (params.get(key).getClass() == File.class)
								file = (File) params.get(key);
							else {
								throw new Exception("文件参数中有未识别的对象");
							}

							if (!file.exists())
								throw new Exception("文件不存在");
							entity.addPart(key, new FileBody(file));
						}
					}
				} else {
					entity.addPart(
							key,
							new StringBody(URLEncoder.encode(
									(String) params.get(key), "UTF-8")));
				}
			}
			post.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post, httpContext);
			String reslutStr = null;
			if (response.getStatusLine().getStatusCode() == 200) {
				reslutStr = EntityUtils.toString(response.getEntity());
			}
			return reslutStr;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void log(String msg) {
		Log.e("sgrb", msg);
	}

	/****
	 * 读取assets中文件
	 * 
	 * @param fileName
	 * @param activity
	 * @return
	 */
	public static String getFromAssets(String fileName, Activity activity) {
		try {
			InputStreamReader inputReader = new InputStreamReader(activity
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * 时间格式化 毫秒转变为分钟和秒数 1minute = 60second = 60*1000 millisecond
	 * 
	 * @author Administrator return 00:00 格式
	 */
	public static String TimeFormat(int msecond) {

		int second = msecond / 1000;
		int minute = second / 60;
		int second2 = second - minute * 60;

		String minuteS, secondS;

		if (minute >= 10) {
			minuteS = minute + "";
		} else {
			minuteS = "0" + minute;
		}

		if (second2 < 10) {
			secondS = "0" + second2;
		} else {
			secondS = second2 + "";
		}

		return minuteS + ":" + secondS;
	}
	
	
	public static void setListViewHeightBasedOnChildren(GridView listView) {
	    //获得adapter
		MPublicNewsAdapter adapter = (MPublicNewsAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int listCount = 0;
        listCount = adapter.getCount() /3;
        if(listCount*3<adapter.getCount())
        	listCount ++;
        

        int totalHeight = 0;
        for (int i = 0; i < listCount; i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            //计算总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //计算分割线高度
        params.height = totalHeight + (listView.getHeight() * listCount);
        //给listview设置高度
        listView.setLayoutParams(params);
    }
	
//	/**
//     * 判断网络是否连接.
//     */
//    private boolean isNetworkConnected(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null) {
//                for (NetworkInfo ni : info) {
//                    if (ni.getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return false;
//    }


}
