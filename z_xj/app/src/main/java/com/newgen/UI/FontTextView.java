package com.newgen.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView{
	
	public FontTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	
	 public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
	
    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(getTypeface(getContext(),"fzlth"));
    }
    
    protected static Typeface getTypeface(Context p_context, String p_fontName){
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(p_context.getAssets(), "font/" + p_fontName + ".otf");
        }catch(Exception e) {}

        if( tf != null ) return tf;

        try {
            tf = Typeface.createFromAsset(p_context.getAssets(), "font/" + p_fontName + ".ttf");
        }catch(Exception e) {}

        return tf;
    }
    
    
    private Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }
	

}
