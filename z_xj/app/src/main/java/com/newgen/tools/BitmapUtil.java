package com.newgen.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class BitmapUtil {
	/**
	 * 生成一个Bitmap图片
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Bitmap returnBitMap(String url) throws IOException {
		Log.i("returnBitMap", "url=" + url);
		URI uri = new URI(url, false, "UTF-8");
		String url_new = uri.toString();
		// URLEncode.encode(url,"utf8");
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url_new);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		conn.setDoInput(true);
		conn.connect();
		try {

			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			Log.e("is", e.getMessage());
			// TODO: handle exception
		}

		return bitmap;
	}

//	public static Bitmap returnBitMapForDzb(String url, Context con)
//			throws IOException {
//		AbImageLoader mAbImageLoader = null;
//		mAbImageLoader = new AbImageLoader(con);
////		mAbImageLoader.setMaxWidth(200);
////		mAbImageLoader.setMaxHeight(200);
//		mAbImageLoader.setLoadingImage(R.drawable.image_loading);
//		mAbImageLoader.setErrorImage(R.drawable.image_error);
//		mAbImageLoader.setEmptyImage(R.drawable.image_empty);
//		mAbImageLoader.
//	}

	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 根据资源文件获取Bitmap
	 * 
	 * @param context
	 * @param drawableId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, String pic_url,
			int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(pic_url);
			HttpURLConnection con;
			con = (HttpURLConnection) url.openConnection(); // 打开连接
			con.setRequestMethod("GET"); // 设置请求方法
			// 设置连接超时时间为5s
			con.setConnectTimeout(5000);
			InputStream in = con.getInputStream(); // 取得字节输入流
			// InputStream stream =
			// context.getResources().openRawResource(drawableId);

			bitmap = BitmapFactory.decodeStream(in, null, options);
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

		return getBitmap(bitmap, screenWidth, screenHight);
	}

	/***
	 * 等比例压缩图片
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
			int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Log.e("jj", "图片宽度" + w + ",screenWidth=" + screenWidth);
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;

		// scale = scale < scale2 ? scale : scale2;

		// 保证图片不变形.
		matrix.postScale(scale, scale);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/***
	 * 保存图片至SD卡
	 * 
	 * @param bm
	 * @param url
	 * @param quantity
	 */
	private static int FREE_SD_SPACE_NEEDED_TO_CACHE = 1;
	private static int MB = 1024 * 1024;
	public final static String DIR = "/sdcard/hypers";

	public static void saveBmpToSd(Bitmap bm, String url, int quantity) {
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return;
		String filename = url;
		// 目录不存在就创建
		File dirPath = new File(DIR);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		File file = new File(DIR + "/" + filename);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, quantity, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/***
	 * 获取SD卡图片
	 * 
	 * @param url
	 * @param quantity
	 * @return
	 * @throws NullPointerException
	 * @throws URIException
	 */
	public static Bitmap GetBitmap(String url, int quantity)
			throws URIException, NullPointerException {
		URI uri = new URI(url, false, "UTF-8");
		String url_new = uri.toString();
		InputStream inputStream = null;
		String filename = "";
		Bitmap map = null;
		URL url_Image = null;
		String LOCALURL = "";
		if (url_new == null)
			return null;
		try {
			filename = url_new;
		} catch (Exception err) {
		}

		LOCALURL = URLEncoder.encode(filename);
		if (Exist(DIR + "/" + LOCALURL)) {
			map = BitmapFactory.decodeFile(DIR + "/" + LOCALURL);
		} else {
			try {
				url_Image = new URL(url_new);
				inputStream = url_Image.openStream();
				map = BitmapFactory.decodeStream(inputStream);
				// url = URLEncoder.encode(url, "UTF-8");
				if (map != null) {
					saveBmpToSd(map, LOCALURL, quantity);
				}
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return map;
	}

	/***
	 * 判断图片是存在
	 * 
	 * @param url
	 * @return
	 */
	public static boolean Exist(String url) {
		File file = new File(DIR + url);
		return file.exists();
	}

	/** * 计算sdcard上的剩余空间 * @return */
	private static int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;

		return (int) sdFreeMB;
	}

}
