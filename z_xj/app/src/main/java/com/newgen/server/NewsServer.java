package com.newgen.server;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.DbException;
import com.newgen.dao.NewsCommentDAO;
import com.newgen.dao.NewsSupportDAO;
import com.newgen.domain.Active;
import com.newgen.domain.NewsComment;
import com.newgen.domain.NewsPub;
import com.newgen.domain.NewsReview;
import com.newgen.domain.NewsSupport;
import com.newgen.domain.RunImage;
import com.newgen.tools.HttpTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;

public class NewsServer {
	// 取新闻详细
	public String getNewsDetail(int newsID, int memberid, String device) {
		String result = null;
		String url = PublicValue.BASEURL + "getNewsById.do?newsid=" + newsID
				+ "&memberid=" + memberid + "&device=" + device;
		Tools.debugLog(url);
		result = Tools.executeHttpGet(url);
		return result;
	}

	// 取最新的三分钟读报数据
//	public String getLateReadPaper(int memberid, String device) {
//		String result = null;
//		String url = PublicValue.BASEURL + "getLateReadPaper.do?" + "memberid="
//				+ memberid + "&device=" + device;
//		Tools.debugLog(url);
//		result = Tools.executeHttpGet(url);
//		return result;
//	}

	/***
	 * 获取新闻详情
	 * 
	 * @param newsID
	 * @param memberid
	 * @param uid
	 * @return
	 */
	public NewsPub getNewsDetailBean(int newsID, int memberid, String device) {
		String result = null;
		String url = PublicValue.BASEURL + "getNewsById.do?newsid=" + newsID
				+ "&memberid=" + memberid + "&device=" + device;
		// String url = PublicValue.BASEURL
		// + "getNewsByIdNew.do?apphardid=xxxxx&newsid=" + newsID
		// + "&memberid=" + memberid + "&uid=" + uid;
		Tools.debugLog(url);
		result = Tools.executeHttpGet(url);
		if (null == result) {
			return null;
		} else {
			try {
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					Gson gson = new Gson();
					NewsPub news = gson.fromJson(json.getString("data"),
							NewsPub.class);
					return news;
				} else {
					return null;
				}
			} catch (JSONException e) {
				return null;
			}
		}
	}

	/****
	 * 获取新闻列表
	 * 
	 * @param startId
	 *            代表最后一条新闻的id 开始请求使用-1
	 * @param count
	 * @param stype
	 * @param cid
	 * @param hradId
	 * @return
	 */
	public List<NewsPub> getNewsList(int cid, int count, int startId) {
		String url = PublicValue.BASEURL + "getNewsList.do?cid=" + cid
				+ "&count=" + count + "&startid=" + startId;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	/****
	 * 获取新闻列表
	 * 
	 * @param startId
	 *            代表最后一条新闻的id 开始请求使用-1
	 * @param count
	 * @param stype
	 * @param cid
	 * @param hradId
	 * @return
	 */
	public List<NewsPub> getLocationNewsList(String area, int count, int startId) {
		String url = PublicValue.BASEURL + "getNewsList.do";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startid", "-1");
		params.put("count", "10");
		params.put("area", area);
		Tools.log("" + url);
		
		String result = null;
		try {
			result = HttpTools.httpPost(params, url, true, "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	
	/**
	 *  我的界面专题入口
	 * @param cid
	 * @param count
	 * @param startId
	 * @return
	 */
	public List<NewsPub> getSubjectNewsList(int cid, int count, int startId) {
		String url = PublicValue.BASEURL + "subjectList.do?startid=" + startId
				+ "&count=" + count;
//		String url = "http://192.168.66.85:18080/DBNewsAppService/subjectList.do?startid="+ startId
//				+ "&count=" + count;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	/****
	 * 获取直播新闻列表
	 * 
	 * @param startId
	 *            代表最后一条新闻的id 开始请求使用-1
	 * @param count
	 */
	public List<NewsPub> getLivenewsList(int count, int startId, int memberid,
			String device, String version) {
//		PublicValue.BASEURL
		String url =  PublicValue.BASEURL+ "getLiveNews.do?count=" + count
				+ "&startid=" + startId + "&memberid=" + memberid + "&device="
				+ device + "&version=" + version;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		Log.i("info", result);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					JSONArray newsListJson = json.getJSONArray("data");
					Gson gson = new Gson();
					List<NewsPub> list = new ArrayList<NewsPub>();
					for (int i = 0; i < newsListJson.length(); i++) {
						NewsPub news = gson.fromJson(newsListJson.getString(i),
								NewsPub.class);
						if (news != null)
							list.add(news);
					}
					return list;
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	/****
	 * 获取直播里面边看边聊列表
	 * 
	 * @param startId
	 *            代表最后一条新闻的id 开始请求使用-1
	 * @param count
	 * @param stype
	 * @param cid
	 * @param hradId
	 * @return
	 */
	public List<NewsReview> getChatNewsList(int cid, int count, int startId,
			int memberid, String device, String version) {
//		String url = PublicValue.BASEURL + cid + "&count=" + count
//				+ "&startid=" + startId + "&memberid=" + memberid + "&device="
//				+ device + "&version=" + version;
		
		//http://192.168.66.189:8080/DBNewsAppService/getLiveReview.do?liveid=20&startid=-1&count=10
		String url =  PublicValue.BASEURL+ "getLiveReview.do?liveid=" + cid
				+ "&count=" + count + "&startid=" + startId + "&memberid="
				+ memberid + "&device=" + device + "&version=" + version;
		;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		Log.i("data", result);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("listReview");
				Gson gson = new Gson();
				List<NewsReview> list = new ArrayList<NewsReview>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsReview news = gson.fromJson(newsListJson.getString(i),
							NewsReview.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	/***
	 * 获取专题下的新闻列表
	 * 
	 * @param cid
	 * @param count
	 * @param startId
	 * @return
	 */
	public List<NewsPub> getNewsListBySubjectId(int sid, int count, int startId) {
		String url = PublicValue.BASEURL + "getNewsList.do?sid=" + sid
				+ "&count=" + count + "&startid=" + startId;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	/****
	 * 获取直播详情
	 * 
	 * @param startId
	 *            代表最后一条新闻的id 开始请求使用-1
	 * @param count
	 * @param stype
	 * @param cid
	 * @param hradId
	 * @return
	 */
	public List<NewsPub> getLiveDetails(int cid, int count, int startId,
			int memberid, String device, String version) {
//		http://192.168.66.189:8080/PublicValue.BASEURL"
		String url =  PublicValue.BASEURL+ "getLiveNewsDetail.do?liveid=" + cid
				+ "&count=" + count + "&startid=" + startId + "&memberid="
				+ memberid + "&device=" + device + "&version=" + version;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		Log.i("info", result);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("listLiveDetail");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	/***
	 * 获取对应地域下的新闻列表
	 * 
	 * @param cid
	 * @param count
	 * @param startId
	 * @return
	 */
	public List<NewsPub> getNewsListByAreaId(int areaId, int count, int startId) {
		String url = PublicValue.BASEURL + "getNewsList.do?area=" + areaId
				+ "&count=" + count + "&startid=" + startId;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	/***
	 * 获取对应地域下的新闻列表
	 * 
	 * @param cid
	 * @param count
	 * @param startId
	 * @return
	 */
	public List<NewsPub> getNewsListByLeaderId(int leaderId, int count,
			int startId) {
		String url = PublicValue.BASEURL + "getNewsList.do?leaderid="
				+ leaderId + "&count=" + count + "&startid=" + startId;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 查询新闻
	 * 
	 * @param keyValue
	 * @param startid
	 * @param count
	 * @return
	 */
	public List<NewsPub> newsSearch(String keyValue, int startid, int count) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuffer url = new StringBuffer(PublicValue.BASEURL);
			url.append("searchNews.do");
			params.put("count", count + "");
			params.put("startid", startid + "");
			params.put("key", keyValue);
			String result = HttpTools.httpPost(params, url.toString(), true,
					"utf-8");
			JSONObject json = new JSONObject(result);
			if (json.getInt("ret") == 1) {
				JSONArray newsListJson = json.getJSONArray("newslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void newsSupport(Context context, int newsId, String device) {
		this.sendSupportStatusToServer(newsId, device);
		this.saveSupportStatusToDB(context, newsId);
	}

	private void sendSupportStatusToServer(int newsId,
			String device) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("addSupport.do");
		params.put("newsid", newsId + "");
		params.put("device", device );
		try {
			HttpTools.httpPost(params, url.toString(), true,
					"utf-8");
		} catch (Exception e) {
			Tools.log("点赞失败");
		}
		
	}
	
    //  新闻点赞
	private void saveSupportStatusToDB(Context context, int newsId) {
		NewsSupport support = new NewsSupport();
		support.setNewsId(newsId);
		NewsSupportDAO newsSupportDAO = new NewsSupportDAO(context);
		try {
			newsSupportDAO.insert(support);
		} catch (DbException e) {
			Tools.log("数据插入失败");
		}
	}
	
	
	
	
	public void newscommentSupport(Context context, int newsId,int commentId, String device) {
		this.sendcommentSupportStatusToServer(commentId, device);
		this.saveCommentStausToDE(context, newsId, commentId);
	}
	
	// 评论点赞
	private void sendcommentSupportStatusToServer(int commentId,
			String device) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("review/addSupport.do");
		params.put("id", commentId + "");
		params.put("device", device );
		try {
			HttpTools.httpPost(params, url.toString(), true,
					"utf-8");
		} catch (Exception e) {
			Tools.log("点赞失败");
		}
		
	}
	//  新闻评论点赞
	private void saveCommentStausToDE(Context context,int newsId,int commentId){
		NewsComment comment =new NewsComment();
		comment.setNewsId(newsId);
		comment.setCommentId(commentId);
		NewsCommentDAO newsCommentDAO =new NewsCommentDAO(context);
		try {
			newsCommentDAO.insert(comment);
		} catch (DbException e) {
			Tools.log("数据插入失败");
		}
	}

	public List<NewsPub> getRecommendNewsList(String uuid, String score) {
		// TODO Auto-generated method stub
		
		String memberid = "-1";
		if(PublicValue.USER!=null)
			memberid = PublicValue.USER.getId().toString();
		String url = PublicValue.BASEURL + "commonendNewsList.do?memberId=" + memberid
				+ "&device=" + uuid + "&score=" + score;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {	
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("commonendnewslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	public List<NewsPub> getMessageList(int count, int startid) {
		// TODO Auto-generated method stub
		String url = PublicValue.BASEURL + "pushNewsList.do?startId=" + startid
				+ "&count=" + count;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {	
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("pushnewslist");
				Gson gson = new Gson();
				List<NewsPub> list = new ArrayList<NewsPub>();
				for (int i = 0; i < newsListJson.length(); i++) {
					NewsPub news = gson.fromJson(newsListJson.getString(i),
							NewsPub.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	public List<Active> getActiveList(int count, int startId) {
		// TODO Auto-generated method stub
		String url = PublicValue.BASEURL + "getActive.do?startid=" + startId
				+ "&count=" + count;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("data");
				Gson gson = new Gson();
				List<Active> list = new ArrayList<Active>();
				for (int i = 0; i < newsListJson.length(); i++) {
					Active active = gson.fromJson(newsListJson.getString(i),
							Active.class);
					if (active != null)
						list.add(active);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	public List<RunImage> getImgsList(int cid) {
		// TODO Auto-generated method stub
		String url = PublicValue.BASEURL + "getFocusePicByCategoryId.do?cid=" + cid;
		Tools.log("" + url);
		String result = Tools.executeHttpGet(url);
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);

				JSONArray newsListJson = json.getJSONArray("data");
				Gson gson = new Gson();
				List<RunImage> list = new ArrayList<RunImage>();
				for (int i = 0; i < newsListJson.length(); i++) {
					RunImage news = gson.fromJson(newsListJson.getString(i),
							RunImage.class);
					if (news != null)
						list.add(news);
				}
				return list;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
	
}
