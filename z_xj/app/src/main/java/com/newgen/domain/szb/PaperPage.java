package com.newgen.domain.szb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报纸片面抽象
 * @author
 *
 */
public class PaperPage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5564307278121633372L;
	/**
	 * 唯一标识
	 */
	private int id;
	/***
	 * 对应那期报纸ID
	 */
	private int paperId;
	/**
	 * 版次
	 */
	private String verOrder;
	/**
	 * 版次标题
	 */
	private String verName;
	
	/***
	 * 版面图（片面图可根据版次取得，此字段可不用）
	 */
	private String faceImg;
	
	
	private List<Article> articles = new ArrayList<Article>();

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

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

	public String getVerOrder() {
		return verOrder;
	}

	public void setVerOrder(String verOrder) {
		this.verOrder = verOrder;
	}

	public String getVerName() {
		return verName;
	}

	public void setVerName(String verName) {
		this.verName = verName;
	}

	public String getFaceImg() {
		return faceImg;
	}

	public void setFaceImg(String faceImg) {
		this.faceImg = faceImg;
	}
}
