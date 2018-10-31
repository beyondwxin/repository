package com.newgen.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.newgen.init.MyApplication;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class FileUtils {
	
	/** 首先默认个文件保存路径 */
	private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
	private static final String SAVE_REAL_PATH = SAVE_PIC_PATH+"/good/savePic";//保存的确切位置
	
	
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/AndroidImage";
	
	public FileUtils(Context context) {
		mDataRootPath = context.getCacheDir().getPath();
	}
	
	/**
	 * 获取储存Image的目录
	 * 
	 * @return
	 */
	public String getStorageDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME
				: mDataRootPath + FOLDER_NAME;
	}

	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * 
	 * @param fileName
	 * @param bitmap
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException {
		if (bitmap == null) {
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}
		/**
		 * 将图片保存到文件夹
		 * @param bm
		 * @param fileName
		 * @param path
		 * @throws IOException
		 */
	public static void saveFile(Bitmap bm, String fileName, String path) throws IOException {
		String subForder = SAVE_REAL_PATH + path;
		File foder = new File(subForder);
		if (!foder.exists()) {
		foder.mkdirs();
		}
		File myCaptureFile = new File(subForder, fileName);
		if (!myCaptureFile.exists()) {
		myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(foder);
		intent.setData(uri);
		MyApplication.getInstance().sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
		}
	/**
	 * 将图片保存到系统图库
	 * @param context
	 * @param bmp
	 */
	public static void saveImageToGallery(Context context, Bitmap bmp) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
		
//		File appDir = new File(SAVE_PIC_PATH, "Boohee");
		
		
	    if (!appDir.exists()) {
	        appDir.mkdir();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    Log.i("info", fileName);
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
		}
	    
	    // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
//	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir)));
	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
//	    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.jpg"))));
	
	}
	
	/**
	 * 从手机或者sd卡获取Bitmap
	 * 
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName) {

//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = false;
//		options.inSampleSize =2; // width，hight设为原来的十分一
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		
		return BitmapFactory.decodeFile(getStorageDirectory() + File.separator
				+ fileName, opt);
		// BitmapFactory.decodeFile(getStorageDirectory() + File.separator +
		// fileName);
	}
	
	public Bitmap getInternetBitmap(String url) {  
        Bitmap bm = null;  
        try {  
            URL iconUrl = new URL(url);  
            URLConnection conn = iconUrl.openConnection();  
            HttpURLConnection http = (HttpURLConnection) conn;  
              
            int length = http.getContentLength();  
              
            conn.connect();  
            // 获得图像的字符流  
            InputStream is = conn.getInputStream();  
            BufferedInputStream bis = new BufferedInputStream(is, length);  
            bm = BitmapFactory.decodeStream(bis);  
            bis.close();  
            is.close();// 关闭流  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bm;  
    }

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.exists();
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.length();
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}

		dirFile.delete();
	}

	public int getAllFilesSize() {
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return 0;
		}
		int num = 0;
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				// new File(dirFile, children[i]).get;
				FileInputStream fis;
				try {
					fis = new FileInputStream(dirFile + "/" + children[i]);
					num += fis.available();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return num;

		}
		return 0;
		// dirFile.delete();

	}

}
