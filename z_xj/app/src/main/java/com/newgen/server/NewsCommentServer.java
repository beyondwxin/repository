package com.newgen.server;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.newgen.tools.HttpTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;

public class NewsCommentServer {

	/***
	 * 获取新闻评论列表
	 * @param newsid
	 * @param startid
	 * @param count
	 * @return
	 */
	public String getCommentList(int newsid, int startid, int count) {
		String url = PublicValue.BASEURL + "getReviewByNewsID.do?newsid="
				+ newsid + "&count=" + count + "&startid=" + startid;
		Log.i("xiaoj", url);
		System.out.println(url);
		String result = Tools.executeHttpGet(url);
		return result;
	}

	/***
	 * 提交新闻评论，已过时
	 * @param content
	 * @param newsID
	 * @param memberid
	 * @param commentperson
	 * @return
	 */
	@Deprecated
	public boolean postComment(String content, int newsID, int memberid,
			String position) {
		try {
			String url = PublicValue.BASEURL + "addReview.do?memberid="
					+ memberid + "&content="
					+ URLEncoder.encode(content, "UTF-8") + "&newsID=" + newsID
					+ "&position=" + position;
			String result = Tools.executeHttpGet(url);
			if (result == null)
				return false;
			else {
				try {
					JSONObject json = new JSONObject(result);
					if (json.getInt("ret") == 1) {
						return true;
					} else {
						return false;
					}
				} catch (JSONException e) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * 用户提交评论
	 * 
	 * @param params
	 * @return
	 */
	@Deprecated
	public boolean postComment(String content, int newsID, int memberid,
			String commentperson, String uid) {
		String url = PublicValue.BASEURL + "addcomment.do";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newsid", newsID + "");
		params.put("memberid", memberid + "");
		params.put("content", content);
		params.put("commentperson", commentperson);
		params.put("uniquecode", uid);
		String result = "";
		try {
			result = HttpTools.httpPost(params, url, true, "utf-8");
		} catch (Exception e) {
			result = null;
		}
		if (result == null)
			return false;
		else {
			try {
				Tools.debugLog(result);
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				return false;
			}
		}
	}

	
	
	public boolean postComment(String content, Integer newsID,
			Integer memberid, String uid, Integer replyId) {
		String url = PublicValue.BASEURL + "addReview.do";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newsid", newsID + "");
		params.put("memberid", memberid + "");
		params.put("content", content);
		params.put("uniquecode", uid);
		if(null != replyId)
			params.put("reviewid", replyId + "");
		
		
		String result = "";
		try {
			result = HttpTools.httpPost(params, url, true, "utf-8");
		} catch (Exception e) {
			result = null;
		}
		if (result == null)
			return false;
		else {
			try {
				Tools.debugLog(result);
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				return false;
			}
		}
	}
	/**
	 * 边看边聊的评论
	 * @param content
	 * @param newsID
	 * @param memberid
	 * @param uid
	 * @param replyId
	 * @return
	 */
	public boolean liveComment(String body,String userName,int userId,int liveId){
		String url = PublicValue.BASEURL + "addLiveReview.do";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("body", body);
		params.put("userName", userName );
		params.put("userId", userId+"");
		params.put("liveId", liveId+"");
		
		String result = "";
		try {
			result = HttpTools.httpPost(params, url, true, "utf-8");
			Log.i("info", result);
		} catch (Exception e) {
			result = null;
		}
		if (result == null)
			return false;
		else {
			try {
				Tools.debugLog(result);
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				return false;
			}
		}
	}

	public boolean postEComment(String txt, Integer newsId, Integer memberid) {
		// TODO Auto-generated method stub
		String url = PublicValue.BASEURL + "addArticleReview.do";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memberid", memberid+"");
		params.put("content", txt );
		params.put("articleid", newsId+"");
		
		String result = "";
		try {
			result = HttpTools.httpPost(params, url, true, "utf-8");
			Log.i("info", result);
		} catch (Exception e) {
			result = null;
		}
		if (result == null)
			return false;
		else {
			try {
				Tools.debugLog(result);
				JSONObject json = new JSONObject(result);
				if (json.getInt("ret") == 1) {
					return true;
				} else {
					return false;
				}
			} catch (JSONException e) {
				return false;
			}
		}
	}

}

