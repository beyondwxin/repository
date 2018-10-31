package com.newgen.adapter;

import java.util.List;

import com.newgen.domain.NewsMix;
import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectListAdapter extends BaseAdapter{
	
	static Context context;
	List<NewsMix> list;
	boolean isNight = false;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	
	public CollectListAdapter(Context context, List<NewsMix> list,boolean isNight) {
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
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		NewsMix newsMix = list.get(position);
		NewsPub news = newsMix.getNewsPub();
		Article article = newsMix.getArticle();
				
		if(convertView==null){
			holder = new ViewHolder();
			
			convertView = layoutInflater.inflate(R.layout.collect_list_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.faceImage = (ImageView) convertView.findViewById(R.id.collect_faceImage);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		if(newsMix.getIsEpaper()==0){
			holder.title.setText(news.getTitle());
			if (news.getNewsPubExt().getFaceimgpath() == null|| "".equals(news.getNewsPubExt().getFaceimgpath())) {// 没有封面图
				holder.faceImage.setVisibility(View.GONE);
			} else {
				holder.faceImage.setVisibility(View.VISIBLE);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ news.getNewsPubExt().getFaceimgpath(), holder.faceImage,
						options);
			}
		}else{
			holder.title.setText(article.getTitle());
			if (article.getImages() == null|| "".equals(article.getImages())) {// 没有封面图
				holder.faceImage.setVisibility(View.GONE);
			} else {
				holder.faceImage.setVisibility(View.VISIBLE);
				imageLoader.displayImage(article.getImages(), holder.faceImage,
						options);
			}
		}
		
		convertView.setTag(holder);
		return convertView;
	}
	
	
	private static class ViewHolder {

		private ImageView faceImage;
		private TextView title;
		
	}

}
