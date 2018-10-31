package com.newgen.fragment.ww;

import java.util.ArrayList;
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class VideoFragment extends Fragment {
	
	private View mView;
	
	int cid = 0;
	
	ViewPager pager;
	SupperPagerSlidingTabStripp category;
	LinearLayout pager_layout;// pager
	List<Category> VIDEO_CATEGORYS = new ArrayList<Category>();
	
	FragmentViewPageAdapter adapter;
	private String isNight;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_video, container, false);
		
		//在setCategory之前 先初始化一下 PublicValue.CATEGORYS 
		this.initVideoCategory();
		
		
		
		pager = (ViewPager) mView.findViewById(R.id.pager);
		category = (SupperPagerSlidingTabStripp) mView.findViewById(R.id.category);
		category.setTextColor(0xFF666666);
		category.setTabTextSelectColor(0xFFFF0000);
		category.setIndicatorColor(0x00000000);
		
		if(VIDEO_CATEGORYS!=null&&VIDEO_CATEGORYS.size()>0){
			adapter = new FragmentViewPageAdapter(getChildFragmentManager());
			adapter.setCategory(VIDEO_CATEGORYS);
			pager.setAdapter(adapter);
			pager.setOffscreenPageLimit(0);
			category.setViewPager(pager);
			pager.setCurrentItem(VIDEO_CATEGORYS.size()-1);
		}
		return mView;
	}
	
	
	private class FragmentViewPageAdapter extends FragmentStatePagerAdapter
	implements  PagerSlidingTabStripAdapterInterface {
		
		private List<Category> categorys;
		
		public void setCategory(List<Category> categorys) {
			this.categorys = categorys;
			notifyDataSetChanged();
		}

		public FragmentViewPageAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public CharSequence getPageSummary(int position) {
			// TODO Auto-generated method stub
			return categorys.get(position).getName();
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Category cate = categorys.get(position);
			IndexFragmentItem item = null ;
			item = new IndexFragmentItem();
			item.setCategory(cate.getId(), position, cate.getName(),cate.getShowType());
			return item;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return categorys.size();
		}
		
	}

	
	@SuppressWarnings("unchecked")
	private void initVideoCategory() {
		// TODO Auto-generated method stub
		String category = null;
		Gson gson = new Gson();
		JSONArray jsonArray;
		
		// 没有设置过频道
		Map<String, String> map1 = (Map<String, String>) SharedPreferencesTools
				.getValue(getActivity(), PublicValue.WORD_NEWS_CATEGORY_FILE_WW);
		category = map1.get(PublicValue.WORD_NEWS_CATEGORY_FILE_WW);
		if(category ==null|| category.equals("")){
			return ;
		}
		
		try {
			jsonArray = new JSONArray(category);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Tools.log(category);
			jsonArray = new JSONArray(category);
			VIDEO_CATEGORYS.clear();
			for (int i = 0; i <jsonArray.length(); i++) {
				Category c = gson.fromJson(jsonArray.getString(i),
						Category.class);
				if (c != null&& c.getShowType()==2){
					for(int j = c.getChild().size()-1 ;j>=0;j--){
						Category d = c.getChild().get(j);
						if(d!=null)
							VIDEO_CATEGORYS.add(d);
						if (j == 0)
							cid = d.getId();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity().getApplicationContext(), "数据异常！", Toast.LENGTH_LONG)
					.show();
		}
		
	}
	
	
	
	
}
	
	
