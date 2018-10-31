package com.newgen.xj_app.hw;

import java.util.ArrayList;
import java.util.List;


import com.newgen.adapter.SearchListAdapter;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.R.id;
import com.newgen.xj_app.R.layout;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HSearchActivity extends Activity implements OnClickListener{
	
	private ImageView backButton;
	private EditText search_key;
	private TextView search;
	private ImageView del_record;
	private ListView record_list;
	
	private List<String> recordsList = new ArrayList<String>();
	private SearchListAdapter adapter;
	boolean isNight = false;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_h);
		
		getSearchRecord();
		initWight();
		initListener();
		
	}
	
	private void initWight() {
		// TODO Auto-generated method stub
		adapter = new SearchListAdapter(HSearchActivity.this, recordsList,isNight);
		backButton = (ImageView) findViewById(R.id.back);
		search_key = (EditText) findViewById(R.id.search_key);
		search = (TextView) findViewById(R.id.search);
		del_record = (ImageView) findViewById(R.id.del_record);
		record_list = (ListView) findViewById(R.id.record_list);
		record_list.setAdapter(adapter);
	}

	
	private void initListener() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		search.setOnClickListener(this);
		del_record.setOnClickListener(this);
		record_list.setOnItemClickListener(new itemClick());
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v ==backButton)
			finish();
		else if(v==search){
			String search_text = search_key.getText().toString().trim();
			if(isLaw(search_text)){
				//将搜索的记录存起来
				saveSearchRecord(search_text);
				//进入搜索结果的activity 真正实现搜索数据
				Intent intent = new Intent(HSearchActivity.this,HSearchResultActivity.class);
				intent.putExtra("key", search_text);
				startActivity(intent);
			}else{
				Toast.makeText(HSearchActivity.this, "请输入关键字，空格不算哦！", 5).show();
			}
		}else if(v==del_record){
			delSearchRecord();
			recordsList.clear();
			adapter.notifyDataSetChanged();
		}
			
	}
	
	/***
	 * 检查关键字是否合法
	 */
	private boolean isLaw(String search_text) {
		// TODO Auto-generated method stub
		search_text = search_text.replace(" ", "");
		if(search_text.equals(""))
			return false;
		else 
			return true;
	}

	/***
	 * 获取缓存数据
	 */
	private void getSearchRecord() {
		// TODO Auto-generated method stub
		String jStr = SharedPreferencesTools.getValue(HSearchActivity.this,
				"search_record", SharedPreferencesTools.CACHEDATA);
		if (null != jStr && !"".equals(jStr)) {
			String[] recordArray=jStr.split(",");
			for(int i = 0;i<recordArray.length;i++){
				recordsList.add(recordArray[i]);
			}
			recordsList.remove(0);
		}
	}
	
	/***
	 * 保存缓存数据
	 */
	private void saveSearchRecord(String key) {
		
		if(recordsList.size()<7)
			recordsList.add(key);
		else{
			recordsList.remove(0);
			recordsList.add(key);
		}
		
		adapter.notifyDataSetChanged();
		
		String recordString = "";
		
		for(int i = 0;i<recordsList.size();i++){
			recordString = recordString+","+recordsList.get(i);
		}
		
		SharedPreferencesTools.setValue(HSearchActivity.this, "search_record", 
				recordString, SharedPreferencesTools.CACHEDATA);
	}
	
	/***
	 * 删除缓存数据
	 */
	private void delSearchRecord() {
		// TODO Auto-generated method stub
		String recordString = "";
		SharedPreferencesTools.setValue(HSearchActivity.this, "search_record", 
				recordString, SharedPreferencesTools.CACHEDATA);
	}
	
	/**
	 * 点击记录处理
	 * */
	class itemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			//进入搜索结果的activity 真正实现搜索数据
			Intent intent = new Intent(HSearchActivity.this,HSearchResultActivity.class);
			intent.putExtra("key", recordsList.get(position));
			startActivity(intent);
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
