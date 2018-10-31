package com.newgen.tools;

import android.content.Context;
import android.content.Intent;

import com.newgen.domain.NewsPub;
import com.newgen.xj_app.detail.mw.MImgNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MNewsDetailActivity;
import com.newgen.tools.PublicValue;

public class MNewsDetailActivityFactory {
	
	public static void openActivity(NewsPub news, Context context) throws Exception{
		if(null == news || null == context)
			throw new Exception("新闻无法查看");
		int newsID = news.getId();
		int infoType = news.getNewsPubExt().getInfotype();
		int type = news.getNewsPubExt().getType();
		
		Intent mIntent = new Intent();
		
		if (newsID > 0) {
			mIntent.putExtra("newsID", newsID);
			mIntent.putExtra("newsId", newsID);
			mIntent.putExtra("type", type);
			mIntent.putExtra("infoType", infoType);
			switch (type) {
			case PublicValue.NEWS_STYLE_WORD:// 文字稿
				mIntent.setClass(context, MNewsDetailActivity.class);
				mIntent.putExtra("newsObject", news);
				break;
			case PublicValue.NEWS_STYLE_IMG:// 图片稿
				mIntent.setClass(context, MImgNewsDetailActivity.class);
				break;
			default:
				mIntent.setClass(context, MNewsDetailActivity.class);
				mIntent.putExtra("newsObject", news);
				break;
			}
			context.startActivity(mIntent);
		}
	}
}
