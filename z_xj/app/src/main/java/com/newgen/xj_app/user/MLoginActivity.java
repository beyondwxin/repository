package com.newgen.xj_app.user;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.mob.MobSDK;
import com.mob.tools.utils.UIHandler;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyDialog;
import com.newgen.domain.Member;
import com.newgen.server.UserServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MLoginActivity extends Activity implements
	PlatformActionListener, Callback{

	EditText loginName, passwrod;
	MongolTextView loginButton,rigestBtn;
	ImageView sinaWB, txQQ, backBtn,wechat;
	MongolTextView findPassword;
	String loginNameTxt;
	
	Handler loginHandler;
	Dialog dialog;
	private static final int MSG_USERID_FOUND = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_m);
		
		PublicValue.USER = Tools.getUserInfo(this);
		
		MobSDK.init(MLoginActivity.this,"247419562dfa0","c6859dcbc0359fd97eaa4038e315bc3d");
		loginNameTxt = SharedPreferencesTools.getValue(this,
				SharedPreferencesTools.LOGINNAME,
				SharedPreferencesTools.SYSTEMCONFIG);
		
		initWight();
		initListener();
		
		loginHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:// 准备请求数据
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					dialog = MyDialog.createLoadingDialog(MLoginActivity.this,
							"登录中...");
					dialog.show();
					break;
				case 2:// 登录成功
					dialog.cancel();
					finish();
					break;
				case 3:// 登录失败
					dialog.cancel();
					Toast.makeText(MLoginActivity.this, "用户名或密码错误",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		};
		
	}

	private void initWight() {
		// TODO Auto-generated method stub
		loginName = (EditText)findViewById(R.id.user_phone);
		passwrod = (EditText)findViewById(R.id.user_password);
		loginButton = (MongolTextView)findViewById(R.id.login);
		rigestBtn = (MongolTextView) findViewById(R.id.register);
		findPassword = (MongolTextView)findViewById(R.id.forget_password);
		findPassword.setText("ᠨᠢᠭᠤᠴᠠ ᠨᠣᠮᠧᠷ ᠪᠡᠨ ᠮᠠᠷᠲᠠᠵᠠᠢ");
		loginButton.setText("ᠨᠢᠭᠤᠴᠠ  ᠨᠣᠮᠧᠷ");
		loginButton.setTextColor(Color.parseColor("#FFFFFF"));
		loginButton.setGravity(Gravity.CENTER);
		loginButton.setTextSize(24);
		rigestBtn.setText("ᠲᠠᠩᠰᠠᠯᠠᠬᠤ");
		rigestBtn.setGravity(Gravity.CENTER);
		rigestBtn.setTextSize(24);
		
		
		backBtn = (ImageView)findViewById(R.id.back);
		sinaWB = (ImageView) findViewById(R.id.weibo_icon);
		txQQ = (ImageView) findViewById(R.id.qq_icon);
		wechat = (ImageView) findViewById(R.id.wechat_icon);
		
		if (null != loginNameTxt)
			loginName.setText(loginNameTxt);
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(new OnClickView());
		loginButton.setOnClickListener(new OnClickView());
		rigestBtn.setOnClickListener(new OnClickView());
		findPassword.setOnClickListener(new OnClickView());
		sinaWB.setOnClickListener(new OnClickView());
		txQQ.setOnClickListener(new OnClickView());
		wechat.setOnClickListener(new OnClickView());
	}
	
	class OnClickView implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == backBtn) {
				finish();
			}else if (v == rigestBtn) {
				Intent intent = new Intent(MLoginActivity.this,
						MUserRegistActivity.class);
				startActivity(intent);
			}else if (v == loginButton) {
				Message msg = new Message();
				msg.what = 1;// 准备请求数据
				MLoginActivity.this.loginHandler.sendMessage(msg);// 告诉界面出现等待框
				// 登录请求
				new Thread() {
					@Override
					public void run() {
						UserServer server = new UserServer();
						Member u = server.userLogin(loginName.getText()
								.toString(), passwrod.getText().toString(),
								MLoginActivity.this);
						Message msg = new Message();
						if (u != null) {// 登录成功
							PublicValue.USER = u;
							//将用户信息保存
							Tools.saveUserInfo(u, MLoginActivity.this);
							msg.what = 2;
						} else {
							msg.what = 3;
						}
						MLoginActivity.this.loginHandler.sendMessage(msg);
					}
				}.start();
			}else if (v == sinaWB) {
				//微博登陆
				authorize(ShareSDK.getPlatform(SinaWeibo.NAME));
				
			} else if (v == txQQ) {
				//QQ登陆
				authorize(ShareSDK.getPlatform(QQ.NAME));
			}
			else if(v==wechat){
				//微信登录
				authorize(ShareSDK.getPlatform(Wechat.NAME));
			}
			else if(v == findPassword){
				Intent intent = new Intent(MLoginActivity.this, MFindPasswordActivity.class);
				startActivity(intent);
			}
		}
		
	}
	
	
	private void authorize(Platform plat) {
		dialog = MyDialog.createLoadingDialog(this, "数据验证中...");
		dialog.show();
		if (plat == null) {
			// popupOthers();
			return;
		}
		// 如果用户是注销过了的，一样进入登录授权页
		if (plat.isAuthValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId) && PublicValue.USER != null) {
				UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				return;
			} else {
				plat.removeAccount(true);
			}
		}
		plat.setPlatformActionListener(this);
		if(plat.getName().equals(SinaWeibo.NAME)){
			plat.SSOSetting(true);
		}else
			plat.SSOSetting(false);
		plat.showUser(null);
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		if (dialog.isShowing())
			dialog.cancel();
	}

	@Override
	public void onComplete(Platform plat, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		String memcode = plat.getName() + "_" + plat.getDb().getUserId();
		String password = "hljsjdbapp";
		String nickname = plat.getDb().getUserName();
		String photopic = plat.getDb().getUserIcon();
		String platform = plat.getName();
		Tools.log(memcode + "---" + password + "---" + nickname + "---"
				+ photopic + "---" + platform + "---"
				+ plat.getDb().getUserId());
		 PlatLoginThread thread = new PlatLoginThread();
		 thread.initParams(memcode, password, nickname, photopic, platform);
		 thread.start();
	}

	@Override
	public void onError(Platform plat, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		try{
			if(dialog!= null)
				dialog.cancel();
			Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	class PlatLoginThread extends Thread {
		private String memcode, password, nickname, photopic, platform;

		public void initParams(String memcode, String password,
				String nickname, String photopic, String platform) {
			this.memcode = 	memcode;
			this.password = password;
			this.nickname = nickname;
			this.photopic = photopic;
			this.platform = platform;
		}

		@Override
		public void run() {
			UserServer server = new UserServer();
			try {
				Member m = server.login3(memcode, password, nickname, photopic,
						platform, MLoginActivity.this);
				if (m != null) {
					PublicValue.USER = m;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = 2;
			loginHandler.sendMessage(msg);
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
