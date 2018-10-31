package com.newgen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author hrd
 *
 * 2015-4-20
 */

public class NewsPubExt implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1091236874700914335L;

	private int id;
	
	private int newsid;
	
	private int type;//图文|图片|视频|音频|投票
	
	private int infotype;//0:默认�?：独家；2：专�?	
	
	private String faceimgname;
	
	private String faceimgpath;
	
	private String infoTypeString;
	
	private int  reviewcount;
	private String luckyid;
	private String url;
	private int showstyle;
	
	private int liveId;
	
	public int getLiveId() {
		return liveId;
	}


	public void setLiveId(int liveId) {
		this.liveId = liveId;
	}


	public int getShowstyle() {
		return showstyle;
	}


	public void setShowstyle(int showstyle) {
		this.showstyle = showstyle;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getLuckyid() {
		return luckyid;
	}


	public void setLuckyid(String luckyid) {
		this.luckyid = luckyid;
	}


	private int  supportcount;
	private List<NewsFile> liveFiles = new ArrayList<NewsFile>();

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


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getInfotype() {
		return infotype;
	}


	public void setInfotype(int infotype) {
		this.infotype = infotype;
	}


	public String getFaceimgname() {
		return faceimgname;
	}


	public void setFaceimgname(String faceimgname) {
		this.faceimgname = faceimgname;
	}


	public String getFaceimgpath() {
		return faceimgpath;
	}


	public void setFaceimgpath(String faceimgpath) {
		this.faceimgpath = faceimgpath;
	}


	public int getReviewcount() {
		return reviewcount;
	}


	public void setReviewcount(int reviewcount) {
		this.reviewcount = reviewcount;
	}


	public int getSupportcount() {
		return supportcount;
	}


	public void setSupportcount(int supportcount) {
		this.supportcount = supportcount;
	}


	public List<NewsFile> getLiveFiles() {
		return liveFiles;
	}


	public void setLiveFiles(List<NewsFile> liveFiles) {
		this.liveFiles = liveFiles;
	}


	public String getInfoTypeString() {
		return infoTypeString;
	}


	public void setInfoTypeString(String infoTypeString) {
		this.infoTypeString = infoTypeString;
	}
	

	
}
