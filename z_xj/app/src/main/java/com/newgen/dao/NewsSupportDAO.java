package com.newgen.dao;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.newgen.domain.NewsSupport;

public class NewsSupportDAO extends BaseDAO {
	
	private DbUtils db = null;
	
	public NewsSupportDAO(Context context) {
		db = DbUtils.create(context, DBName);
	}

	public void insert(NewsSupport support) throws DbException{
		db.createTableIfNotExist(NewsSupport.class); //创建一个表NewsSupport
		db.save(support);
	}
	
	public NewsSupport findByNewsId(int id) throws DbException{
		return db.findFirst(Selector.from(NewsSupport.class).where("newsId", "=", id));
	}
}
