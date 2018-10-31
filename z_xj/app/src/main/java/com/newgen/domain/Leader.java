package com.newgen.domain;

import java.io.Serializable;

public class Leader implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4502146414215902971L;

	private int id;
	
	private String leadername;
	
	private String leadersummary;
	
	private String leaderphotopath;
	
	private String leaderphotoname;
	
	
	private String recorde;
	private int sno;
	
	

	public String getRecorde() {
		return recorde;
	}

	public void setRecorde(String recorde) {
		this.recorde = recorde;
	}

	private int flag;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLeadername() {
		return leadername;
	}

	public void setLeadername(String leadername) {
		this.leadername = leadername;
	}

	public String getLeadersummary() {
		return leadersummary;
	}

	public void setLeadersummary(String leadersummary) {
		this.leadersummary = leadersummary;
	}

	public String getLeaderphotopath() {
		return leaderphotopath;
	}

	public void setLeaderphotopath(String leaderphotopath) {
		this.leaderphotopath = leaderphotopath;
	}

	public String getLeaderphotoname() {
		return leaderphotoname;
	}

	public void setLeaderphotoname(String leaderphotoname) {
		this.leaderphotoname = leaderphotoname;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
