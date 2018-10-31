package com.newgen.typeface;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Typeface;

/***
 * ���幤��
 * @author yilang
 *
 */
public class TypefaceFactory {
	private static final Map<String, Typeface> typefaceMap = new HashMap<String, Typeface>();
	private static Typeface typeface = null;
	public static Typeface getTypeface(Context context, String fontName){
		try{
			if(null == typeface){
				typeface = Typeface.createFromAsset(context.getAssets(), "font/" + fontName);
				typefaceMap.put(fontName, typeface);
			}
			return typeface;
		}catch(Exception e){
			return Typeface.DEFAULT;
		}
	}
}
