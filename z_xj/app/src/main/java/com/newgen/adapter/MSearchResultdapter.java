package com.newgen.adapter;

import java.util.List;

import com.newgen.UI.MongolTextView;
import com.newgen.domain.NewsPub;
import com.newgen.xj_app.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MSearchResultdapter extends BaseAdapter{
	
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<NewsPub> list;
	private boolean isNight;
	private String key;

	public MSearchResultdapter(Context context,List<NewsPub> List,String key,boolean isNight){
		this.mContext = context;
		this.list = List;
		this.key = key;
		this.isNight = isNight;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list!=null)
			return list.size();
		else 
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = mInflater.inflate(R.layout.search_result_item_m, null);
			
			holder.title = (MongolTextView) convertView.findViewById(R.id.title);
			holder.time = (MongolTextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else
			holder=(ViewHolder)convertView.getTag();
		
		
		int start = list.get(position).getShorttitle().indexOf(key);
		
		if (start != -1) {
			int end = start+key.length();
			SpannableStringBuilder spannable = new SpannableStringBuilder(list.get(position).getShorttitle());
			spannable.setSpan(new ForegroundColorSpan(Color.RED), start, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.title.setText(spannable);	
		}else{
			holder.title.setText(list.get(position).getShorttitle());
		}
		
		holder.time.setText(list.get(position).getPublishtime());
		return convertView;
	}
	
	
	private static class ViewHolder {
		
		private MongolTextView title;
		private MongolTextView time;
		
	}


}
