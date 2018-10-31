package com.newgen.server;

import java.util.HashMap;
import java.util.Map;

import com.newgen.tools.HttpTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;

public class MainServer {
	public String getNewsCategoryAndADPath(){
//		String url="http://192.168.66.201:8989/JBNewsAppServer/getListCategory.do";
		String url = PublicValue.BASEURL + "getListCategory.do";// 得到请求数据url
		Tools.log(url);
		String resultStr = HttpTools.httpGet(url, 6);//设置6秒超时
		return resultStr;
	}

	public String getLoacation(String address) {
		// TODO Auto-generated method stub
		//http://api.map.baidu.com/geocoder?output=json&location=30.86781666666666,116.41652833333335
		String url = "http://api.map.baidu.com/geocoder?output=json&location="+address;
		Tools.log(url);
		String resultStr = HttpTools.httpGet(url, 6);//设置6秒超时
		return resultStr;
	}

	public String TranslateCity() {
		// TODO Auto-generated method stub
		String url = PublicValue.BASEURL + "contentTran.do";// 得到请求数据url
		Map<String, Object> params = new HashMap<String, Object>();
		if(PublicValue.LOCATIONCITY!=null)
			params.put("content", PublicValue.LOCATIONCITY.getCity());
		else	
			params.put("content", "北京");
			
		Tools.log(url);
		String result = null;
		try {
			result = HttpTools.httpPost(params,url,true,"utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
}
