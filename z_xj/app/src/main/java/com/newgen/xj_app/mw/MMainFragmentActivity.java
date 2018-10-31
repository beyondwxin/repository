package com.newgen.xj_app.mw;

import com.igexin.sdk.PushManager;
import com.newgen.fragment.mw.MIndexFragment;
import com.newgen.fragment.mw.MPersonalFragment;
import com.newgen.fragment.mw.MServiceFragment;
import com.newgen.fragment.mw.MVideoFragment;
import com.newgen.fragment.ww.EpaperFragment;
import com.newgen.xj_app.R;
import com.newgen.xj_app.SelectLanguageActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MMainFragmentActivity extends FragmentActivity implements OnPageChangeListener {
	
	Fragment currentFragment;
	MIndexFragment indexFragment; // 
	MVideoFragment videoFragment;
	MServiceFragment serviceFragment;
	MPersonalFragment personalFragment;
	private final static int SCANNIN_GREQUEST_CODE = 1;

	private ImageView iv_go_languagePage;
	private RadioGroup radioGroup;
	private RadioButton index_button,video_button,epaper_button,personal_button;
	private LinearLayout index_header,fill_header;
	private RelativeLayout main;
	private ImageView search_imgView;
	
	private ImageView guide_img,paper_guide_img;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mmain_fragment);
		
		// com.getui.demo.DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), com.newgen.service.PushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), com.newgen.service.IntentService.class);
        
		//初始化控件
        initWidget();
        // 初始化默认显示的界面
        initView();
        //注册监听事件
        initListener();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		if (indexFragment == null) {
			indexFragment = new MIndexFragment();
			addFragment(indexFragment);
			showFragment(indexFragment);
		} else {
			showFragment(indexFragment);
		}
		radioGroup.check(R.id.index_button);
	}
	
	
	private void initWidget() {
		// TODO Auto-generated method stub
		iv_go_languagePage =findViewById(R.id.iv_go_languagePage);
		radioGroup = (RadioGroup)findViewById(R.id.main_radiogroup);
		index_button = (RadioButton)findViewById(R.id.index_button);
		video_button = (RadioButton)findViewById(R.id.video_button);//视频
		epaper_button = (RadioButton)findViewById(R.id.epaper_button);//电子报
		personal_button = (RadioButton)findViewById(R.id.personal_button);
		
		index_header = (LinearLayout)findViewById(R.id.index_header);
//		epaper_header = (LinearLayout)findViewById(R.id.epaper_header);
		fill_header = (LinearLayout)findViewById(R.id.fill_header);
		main = (RelativeLayout)findViewById(R.id.main);
		
//		past_txtView = (TextView)findViewById(R.id.past_txtView);
//		directory_imgView = (ImageView)findViewById(R.id.directory_imgView);
		
//		search_txt = (EditText)findViewById(R.id.search_txt);
		search_imgView = (ImageView)findViewById(R.id.search_imgView);
//		scan_imgView = (ImageView)findViewById(R.id.scan_imgView);
		
//		guide_img = (ImageView)findViewById(R.id.guide_img);
//		guide_img.setBackgroundResource(R.drawable.home1);
//		SharedPreferences pePreferences = getSharedPreferences("isFirstEnterIndex",
//				MODE_PRIVATE);
//		C = pePreferences.getBoolean("isFirstEnterIndex", true);
//		Log.i("isFirstEnterIndex", C + "");
//		if (C) {
//			guide_img.setVisibility(View.VISIBLE);
//			Editor editor = pePreferences.edit();
//			editor.putBoolean("isFirstEnterIndex", false);
//			editor.commit();
//		} 
//		
//		paper_guide_img = (ImageView)findViewById(R.id.paper_guide_img);
//		paper_guide_img.setBackgroundResource(R.drawable.newspaper1);
//		SharedPreferences paper_pePreferences = getSharedPreferences("isFirstEnterPaper",
//				MODE_PRIVATE);
//		D = paper_pePreferences.getBoolean("isFirstEnterPaper", true);
//		Log.i("isFirstEnterPaper", D+ "");
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		iv_go_languagePage.setOnClickListener(new Click());
		index_button.setOnClickListener(new Click());
		video_button.setOnClickListener(new Click());
		epaper_button.setOnClickListener(new Click());
		personal_button.setOnClickListener(new Click());
		
//		past_txtView.setOnClickListener(new Click());
//		directory_imgView.setOnClickListener(new Click());
		
//		search_txt.setOnClickListener(new Click());
		search_imgView.setOnClickListener(new Click());
//		scan_imgView.setOnClickListener(new Click());
		
//		guide_img.setOnClickListener(new Click());
//		paper_guide_img.setOnClickListener(new Click());
	}
	
	
	class Click implements OnClickListener{	

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==index_button){
				if(currentFragment!=null&&!currentFragment.equals(indexFragment)){
					showHeader(true,false);
					if (indexFragment == null) {
						indexFragment = new MIndexFragment();
						addFragment(indexFragment);
						showFragment(indexFragment);
					} else {
						showFragment(indexFragment);
					}
					currentFragment = indexFragment;
				}
			}else if(v==video_button){
				showHeader(true,false);
				if (videoFragment == null) {
					videoFragment = new MVideoFragment();
					addFragment(videoFragment);
					showFragment(videoFragment);
				} else {
					showFragment(videoFragment);
				}
				currentFragment = videoFragment;
			}else if(v==epaper_button){
				showHeader(true,false);
				if (serviceFragment == null) {
					serviceFragment = new MServiceFragment();
					addFragment(serviceFragment);
					showFragment(serviceFragment);
				} else {
					showFragment(serviceFragment);
				}
				currentFragment = serviceFragment;
			}else if(v==personal_button){
				showHeader(false,false);
				if (personalFragment == null) {
					personalFragment = new MPersonalFragment();
					addFragment(personalFragment);
					showFragment(personalFragment);
				} else {
					showFragment(personalFragment);
				}
				currentFragment = personalFragment;
			}
			else if(v==search_imgView){
				Intent intent = new Intent();
				intent.setClass(MMainFragmentActivity.this, MSearchActivity.class);
				startActivity(intent);
			}
			else if (v == iv_go_languagePage){
				Intent intent = new Intent();
				intent.setClass(MMainFragmentActivity.this, SelectLanguageActivity.class);
				startActivity(intent);
			}
		}
		
	}
	
	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		ft.add(R.id.pager_layout, fragment);
		ft.commit();
	}
	
	
	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getSupportFragmentManager()
				.beginTransaction();
		// 设置Fragment的切换动画
		ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

		// 判断页面是否已经创建，如果已经创建，那么就隐藏掉
		if (indexFragment != null) { // 首页
			ft.hide(indexFragment);
		}
		if (videoFragment != null) { // 视频
			ft.hide(videoFragment);
		}
		if (serviceFragment != null) { // 服务
			ft.hide(serviceFragment);
		}
		if (personalFragment != null) { // 我的
			ft.hide(personalFragment);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();
	}
	
	
	
	/**
	 * 更改头部的布局变化
	 * 而且是禁用了左右滑动切换
	 * */
	private void showHeader(boolean isShowIndex,boolean isShowPaper) {
		// TODO Auto-generated method stub
		if(isShowIndex)
			index_header.setVisibility(View.VISIBLE);
		else 
			index_header.setVisibility(View.GONE);
		
		if(!isShowIndex&&!isShowPaper)
			fill_header.setVisibility(View.GONE);
		else 
			fill_header.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
		
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 这个方法有一个参数position，代表哪个页面被选中。当用手指滑动翻页的时候，如果翻动成功了（滑动的距离够长），
	 * 手指抬起来就会立即执行这个方法，position就是当前滑动到的页面。如果直接setCurrentItem翻页，
	 * 那position就和setCurrentItem的参数一致，这种情况在onPageScrolled执行方法前就会立即执行。
	 */
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			radioGroup.check(R.id.index_button);
			break;
		case 1:
			radioGroup.check(R.id.video_button);
			break;
		case 2:
			radioGroup.check(R.id.epaper_button);
			break;
		case 3:
			radioGroup.check(R.id.personal_button);
			break;
		default:
			break;
		}
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				if(resultCode == RESULT_OK){
					Bundle bundle = data.getExtras();
					Toast.makeText(MMainFragmentActivity.this, 
							bundle.getString("result"), 5).show();
				}
			}
			break;
		}
    }	
	
	
	@Override  
	public Resources getResources() {  
	    Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res;  
	}  
	
}
