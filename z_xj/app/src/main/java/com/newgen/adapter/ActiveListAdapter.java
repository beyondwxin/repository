package com.newgen.adapter;

import java.util.List;

import com.newgen.domain.Active;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActiveListAdapter extends BaseAdapter{
	
	static Context context;
	List<Active> list;
	boolean isNight = false;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;

	public ActiveListAdapter(Context context,
			List<Active> newsList, boolean isNight) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = newsList;
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
		Active active = list.get(position);
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.active_list_item, null);
			
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.activeType = (TextView)convertView.findViewById(R.id.activeType);
			holder.activeFlag = (TextView)convertView.findViewById(R.id.activeFlag);
			holder.faceImage = (ImageView) convertView.findViewById(R.id.faceImage);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.title.setText(active.getTitle());
		holder.activeType.setText(active.getActiveType());
		holder.activeFlag.setText(active.getActiveFlag());
		
		if(active.getFlag()==1)
			holder.activeFlag.setTextColor(Color.parseColor("#82d169"));
		else 
			holder.activeFlag.setTextColor(Color.parseColor("#9d9d9d"));
		
		
		String imgUrl = "";
		imgUrl = active.getImgpath()+"/M_"+active.getImgname();
		
		if (imgUrl == null|| "".equals(imgUrl)) {// 没有封面图
			holder.faceImage.setVisibility(View.GONE);
		} else {
			holder.faceImage.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imgUrl, holder.faceImage,
					options);
		}
		
		convertView.setTag(holder);
		return convertView;
	}
	
	
	private static class ViewHolder {

		private ImageView faceImage;
		private TextView title,activeType,activeFlag;
		
	}

}
