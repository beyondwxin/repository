package com.newgen.adapter;

import java.util.List;

import com.baidu.tts.a.a.c;
import com.newgen.UI.MongolTextView;
import com.newgen.domain.Active;
import com.newgen.domain.Complain;
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

public class MComplainListAdapter extends BaseAdapter{
	
	
	static Context context;
	List<Complain> list;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;	
	DisplayImageOptions options;
	
	public MComplainListAdapter(Context context,List<Complain> newsList) {
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
		Complain complain = list.get(position);
		
		if(convertView==null){
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.complain_list_item_m, null);
			holder.title = (MongolTextView ) convertView.findViewById(R.id.title);
			holder.time = (MongolTextView ) convertView.findViewById(R.id.time);
			holder.state = (MongolTextView )convertView.findViewById(R.id.state);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.title.setText(complain.getTitle());
		holder.time.setText(complain.getCreateTime());
		if(0==complain.getState()){//未处理
			holder.state.setTextColor(Color.RED);
			holder.state.setText("ᠰᠢᠶᠳᠪᠦᠷᠢᠯᠡᠭᠡᠳᠦᠶ ");
		}else if(1==complain.getState()){//处理中
			holder.state.setTextColor(Color.GREEN);
			holder.state.setText("ᠰᠢᠶᠳᠪᠦᠷᠢᠯᠡᠬᠦ ᠳᠤᠮᠳᠠ ");
		}else{//已处理
			holder.state.setTextColor(Color.GRAY);
			holder.state.setText("ᠨᠢᠭᠡᠨᠲᠡ ᠰᠢᠶᠳᠪᠦᠷᠢᠯᠡᠵᠦ ᠂ ");
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		private MongolTextView  title,time,state;
		
	}
	

}
