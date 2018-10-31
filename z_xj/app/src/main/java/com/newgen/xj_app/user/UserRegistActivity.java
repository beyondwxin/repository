package com.newgen.xj_app.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.newgen.UI.MyDialog;
import com.newgen.UI.MyToast;
import com.newgen.server.NewsCommentServer;
import com.newgen.server.UserServer;
import com.newgen.tools.MD5;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserRegistActivity extends Activity {

	Timer timer;
	ImageView back;
	EditText user_phone,user_password,user_code;
	TextView reigst,login,get_code;
	Handler mHandler;
	int time = 60;//用于计时	
	Dialog dialog;
	boolean isNight = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_regist);
		
		initWight();
		initListener();
		initHandler();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String result = msg.getData().getString("result");
					Toast.makeText(UserRegistActivity.this, result, Toast.LENGTH_SHORT)
							.show();
					break;
				case 2:
					get_code.setClickable(false);
					get_code.setText(time+"秒");
					break;
				case 3:
					get_code.setClickable(true);
					get_code.setText("获取验证码");
					break;
				}
			}
		};
	}
	
//	15660053043

	private void initWight() {
		// TODO Auto-generated method stub
		back = (ImageView)findViewById(R.id.back);
		user_phone = (EditText)findViewById(R.id.user_phone);
		user_password = (EditText)findViewById(R.id.user_password);
		user_code = (EditText)findViewById(R.id.user_code);
		reigst = (TextView)findViewById(R.id.reigst);
		login = (TextView)findViewById(R.id.login);
		get_code = (TextView)findViewById(R.id.get_code);
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new Click());
		get_code.setOnClickListener(new Click());
		reigst.setOnClickListener(new Click());
		login.setOnClickListener(new Click());
	}
	
	
	class Click implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==back)
				finish();
			else if(v==get_code){
				String phone = user_phone.getText().toString().trim();
				if(isMobileNO(phone)){
					timer = new Timer();
					timer.schedule(new DataTask(), 1000, 1000);//一秒后执行，一秒执行一次
					getCodeThread thread = new getCodeThread(phone);
					thread.start();
				}else
					MyToast.showToast(UserRegistActivity.this, "请输入正确的手机号", 1);
			
			}else if(v==reigst){
				String phone = user_phone.getText().toString().trim();
				String password = user_password.getText().toString().trim();
				String code = user_code.getText().toString().trim();
				
				if (validata()){
					new RegistTask().execute(100);
				}
			}else if(v==login){
				finish();
			}
		}
		
	}
	
	class getCodeThread extends Thread{
		
		private String phone;
		
		public getCodeThread(String phone){
			this.phone = phone;
		}
		
		@Override
		public void run() {
			UserServer server = new UserServer();
			String result = server.getCode(phone);
			Message msg = new Message();
			msg.what = 1;
			Bundle data = new Bundle();
			data.putString("result", result);;
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
		
	}
	
	class reigstThread extends Thread{
		private String phone;
		private String password;
		private String code;
		
		public reigstThread(String phone,String password,String code){
			this.phone = phone;
			this.password = password;
			this.code = code;
		}
		
		@Override
		public void run() {
			UserServer server = new UserServer();
			String result = server.getCode(phone);
			Message msg = new Message();
			msg.what = 4;
			Bundle data = new Bundle();
			data.putString("result", result);;
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
		
		
	}
	
	
	/***
	 * 验证注册数据是否合法
	 * 
	 * @return
	 */
	private boolean validata() {
		// 数据校验
		String pwd = user_password.getText().toString();
		String code = user_code.getText().toString();
		String userName = user_phone.getText().toString().trim();
		if (!isMobileNO(userName)) {
			Toast.makeText(getApplicationContext(), "用户手机号填写有误",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!passwordValidate(pwd)) {
			Toast.makeText(getApplicationContext(), "密码由6-20位数字和字母组成",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (code.equals("")) {
			Toast.makeText(getApplicationContext(), "验证码不能为空",
					Toast.LENGTH_LONG).show();
			return false;
		} 
		return true;
	}
	
	
	/**
     * 判断手机格式是否正确
     * @param mobiles
     * @return
     * 总结起来就是第一位必定为1，第二位必定为3或5或8或5或7，其他位置的可以为0-9 新出的号码第二位可以再添加就好
     */
    public static boolean isMobileNO(String mobiles) {  
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
        String telRegex = "[1][34578]\\d{9}" ;
        if (TextUtils.isEmpty(mobiles)) return false ;  
        else return mobiles.matches( telRegex ) ;  
    } 
    
    /***
	 * 密码校验
	 * @param password
	 * @return
	 */
	public static boolean passwordValidate(String password){
		if(null == password || "".equals(password))
			return false;
		else{
			String v = "^[a-zA-Z0-9]{6,20}$";
			return password.matches(v);
		}
	}
    
    private class DataTask extends TimerTask {
		public DataTask() {
			time =60;
		}

		@Override
		public void run() {
			time--;// 加一秒
			if (time>0) {
				mHandler.sendEmptyMessage(2);
			}else{
				mHandler.sendEmptyMessage(3);
				timer.cancel();
			}
		}
	}
    
    
    /**
	 * 
	 * 注册时
	 *
	 */
	private class RegistTask extends AsyncTask<Object, Integer, Integer> {
		
		String data = "";
		
		@Override
		protected void onPreExecute() {
			dialog = MyDialog.createLoadingDialog(UserRegistActivity.this,
					"正在注册，请稍后……");
			dialog.show();
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			int ret = -1;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("password",MD5.md5(user_password.getText().toString().trim()));
			params.put("email", "");
			params.put("nickname", "user_phone.getText().toString()");
			params.put("photo", "");
			params.put("code ", user_code.getText().toString());
			params.put("phone ", user_phone.getText().toString());
			
			UserServer server = new UserServer();
			data = server.rigest(params);
			if (data == null) {
				ret = -1;
			} else {
				Tools.debugLog(data);
				try {
					JSONObject json = new JSONObject(data);
					if (json.getInt("ret") == 0) {
						ret = 0;
					} else if (json.getInt("ret") == 1) {
						ret = 1;
					}
				} catch (Exception e) {
					ret = -1;
				}
			}
			return ret;
		}
		
		
		@Override
		protected void onPostExecute(Integer result) {
			if (dialog != null && dialog.isShowing())
				dialog.cancel();
			switch (result) {
			case -1:
				Toast.makeText(getApplicationContext(), "亲，网络不给力啊！",
						Toast.LENGTH_SHORT).show();
				break;
			case 0:
				try {
					JSONObject json = new JSONObject(data);
					Toast.makeText(getApplicationContext(),json.getString("msg"),
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "注册成功",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
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
