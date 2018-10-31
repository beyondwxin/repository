package com.newgen.domain.szb;

import java.io.Serializable;

public class Article implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 867719036463554824L;
	//唯一标识
	private int id;
	//对应报纸ID
	private int paperId;
	//版面名
	private String verName;
	//出报时间
	private String publishDate;
	//版次
	private String verOrder;
	//引题(应放在最顶部)
	private String leadTitle;
	//标题（放引题下面）
	private String title;
	//副标题（放标题下面）
	private String title1;
	//图片信息
	private String images;
	//坐标信息
	private String coordinate;
	//作者（放副标题下面）
	private String author;
	//新闻内容
	private String content;
	//对应版面ID
	private int orderId;
	//新闻点击数
	private int click;
	//图片说明
	private String imginfo;
	//新闻内容（带html标签）
	private String htmlContent;
	//评论数
	private int reviewCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getVerOrder() {
		return verOrder;
	}

	public void setVerOrder(String verOrder) {
		this.verOrder = verOrder;
	}

	public String getLeadTitle() {
		return leadTitle;
	}

	public void setLeadTitle(String leadTitle) {
		this.leadTitle = leadTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public String getImginfo() {
		return imginfo;
	}

	public void setImginfo(String imginfo) {
		this.imginfo = imginfo;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	
}
