package com.newgen.domain;

import java.util.List;

public class NewsVoteItem {

	
	private int id;
	
	private int voteid;
	
	private int parentid;
	
	private String body;
	
	private String img;
	
	private String color;
	
	private int  count;

	private List<NewsVoteItem> listchildren;
	
	public int getParentid() {
		return parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public List<NewsVoteItem> getListchildren() {
		return listchildren;
	}

	public void setListchildren(List<NewsVoteItem> listchildren) {
		this.listchildren = listchildren;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVoteid() {
		return voteid;
	}

	public void setVoteid(int voteid) {
		this.voteid = voteid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
