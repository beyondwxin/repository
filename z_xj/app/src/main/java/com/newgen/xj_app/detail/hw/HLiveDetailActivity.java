package com.newgen.xj_app.detail.hw;

import java.util.ArrayList;

import com.newgen.UI.FontTextView;
import com.newgen.fragment.ChatFragmentItem;
import com.newgen.fragment.LiveFragmentItem;
import com.newgen.share.ShareTools;
import com.newgen.xj_app.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * 直播详情界面
 * @author Administrator
 */


public class HLiveDetailActivity extends FragmentActivity {
	
	ArrayList<Fragment> fragmentsList;
	FragmentTransaction fragmentTransaction;
	Button back;
	FontTextView textView_live, textView_chat,createtime_text;
	TextView title_livedetile;
	ViewPager live_pager;
	ChatFragmentItem chatFragmentItem;
	LiveFragmentItem liveFragmentItem;
	String title = null;
	String liveid = null;
	String image_url = null;
	String createtime=null;
	int livestate;
	int newsid;
	Button share;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_detail);
		
		title = getIntent().getStringExtra("title");
		liveid = String.valueOf(getIntent().getIntExtra("liveid", 0));
		image_url = getIntent().getStringExtra("image_url");
		createtime=getIntent().getStringExtra("createtime");
		livestate=getIntent().getIntExtra("livestate", 0);
		
		back = (Button) findViewById(R.id.back);
		textView_live = (FontTextView) findViewById(R.id.textView_live);
		textView_chat = (FontTextView) findViewById(R.id.textView_chat);
		live_pager = (ViewPager) findViewById(R.id.live_pager);
		title_livedetile = (TextView) findViewById(R.id.title_livedetile);
		createtime_text=(FontTextView) findViewById(R.id.createtime_text);
		createtime_text.setText(createtime);
		share=(Button)findViewById(R.id.share);
		
		title_livedetile.setText(title);
		back.setOnClickListener(new Click());
		newsid = Integer.parseInt(liveid);
		liveFragmentItem = new LiveFragmentItem();
		chatFragmentItem = new ChatFragmentItem();
		Bundle bundle = new Bundle();
		bundle.putInt("newsid", newsid);
		liveFragmentItem.setArguments(bundle);
		
		Bundle bundle2 = new Bundle();
		bundle2.putInt("newsid", newsid);
		bundle2.putString("title", title);
		bundle2.putString("ima_url", image_url);
		bundle2.putInt("livestate", livestate);
		chatFragmentItem.setArguments(bundle2);
		
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(liveFragmentItem);
		fragmentsList.add(chatFragmentItem);
		MyFragmengtAdapter adapter = new MyFragmengtAdapter(
				getSupportFragmentManager(), fragmentsList);
		live_pager.setAdapter(adapter);
		live_pager.setCurrentItem(0);
		textView_live.setTextColor(getResources().getColor(R.color.bjs));
		textView_live.setOnClickListener(new TextClick());
		textView_chat.setOnClickListener(new TextClick());
		
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareTools share = new ShareTools();
				// NewsReview newsReview = newsList.get(newsId);
				// share.showShare(false, null, getActivity(),newsReview);
//				share.Sharelive(false, null, LiveDetailActivity.this, newsid, title,
//						image_url);
			}
		});
		
		/**
		 * viewpager的滑动监听
		 */
		live_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					textView_live.setTextColor(getResources().getColor(
							R.color.bjs));
					textView_chat.setTextColor(Color.GRAY);
					break;
				case 1:
					textView_live.setTextColor(Color.GRAY);
					textView_chat.setTextColor(getResources().getColor(
							R.color.bjs));
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		
	}
	
	
	/**
	 * fragment的适配器
	 */
	public class MyFragmengtAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmengtAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		public MyFragmengtAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

	}
	
	
	/**
	 * textview的监听事件
	 */
	public class TextClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textView_live:
				live_pager.setCurrentItem(0);
				break;

			case R.id.textView_chat:
				live_pager.setCurrentItem(1);
				break;
			}
		}
	}

	class Click implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == back) {
				finish();
			}
		}

	}
	
	
	
}
