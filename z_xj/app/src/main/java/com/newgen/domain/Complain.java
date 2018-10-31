package com.newgen.domain;

public class Complain {

	private String content;
	private String createTime;
	private int id;
	private String language;
	private String lastHandleTime;
	private int state;
	private String title;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLastHandleTime() {
		return lastHandleTime;
	}
	public void setLastHandleTime(String lastHandleTime) {
		this.lastHandleTime = lastHandleTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
