package com.newgen.datetchoose.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fancyy.calendarweight.KCalendar;
import com.fancyy.calendarweight.KCalendar.OnCalendarClickListener;
import com.fancyy.calendarweight.KCalendar.OnCalendarDateChangedListener;
import com.newgen.fragment.ww.EpaperFragment;
import com.newgen.xj_app.R;

//日期控件
public class DatePopupWindows extends PopupWindow {
	private String date;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private ImageView time_img;
	private String date_time;
	private Dialog proDialog;
	private Activity activity;
	private Fragment fragment;
	private GridView select_time_grid;
	
	private String[] Months = {"一月","二月","三月","四月",
								"五月","六月","七月","八月",
								"九月","十月","十一月","十二月",};
	private String[] Years;
	
	private int year,month;
	private boolean isFirstClick = true;
	
	private List<String> yearList = new ArrayList<String>();
	
//	private int paperid;
//	private String address;

	public DatePopupWindows(Context mContext, final Fragment fragment,View parent) {
		
		activity=(Activity) mContext;
		View view = View.inflate(mContext, R.layout.popupwindow_calendar, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.push_bottom_in_1));
		this.fragment =fragment;
//		this.paperid=paperid_1;
//		this.address=address;

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		final TextView popupwindow_calendar_month = (TextView) view
				.findViewById(R.id.popupwindow_calendar_month);
		final KCalendar calendar = (KCalendar) view
				.findViewById(R.id.popupwindow_calendar);
		
		final GridView select_time_grid = (GridView) view.findViewById(R.id.
				select_time_grid);
		
		initYears();
		select_time_grid.setAdapter(new GridViewAdapter(Years));
		
		Button popupwindow_calendar_bt_enter = (Button) view
				.findViewById(R.id.popupwindow_calendar_bt_enter);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);
		}

		List<String> list = new ArrayList<String>(); // 设置标记列表
		list.add("2014-04-01");
		list.add("2014-04-02");
		calendar.addMarks(list, 0);

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date_time = dateFormat;// 最后返回给全局 date
					Log.e("date_time", date_time);
				}
			}
		});
		
		//监听切换年份
		popupwindow_calendar_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.setVisibility(View.GONE);
				select_time_grid.setVisibility(View.VISIBLE);
				if(isFirstClick)
					select_time_grid.setAdapter(new GridViewAdapter(Years));
				else 
					select_time_grid.setAdapter(new GridViewAdapter(Months));
			}
			
		});
		
		select_time_grid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(isFirstClick){
					isFirstClick = false;
					year = Integer.parseInt(Years[position]);
					select_time_grid.setAdapter(new GridViewAdapter(Months));
				}else{
					month = position+1;
					isFirstClick = true;
					calendar.setVisibility(View.VISIBLE);
					select_time_grid.setVisibility(View.GONE);
					
					popupwindow_calendar_month.setText(year + "年" + month + "月");

					calendar.showCalendar(year, month);
					
					if(month<10)
						date = year+"-0"+month+"-"+"01";
					else 
						date = year+"-"+month+"-"+"01";
					
					calendar.setCalendarDayBgColor(date,
							R.drawable.calendar_date_focused);
				}
			}
		});
		

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});

		// 关闭窗口
		popupwindow_calendar_bt_enter.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (date_time != null && !"".equals(date_time)) {
//					Toast.makeText(activity, date_time, Toast.LENGTH_LONG).show();
					Log.e("date_time", date_time);
					if(fragment!=null){
						((EpaperFragment) fragment).ReLoadSZB(date_time);
					}
				}else if( date != null && !"".equals(date)){
					Log.e("date_time", date);
					if(fragment!=null){
						((EpaperFragment) fragment).ReLoadSZB(date);
					}
				}
				dismiss();
			}
		});
		
	}
	
	/**
	 * 根据系统的年份 往前添加20年的数据
	 * */
	private void initYears() {
		// TODO Auto-generated method stub
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy");       
		String date = sDateFormat.format(new java.util.Date());
		int nowYear = Integer.parseInt(date);
		for(int i = 0;i<20;i++){
			yearList.add( nowYear-i+"");
		}
		Years = new String[yearList.size()];
		yearList.toArray(Years);
	}


	class GridViewAdapter extends BaseAdapter{
		
		private String[] list;
		LayoutInflater layoutInflater;
		
		public GridViewAdapter(String[] list){
			this.list = list;
			layoutInflater = LayoutInflater.from(activity);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.length;
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
			if (convertView == null) {  
				holder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.date_gridview_item, null);
				holder.title = (TextView) convertView.findViewById(R.id.date_);
            } else {  
            	holder=(ViewHolder)convertView.getTag();;  
            }  
			holder.title.setText(list[position]); // 为ImageView设置要显示的图片  
			
			convertView.setTag(holder);
			return convertView;
		}
		
	}
	
	private static class ViewHolder {
		private TextView title;
	}
	
}
