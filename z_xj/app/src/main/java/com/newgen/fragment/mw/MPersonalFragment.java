package com.newgen.fragment.mw;


import java.util.HashMap;
import java.util.Map;

import com.newgen.UI.CircleImageView;
import com.newgen.UI.MongolAlertDialog;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MongolToast;
import com.newgen.domain.Member;
import com.newgen.xj_app.mw.MActiveActivity;
import com.newgen.xj_app.mw.MCollectListActivity;
import com.newgen.xj_app.mw.MComplainListActivity;
import com.newgen.xj_app.mw.MMessageActivity;
import com.newgen.xj_app.user.MLoginActivity;
import com.newgen.xj_app.user.MSuggestionActivity;
import com.newgen.xj_app.user.MUpdateUserInfoActivity;
import com.newgen.xj_app.ww.AboutUsActivity;
import com.newgen.server.UserServer;
import com.newgen.tools.DataCleanManager;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Toast;


public class MPersonalFragment extends Fragment{
	
	Dialog dialog;
	private View mView;
	private CircleImageView loginButton;
	private MongolTextView  user_name;
	private MongolTextView brightness_txt,fontsize_txt,cache_txt,
	version_txt,aboutus_txt,active_txt,suggestion_txt,logout_txt;
	
	int LoginCode = 1;
	ImageLoader loader;
	DisplayImageOptions options;
	
	private LinearLayout brightness_linear,fontsize_linear,
	cache_linear,version_linear,aboutus_linear,active_linear,
	suggestion_linear,logout_linear;
	
