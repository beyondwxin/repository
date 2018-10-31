package com.newgen.domain;

import java.io.Serializable;

public class Comment implements Serializable{


	private static final long serialVersionUID = 8591721207079947234L;

	private Integer id;
	
	private Integer newsid;
	
	private String ptime;
	
	private String region;
	
	private String content;
	
	private Integer supportcount;
	
	private Integer opposecount;
	
	private Integer flag;

	private String commentperson;
	
	private Integer memberid;
	
	public String getCommentperson() {
		return commentperson;
	}

	public void setCommentperson(String commentperson) {
		this.commentperson = commentperson;
	}



	public Integer getMemberid() {
		return memberid;
	}

	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNewsid() {
		return newsid;
	}

	public void setNewsid(Integer newsid) {
		this.newsid = newsid;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSupportcount() {
		return supportcount;
	}

	public void setSupportcount(Integer supportcount) {
		this.supportcount = supportcount;
	}

	public Integer getOpposecount() {
		return opposecount;
	}

	public void setOpposecount(Integer opposecount) {
		this.opposecount = opposecount;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
