package com.newgen.adapter;

import java.util.List;

import com.newgen.UI.MongolTextView;
import com.newgen.domain.ConvenienceService;
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

public class MServiceAAdapter extends BaseAdapter{
	
	static Context context;
	List<ConvenienceService> list;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	
	public MServiceAAdapter(Context context, List<ConvenienceService> list) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.image_load_error)
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(360))//是否设置为圆角，弧度为多少 
				.build();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null)
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
		if(convertView==null){
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.service_list_item_m, null);
			holder.title = (MongolTextView) convertView.findViewById(R.id.title);
			holder.faceImage = (ImageView) convertView.findViewById(R.id.faceImage);
			holder.line = (View)convertView.findViewById(R.id.line);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		if(position%2!=1)
			holder.line.setVisibility(View.VISIBLE);
		else
			holder.line.setVisibility(View.GONE);
		
		
		holder.title.setText(list.get(position).getName());
		
		if (list.get(position).getImgpath() == null|| "".equals(list.get(position).getImgpath())) {// 没有封面图
			holder.faceImage.setVisibility(View.GONE);
		} else {
			holder.faceImage.setVisibility(View.VISIBLE);
			
			
//				PublicValue.RUNIMAGE_SERVER_PATH
			Log.e("url",PublicValue.RUNIMAGE_SERVER_PATH
					+ list.get(position).getImgpath()+"/M_"+list.get(position).getImgname() );
			
			imageLoader.displayImage(PublicValue.RUNIMAGE_SERVER_PATH
					+ list.get(position).getImgpath()+"/M_"+list.get(position).getImgname(), holder.faceImage,
					options);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {

		private ImageView faceImage;
		private MongolTextView  title;
		private View line;
		
	}

}