	private ImageView message_linear,collect_linear;
	
	
	String version;
	DataCleanManager manager = new DataCleanManager();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.fragment_personal_m, container, false);
		
		PublicValue.USER = Tools.getUserInfo(getActivity());
		version = Tools.getVision(getActivity());
		
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
//		GetPonint m = new GetPonint();
//		new Thread(m).start();
	}
	
	
	private void initWight() {
		// TODO Auto-generated method stub
		loginButton = (CircleImageView)mView.findViewById(R.id.loginButton);
		user_name = (MongolTextView )mView.findViewById(R.id.user_name);
		brightness_txt =(MongolTextView)mView.findViewById(R.id.brightness_txt);
		brightness_linear = (LinearLayout)mView.findViewById(R.id.brightness_linear);
		fontsize_txt = (MongolTextView)mView.findViewById(R.id.fontsize_txt);
		fontsize_linear = (LinearLayout)mView.findViewById(R.id.fontsize_linear);
		message_linear = (ImageView)mView.findViewById(R.id.message_img);
		cache_txt = (MongolTextView)mView.findViewById(R.id.cache_txt);
		cache_linear = (LinearLayout)mView.findViewById(R.id.cache_linear);
		version_txt = (MongolTextView)mView.findViewById(R.id.version_txt);
		version_linear = (LinearLayout)mView.findViewById(R.id.version_linear);
		aboutus_txt = (MongolTextView)mView.findViewById(R.id.aboutus_txt);
		aboutus_linear = (LinearLayout)mView.findViewById(R.id.aboutus_linear);
		collect_linear = (ImageView)mView.findViewById(R.id.collect_img);
		active_txt = (MongolTextView)mView.findViewById(R.id.active_txt);
		active_linear = (LinearLayout)mView.findViewById(R.id.active_linear);
		suggestion_txt = (MongolTextView)mView.findViewById(R.id.suggestion_txt);
		suggestion_linear = (LinearLayout)mView.findViewById(R.id.suggestion_linear);
		logout_txt = (MongolTextView)mView.findViewById(R.id.logout_txt);
		logout_linear = (LinearLayout)mView.findViewById(R.id.logout_linear);
		
		
		user_name.setText("ᠨᠡᠪᠲᠡᠷᠡᠬᠦ");
		user_name.setTextSize(26);
		user_name.setTextColor(Color.parseColor("#000000"));
		
		brightness_txt.setText("ᠭᠡᠷᠡᠯᠳᠦᠴᠡ");
		brightness_txt.setTextSize(24);
		brightness_txt.setTextColor(Color.parseColor("#808080"));
		
		fontsize_txt.setText("ᠦᠰᠦᠭ ᠤᠨ ᠲᠢᠭ");
		fontsize_txt.setTextSize(24);
		fontsize_txt.setTextColor(Color.parseColor("#808080"));
		
		
		cache_txt.setText("ᠠᠶᠠᠳᠠᠯ ᠬᠠᠳᠠᠭᠠᠯᠠᠯᠲᠠ");
		cache_txt.setTextSize(24);
		cache_txt.setTextColor(Color.parseColor("#808080"));
		
		version_txt.setText("ᠬᠡᠪᠯᠡᠯ ᠤᠨ ᠳ᠋ᠤᠭᠠᠷ");
		version_txt.setTextSize(24);
		version_txt.setTextColor(Color.parseColor("#808080"));
		
		aboutus_txt.setText("ᠪᠢᠳᠡᠨ ᠤ ᠲᠤᠬᠠᠢ");
		aboutus_txt.setTextSize(24);
		aboutus_txt.setTextColor(Color.parseColor("#808080"));
		
		active_txt.setText("ᠬᠥᠳᠡᠯᠭᠡᠭᠡᠨ");
		active_txt.setTextSize(24);
		active_txt.setTextColor(Color.parseColor("#808080"));
		
		suggestion_txt.setText("ᠰᠠᠨᠠᠯ ᠰᠠᠨᠠᠭᠤᠯᠭ᠎ᠠ ");
		suggestion_txt.setTextSize(24);
		suggestion_txt.setTextColor(Color.parseColor("#808080"));

		logout_txt.setText("ᠭᠠᠷᠬᠤ");
		logout_txt.setTextSize(24);
		logout_txt.setTextColor(Color.parseColor("#808080"));
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		loginButton.setOnClickListener(new OnClick());
		
		brightness_linear.setOnClickListener(new OnClick());
		fontsize_linear.setOnClickListener(new OnClick());
		message_linear.setOnClickListener(new OnClick());
		cache_linear.setOnClickListener(new OnClick());
		version_linear.setOnClickListener(new OnClick());
		aboutus_linear.setOnClickListener(new OnClick());
		collect_linear.setOnClickListener(new OnClick());
		active_linear.setOnClickListener(new OnClick());
		suggestion_linear.setOnClickListener(new OnClick());
		logout_linear.setOnClickListener(new OnClick());
	}
	
	
	class OnClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==loginButton){
				if(PublicValue.USER!=null){//进入编辑页面
					// 用户信息页面
					Intent intent = new Intent(getActivity(),
							MUpdateUserInfoActivity.class);
					startActivity(intent);
				}else{//进入登录
					Intent intent = new Intent(getActivity(),
							MLoginActivity.class);
					startActivityForResult(intent, LoginCode);
				}
			}else if(v==brightness_linear){	
				Intent intent = new Intent(getActivity(),
						MComplainListActivity.class);
				startActivity(intent);	
			}else if(v==fontsize_linear){
				setFontSize();
			}else if(v==message_linear){
				Intent intent = new Intent(getActivity(),
						MMessageActivity.class);
				startActivity(intent);
			}else if(v==cache_linear){
				showMongolAlertClick();
			}else if(v==version_linear){
				if(version.equals(PublicValue.lastVersion))
					Toast.makeText(getActivity(), "ᠨᠢᠭᠡᠨᠲᠡ ᠪᠣᠯ ᠬᠠᠮᠤᠭ ᠤᠨ ᠰᠢᠨ᠎ᠡ ᠬᠡᠪᠯᠡᠯ", MongolToast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity(), "ᠰᠢᠨ᠎ᠡ ᠬᠡᠪᠯᠡᠯ", MongolToast.LENGTH_LONG).show();
				
			}else if(v==aboutus_linear){
				Intent intent = new Intent(getActivity(),
						AboutUsActivity.class);
				startActivity(intent);
			}else if(v==collect_linear){
				Intent intent = new Intent(getActivity(),
						MCollectListActivity.class);
				startActivity(intent);
			}else if(v==active_linear){
				Intent intent = new Intent(getActivity(),
						MActiveActivity.class);
				startActivity(intent);
			}else if(v==suggestion_linear){
				Intent intent = new Intent(getActivity(),
						MSuggestionActivity.class);
				startActivity(intent);
			}else if(v==logout_linear){
				Tools.cleanUserInfo(getActivity());
				PublicValue.USER = Tools.getUserInfo(getActivity());
				checkUserIsLogin();
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
 			user_name.setText("ᠲᠡᠮᠳᠡᠭᠯᠡᠬᠦ");
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
	  
	  
	  
	  /**
	 * 设置字体
	 */
	public void setFontSize() {
		final String[] items = getResources().getStringArray(R.array.font_size_m);
		new AlertDialog.Builder(getActivity()).setTitle("ᠦᠰᠦᠭ ᠦᠨ ᠲᠢᠭ ᠦᠨ ᠶᠡᠬᠡ ᠪᠠᠭ᠎ᠠ")
				.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 设置字体
						switch (which) {
						case 0:// 特大号
							SharedPreferencesTools.NORMALSIZE_M = 0;
							SharedPreferencesTools.SIZENAME_M = "ᠴᠤᠤ";
							break;
						case 1:// 大号
							SharedPreferencesTools.NORMALSIZE_M = 1;
							SharedPreferencesTools.SIZENAME_M = "ᠠᠶᠠᠳᠠᠬᠤ";
							break;
						case 2:// 中号
							SharedPreferencesTools.NORMALSIZE_M = 2;
							SharedPreferencesTools.SIZENAME_M = "ᠪᠠᠭ᠎ᠠ ᠨᠣᠮᠧᠷ";
							break;
						}
						/****
						 * 保存设置
						 */
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", SharedPreferencesTools.SIZENAME_M);
						map.put("size", SharedPreferencesTools.NORMALSIZE_M + "");
						SharedPreferencesTools.setValue(getActivity(), map,
								SharedPreferencesTools.KEY_FONT_SIZE);
					}
				}).show();
	}
	
	
	
	private void showMongolAlertClick() {
		// TODO Auto-generated method stub
		com.newgen.UI.MongolAlertDialog.Builder builder = new MongolAlertDialog.Builder(getActivity());
        builder.setTitle("ᠭᠠᠷᠭᠠᠵᠤ ᠦᠵᠡᠭᠦᠯᠬᠦ ");
        builder.setMessage("ᠨᠠᠮᠬᠠᠳᠬᠠᠬᠤ ᠠᠷᠢᠯᠭᠠᠬᠤ ᠡᠰᠡᠬᠦ");
        
        builder.setPositiveButton("ᠲᠣᠭᠲᠠᠭᠠᠬᠤ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            	manager.cleanFiles(getActivity());

				Toast.makeText(getActivity(), "ᠨᠠᠮᠬᠠᠳᠬᠠᠬᠤ ᠴᠡᠪᠡᠷᠯᠡᠬᠦ ᠠᠮᠵᠢᠯᠲᠠ",
						Toast.LENGTH_SHORT).show();

				manager.cleanInternalCache(getActivity());
				manager.cleanExternalCache(getActivity());
            }
        });
        
        builder.setNegativeButton("ᠬᠦᠴᠦᠨ ᠦᠭᠡᠢ ᠪᠣᠯᠭᠠᠬᠤ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        });
        
        
        // create and show the alert dialog
        MongolAlertDialog dialog = builder.create();
        dialog.show();
	}
	
//	class GetPonint implements Runnable{
//		  
//		  @Override
//	      public void run() {
//		      // TODO Auto-generated method stub
//			  UserServer server = new UserServer();
//			  String point = server.getPoints();
//			  Log.e("point", point);
//			  Message msg = new Message();
//			  Bundle bundle = new Bundle();
//			  bundle.putString("point", point);
//			  msg.what = 1;	
//			  msg.setData(bundle);
//			  myHandler.sendMessage(msg);
//	      }
//	  }
	  

}
