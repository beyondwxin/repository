package com.newgen.UI;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newgen.tools.Tools;
import com.newgen.xj_app.R;

public class BaseTitleLayout extends RelativeLayout {

    OnBackListener onBackListener;
    String titleText;
    int backgroundColor;
    int titleColor;
    int backImageViewDrawable;


    public BaseTitleLayout(Context context) {
        this(context, null, 0);
    }

    public BaseTitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);

        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseTitleLayout);
        titleText = ta.getString(R.styleable.BaseTitleLayout_titleText);
        backgroundColor = ta.getColor(R.styleable.BaseTitleLayout_titleBackgroundColor, getResources().getColor(R.color.white));
        titleColor = ta.getColor(R.styleable.BaseTitleLayout_titleColor, getResources().getColor(R.color.result_view));
        backImageViewDrawable = ta.getResourceId(R.styleable.BaseTitleLayout_backImageDrawable,R.drawable.back_arrow);
        ta.recycle();

        if (TextUtils.isEmpty(titleText)) {
            titleText = "  ";
        }


    }

    private void init(Context context) {
        setBackgroundColor(backgroundColor);
        //添加一个RelativeLayout包裹返回的Image
        RelativeLayout childRelativeLayout = new RelativeLayout(context);
        LayoutParams rlLp = new LayoutParams(Tools.dip2px(getContext(),47), ViewGroup.LayoutParams.MATCH_PARENT);
        rlLp.addRule(ALIGN_PARENT_LEFT);
        childRelativeLayout.setLayoutParams(rlLp);
        childRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBackListener == null) {
                    ((Activity) getContext()).finish();
                } else {
                    onBackListener.onBack();
                }
            }
        });
        //返回的Image
        ImageView ivBack = new ImageView(context);
        LayoutParams ivLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivLp.setMargins(Tools.dip2px(getContext(),10), 0, 0, 0);
        ivLp.addRule(CENTER_VERTICAL);
        ivBack.setLayoutParams(ivLp);
        ivBack.setImageResource(backImageViewDrawable);

        childRelativeLayout.addView(ivBack);
        addView(childRelativeLayout);

        //Title
        TextView tvTitle = new TextView(context);
        LayoutParams tvLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvLp.addRule(ALIGN_PARENT_TOP);
        tvLp.addRule(CENTER_HORIZONTAL);
        tvTitle.setLayoutParams(tvLp);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvTitle.setText(titleText);
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        addView(tvTitle);
    }

    public OnBackListener getOnBackListener() {
        return onBackListener;
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }


    public interface OnBackListener {
        void onBack();
    }
}
