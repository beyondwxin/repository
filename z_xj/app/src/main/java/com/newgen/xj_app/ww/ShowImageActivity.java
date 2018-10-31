package com.newgen.xj_app.ww;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.newgen.UI.DragImageView;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.drawable;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;


/****
 * 此 Activity 用于展示单张图片的大图 功能目标：可以放大，缩小，下载
 * 
 * @author yilang
 * 
 */
public class ShowImageActivity extends Activity {
	
	private String imgSrc;// 图片资源地址
	private DragImageView imageview;
	private ImageLoader loader;
	private DisplayImageOptions options;

	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = ZOOM;

	int window_width, window_height;
	private int state_height;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	private ViewTreeObserver viewTreeObserver;
	private Bitmap bitmap;
	
	Dialog loadDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (null != savedInstanceState) {
			imgSrc = savedInstanceState.getString("imgSrc");
			imgSrc = imgSrc.replace("/M_", "/L_");
		} else {
			imgSrc = getIntent().getExtras().getString("imgSrc");
			imgSrc = imgSrc.replace("/M_", "/L_");
		}
		setContentView(R.layout.activity_show_image);
		Log.i("info", imgSrc);
		this.init();
	}
	
	private void init() {
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.showImageOnLoading(R.drawable.image_load_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		loader = ImageLoader.getInstance();
		imageview = (DragImageView) findViewById(R.id.showImage);
		imageview.setmActivity(this);

		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();
		loader.displayImage(imgSrc, imageview, options,
				new AnimateFirstDisplayListener());
	}
	
	
	private final class AnimateFirstDisplayListener extends
		SimpleImageLoadingListener {
	
		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				DragImageView imageView = (DragImageView) view;
		
				viewTreeObserver = imageview.getViewTreeObserver();
				viewTreeObserver
						.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		
							@Override
							public void onGlobalLayout() {
								if (state_height == 0) {
									// 获取状况栏高度
									Rect frame = new Rect();
									getWindow()
											.getDecorView()
											.getWindowVisibleDisplayFrame(frame);
									state_height = frame.top;
									imageview.setScreen_H(window_height
											- state_height);
									imageview.setScreen_W(window_width);
								}
		
							}
						});
		
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
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
