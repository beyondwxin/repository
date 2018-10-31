/**
 * 
 */
package com.newgen.domain;
/**
 * @author zhl
 * 2014-4-2
 */
public class Image {
	private Integer id;
	private Integer newsid;
	private String sno;
	private String picsrc;
	private String picurl;
	private Integer height;
	private Integer width;
	private String digest;
	private String createdatetime;
	private Integer pictype;
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
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getPicsrc() {
		return picsrc;
	}
	public void setPicsrc(String picsrc) {
		this.picsrc = picsrc;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	public String getCreatedatetime() {
		return createdatetime;
	}
	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}
	public Integer getPictype() {
		return pictype;
	}
	public void setPictype(Integer pictype) {
		this.pictype = pictype;
	}
	
}
