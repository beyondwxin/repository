package com.newgen.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapTools {

	/***
	 * bitmap 切图
	 * 
	 * @param bitmap
	 *            bitmap数据
	 * @param HWRate
	 *            height/width
	 * @return
	 */
	public static Bitmap cutBitmap(Bitmap bitmap, float HWRate) {
		try {
			int sh = bitmap.getHeight();
			int sw = bitmap.getWidth();
			int needW, needH;
			float p1 = 1.0f * sh / sw;
			float p2 = HWRate;
			if (p1 > p2) {// 长图
				needW = sw;
				needH = (int) (sw * HWRate);
			} else {// 宽图
				needW = (int) (sh / HWRate);
				needH = sh;
			}
			int x = (sw - needW) / 2;
			int y = (sh - needH) / 2;

			Matrix matrix = new Matrix();
			matrix.postScale(1, 1);
			Bitmap bm = Bitmap.createBitmap(bitmap, x,  y,
					needW, needH, matrix, true);
			return bm;
		} catch (Exception ex) {
			return bitmap;
		}
	}
	/**
	 * 领导人截取
	 * @param bitmap
	 * @param HWRate
	 * @return
	 */
	public static Bitmap cutleaderBitmap(Bitmap bitmap, float HWRate) {
		try {
			int sh = bitmap.getHeight();
			int sw = bitmap.getWidth();
			int needW, needH;
			float p1 = 1.0f * sh / sw;
			float p2 = HWRate;
			if (p1 > p2) {// 长图
				needW = sw;
				needH = (int) (sw * HWRate);
			} else {// 宽图
				needW = (int) (sh / HWRate);
				needH = sh;
			}
			int x = (sw - needW) / 2;
			int y = (sh - needH) / 2;

			Matrix matrix = new Matrix();
			matrix.postScale(1, 1);
			Bitmap bm = Bitmap.createBitmap(bitmap, 0,  0,
					needW, needH, matrix, true);
			return bm;
		} catch (Exception ex) {
			return bitmap;
		}
	}
}
