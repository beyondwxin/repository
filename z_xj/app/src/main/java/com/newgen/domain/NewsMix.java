package com.newgen.domain;

import com.newgen.domain.szb.Article;

public class NewsMix {
	
	private Integer isEpaper;
	private  NewsPub newsPub;
	private Article article;
	public Integer getIsEpaper() {
		return isEpaper;
	}
	public void setIsEpaper(Integer isEpaper) {
		this.isEpaper = isEpaper;
	}
	public NewsPub getNewsPub() {
		return newsPub;
	}
	public void setNewsPub(NewsPub newsPub) {
		this.newsPub = newsPub;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}

}
