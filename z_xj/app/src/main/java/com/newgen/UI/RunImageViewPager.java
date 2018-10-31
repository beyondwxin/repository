package com.newgen.UI;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 轮播图的ViewPager
 * 
 * @author hrd
 * 
 */
public class RunImageViewPager extends ViewPager {

	PointF downPoint = new PointF();

	public RunImageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {

		return super.onTouchEvent(evt);
		
	}

	/**
	 * 设置滑动冲突的事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	float startX;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return super.onInterceptTouchEvent(event);
	}
}
