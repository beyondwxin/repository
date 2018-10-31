package com.newgen.xj_app.mw;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.newgen.adapter.MServiceAAdapter;
import com.newgen.domain.ConvenienceService;
import com.newgen.server.ServiceServer;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.mw.MServiceDetailActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class MOfficeHallActivity extends Activity implements OnItemClickListener{
	
	ImageView back;
	GridView gridview;
	boolean isFrist = true;
	MServiceAAdapter adapter;
	String jsonData;
	private List<ConvenienceService> lifes = new ArrayList<ConvenienceService>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_office_hall);
		
		back = (ImageView)findViewById(R.id.back);
		gridview  = (GridView) findViewById(R.id.gridview);
		adapter = new MServiceAAdapter(MOfficeHallActivity.this, lifes);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (isFrist) {
			isFrist = false;
			new LoadData().execute();
		}
	}
	
	private class LoadData extends AsyncTask<Object, Object, Integer> {

		
		@Override
		protected Integer doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {
				ServiceServer server = new ServiceServer();
				String tempStr = server.getOfficeHallJson();
				if (null != tempStr && !"".equals(tempStr))
					jsonData = tempStr;
				JSONObject json = new JSONObject(jsonData);
				if (json.getInt("ret") == 0)
					return 0;
				else
					return 1;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			try {
				if (result == 1) {
					JSONObject json = new JSONObject(jsonData);
					if (json.getInt("ret") == 1) {
						Gson gson = new Gson();
						JSONArray jsonLifes = json.getJSONArray("lifes");
						List<ConvenienceService> temp = new ArrayList<ConvenienceService>();
						for (int i = 0; i < jsonLifes.length(); i++) {
							ConvenienceService convenienceService = gson.fromJson(
									jsonLifes.getString(i), ConvenienceService.class);
							if (null != convenienceService)
								temp.add(convenienceService);
						}
						if (temp.size() > 0) {
							lifes.clear();
							lifes.addAll(temp);
							// 通知adapter
							if(adapter!=null)
								adapter.notifyDataSetChanged();
						}
						
					}
				}
				
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MOfficeHallActivity.this, MServiceDetailActivity.class);
		intent.putExtra("url", lifes.get(position).getUrl());
		startActivity(intent);
	}
}
