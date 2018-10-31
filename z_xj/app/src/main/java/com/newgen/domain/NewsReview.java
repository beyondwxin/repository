package com.newgen.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻回复表
 * 
 * @author liusl
 *
 * 2015-4-21
 */
public class NewsReview implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9110418175675927464L;

	private int id;
	
	private int  newsid;
	
	private int parentid;
	

	private String body;
	
	private Integer state;//0：未审核；1：审核；2：删除

	private String username;
	
	private int userid;
	
	private String ip;
	
	
	private String position;
	
	
	private String createtime;

	private List<NewsReview> listchild = new ArrayList<NewsReview>();
	private String userName;
	private String userPhoto;
	
	
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserPhoto() {
		return userPhoto;
	}


	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}


	public List<NewsReview> getListchild() {
		return listchild;
	}


	public void setListchild(List<NewsReview> listchild) {
		this.listchild = listchild;
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


	public int getParentid() {
		return parentid;
	}


	public void setParentid(int parentid) {
		this.parentid = parentid;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getUserid() {
		return userid;
	}


	public void setUserid(int userid) {
		this.userid = userid;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getCreatetime() {
		return createtime;
	}


	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
	
	
	
	
}
