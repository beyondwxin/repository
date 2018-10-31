package com.newgen.adapter;

import java.util.List;


import com.newgen.domain.szb.PaperPage;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<PaperPage> pages;
	ImageLoader imageLoader = null;
	DisplayImageOptions options = null;
	private int selectIndex = -1;
	
	public HorizontalListViewAdapter(Context context,List<PaperPage> pages){
		this.mContext = context;
		this.pages = pages;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	
		imageLoader = ImageLoader.getInstance();
		
		options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.cacheInMemory(true)
				.showImageOnFail(R.drawable.image_load_error)
				.build();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(pages==null)
			return 0;
		else 
			return pages.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.mOrder=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		holder.mOrder.setText("第"+pages.get(position).getVerOrder()+"版");
		imageLoader.displayImage(pages.get(position).getFaceImg(), holder.mImage,options);
		
		return convertView;
		
	}
	
	private static class ViewHolder {
		/**
		 * 版次
		 */
		private TextView mOrder;
		/**
		 * 版面图
		 */
		private ImageView mImage;
	}

	public void setSelectIndex(int position) {
		// TODO Auto-generated method stub
		selectIndex = position;
	}

}
