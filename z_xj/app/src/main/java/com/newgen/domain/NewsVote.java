package com.newgen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 投票表
 * 
 * @author liusl
 *
 * 2015-4-20
 */
public class NewsVote implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5803487085960912552L;

	private int id;
	
	private int newsid;
	
	private String createtime;
	
	private int type; //0 普通投票， 2 问卷调查
	
	private int count; //总票数
	
	private String endtime;
	
	private int  flag;//0未开始 1 进行中2已结束
	
	
	
	private int confineobject;
	
	private int interval;
	
	private int maxitemnumber;
	
	private String title;
	
	private String summary;
	
	private List<NewsVoteItem> listVoteItem =new ArrayList<NewsVoteItem>();
	
	public static final int VOTE_ABLE = 1;//能投票
	public static final int VOTE_DISABLE = 0;//不能投票
	
	private Integer isvoteflag;
	
	private String imgpath = "";
	
	private String imgname = "";

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public Integer getIsvoteflag() {
		return isvoteflag;
	}

	public void setIsvoteflag(Integer isvoteflag) {
		this.isvoteflag = isvoteflag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNewsid() {
		return newsid;
	}

	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getConfineobject() {
		return confineobject;
	}

	public void setConfineobject(int confineobject) {
		this.confineobject = confineobject;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getMaxitemnumber() {
		return maxitemnumber;
	}

	public void setMaxitemnumber(int maxitemnumber) {
		this.maxitemnumber = maxitemnumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<NewsVoteItem> getListVoteItem() {
		return listVoteItem;
	}

	public void setListVoteItem(List<NewsVoteItem> listVoteItem) {
		this.listVoteItem = listVoteItem;
	}
	
	
	
	

	
	
}
