package com.newgen.fragment.ww;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.newgen.adapter.MServiceAdapter;
import com.newgen.adapter.ServiceAdapter;
import com.newgen.domain.ConvenienceService;
import com.newgen.server.ServiceServer;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.mw.MServiceDetailActivity;
import com.newgen.xj_app.ww.OfficeHallActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServiceFragment extends Fragment implements OnItemClickListener{
	
	private View mView;
	GridView gridview;
	boolean isFrist = true;
	ServiceAdapter adapter;
	String jsonData;
	ConvenienceService Officehall;
	private List<ConvenienceService> lifes = new ArrayList<ConvenienceService>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// 取缓存数据
		mView = inflater.inflate(R.layout.fragment_service, container, false);
		
		Officehall = new ConvenienceService();
		Officehall.setName("ئىش بېجىرىش زالى");
		
		gridview  = (GridView) mView.findViewById(R.id.gridview);
		adapter = new ServiceAdapter(getActivity(), lifes);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(this);
		
		return mView;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
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
				String tempStr = server.getServiceJson();
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
						temp.add(Officehall);
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
		Log.e("position", position+"");
		
		if(position!=0){
			Intent intent = new Intent(getActivity(), MServiceDetailActivity.class);
			intent.putExtra("url", lifes.get(position).getUrl());
			startActivity(intent);
		}else{
			Intent intent = new Intent(getActivity(), OfficeHallActivity.class);
			startActivity(intent);
		}
		
	}
	
	
}
