package com.newgen.domain;

import java.io.Serializable;

public class RunImage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3108489410255127374L;

	private int id;

	private int categoryid;

	private String imgname;

	private String imgpath;

	private int newsid;

	private int newstype;

	private int newsinfotype;

	private int sno;

	private String summary;

	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public int getNewsid() {
		return newsid;
	}

	public void setNewsid(int newsid) {
		this.newsid = newsid;
	}

	public int getNewstype() {
		return newstype;
	}

	public void setNewstype(int newstype) {
		this.newstype = newstype;
	}

	public int getNewsinfotype() {
		return newsinfotype;
	}

	public void setNewsinfotype(int newsinfotype) {
		this.newsinfotype = newsinfotype;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
