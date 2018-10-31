package com.newgen.xj_app.user;

import org.json.JSONObject;

import cn.net.newgen.widget.dialog.ArtWaitDialog;

import com.newgen.server.MainServer;
import com.newgen.server.UserServer;
import com.newgen.xj_app.R;
import com.newgen.xj_app.SelectLanguageActivity;
import com.newgen.xj_app.mw.MMainFragmentActivity;
import com.newgen.xj_app.user.FindPasswordActivity.Click;
import com.newgen.xj_app.ww.MainFragmentActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HSuggestionActivity extends Activity implements OnClickListener{
	
	ImageView back;
	EditText suggestion_txt;
	TextView submit;
	Dialog dialog;
	Handler myHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion_h);
		
		dialog = new ArtWaitDialog(HSuggestionActivity.this,"تاپشۇرغان ئوتتۇرا");
		
		back = (ImageView)findViewById(R.id.back);
		suggestion_txt = (EditText)findViewById(R.id.suggestion_txt);
		submit = (TextView)findViewById(R.id.confirm);
		
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
		
		initHandler();
	}

	private void initHandler() {
		// TODO Auto-generated method stub
		myHandler = new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
					case 1:
						if(dialog!=null)
							dialog.dismiss();
						Toast.makeText(HSuggestionActivity.this, "مۇۋەپپەقىيەت", 5).show();
					break;
					case 2:
						if(dialog!=null)
							dialog.dismiss();
						Toast.makeText(HSuggestionActivity.this, "مەغلۇپ بولۇش", 5).show();
						break;
					default:
						break;	
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==back)
			finish();
		else if(v==submit){
			String content = suggestion_txt.getText().toString().trim();
			if(content==null||content.equals("")){
				Toast.makeText(HSuggestionActivity.this, "意见不能为空", 5).show();
			}else{
				dialog.show();
				AddSuggestionThread m = new AddSuggestionThread(content);
				new Thread(m).start();
			}
		}
	}
	
	
	
	class AddSuggestionThread implements Runnable{
		
		String content;
		
		public AddSuggestionThread(String content){
			this.content = content;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			UserServer server = new UserServer();
			String resultStr = server.addAdvice(content);
			Message msg = new Message();
			Log.e("info", "++++++++++++++++" + resultStr);
			try {
				JSONObject json = new JSONObject(resultStr);
				if (json.getInt("ret") == 1) // 成功
					msg.what = 1;	
				else 
					msg.what = 2;	
				
			}catch (Exception e) {
				
			};
			myHandler.sendMessage(msg);
		}
		
	}
	
}
