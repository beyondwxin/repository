package com.newgen.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.newgen.UI.CircleImageView;
import com.newgen.UI.FontTextView;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsPub;
import com.newgen.domain.RunImage;
import com.newgen.tools.BitmapTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.ww.ShowImageActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 现场直播的适配器
 * 
 */
public class LiveAdapter extends BaseAdapter{

	static Context context;
	List<NewsPub> list;
	public int selectIndex;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	AnimateFirstDisplayListener displayListenter = new AnimateFirstDisplayListener();
	AnimateFirstDisplayHrefListener displayHrefListenter = new AnimateFirstDisplayHrefListener();
	OneAnimateFirstDisplayListener oneDisplayListenter = new OneAnimateFirstDisplayListener();

	List<ImageView> pointers;
	int currentPoint;
	TextView imageState;
	List<RunImage> runImages;

	private final int itemType = 4;
	private final int ZERO = 0, ONE = 1, THERE = 2, FOUR = 3;

	public LiveAdapter(Context context, List<NewsPub> list) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.image_load_error)
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0)).build();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null || list.size() <= 0)
			return 0;
		else
			return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return itemType;
	}

	@Override
	public int getItemViewType(int position) {
		NewsPub news = list.get(position);
		List<NewsFile> imgs = news.getLiveFiles();
		Log.i("size", imgs.size() + "");
		// List<NewsFile> imgs = news.getListpic();
		if (null == imgs || imgs.size() <= 0)
			return ZERO;
		else if (imgs.size() <= 2)
			return ONE;
		else if (imgs.size() == 3)
			return THERE;
		else
			return FOUR;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Hodler mHodler = null;
		final NewsPub newsPub = list.get(position);
		int type = getItemViewType(position);
		switch (type) {
		case ZERO:// 无附件图片
			if (convertView == null) {
				mHodler = new Hodler();
				convertView = layoutInflater.inflate(R.layout.live_item, parent,
						false);
				mHodler.text_time = (FontTextView) convertView
						.findViewById(R.id.text_time);
				mHodler.imView_head = (CircleImageView) convertView
						.findViewById(R.id.live_head);
				mHodler.textView = (FontTextView) convertView
						.findViewById(R.id.live_content);
				mHodler.text_name = (FontTextView) convertView
						.findViewById(R.id.text_name);

			} else
				mHodler = (Hodler) convertView.getTag();
			convertView.setTag(mHodler);
			break;
		case ONE: // 一张 二张
			if (convertView == null) {
				mHodler = new Hodler();
				convertView = layoutInflater.inflate(R.layout.live_item1,
						parent, false);
				mHodler.imView_head = (CircleImageView) convertView
						.findViewById(R.id.live_head);
				mHodler.text_time = (FontTextView) convertView
						.findViewById(R.id.text_time);
				mHodler.textView = (FontTextView) convertView
						.findViewById(R.id.live_content);
				mHodler.text_name = (FontTextView) convertView
						.findViewById(R.id.text_name);
				mHodler.pic1 = (ImageView) convertView
						.findViewById(R.id.image_item);
			} else
				mHodler = (Hodler) convertView.getTag();

			convertView.setTag(mHodler);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(0).getFilePath(),
					mHodler.pic1, options, oneDisplayListenter);
			mHodler.pic1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(0).getFilePath());
					context.startActivity(intent);
				}
			});
			break;
		case THERE: // 三张
			if (convertView == null) {
				mHodler = new Hodler();
				convertView = layoutInflater.inflate(R.layout.live_item3,
						parent, false);
				mHodler.imView_head = (CircleImageView) convertView
						.findViewById(R.id.live_head);
				mHodler.text_time = (FontTextView) convertView
						.findViewById(R.id.text_time);
				mHodler.textView = (FontTextView) convertView
						.findViewById(R.id.live_content);
				mHodler.text_name = (FontTextView) convertView
						.findViewById(R.id.text_name);
				mHodler.pic1 = (ImageView) convertView
						.findViewById(R.id.image_item1);
				mHodler.pic2 = (ImageView) convertView
						.findViewById(R.id.image_item2);
				mHodler.pic3 = (ImageView) convertView
						.findViewById(R.id.image_item3);
			} else
				mHodler = (Hodler) convertView.getTag();
			convertView.setTag(mHodler);

			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(0).getFilePath(),
					mHodler.pic1, options, displayHrefListenter);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(1).getFilePath(),
					mHodler.pic2, options, displayHrefListenter);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(2).getFilePath(),
					mHodler.pic3, options, displayListenter);
			mHodler.pic1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(0).getFilePath());
					context.startActivity(intent);
				}
			});
			mHodler.pic2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(1).getFilePath());
					context.startActivity(intent);
				}
			});
			mHodler.pic3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(2).getFilePath());
					context.startActivity(intent);
				}
			});
			break;
		case FOUR: // 四张
			if (convertView == null) {
				mHodler = new Hodler();
				convertView = layoutInflater.inflate(R.layout.live_item4,
						parent, false);
				mHodler.imageView = (ImageView) convertView
						.findViewById(R.id.live_head);
				mHodler.imView_head = (CircleImageView) convertView
						.findViewById(R.id.live_head);
				mHodler.text_time = (FontTextView) convertView
						.findViewById(R.id.text_time);
				mHodler.textView = (FontTextView) convertView
						.findViewById(R.id.live_content);
				mHodler.text_name = (FontTextView) convertView
						.findViewById(R.id.text_name);
				mHodler.pic1 = (ImageView) convertView
						.findViewById(R.id.image_item1);
				mHodler.pic2 = (ImageView) convertView
						.findViewById(R.id.image_item2);
				mHodler.pic3 = (ImageView) convertView
						.findViewById(R.id.image_item3);
				mHodler.pic4 = (ImageView) convertView
						.findViewById(R.id.image_item4);
			} else
				mHodler = (Hodler) convertView.getTag();
			convertView.setTag(mHodler);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(0).getFilePath(),
					mHodler.pic1, options, displayHrefListenter);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(1).getFilePath(),
					mHodler.pic2, options, displayHrefListenter);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(2).getFilePath(),
					mHodler.pic3, options, displayHrefListenter);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveFiles().get(3).getFilePath(),
					mHodler.pic4, options, displayHrefListenter);
			mHodler.pic1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(0).getFilePath());
					context.startActivity(intent);
				}
			});
			mHodler.pic2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(1).getFilePath());
					context.startActivity(intent);
				}
			});
			mHodler.pic3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(2).getFilePath());
					context.startActivity(intent);
				}
			});
			mHodler.pic4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, ShowImageActivity.class);
					intent.putExtra("imgSrc", PublicValue.IMAGE_SERVER_PATH
							+ newsPub.getLiveFiles().get(3).getFilePath());
					context.startActivity(intent);
				}
			});
			break;
		}
		mHodler.text_time.setText(newsPub.getCreateTime());
		mHodler.textView.setText(newsPub.getBody());
		mHodler.text_name.setText("[主持人]" + newsPub.getEditor());
		if (newsPub.getLiveHost() != null) {
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ newsPub.getLiveHost().getTxPath(), mHodler.imView_head,
					options);
		}

		// convertView.setTag(mHodler);
		return convertView;

	}

	// private Object getResources() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	class Hodler {
		ImageView pic1, pic2, pic3, pic4;
		ImageView imageView;
		CircleImageView imView_head;
		FontTextView textView, text_name, text_time;
	}

	private static class AnimateFirstDisplayHrefListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				try {
					LayoutParams params = Tools
							.getLinearLayoutParams((PublicValue.WIDTH - Tools
									.dip2px(context, 77)) / 2, 3 / 4f);
					ImageView imageView = (ImageView) view;
					Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 4f);
					imageView.setImageBitmap(bm);
					imageView.setLayoutParams(params);
					boolean firstDisplay = !displayedImages.contains(imageUri);
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						displayedImages.add(imageUri);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/***
	 * 只有一张图展示时
	 * 
	 * @author yilang
	 * 
	 */
	private static class OneAnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				LayoutParams params = Tools
						.getLinearLayoutParams(
								(PublicValue.WIDTH - Tools.dip2px(context, 20)),
								3 / 5f);
				ImageView imageView = (ImageView) view;
				Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 5f);
				imageView.setImageBitmap(bm);
				imageView.setLayoutParams(params);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				LayoutParams params = Tools
						.getLinearLayoutParams(
								(PublicValue.WIDTH - Tools.dip2px(context, 20)),
								3 / 8f);
				ImageView imageView = (ImageView) view;
				Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 8f);
				imageView.setImageBitmap(bm);
				imageView.setLayoutParams(params);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
