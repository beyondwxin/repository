package com.newgen.domain;

import com.lidroid.xutils.db.annotation.Id;

  /**
   * 新闻评论点赞
   *
   */
public class NewsComment {
	@Id
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int newsId;

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}
	private int commentId;

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
}
