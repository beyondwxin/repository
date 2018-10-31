package com.newgen.fragment.ww;


import com.newgen.UI.CircleImageView;
import com.newgen.domain.Member;
import com.newgen.xj_app.user.LoginActivity;
import com.newgen.xj_app.user.SettingActivity;
import com.newgen.xj_app.user.SuggestionActivity;
import com.newgen.xj_app.user.UpdateUserInfoActivity;
import com.newgen.xj_app.ww.ActiveActivity;
import com.newgen.xj_app.ww.ChannelActivity;
import com.newgen.xj_app.ww.CollectListActivity;
import com.newgen.xj_app.ww.ComplainListActivity;
import com.newgen.xj_app.ww.LiveListActivity;
import com.newgen.xj_app.ww.MessageActivity;
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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PersonalFragment extends Fragment{
	
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
	Handler myHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_personal, container, false);
		
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
							UpdateUserInfoActivity.class);
					startActivity(intent);
				}else{//进入登录
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivityForResult(intent, LoginCode);
				}
			}else if(v==my_message){
				Intent intent = new Intent(getActivity(),
						MessageActivity.class);
				startActivityForResult(intent, LoginCode);
			}else if(v==my_collect){
				Intent intent = new Intent(getActivity(),
						CollectListActivity.class);
				startActivity(intent);
			}else if(v==user_setting){
				Intent intent = new Intent(getActivity(),
						SettingActivity.class);
				startActivity(intent);
			}else if(v==my_subscription){
				Intent intent = new Intent(getActivity(),
						ChannelActivity.class);
				startActivity(intent);
				getActivity().finish();
			}else if(v==my_active){
				Intent intent = new Intent(getActivity(),
						ActiveActivity.class);
				startActivity(intent);
			}else if(v==my_suggestion){
				Intent intent = new Intent(getActivity(),
						SuggestionActivity.class);
				startActivity(intent);
			}else if(v==my_live){
				Intent intent = new Intent(getActivity(),
						LiveListActivity.class);
				startActivity(intent);
			}else if(v==my_complain){
				Intent intent = new Intent(getActivity(),
						ComplainListActivity.class);
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
 			user_name.setText("كىرىش");
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
 	   *
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
