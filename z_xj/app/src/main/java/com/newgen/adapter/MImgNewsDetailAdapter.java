package com.newgen.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newgen.UI.MongolTextView;
import com.newgen.UI.ZoomImageView;
import com.newgen.domain.NewsFile;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MImgNewsDetailAdapter extends PagerAdapter {
	ImageLoader picLoader;
	
	DisplayImageOptions options;
	LayoutInflater inflater;
	List<NewsFile> images;
	String newsTitle;
	Context context;
	AnimateFirstDisplayListener displayListener = new AnimateFirstDisplayListener();
	public MImgNewsDetailAdapter(List<NewsFile> imgs, String newsTitle,
			Context context) {
		picLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		inflater = LayoutInflater.from(context);
		this.images = imgs;
		this.newsTitle = newsTitle;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0.equals(arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View v = inflater.inflate(R.layout.img_news_detail_item_m, view, false);
		assert v != null;
		ZoomImageView pic = (ZoomImageView) v.findViewById(R.id.image);
		MongolTextView content = (MongolTextView) v.findViewById(R.id.imgState);

		NewsFile img = images.get(position);
//		imageNumber.setText((position + 1) + "/" + images.size());
//		img.getSummary()
		content.setTextColor(Color.parseColor("#FFFFFF"));
		content.setText((position + 1) + "/" + images.size()+"    "+newsTitle+"\n"+"    "+img.getSummary());
		
		picLoader.displayImage(
				PublicValue.IMAGE_SERVER_PATH + img.getFilePath()
						+ PublicValue.IMG_SIZE_M + img.getFileName(), 
				pic,options,displayListener);
		
		view.addView(v, 0);
		return v;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

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

}
