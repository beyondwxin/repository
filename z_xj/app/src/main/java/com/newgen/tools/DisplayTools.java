package com.newgen.tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayTools {
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	public static int sp2dp(Context context, float spValue){
		final float pxValue = sp2px(context, spValue);
		return px2dip(context, pxValue);
	}

	public static int getScreenWidth(Activity activty) {
		DisplayMetrics dm = new DisplayMetrics();
		activty.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity activty) {
		DisplayMetrics dm = new DisplayMetrics();
		activty.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int getInt(Context context, int res) {
		try {
			int i = (int) context.getResources().getDimension(res);
			return i;
		} catch (Exception e) {
			return 0;
		}
	}
}
