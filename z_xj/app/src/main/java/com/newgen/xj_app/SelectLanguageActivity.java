package com.newgen.xj_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.newgen.domain.Category;
import com.newgen.server.MainServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.xj_app.hw.HMainFragmentActivity;
import com.newgen.xj_app.mw.MMainFragmentActivity;
import com.newgen.xj_app.ww.MainFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.net.newgen.widget.dialog.ArtWaitDialog;

public class SelectLanguageActivity extends Activity implements OnClickListener {

    private ImageButton ib_1, ib_2, ib_3;
    Dialog dialog;
    int type = 1;//1是蒙文 2 是维文  3是哈文
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        dialog = new ArtWaitDialog(SelectLanguageActivity.this, "加载栏目中..");

        ib_1 = findViewById(R.id.ib_1);
        ib_2 = findViewById(R.id.ib_2);
        ib_3 = findViewById(R.id.ib_3);
        ib_1.setOnClickListener(this);
        ib_2.setOnClickListener(this);
        ib_3.setOnClickListener(this);

        initHandler();
    }

    private void initHandler() {
        // TODO Auto-generated method stub
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Intent intent = null;
                        dialog.dismiss();
                        if (type == 1) {
                            intent = new Intent(SelectLanguageActivity.this, MMainFragmentActivity.class);
                            startActivity(intent);
                        } else if (type == 2) {
                            intent = new Intent(SelectLanguageActivity.this, MainFragmentActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(SelectLanguageActivity.this, HMainFragmentActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        dialog.dismiss();
                        Toast.makeText(SelectLanguageActivity.this,
                                "获取栏目失败", 5).show();
                        break;
                    case 3:
                        PublicValue.TRANSLOCATIONCITY = msg.getData().getString("city");
                        break;
                    default:
                        break;
                }

            }
        };
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == ib_1) {//蒙文
            type = 1;
            PublicValue.BASEURL = PublicValue.BASEURL_MW;
            dialog.show();
            GetData m = new GetData();
            new Thread(m).start();
            TranslateCity n = new TranslateCity();
            new Thread(n).start();
        } else if (v == ib_2) {//维吾尔文
            type = 2;
            PublicValue.BASEURL = PublicValue.BASEURL_WW;
            dialog.show();
            GetData m = new GetData();
            new Thread(m).start();
            TranslateCity n = new TranslateCity();
            new Thread(n).start();
        } else {//哈文
            type = 3;
            PublicValue.BASEURL = PublicValue.BASEURL_HW;
            dialog.show();
            GetData m = new GetData();
            new Thread(m).start();
            TranslateCity n = new TranslateCity();
            new Thread(n).start();
        }
    }

    class TranslateCity implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            MainServer mserver = new MainServer();
            String resultStr = mserver.TranslateCity();
            Message msg = new Message();
//			Log.e("info", "++++++++++++++++" + resultStr);
            try {
                JSONObject json = new JSONObject(resultStr);
                if (json.getInt("ret") == 1) {// 成功
                    msg.what = 3;
                    Bundle bundle = new Bundle();
                    bundle.putString("city", json.getString("tranContent"));
                    msg.setData(bundle);
                }
            } catch (Exception e) {

            }
            ;
            myHandler.sendMessage(msg);
        }

    }


    class GetData implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            MainServer mserver = new MainServer();
            String resultStr = mserver.getNewsCategoryAndADPath();
            Message msg = new Message();
            Log.e("info", "++++++++++++++++" + resultStr);
            try {
                JSONObject json = new JSONObject(resultStr);
                if (json.getInt("ret") == 1) {// 成功
                    readData(json);
                } else {
                    msg.what = 2;
                }
            } catch (Exception e) {

            }
            ;
            myHandler.sendMessage(msg);

        }
    }


    /***
     * 读取栏目信息
     *
     * @param json
     * @throws JsonSyntaxException
     * @throws JSONException
     */
    private void readData(JSONObject json) throws JsonSyntaxException,
            JSONException {
        // TODO Auto-generated method stub
        JSONArray categorysJson = json.getJSONArray("listCategory");
        Gson gson = new Gson();
        /* 保存栏目数据到文件 */
        Map<String, String> map = new HashMap<String, String>();
        if (type == 1) {
            map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_MW, categorysJson.toString());
            Log.i("tostring", categorysJson.toString());
            SharedPreferencesTools.setValue(SelectLanguageActivity.this, map,
                    PublicValue.WORD_NEWS_CATEGORY_FILE_MW);
        } else if (type == 2) {
            map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_WW, categorysJson.toString());
            Log.i("tostring", categorysJson.toString());
            SharedPreferencesTools.setValue(SelectLanguageActivity.this, map,
                    PublicValue.WORD_NEWS_CATEGORY_FILE_WW);
        } else {
            map.put(PublicValue.WORD_NEWS_CATEGORY_FILE_HW, categorysJson.toString());
            Log.i("tostring", categorysJson.toString());
            SharedPreferencesTools.setValue(SelectLanguageActivity.this, map,
                    PublicValue.WORD_NEWS_CATEGORY_FILE_HW);
        }

        PublicValue.CATEGORYS.clear();

        for (int i = 0; i < categorysJson.length(); i++) {

            Category c = gson.fromJson(categorysJson.getString(i),
                    Category.class);
            if (c != null)
                PublicValue.CATEGORYS.add(c);
        }

        Message msg = new Message();
        msg.what = 1;
        myHandler.sendMessage(msg);
    }

}
