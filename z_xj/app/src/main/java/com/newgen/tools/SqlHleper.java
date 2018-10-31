package com.newgen.tools;

import java.util.ArrayList;
import java.util.List;

import com.newgen.domain.NewsMix;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.newgen.domain.NewsPub;
import com.newgen.domain.szb.Article;
import com.newgen.xj_app.detail.ww.NewsDetailActivity;

public class SqlHleper {

	

	private void createTable(Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb.db",
				Context.MODE_PRIVATE, null);
		db.execSQL("create table if not exists collect (_id INTEGER PRIMARY KEY AUTOINCREMENT,title varchar, digest varchar, commentCount INTEGER, stype INTEGER, isEpaper INTEGER,faceImage varchar, newsid INTEGER,infoType INTEGER,reviewcount INTEGER)");
		db.close();
	}
	
	private void createSupportTable(Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_support.db",
				Context.MODE_PRIVATE, null);
		db.execSQL("create table if not exists support (_id INTEGER PRIMARY KEY AUTOINCREMENT,title varchar, digest varchar, commentCount INTEGER, stype INTEGER, faceImage varchar, newsid integer)");
		db.close();
	}
	
	private void createESupportTable(Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_Esupport.db",
				Context.MODE_PRIVATE, null);
		db.execSQL("create table if not exists Esupport (_id INTEGER PRIMARY KEY AUTOINCREMENT, newsid INTEGER)");
		db.close();
	}
	

	public void addCollect(NewsMix news, Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb.db",
				Context.MODE_PRIVATE, null);
		this.createTable(act);
		Cursor cursor = null;
		
		try {
			if(news.getIsEpaper()==0){
				cursor = db.rawQuery("select * from collect where newsid=? and isEpaper = ?",
						new String[] { String.valueOf(news.getNewsPub().getId()) ,String.valueOf(news.getIsEpaper())});
			
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						Log.v("yyyy", "collect is :" + cursor.getInt(0));
					}
					return;
				}
			
				String digest = "";
				if(null != news.getNewsPub().getDigest() && !"".equals(news.getNewsPub().getDigest()))
					digest = news.getNewsPub().getDigest();
				else if(news.getNewsPub().getBody().length()>=50)
					digest = news.getNewsPub().getBody().substring(0,50);
				else
					digest = news.getNewsPub().getBody();
				db.execSQL(
						"insert into collect(title, digest, commentCount, stype, faceImage, newsid,isEpaper,infoType,reviewcount) values (?,?,?,?,?,?,?,?,?)",
						new Object[] { news.getNewsPub().getShorttitle(), digest, 0,
								news.getNewsPub().getNewsPubExt().getType(),news.getNewsPub().getNewsPubExt().getFaceimgpath()+ PublicValue.IMG_SIZE_M
								+ news.getNewsPub().getNewsPubExt().getFaceimgname() , news.getNewsPub().getId() ,news.getIsEpaper(),news.getNewsPub().getNewsPubExt().getInfotype(),news.getNewsPub().getNewsPubExt().getReviewcount()});
			}else{
				cursor = db.rawQuery("select * from collect where newsid=? and isEpaper = ?",
						new String[] { String.valueOf(news.getArticle().getId()) ,String.valueOf(news.getIsEpaper())});
			
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						Log.v("yyyy", "collect is :" + cursor.getInt(0));
					}
					return;
				}
				db.execSQL(
						"insert into collect(title, faceImage, newsid,isEpaper,reviewcount) values (?,?,?,?,?)",
						new Object[] { news.getArticle().getTitle(), 
								news.getArticle().getImages(),
								news.getArticle().getId(),news.getIsEpaper(),
								news.getArticle().getReviewCount()});
			}
			
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}
	
	
	public void addSupport(NewsPub news, Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_support.db",
				Context.MODE_PRIVATE, null);
		this.createSupportTable(act);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from support where newsid=?",
					new String[] { String.valueOf(news.getId()) });
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Log.v("yyyy", "support is :" + cursor.getInt(0));
				}
				return;
			}
			String digest = "";
			if(null != news.getDigest() && !"".equals(news.getDigest()))
				digest = news.getDigest();
			else if(news.getBody().length()>=50)
				digest = news.getBody().substring(0,50);
			else
				digest = news.getBody();
			db.execSQL(
					"insert into support(title, digest, commentCount, stype, faceImage, newsid) values (?,?,?,?,?,?)",
					new Object[] { news.getShorttitle(), digest, 0,
							news.getNewsPubExt().getType(), "", news.getId() });
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}
	
	public void addESupport(Article article,Activity act) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_Esupport.db",
				Context.MODE_PRIVATE, null);
		this.createESupportTable(act);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from Esupport where newsid=?",
					new String[] { String.valueOf(article.getId()) });
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					Log.v("yyyy", "Esupport is :" + cursor.getInt(0));
				}
				return;
			}
			db.execSQL("insert into Esupport(newsid) values (?)",
					new Object[] { article.getId() });
		} catch (Exception e) {

		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
		
	}

	public List<NewsMix> getNewsList(Activity act, int page, int size) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb.db",
				Context.MODE_PRIVATE, null);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(
							"select title, digest, commentCount, stype, isEpaper,faceImage, newsid ,infoType ,reviewcount from collect order by _id DESC limit ?,?",
							new String[] { (page - 1) * size + "",
									page * size + "" });
			List<NewsMix> list = new ArrayList<NewsMix>();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					NewsMix newsMix = new NewsMix();
					if(cursor.getInt(cursor.getColumnIndex("isEpaper"))==0){
						NewsPub news = new NewsPub();
						news.setId(cursor.getInt(cursor.getColumnIndex("newsid")));
						news.setTitle(cursor.getString(cursor
								.getColumnIndex("title")));
						news.getNewsPubExt().setFaceimgpath(cursor.getString(cursor
								.getColumnIndex("faceImage")));
						news.getNewsPubExt().setType(cursor.getInt(cursor.getColumnIndex("stype")));
						news.getNewsPubExt().setInfotype(cursor.getInt(cursor.getColumnIndex("infoType")));
						news.setDigest(cursor.getString(cursor
								.getColumnIndex("digest")));
						news.getNewsPubExt().setReviewcount(cursor.getInt(cursor.getColumnIndex("reviewcount")));
						newsMix.setArticle(null);
						newsMix.setNewsPub(news);
					}else{
						Article article = new Article();
						article.setId(cursor.getInt(cursor.getColumnIndex("newsid")));
						article.setTitle(cursor.getString(cursor
								.getColumnIndex("title")));
						article.setImages(cursor.getString(cursor
								.getColumnIndex("faceImage")));
						article.setReviewCount(cursor.getInt(cursor.getColumnIndex("reviewcount")));
						newsMix.setArticle(article);
						newsMix.setNewsPub(null);
					}
					newsMix.setIsEpaper(cursor.getInt(cursor.getColumnIndex("isEpaper")));
					list.add(newsMix);
				}
			}else{
				
				
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}
	
	
	public List<NewsPub> getSupportNewsList(Activity act, int page, int size) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_support.db",
				Context.MODE_PRIVATE, null);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(
							"select title, digest, commentCount, stype, faceImage, newsid from support order by _id DESC limit ?,?",
							new String[] { (page - 1) * size + "",
									page * size + "" });
			List<NewsPub> list = new ArrayList<NewsPub>();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					NewsPub news = new NewsPub();
					news.setId(cursor.getInt(cursor.getColumnIndex("newsid")));
					news.setTitle(cursor.getString(cursor
							.getColumnIndex("title")));
					news.setStype(cursor.getInt(cursor.getColumnIndex("stype")));
					news.setDigest(cursor.getString(cursor
							.getColumnIndex("digest")));
					
					list.add(news);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}

	public int deleteCollectNews(Integer newsId,Integer isEpaper,Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb.db",
				Context.MODE_PRIVATE, null);
		try {
			db.execSQL("delete from collect where newsid=? and isEpaper = ?",
					new String[] { String.valueOf(newsId), String.valueOf(isEpaper)});
			
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	public int deleteSupportNews(Integer newsId, Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_support.db",
				Context.MODE_PRIVATE, null);
		try {
//			db.execSQL("delete from support where newsid = ?",
//					new Object[] { newsId });
			
			db.delete("support", "newsid=?", new String[]{newsId.toString()});
			db.close();
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	public int deleteESupportNews(Integer newsId, Activity act) {
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_Esupport.db",
				Context.MODE_PRIVATE, null);
		try {
//			db.execSQL("delete from support where newsid = ?",
//					new Object[] { newsId });
			
			db.delete("Esupport", "newsid=?", new String[]{newsId.toString()});
			db.close();
			return 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	public boolean findSupportByNewsId(Integer newsId, Activity act) {
		
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_support.db",
				Context.MODE_PRIVATE, null);
		
		Cursor cursor = null;
		try {
			
			cursor = db.rawQuery("select * from support where newsid=?",new String[]{newsId.toString()});
			
			if (cursor != null && cursor.getCount() > 0) {
				
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}
	
	
public boolean findESupportByNewsId(Integer newsId, Activity act) {
		
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb_Esupport.db",
				Context.MODE_PRIVATE, null);
		
		Cursor cursor = null;
		try {
			
			cursor = db.rawQuery("select * from Esupport where newsid=?",new String[]{newsId.toString()});
			
			if (cursor != null && cursor.getCount() > 0) {
				
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}
	
	
	
	public boolean findCollectByNewsId(Integer newsId, Integer type, Activity act) {
		
		SQLiteDatabase db = act.openOrCreateDatabase("bj_sgrb.db",
				Context.MODE_PRIVATE, null);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("select * from collect where newsid=? and isEpaper = ?",new String[]{newsId.toString(),type.toString()});
			if (cursor != null && cursor.getCount() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}
	}

	

	
	
	
}
