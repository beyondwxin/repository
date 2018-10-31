package com.newgen.xj_app.hw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.SpUtil;
import com.newgen.xj_app.R;

import java.util.HashMap;
import java.util.Map;

public class HTextSizeActivity extends Activity implements View.OnClickListener {

    private RelativeLayout img_re1;
    private RelativeLayout img_re2;
    private RelativeLayout img_re3;
    private RelativeLayout img_re4;
    private ImageView img_1;
    private ImageView img_2;
    private ImageView img_3;
    private ImageView img_4;
    private ImageView img_fanhui;
    private int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_size);
        initView();
        initSysoutFontSize();
        initDataView();
    }

    /**
     * 获取控件 设置点击事件
     */
    private void initView() {
        // 布局控件
        img_re1 = (RelativeLayout) findViewById(R.id.img_re1);
        img_re2 = (RelativeLayout) findViewById(R.id.img_re2);
        img_re3 = (RelativeLayout) findViewById(R.id.img_re3);
        img_re4 = (RelativeLayout) findViewById(R.id.img_re4);
        // 选中时控件
        img_1 = (ImageView) findViewById(R.id.img_1);
        img_2 = (ImageView) findViewById(R.id.img_2);
        img_3 = (ImageView) findViewById(R.id.img_3);
        img_4 = (ImageView) findViewById(R.id.img_4);
        // 返回控件
//		img_fanhui = (ImageView) findViewById(R.id.img_fanhui);
        // 设置点击事件
        img_re1.setOnClickListener(this);
        img_re2.setOnClickListener(this);
        img_re3.setOnClickListener(this);
        img_re4.setOnClickListener(this);
//		img_fanhui.setOnClickListener(this);
    }

    /**
     * 获取保存值，设置对勾的显示
     */
    private void initDataView() {
        int img = SpUtil.getInt(this, "img", 0);
        switch (img){
            case 0:
                img_1.setVisibility(View.VISIBLE);
                break;
            case 1:
                img_2.setVisibility(View.VISIBLE);
                break;
            case 2:
                img_3.setVisibility(View.VISIBLE);
                break;
            case 3:
                img_4.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 点击处理 如果被点击 则显示选中√  并保存值
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_re1:
                img_1.setVisibility(View.VISIBLE);
                img_2.setVisibility(View.GONE);
                img_3.setVisibility(View.GONE);
                img_4.setVisibility(View.GONE);
                SpUtil.putInt(this,"img",0);
                SharedPreferencesTools.NORMALSIZE = 2;
                SharedPreferencesTools.SIZENAME = "小";
                saveFontSizeData(SharedPreferencesTools.NORMALSIZE,
                        SharedPreferencesTools.SIZENAME);
                break;
            case R.id.img_re2:
                img_1.setVisibility(View.GONE);
                img_2.setVisibility(View.VISIBLE);
                img_3.setVisibility(View.GONE);
                img_4.setVisibility(View.GONE);
                SpUtil.putInt(this,"img",1);
                SharedPreferencesTools.NORMALSIZE = 3;
                SharedPreferencesTools.SIZENAME = "默认";
                saveFontSizeData(SharedPreferencesTools.NORMALSIZE,
                        SharedPreferencesTools.SIZENAME);
                break;
            case R.id.img_re3:
                img_1.setVisibility(View.GONE);
                img_2.setVisibility(View.GONE);
                img_3.setVisibility(View.VISIBLE);
                img_4.setVisibility(View.GONE);
                SpUtil.putInt(this,"img",2);
                SharedPreferencesTools.NORMALSIZE = 4;
                SharedPreferencesTools.SIZENAME = "大";
                saveFontSizeData(SharedPreferencesTools.NORMALSIZE,
                        SharedPreferencesTools.SIZENAME);
                break;
            case R.id.img_re4:
                img_1.setVisibility(View.GONE);
                img_2.setVisibility(View.GONE);
                img_3.setVisibility(View.GONE);
                img_4.setVisibility(View.VISIBLE);
                SpUtil.putInt(this,"img",3);
                SharedPreferencesTools.NORMALSIZE = 5;
                SharedPreferencesTools.SIZENAME = "超大";
                saveFontSizeData(SharedPreferencesTools.NORMALSIZE,
                        SharedPreferencesTools.SIZENAME);
                break;
        }
    }


    /***
     * 初始化字号大小
     */
    private void initSysoutFontSize() {
        // 字号初始化
        Map<String, ?> map = SharedPreferencesTools.getValue(this,
                SharedPreferencesTools.KEY_FOUNT_SIZE_HW);
        try {
            if (null != map)
                fontSize = Integer.parseInt((String) map.get("size"));
            else
                fontSize = 3;
        } catch (Exception e) {
            fontSize = 3;
        }
    }

    private void saveFontSizeData(int size, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("size", size + "");
        SharedPreferencesTools.setValue(HTextSizeActivity.this, map,
                SharedPreferencesTools.KEY_FOUNT_SIZE_HW);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
