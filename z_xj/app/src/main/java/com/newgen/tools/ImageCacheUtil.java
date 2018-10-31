package com.newgen.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

/**
 * ImageCacheLoader工具
 * @auther sy
 */
public class ImageCacheUtil {

	/**
	 * 必须在application create的时候调
	 * @param context
	 */

	public static void init(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().threadPoolSize(5)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
				.writeDebugLogs().build();															// necessary
		ImageLoader.getInstance().init(config);
	}

	// options
	public interface OPTIONS {
		
		public DisplayImageOptions default_options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showStubImage(R.drawable.image_load_error)
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.cacheInMemory()
				.cacheOnDisc()
				.build();

	}

}
