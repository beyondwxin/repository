package com.newgen.dao;

import java.util.List;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.newgen.domain.NewsComment;

public class NewsCommentDAO extends CommentDAO {
	
	private DbUtils db = null;
	
	public NewsCommentDAO(Context context) {
		db = DbUtils.create(context, DDBName);
	}

	public void insert(NewsComment support) throws DbException{
		db.createTableIfNotExist(NewsComment.class); //创建一个表NewsComment
		db.save(support);
	}
	
	public List<NewsComment> findByNewsId(int id) throws DbException{
		return db.findAll(Selector.from(NewsComment.class).where("newsId", "=", id ));
		
	}
	public int deleteByid(int id) throws DbException{
		try {
			db.deleteById(NewsComment.class, WhereBuilder.b("newsId", "=", id));
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
	} 
}