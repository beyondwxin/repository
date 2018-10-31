package com.newgen.UI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.util.AttributeSet;

public class MyViewPager extends ViewPager{


	public MyViewPager(Context context) {  
        super(context);  
    }  
  
    public MyViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent arg0) {  
        return false;  
    }  
  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        return false;  
    }  

}
