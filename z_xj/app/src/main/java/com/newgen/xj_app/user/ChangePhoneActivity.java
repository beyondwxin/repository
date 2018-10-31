package com.newgen.xj_app.user;



import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import com.newgen.UI.MyDialog;
import com.newgen.UI.MyToast;
import com.newgen.domain.Member;
import com.newgen.server.UserServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePhoneActivity extends Activity {
	
	ImageView backBtn;
	TextView submitBtn;
	EditText oldPhone, newPhone,code;
	TextView get_code;
	Dialog dialog;
	Handler handler;
	Timer timer;
	int time = 60;//用于计时	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_phone);
		
		initWight();
		initListener();
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				switch (msg.what) {
				case 1:// 数据更新情况
					dialog.cancel();
					if (data.getInt("ret") == 0) {
						Toast.makeText(getApplicationContext(), "操作失败",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "换绑成功",
								Toast.LENGTH_SHORT).show();
						finish();
					}
					break;
				case 2:
					get_code.setClickable(false);
					get_code.setText(time+"سېكۇنت");
					break;
				case 3:
					get_code.setClickable(true);
					get_code.setText(R.string.get_code);
				case 4:
					String result = msg.getData().getString("result");
					Toast.makeText(ChangePhoneActivity.this, result, Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}
			}
		};
		
	}
	
	
	private void initWight() {
		// TODO Auto-generated method stub
		backBtn = (ImageView) findViewById(R.id.back);
		submitBtn = (TextView) findViewById(R.id.submit);
		oldPhone = (EditText) findViewById(R.id.oldPhone);
		newPhone = (EditText) findViewById(R.id.newPhone);
		code = (EditText)findViewById(R.id.code);
		get_code = (TextView)findViewById(R.id.get_code);
	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(new Click());
		submitBtn.setOnClickListener(new Click());
		get_code.setOnClickListener(new Click());
	}
	
	private class Click implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == backBtn)
				finish();
			else if (v == submitBtn) {
				if(isMobileNO(newPhone.getText().toString())){
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("memcode", PublicValue.USER.getMemcode());
					params.put("newPhone ", newPhone.getText().toString());
					params.put("oldPhone  ", oldPhone.getText().toString());
					params.put("code", code.getText().toString());
					UpdateThread thread = new UpdateThread();
					thread.setParams(params);
					thread.start();
					dialog = MyDialog.createLoadingDialog(
							ChangePhoneActivity.this, "数据提交中...");
					dialog.show();
				}
			}else if(v==get_code){
				String phone = newPhone.getText().toString().trim();
				if(isMobileNO(phone)){
					timer = new Timer();
					timer.schedule(new DataTask(), 1000, 1000);//一秒后执行，一秒执行一次
					getCodeThread thread = new getCodeThread(phone);
					thread.start();
				}else
					MyToast.showToast(ChangePhoneActivity.this, "يانفون نومۇرىنى كىرگۈزۈڭ", 1);
			}
		}
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
			user = server.updateBindPhone(params);
			if (user == null) {
				data.putInt("ret", 0);
			} else {
				PublicValue.USER = user;
				Tools.saveUserInfo(user, ChangePhoneActivity.this);
				data.putInt("ret", 1);
			}

			handler.sendMessage(msg);
		}
	}
    
    
    private class DataTask extends TimerTask {
		public DataTask() {
			time =59;
		}

		@Override
		public void run() {
			time--;// 加一秒
			if (time>0) {
				handler.sendEmptyMessage(2);
			}else{
				handler.sendEmptyMessage(3);
				timer.cancel();
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
			msg.what = 4;
			Bundle data = new Bundle();
			data.putString("result", result);;
			msg.setData(data);
			handler.sendMessage(msg);
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
