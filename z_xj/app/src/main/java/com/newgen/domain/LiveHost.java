package com.newgen.domain;

import java.io.Serializable;

public class LiveHost implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5837221850042038488L;
	private int id;
	private String name;
	private String txName;
	private String txPath;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxName() {
		return txName;
	}
	public void setTxName(String txName) {
		this.txName = txName;
	}
	public String getTxPath() {
		return txPath;
	}
	public void setTxPath(String txPath) {
		this.txPath = txPath;
	}
}
