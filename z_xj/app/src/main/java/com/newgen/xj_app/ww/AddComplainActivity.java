package com.newgen.xj_app.ww;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

import cn.net.newgen.widget.dialog.ArtWaitDialog;

import com.google.gson.JsonObject;
import com.newgen.server.UserServer;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.layout;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddComplainActivity extends Activity {
	
	ImageView submit_complain,back;
	EditText title_txt,content_txt;
	Dialog dialog;
	Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_complain);
		
		dialog = new ArtWaitDialog(AddComplainActivity.this, "تاپشۇرغان ئوتتۇرا");
		submit_complain = (ImageView)findViewById(R.id.submit_complain);
		back = (ImageView)findViewById(R.id.back);
		title_txt = (EditText)findViewById(R.id.title_txt);
		content_txt = (EditText)findViewById(R.id.content_txt);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		submit_complain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = title_txt.getText().toString();
				String content = content_txt.getText().toString();
				if(PublicValue.USER==null){//您暂时未登陆
					Toast.makeText(AddComplainActivity.this,"سىز ۋاقتىنچە تېخى قۇرۇقلۇققا چىقىش",5).show();
					return ;
				}else if(title.equals("")&&title!=null){
					Toast.makeText(AddComplainActivity.this, "تېما قۇرۇق بولسا بولمايدۇ", 5).show();
					return ;
				}else if(content.equals("")&&content!=null){
					Toast.makeText(AddComplainActivity.this, "مەزمۇننى قۇرۇق قالدۇرماڭ", 5).show();
					return ;
				}else{
					dialog.show();
					AddComplainThread m = new AddComplainThread(title,content);
					new Thread(m).start();
				}
			}
		});
		
		initHandler();
		
	}
	

	class AddComplainThread implements Runnable{
		
		private String title,content;
		
		public AddComplainThread(String title,String content){
			this.title = title;
			this.content = content;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String result = "";
			UserServer server = new UserServer();
			result = server.addComplain(title,content);
			Message msg = new Message();
			if(result==null){
				msg.what=2;
				mHandler.sendMessage(msg);
			}else{
				try {
					JSONObject json = new JSONObject(result);
					if(json.getInt("ret")==1){
						msg.what=1;
						Bundle data = new Bundle();
						data.putString("tip", json.getString("msg"));
						msg.setData(data);
					}else
						msg.what=2;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	private void initHandler() {
		// TODO Auto-generated method stub
		mHandler = new Handler(){
			@Override
            public void handleMessage(Message msg) {
				super.handleMessage(msg);
                switch (msg.what) {
				case 1:
					if(dialog!=null)
						dialog.dismiss();
					Toast.makeText(AddComplainActivity.this, 
							msg.getData().getString("tip"), 5).show();
					break;
				case 2:
					if(dialog!=null)
						dialog.dismiss();
					Toast.makeText(AddComplainActivity.this, 
							"添加失败", 5).show();
					break;
				default:
					break;
				}
			}
		};
	}
	
	
}
