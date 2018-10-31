package com.newgen.server;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.newgen.domain.szb.PaperPage;
import com.newgen.tools.HttpTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;

public class PaperServer {
	
	/**
	 * 获取版面列表
	 * @param paperName
	 * @param dateTime
	 * @return
	 */
	public List<PaperPage> getPages(String paperName, String dateTime){
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("getPagesInfo.do?pSName=").append(paperName);
		url.append("&getArticleFlag=true");
		url.append("&date=").append("dateTime");
		
		Tools.log(url.toString());
		String result = HttpTools.httpGet(url.toString(), 3);
		try{
			if(null == result)
				return null;
			JSONObject json = new JSONObject(result);
			if(json.getInt("ret") == 1){
				List<PaperPage> list = new ArrayList<PaperPage>();
				JSONArray pages = json.getJSONArray("pages");
				Gson gson = new Gson();
				for(int i=0;i<pages.length();i++){
					PaperPage page = gson.fromJson(pages.getString(i), PaperPage.class);
					if(null != page)
						list.add(page);
				}
				return list;
			}else
				return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 以json格式获取版面列表
	 * @param paperName
	 * @param dateTime
	 * @return
	 */
	public String getPagesJson(String paperName, String dateTime){
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("getPagesInfo.do?pSName=").append(paperName);
		url.append("&getArticleFlag=true");
		if(null != dateTime && !"".equals(dateTime))
			url.append("&date=").append(dateTime);
		Tools.log(url.toString());
		Log.i("info", url.toString());
		String result = HttpTools.httpGet(url.toString(), 6);
		
		return result;
	}
	
	/***
	 * 获取文章详情
	 * @param aid  文章ID
	 * @param paperName  报纸简称
	 * @return
	 */
	public String getArticle(int aid, String paperName){
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("getArticle.do?aid=").append(aid);
		url.append("&pSName=").append(paperName);
		Tools.log(url.toString());
		String result = HttpTools.httpGet(url.toString(), 5);
		return result;
	}
}
