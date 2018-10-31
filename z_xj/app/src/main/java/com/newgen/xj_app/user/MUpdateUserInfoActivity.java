package com.newgen.xj_app.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;

import cn.net.newgen.widget.dialog.ArtInputDialog;

import com.newgen.UI.ActionSheetDialog;
import com.newgen.UI.ActionSheetDialog.OnSheetItemClickListener;
import com.newgen.UI.ActionSheetDialog.SheetItemColor;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyDialog;
import com.newgen.domain.Member;
import com.newgen.server.UserServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MUpdateUserInfoActivity extends Activity {

	Dialog mydialog,inputDialog;
	Handler mhandler;
	
	ImageLoader loader;
	DisplayImageOptions options;
	
	ImageView loginButton;
	ImageView back;
	RelativeLayout user_photo,nick_name,change_phone,change_password,logout;
	MongolTextView user_photo_title,nick_name_title,change_phone_title,
	change_password_title,logout_title;
	
	
	MongolTextView nick_name_txt;
	MongolTextView edit_user_title;
	
	
	private ActionSheetDialog mySheetDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_update_user_info_m);
		
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.user_login_btn)
				.showImageOnFail(R.drawable.user_login_btn)
				.showImageOnLoading(R.drawable.user_login_btn)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		
		initWight();
		initListener();
		
		mhandler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				switch (msg.what) {
				case 1:// 数据更新情况
					mydialog.cancel();
					if (data.getInt("ret") == 0) {
						Toast.makeText(getApplicationContext(), "操作失败",
								Toast.LENGTH_SHORT).show();
					} else {
						initValue();
					}
					break;
				default:
					break;
				}
			}
		};
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		this.initValue();
	}

	private void initWight() {
		// TODO Auto-generated method stub
		back = (ImageView)findViewById(R.id.back);
		user_photo = (RelativeLayout)findViewById(R.id.user_photo);
		nick_name = (RelativeLayout)findViewById(R.id.nick_name);
		change_phone = (RelativeLayout)findViewById(R.id.change_phone);
		change_password = (RelativeLayout)findViewById(R.id.change_password);
		loginButton = (ImageView)findViewById(R.id.loginButton);
		nick_name_txt = (MongolTextView)findViewById(R.id.nick_name_txt);
		nick_name_txt.setTextColor(Color.parseColor("#a0a0a0"));
		logout = (RelativeLayout)findViewById(R.id.logout);
		edit_user_title = (MongolTextView)findViewById(R.id.edit_user_title);
		user_photo_title = (MongolTextView)findViewById(R.id.user_photo_title);
		nick_name_title = (MongolTextView)findViewById(R.id.nick_name_title);
		change_phone_title = (MongolTextView)findViewById(R.id.change_phone_title);
		change_password_title = (MongolTextView)findViewById(R.id.change_password_title);
		logout_title = (MongolTextView)findViewById(R.id.logout_title);
		edit_user_title.setText("ᠮᠠᠲ᠋ᠧᠷᠢᠶᠠᠯ ᠢ ᠨᠠᠶᠢᠷᠠᠭᠤᠯᠬᠤ");//编辑资料
		user_photo_title.setText("ᠬᠦᠷᠦᠭ");//头像 
		nick_name_title.setText("ᠳᠣᠲᠣᠨᠣᠴᠢᠯᠠᠭᠰᠠᠨ ᠳᠠᠭᠤᠳᠠᠯᠭ᠎ᠠ");//昵称 
		change_phone_title.setText("ᠭᠠᠷ ᠤᠲᠠᠰᠤᠨ ᠳ᠋ᠤᠭᠠᠷ");//手机号
		change_password_title.setText("ᠨᠢᠭᠤᠴᠠ ᠺᠣᠳ᠋ ᠵᠠᠰᠠᠬᠤ");//修改密码
		logout_title.setText("ᠭᠠᠷᠬᠤ");//退出
		
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new Click());
		user_photo.setOnClickListener(new Click());
		nick_name.setOnClickListener(new Click());
		change_phone.setOnClickListener(new Click());
		change_password.setOnClickListener(new Click());
		logout.setOnClickListener(new Click());
	}
	
	
	
	
	class Click implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==back)
				finish();
			else if(v==user_photo){
				mySheetDialog = new ActionSheetDialog(
						MUpdateUserInfoActivity.this);
				mySheetDialog
						.builder()
						.setCancelable(true)
						.setCanceledOnTouchOutside(true)
						.addSheetItem("فازا رېشاتكىسى", SheetItemColor.Blue,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										Intent intent = new Intent(
												Intent.ACTION_PICK, null);
										intent.setDataAndType(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												"image/*");
										startActivityForResult(intent, 1);
									}
								})
						.addSheetItem("فوتو ئاپپارات", SheetItemColor.Blue,
								new OnSheetItemClickListener() {
									@Override
									public void onClick(int which) {
										Intent intent = new Intent(
												MediaStore.ACTION_IMAGE_CAPTURE);
										// 下面这句指定调用相机拍照后的照片存储的路径
										intent.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(new File(
														Environment
																.getExternalStorageDirectory(),
														"sgrb_user.jpg")));
										startActivityForResult(intent, 2);
									}
								}).show();
				
			}else if(v==nick_name){
				inputDialog = new ArtInputDialog(MUpdateUserInfoActivity.this,"سىزنىڭ يېڭى ئىسىم كىرگۈزۈڭ", "ئېنىق", "ئەمەلدىن قالدۇرماق", new ArtInputDialog.OnInputClickListener() {
					
					@Override
					public void okButtonClick(String inputStr) {
						// TODO Auto-generated method stub
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("memcode", PublicValue.USER.getMemcode());
						params.put("nickname", inputStr);
						UpdateThread thread = new UpdateThread();
						thread.setParams(params);
						thread.start();
						mydialog = MyDialog.createLoadingDialog(
								MUpdateUserInfoActivity.this, "数据提交中...");
						mydialog.show();
					}
					
					@Override
					public void cancelButtonClick() {
						// TODO Auto-generated method stub
						inputDialog.dismiss();
					}
				});
				inputDialog.show();
			}else if(v==change_phone){
				Intent intent = new Intent(MUpdateUserInfoActivity.this,
						MChangePhoneActivity.class);
				startActivity(intent);
				finish();
			}else if(v==change_password){
				Intent intent = new Intent(MUpdateUserInfoActivity.this,
						MChangePwdActivity.class);
				startActivity(intent);
				finish();
			}else if(v==logout){
				Tools.cleanUserInfo(MUpdateUserInfoActivity.this);
				finish();
			}
		}
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			try {
				startPhotoZoom(data.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			try {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/sgrb_user.jpg");
				startPhotoZoom(Uri.fromFile(temp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			if (photo != null) {
				String name = DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA))
						+ ".jpg";
				try {
					Tools.saveImage(photo, PublicValue.MtempImage, name);
					Map<String, Object> params = new HashMap<String, Object>();
					List<String> list = new ArrayList<String>();
					list.add(PublicValue.MtempImage + name);
					params.put("myfiles", list);
					params.put("memcode", PublicValue.USER.getMemcode());
					Tools.debugLog(PublicValue.USER.getMemcode());

					UpdateThread thread = new UpdateThread();
					thread.setParams(params);
					thread.start();
					mydialog = MyDialog.createLoadingDialog(this, "提交数据中...");
					mydialog.show();
				} catch (IOException e) {
					Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	
	class UpdateThread extends Thread {
		private Map<String, Object> params;

		public void setParams(Map<String, Object> params) {
			this.params = params;
		}

		@Override
		public void run() {
			UserServer server = new UserServer();
			Message msg = new Message();
			Bundle data = new Bundle();
			msg.what = 1;
			msg.setData(data);
			Member user;
			try {
				user = server.updateInfo(params);
				if (user == null) {
					data.putInt("ret", 0);
				} else {
					PublicValue.USER = user;
					Tools.saveUserInfo(user, MUpdateUserInfoActivity.this);
					data.putInt("ret", 1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				data.putInt("ret", 0);
			}

			mhandler.sendMessage(msg);
		}
	}
	
	
	private void initValue() {
		Member user = PublicValue.USER;
		String picSrc = user.getPhoto();
		loader.displayImage(picSrc, loginButton, options);
		nick_name_txt.setText(user.getNickname());
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
