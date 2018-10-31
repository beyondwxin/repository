package com.newgen.fragment.ww;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.newgen.UI.HorizontalListView;
import com.newgen.UI.MyDialog;
import com.newgen.UI.MyToast;
import com.newgen.adapter.HorizontalListViewAdapter;
import com.newgen.datetchoose.tools.DatePopupWindows;
import com.newgen.domain.szb.PaperPage;
import com.newgen.fragment.szb.DZBPicFragment;
import com.newgen.server.PaperServer;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;

public class EpaperFragment extends Fragment{
	
	private final String cate = "cate_";
	int cid = 0;
	int sType, index;
	String cname = null;
	
	private View mView;
	Dialog dialog = null;
	String paperName = "sgrb";// 查看报纸
	String dateTime = "";// 查看时间
	String jsonData;
	
//	VerticalViewPager pager;
	ViewPager pager;
	Adapter mAdapter = null;
	boolean isFrist = true;
	private List<PaperPage> pages = new ArrayList<PaperPage>();
	
	PopupWindow popupWindow; 
	private HorizontalListView hlistview;
	HorizontalListViewAdapter hListViewAdapter;
	
	private DZBPicFragment frmt;
	private boolean C = false;
	
	public void setCategory(int cid, int sType, int index, String cname) {
		this.cid = cid;
		this.sType = sType;
		this.index = index;
		this.cname = cname;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.cid = savedInstanceState.getInt("cid");
			this.sType = savedInstanceState.getInt("sType");
			this.index = savedInstanceState.getInt("index");
			this.cname = savedInstanceState.getString("cname");
		}
		// 取缓存数据
		jsonData = SharedPreferencesTools.getValue(getActivity(), cate + cid,
				SharedPreferencesTools.CACHEDATA);
		
		SharedPreferences paper_pePreferences = getActivity().getSharedPreferences("isFirstEnterPaper",
				getActivity().MODE_PRIVATE);
		C = paper_pePreferences.getBoolean("isFirstEnterPaper", true);
		
		mView = inflater.inflate(R.layout.fragment_epaper, container, false);
		
		return mView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("cid", cid);
		outState.putInt("sType", sType);
		outState.putInt("index", index);
		outState.putString("cname", cname);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (isFrist) {
			isFrist = false;
			View v = getView();
			pager = (ViewPager) v.findViewById(R.id.pager);
			pager.setOffscreenPageLimit(0);
			if (null == mAdapter) {
				mAdapter = new Adapter(getChildFragmentManager());
				pager.setAdapter(mAdapter);
			}
			new LoadData().execute();
		}
	}
	
	
	private class LoadData extends AsyncTask<Object, Object, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(!C){
				try {
					dialog = MyDialog.createLoadingDialog(getActivity(),
							getActivity().getString(R.string.get_paper_data));
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
				PaperServer server = new PaperServer();
				String tempStr = server.getPagesJson(paperName, dateTime);
				if (null != tempStr && !"".equals(tempStr))
					jsonData = tempStr;
				JSONObject json = new JSONObject(jsonData);
				if (json.getInt("ret") == 0)
					return 0;
				else
					return 1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			try {
				if (null != dialog)
					dialog.cancel();
				if (result == 1) {
					JSONObject json = new JSONObject(jsonData);
					if (json.getInt("ret") == 1) {
						Gson gson = new Gson();
						JSONArray jsonPages = json.getJSONArray("pages");
						List<PaperPage> temp = new ArrayList<PaperPage>();
						for (int i = 0; i < jsonPages.length(); i++) {
							PaperPage page = gson.fromJson(
									jsonPages.getString(i), PaperPage.class);
							if (null != page)
								temp.add(page);
						}
						if (temp.size() > 0) {
							pages.clear();
							pages.addAll(temp);
							// 通知adapter
							mAdapter = new Adapter(getChildFragmentManager());
							pager.setAdapter(mAdapter);
							if(hListViewAdapter!=null)
								hListViewAdapter.notifyDataSetChanged();
						}
					}

					// 缓存最新数据
					if (null == dateTime || "".equals(dateTime)) {
						SharedPreferencesTools.setValue(getActivity(), cate
								+ cid, jsonData,
								SharedPreferencesTools.CACHEDATA);
					}
					
					if(C){
						SharedPreferences paper_pePreferences = getActivity().getSharedPreferences("isFirstEnterPaper",
								getActivity().MODE_PRIVATE);
						Editor editor = paper_pePreferences.edit();
						editor.putBoolean("isFirstEnterPaper", false);
						editor.commit();
					}
					
				} else {
					MyToast.showToast(getActivity(), R.string.not_paper, 3);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	
	
	private class Adapter extends FragmentStatePagerAdapter {
		private int count = 0;

		public Adapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			// 显示的内容的碎片
			frmt = new DZBPicFragment();
			frmt.setData(pages.get(position), paperName);
			return frmt;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pages == null ? 0 : pages.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
			super.destroyItem(container, position, object);
			Log.i("INFO", "Destroy Item...");
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			if (count > 0) {
				count--;
				return POSITION_NONE;
			}
			return super.getItemPosition(object);
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			count = getCount();
			super.notifyDataSetChanged();
		}

	}

	/***
	 * 在activity 里面点击了版面选择  在fragment 里面创建
	 * 定义popuwindow用于显示版面选项
	 * */
	public void initpopupWindow() {
		// TODO Auto-generated method stub
		if (popupWindow == null) {
	   		LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
	   		View view = mLayoutInflater.inflate(R.layout.epaper_sheet_popup_layout, null);
	   		
	   		hlistview = (HorizontalListView) view.findViewById(R.id.horizon_listview);
		   	hListViewAdapter = new HorizontalListViewAdapter(getActivity(), pages);
		   	hlistview.setAdapter(hListViewAdapter);
		   	
		   	hlistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.e("position", position+"");
//					hListViewAdapter.setSelectIndex(position);
//					hListViewAdapter.notifyDataSetChanged();
					
					if(popupWindow!=null)
						popupWindow.dismiss();
					//切换电子报的版面
					frmt.setData(pages.get(position),paperName);
					pager.setCurrentItem(position); 
				}
			});
		   	
	   		
	   		popupWindow = new PopupWindow(view,LayoutParams.MATCH_PARENT,  
	   				LayoutParams.WRAP_CONTENT);
	   	}
		
   		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd); 
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
	   	popupWindow.showAtLocation(getView().findViewById(R.id.epaper_frame),Gravity.BOTTOM,0,Tools.dip2px(getActivity(), 51));
	   	popupWindow.update();
	}

	
	/***
	 * 在activity 里面点击了往期选择  在fragment 里面创建
	 * 定义popuwindow用于显示日历 ，让用户选择时间
	 * */
	public void creatDatePopupwindow() {
		// TODO Auto-generated method stub
		new DatePopupWindows(getActivity(),this, getView().findViewById(R.id.epaper_frame));
	}
	
	
	public void ReLoadSZB(String dateTime) {
		// TODO Auto-generated method stub
		this.dateTime = dateTime;
		new LoadData().execute();
	}
	

}
