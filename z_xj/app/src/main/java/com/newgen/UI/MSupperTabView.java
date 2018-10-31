package com.newgen.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newgen.typeface.TypefaceFactory;
import com.newgen.xj_app.R;

public class MSupperTabView extends RelativeLayout{
	private RelativeLayout parent;
	private MongolTextView titelTxtView;
//	private ImageView divideView;
	private TextView summaryTxtView;
	public MSupperTabView(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.supper_tab_view_m, this);
		parent = (RelativeLayout)findViewById(R.id.parent);
		titelTxtView = (MongolTextView) findViewById(R.id.title);
//		Typeface typeface = TypefaceFactory.getTypeface(context, "FZCSK.TTF");
//		Typeface typeface = TypefaceFactory.getTypeface(context, "HWZS.TTF");
//		titelTxtView.setTypeface(typeface);
		titelTxtView.setTextSize(17);
		
//		divideView = (ImageView) findViewById(R.id.divide);
		
		summaryTxtView = (TextView) findViewById(R.id.summary);
		summaryTxtView.setVisibility(View.GONE);
	}

	/***
	 * 设置栏目值
	 * @param title
	 * @param summary
	 */
	public void setValues(String title, String summary){
		this.titelTxtView.setText(title);
		if (null==summary) {
			this.summaryTxtView.setText("");
		}else if(summary.length() >= 2){
			summary=summary.substring(0, 2);
			char[] chs = summary.toCharArray();
			summary = chs[0] + "\n" +chs[1];
		}
		this.summaryTxtView.setText("");
//		this.summaryTxtView.setText(summary);
	}
	
	/**
	 * 设置颜色
	 * @param color
	 */
	public void setColor(int color){
		this.titelTxtView.setTextColor(color);
//		this.divideView.setBackgroundColor(color);
		this.summaryTxtView.setTextColor(color);
	}

	/**
	 * 当tab的个数不足充满屏幕时候，根据标题设置宽度
	 * @param color
	 */
	public void setMaxWidth(String title){
		int setWidth = 0;
//		if(title.length()==2)
//			setWidth= title.length()*45;
//		else if(title.length()==3)
//			setWidth= title.length()*58;
//		else if(title.length()==4)
//			setWidth= title.length()*55;
//		else	
		setWidth= title.length()*12;
			
		parent.setLayoutParams(new RelativeLayout.LayoutParams(setWidth, 200));
	}
}
