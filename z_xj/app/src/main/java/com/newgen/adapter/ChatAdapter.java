package com.newgen.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.newgen.UI.FontTextView;
import com.newgen.domain.Member;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsPub;
import com.newgen.domain.NewsReview;
import com.newgen.domain.RunImage;
import com.newgen.tools.BitmapTools;
import com.newgen.tools.DisplayTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
	/**
	 * 边看边聊的适配器
	 *
	 */
public class ChatAdapter extends BaseAdapter {
	Context context;
	List<NewsReview>list;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader ;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	List<ImageView> pointers;
	int currentPoint;
	TextView imageState;
	List<RunImage> runImages;
	Member umMember;
		
	public ChatAdapter(Context context, List<NewsReview> list,Member umMember
			) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.umMember=umMember;
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
		Hodler mHodler=null;
		NewsReview newsReview = list.get(position);
//		NewsReview newsReview = newsPub.getListreview().get(position);
		if (convertView==null) {
			mHodler=new Hodler();
			convertView=layoutInflater.inflate(R.layout.chat_item, parent, false);
			mHodler.imageView=(ImageView) convertView.findViewById(R.id.chat_head);
			mHodler.textView=(FontTextView) convertView.findViewById(R.id.chat_content);
			mHodler.text_chatname=(FontTextView) convertView.findViewById(R.id.text_chatname);
			convertView.setTag(mHodler);
		}else {
			mHodler=(Hodler) convertView.getTag();
		}
		if (newsReview.getUserPhoto()!=null && !newsReview.getUserPhoto().equals("")) {
			imageLoader.displayImage(newsReview.getUserPhoto(), mHodler.imageView, options);
		}
		else {
			mHodler.imageView.setImageResource(R.drawable.user_login_btn);
		}
		if (newsReview.getUserName()!=null && !newsReview.getUserName().equals("")) {
			mHodler.text_chatname.setText(newsReview.getUserName());
			Tools.debugLog(newsReview.getUserName());
		}else {
			mHodler.text_chatname.setText("游客");
		}
//		mHodler.text_chatname.setText(newsReview.getUserName());
		mHodler.textView.setText(newsReview.getBody());
		return convertView;
	
	}
	class Hodler {
		ImageView imageView;
		FontTextView textView,text_chatname;
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
