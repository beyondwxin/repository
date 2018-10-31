package com.newgen.xj_app.detail.mw;

import cn.net.newgen.widget.dialog.ArtWaitDialog;

import com.newgen.UI.MongolTextView;
import com.newgen.domain.Complain;
import com.newgen.server.UserServer;
import com.newgen.xj_app.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MComplainDetailActivity extends Activity {
	
	private int complaintId;
	private MongolTextView head_title,title,content;
	private ImageView back;
	LoadDateTask task;
	Dialog dialog;
	Complain complain;
	Handler mhandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain_detail_m);
		
		complaintId = getIntent().getIntExtra("Id", 0);
		title = (MongolTextView) findViewById(R.id.title);
		content = (MongolTextView) findViewById(R.id.content);
		head_title = (MongolTextView) findViewById(R.id.head_title);
		head_title.setText("ᠵᠠᠭᠠᠯᠳᠤᠬᠤ ᠨᠠᠷᠢᠨ ᠪᠠᠶᠢᠳᠠᠯ");
		
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		initHandler();
		
	}
	

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		task = new LoadDateTask();
		task.execute();	
	}
	
	private class LoadDateTask extends AsyncTask<Object, Integer, Integer> {
		
		@Override
		protected void onPreExecute() {
			dialog = new ArtWaitDialog(MComplainDetailActivity.this, "جۇكتەۋ بارىسىندا");
			dialog.show();
		}
		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			int ret = -1;
			try {
				UserServer server = new UserServer();
				complain = server.getComplainDetailById(complaintId);
				if(complain!=null)
					ret = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			try {
				switch (result) {
				case 1:
					Message msg = new Message();
					msg.what = 1;
					mhandler.sendMessage(msg);
					break;
				case -1:
					break;
				}
			} catch (Exception e) {

			}
			
		}
		
	}
	
	
	
	private void initHandler() {
		// TODO Auto-generated method stub
		mhandler = new Handler(){
			
			@Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
				case 1:
					if(dialog!=null)
						dialog.dismiss();
					title.setText(complain.getTitle());
					content.setText(complain.getContent());
					break;
				default:
					break;
				}
            }
		};
	}
	
}
