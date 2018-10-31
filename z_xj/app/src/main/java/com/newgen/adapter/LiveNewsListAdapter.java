package com.newgen.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.newgen.UI.FontTextView;
import com.newgen.domain.NewsPub;
import com.newgen.tools.BitmapTools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LiveNewsListAdapter extends BaseAdapter {

	Context context;
	List<NewsPub> list;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	
	public LiveNewsListAdapter(Context context, List<NewsPub> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		NewsPub news = list.get(position);
		Holder mhHolder = null;
		if (convertView == null) {
			mhHolder = new Holder();
			convertView = layoutInflater.inflate(R.layout.live_list_item, null);
			mhHolder.imageView_pic = (ImageView) convertView
					.findViewById(R.id.live_pic);
			mhHolder.title = (FontTextView) convertView
					.findViewById(R.id.live_title);
			
			mhHolder.state = (FontTextView) convertView
					.findViewById(R.id.live_state);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (Holder) convertView.getTag();
		}
			if (!news.getLiveFile().equals(null)) {
				
				if (TextUtils.isEmpty(news.getLiveFile().getFilePath())) {
					mhHolder.imageView_pic.setVisibility(View.VISIBLE);
				}
				
				else {
					imageLoader.displayImage(news.getLiveFile().getFilePath(),
							mhHolder.imageView_pic, options, animateFirstListener);
				}
			}
		mhHolder.title.setText(news.getTitle());
		
		int livestate = news.getLivestate();
		
		if (livestate == 0) {
			mhHolder.state.setText("未开始");
		} else if (livestate == 1) {
			mhHolder.state.setText("进行中");
		} else {
			mhHolder.state.setText("已结束");
		}

		return convertView;
	}
	
	class Holder {
		FontTextView title, state;
		ImageView imageView_pic;
	}
	
	private static class AnimateFirstDisplayListener extends
		SimpleImageLoadingListener {
	
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				try {
					ImageView imageView = (ImageView) view;
					Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 4f);
					imageView.setImageBitmap(bm);
					boolean firstDisplay = !displayedImages.contains(imageUri);
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						displayedImages.add(imageUri);
					}
				} catch (Exception e) {
		
				}
			}
		}
	}

}
