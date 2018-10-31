package com.newgen.fragment.hw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.newgen.UI.SupperPagerSlidingTabStripp;
import com.newgen.UI.SupperPagerSlidingTabStripp.PagerSlidingTabStripAdapterInterface;
import com.newgen.domain.Category;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.hw.HChannelActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HIndexFragment extends Fragment{
	
	private View mView;
	
	int cid = 0;
	
	ImageButton addcolumn;
	ViewPager pager;
	SupperPagerSlidingTabStripp category;
	int categoryTabWidth = 0;
	LinearLayout pager_layout;// pager
	HIndexFragmentItem item = null ;
	
//	ImageLoader loader;
//	DisplayImageOptions options;
	FragmentViewPageAdapter adapter;
	
	private boolean C = false;

	private String isNight;
	
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	
	private Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		isNight = SharedPreferencesTools.getValue(getActivity(),
				SharedPreferencesTools.KEY_NIGHT, SharedPreferencesTools.KEY_NIGHT);
		
		if(isNight.equals("true"))
			mView = inflater.inflate(R.layout.fragment_index_night, container, false);
		else
			mView = inflater.inflate(R.layout.fragment_index, container, false);
		
//		mView = inflater.inflate(R.layout.fragment_index, container, false);
		
//		initImageLoadAndDisPlayImageOptions();
		
		PublicValue.HARDID = SharedPreferencesTools.getValue(getActivity(),
				SharedPreferencesTools.KEY_DEVICE_ID,
				SharedPreferencesTools.KEY_DEVICE_ID);
		
		addcolumn = (ImageButton)mView.findViewById(R.id.addcolumn);
		addcolumn.setOnClickListener(new Click());
		
		//在setCategory之前 先初始化一下 PublicValue.CATEGORYS 
		this.initCategory();
		/* 顶部标题栏 */
		pager = (ViewPager) mView.findViewById(R.id.pager);
		category = (SupperPagerSlidingTabStripp) mView.findViewById(R.id.category);
		
		category.setTextColor(0xFF666666);
		category.setTabTextSelectColor(0xFFFF0000);
		category.setIndicatorColor(0x00000000);

		adapter = new FragmentViewPageAdapter(getChildFragmentManager());

		adapter.setCategory(PublicValue.CATEGORYS);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(0);
		category.setViewPager(pager);
		
		pager.setCurrentItem(PublicValue.CATEGORYS.size()-1);
		
		return mView;
	}
	
	class Click implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==addcolumn){
				Intent intent = new Intent(getActivity(),
						HChannelActivity.class);
				int size = adapter.getCount();
				intent.putExtra("size", size);	
				startActivityForResult(intent, CHANNELREQUEST);
				getActivity().finish();
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void initCategory() {
		// TODO Auto-generated method stub
		SharedPreferences pePreferences = getActivity().getSharedPreferences("isSetedCategory_hw",
				getActivity().MODE_PRIVATE);
		C = pePreferences.getBoolean("isSetedCategory_hw", false);
		Log.i("isSetedCategory_hw", C + "");
		String category = null;
		Gson gson = new Gson();
		JSONArray jsonArray;
		if (!C) {
			// 没有设置过频道
			Map<String, String> map1 = (Map<String, String>) SharedPreferencesTools
					.getValue(getActivity(), PublicValue.WORD_NEWS_CATEGORY_FILE_HW);
			category = map1.get(PublicValue.WORD_NEWS_CATEGORY_FILE_HW);
			
			if(category ==null|| category.equals("")){
				category = Tools.getFromAssets("category.json", getActivity());
			}
			
			try {
				Log.i("tag", "1111111111111111111111111111");
				jsonArray = new JSONArray(category);
				Map<String, String> map = new HashMap<String, String>();
				map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED,
						jsonArray.toString());
				Log.i("tostring", jsonArray.toString());
				SharedPreferencesTools.setValue(getActivity(), map,
						PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED);
				Editor editor = pePreferences.edit();
				editor.putBoolean("isSetedCategory_hw", true);
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			// 设置过频道,或者是没有设置，但是 已经setValue了
			Log.i("tag", "222222222222222222222222");
			Map<String, String> map = (Map<String, String>) SharedPreferencesTools
					.getValue(getActivity(), PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED);
			category = map.get(PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED);
			if (category == null || category.equals("")) {
				category = Tools.getFromAssets("category.json", getActivity());
			}
			Editor editor = pePreferences.edit();
			editor.putBoolean("isSetedCategory_hw", true);
			editor.commit();
		}
		try {
			Tools.log(category);
			jsonArray = new JSONArray(category);
			PublicValue.CATEGORYS.clear();
			for (int i = jsonArray.length()-1; i >=0; i--) {
				Category c = gson.fromJson(jsonArray.getString(i),
						Category.class);
				if (c != null&& c.getShowType()==1||c.getShowType()==4)
					PublicValue.CATEGORYS.add(c);
				if (i == 0)
					cid = c.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();	
			Toast.makeText(getActivity().getApplicationContext(), "数据异常！", Toast.LENGTH_LONG)
					.show();
		}
	}


//	private void initImageLoadAndDisPlayImageOptions() {
//		loader = ImageLoader.getInstance();
//		options = new DisplayImageOptions.Builder()
//				.showImageForEmptyUri(R.drawable.image_load_error)
//				.showImageOnFail(R.drawable.image_load_error)
//				.showImageOnLoading(R.drawable.image_load_error)
//				.resetViewBeforeLoading(true).cacheOnDisc(true)
//				.imageScaleType(ImageScaleType.EXACTLY)
//				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
//				.displayer(new FadeInBitmapDisplayer(300)).build();
//	}
	
	
	private class FragmentViewPageAdapter extends FragmentStatePagerAdapter
		implements  PagerSlidingTabStripAdapterInterface {
		
		private List<Category> categorys;
	
		public void setCategory(List<Category> categorys) {
			this.categorys = categorys;
			notifyDataSetChanged();
		}
	
		public FragmentViewPageAdapter(FragmentManager fm) {
			super(fm);
		} 	
		
		@Override
		public Fragment getItem(int position) {
			Category cate = categorys.get(position);
			item = new HIndexFragmentItem();
			item.setCategory(cate.getId(), position, cate.getName(),cate.getShowType());
			return item;
		}
		
		@Override
		public int getCount() {
			return categorys.size();
		}
		
		@Override
		public CharSequence getPageSummary(int position) {
		
			return categorys.get(position).getName();
		}
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("requestCode:" + requestCode + "------resultCode:"
				+ resultCode);
		switch (requestCode) {
		case CHANNELREQUEST:
			setChangelView();
			Log.i("info", "调用了");
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 当栏目项发生变化时候调用
	 **/
	private void setChangelView() {
		initColumnData();
	}
	
	 
	/** 获取Column栏目 数据 */
	private void initColumnData() {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) SharedPreferencesTools
				.getValue(getActivity(), PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED);
		@SuppressWarnings("unused")
		String category = map.get(PublicValue.WORD_NEWS_CATEGORY_FILE_HW_ED);
		Log.i("aaaaaaaaa", category);
		Gson gson = new Gson();
		JSONArray jsonArray;
		PublicValue.CATEGORYS.clear();
		try {
			jsonArray = new JSONArray(category);
			if (category != null || "".equals(category)) {
				Log.i("22222", "222222");
				for (int i = 0; i < jsonArray.length(); i++) {
					Category c2 = gson.fromJson(jsonArray.getString(i),
							Category.class);
					if (c2 != null)
						PublicValue.CATEGORYS.add(c2);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
		this.category.notifyDataSetChanged();
		pager.setAdapter(adapter);
		pager.setCurrentItem(0);
	}

}
