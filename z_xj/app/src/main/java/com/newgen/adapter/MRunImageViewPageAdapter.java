package com.newgen.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.newgen.domain.RunImage;
import com.newgen.xj_app.detail.mw.MImgNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MLinkDetailActivity;
import com.newgen.xj_app.detail.mw.MNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MSubjectDetailActivity;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MRunImageViewPageAdapter extends PagerAdapter {
	Context context;
	List<RunImage> list;
	LayoutInflater inflater;
	DisplayImageOptions options;
	ImageLoader picLoader;
	AnimateFirstDisplayListener displayListener = new AnimateFirstDisplayListener();

	public MRunImageViewPageAdapter(Context context, List<RunImage> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);

		picLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View v = inflater.inflate(R.layout.view_page_item, view, false);
		assert v != null;
		ImageView pic = (ImageView) v.findViewById(R.id.runImage);
		pic.setOnClickListener(new Click());
		RunImage img = list.get(position);
		pic.setTag(img);
		
		Log.e("runimgURL", PublicValue.RUNIMAGE_SERVER_PATH + img.getImgpath()
				+ PublicValue.IMG_SIZE_M + img.getImgname());
		
		picLoader.displayImage(PublicValue.RUNIMAGE_SERVER_PATH + img.getImgpath()
				+ PublicValue.IMG_SIZE_M + img.getImgname(), pic, options);
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
				 LayoutParams layoutParams = new 
						 LayoutParams(PublicValue.WIDTH, PublicValue.WIDTH * 3 / 5);
				ImageView imageView = (ImageView) view;
				 view.setLayoutParams(layoutParams);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	class Click implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				RunImage image = (RunImage) v.getTag();
				int newsId = image.getNewsid();
				int type = image.getNewstype();
				int infoType = image.getNewsinfotype();
				String url = image.getUrl();
				Intent intent = new Intent();
				if(newsId > 0){//绑定了新闻的情况
					if(infoType == PublicValue.NEWS_TAG_SUBJECT){//专题
						intent.setClass(context, MSubjectDetailActivity.class);
						intent.putExtra("newsId", newsId);
						intent.putExtra("summary", image.getSummary());
						intent.putExtra("faceImage", PublicValue.RUNIMAGE_SERVER_PATH + image.getImgpath()
								+ PublicValue.IMG_SIZE_M + image.getImgname());
						context.startActivity(intent);
					}else{
						switch (type) {
						case PublicValue.NEWS_STYLE_WORD:      //新闻
							intent.setClass(context, MNewsDetailActivity.class);
							intent.putExtra("newsId", newsId);
							context.startActivity(intent);
							break;
						case PublicValue.NEWS_STYLE_IMG:     //图片新闻
							intent.setClass(context, MImgNewsDetailActivity.class);
							intent.putExtra("newsId", newsId);
							context.startActivity(intent);
							break;
						default:
							intent.setClass(context, MNewsDetailActivity.class);
							intent.putExtra("newsId", newsId);
							context.startActivity(intent);
							break;
						}
					}
				}else if(null != url && !"".equals(url)){
					intent.setClass(context, MLinkDetailActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("wbName", "");
					context.startActivity(intent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
