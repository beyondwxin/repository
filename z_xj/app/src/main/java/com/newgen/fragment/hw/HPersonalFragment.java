package com.newgen.fragment.hw;


import com.newgen.UI.CircleImageView;
import com.newgen.domain.Member;
import com.newgen.xj_app.hw.HActiveActivity;
import com.newgen.xj_app.hw.HChannelActivity;
import com.newgen.xj_app.hw.HCollectListActivity;
import com.newgen.xj_app.hw.HComplainListActivity;
import com.newgen.xj_app.hw.HLiveListActivity;
import com.newgen.xj_app.hw.HMessageActivity;
import com.newgen.xj_app.user.HLoginActivity;
import com.newgen.xj_app.user.HSettingActivity;
import com.newgen.xj_app.user.HSuggestionActivity;
import com.newgen.xj_app.user.HUpdateUserInfoActivity;
import com.newgen.xj_app.ww.ComplainListActivity;
import com.newgen.xj_app.ww.LiveListActivity;
import com.newgen.server.UserServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HPersonalFragment extends Fragment{
	
	Dialog dialog;
	private View mView;
	private CircleImageView loginButton;
	private TextView user_name;
	private ImageView my_message,my_collect;
	private ImageView user_setting;
	private RelativeLayout my_subscription,my_active,my_suggestion,my_live,my_complain;
	int LoginCode = 1;  
	ImageLoader loader;
	DisplayImageOptions options;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_personal_h, container, false);
		
		PublicValue.USER = Tools.getUserInfo(getActivity());
		
		initWight();
		initListener();
		initImageLoadAndDisplayImageOptions();
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		PublicValue.USER = Tools.getUserInfo(getActivity());
		checkUserIsLogin();
	}
	
	
	private void initWight() {
		// TODO Auto-generated method stub
		loginButton = (CircleImageView)mView.findViewById(R.id.loginButton);
		user_name = (TextView)mView.findViewById(R.id.user_name);
		my_collect = (ImageView)mView.findViewById(R.id.my_collect);
		my_message = (ImageView)mView.findViewById(R.id.my_message);
		user_setting = (ImageView)mView.findViewById(R.id.user_setting);
		my_subscription = (RelativeLayout)mView.findViewById(R.id.my_subscription);
		my_active = (RelativeLayout)mView.findViewById(R.id.my_active);
		my_suggestion = (RelativeLayout)mView.findViewById(R.id.my_suggestion);
		my_live = (RelativeLayout)mView.findViewById(R.id.my_live);
		my_complain = (RelativeLayout)mView.findViewById(R.id.my_complain);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		loginButton.setOnClickListener(new OnClick());
		my_collect.setOnClickListener(new OnClick());
		my_message.setOnClickListener(new OnClick());
		user_setting.setOnClickListener(new OnClick());
		my_subscription.setOnClickListener(new OnClick());
		my_active.setOnClickListener(new OnClick());
		my_suggestion.setOnClickListener(new OnClick());
		my_live.setOnClickListener(new OnClick());
		my_complain.setOnClickListener(new OnClick());
	}	
	
	class OnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==loginButton){
				if(PublicValue.USER!=null){//进入编辑页面
					// 用户信息页面
					Intent intent = new Intent(getActivity(),
							HUpdateUserInfoActivity.class);
					startActivity(intent);
				}else{//进入登录
					Intent intent = new Intent(getActivity(),
							HLoginActivity.class);
					startActivityForResult(intent, LoginCode);
				}
			}else if(v==my_message){
				Intent intent = new Intent(getActivity(),
						HMessageActivity.class);
				startActivityForResult(intent, LoginCode);
			}else if(v==my_collect){
				Intent intent = new Intent(getActivity(),
						HCollectListActivity.class);
				startActivity(intent);
			}else if(v==user_setting){
				Intent intent = new Intent(getActivity(),
						HSettingActivity.class);
				startActivity(intent);
			}else if(v==my_subscription){
				Intent intent = new Intent(getActivity(),
						HChannelActivity.class);
				startActivity(intent);
				getActivity().finish();
			}else if(v==my_active){
				Intent intent = new Intent(getActivity(),
						HActiveActivity.class);
				startActivity(intent);
			}else if(v==my_suggestion){
				Intent intent = new Intent(getActivity(),
						HSuggestionActivity.class);
				startActivity(intent);
			}else if(v==my_live){
				Intent intent = new Intent(getActivity(),
						HLiveListActivity.class);
				startActivity(intent);
			}else if(v==my_complain){
				Intent intent = new Intent(getActivity(),
						HComplainListActivity.class);
				startActivity(intent);
			}
		}
		
	}
	
	// 回调方法，从第二个页面回来的时候会执行这个方法  
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // 根据上面发送过去的请求吗来区别  
        switch (requestCode) {  
        case 1:  
        	PublicValue.USER = Tools.getUserInfo(getActivity());
        	checkUserIsLogin();
            break;  
        default:  
            break;  
        }  
    }  
    
    // 登陆
 	public void checkUserIsLogin() {
 		Member m = Tools.getUserInfo(getActivity());
 		if (m != null) {
 			user_name.setText(m.getNickname());
 			Tools.log(m.getPhoto());
 			loader.displayImage(m.getPhoto(), loginButton, options);
 		} else {
 			user_name.setText("登录");
 			loginButton.setImageResource(R.drawable.user_login_btn);
 		}
 	}
 	
 	private void initImageLoadAndDisplayImageOptions() {
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.user_login_btn)
				.showImageOnFail(R.drawable.user_login_btn)
				.showImageOnLoading(R.drawable.user_login_btn)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}
 	
 	/* 改变App当前Window亮度
 	 * @param brightness
 	 */
 	public void changeAppBrightness(int brightness) {
 		
 		Window window = getActivity().getWindow();
 		WindowManager.LayoutParams lp = window.getAttributes();
 		if (brightness == -1) {
 			lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
 		} else {
 			lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
 		}
 		window.setAttributes(lp);
 	}
	  
}
