package com.newgen.fragment.mw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyGridView;
import com.newgen.UI.MyToast;
import com.newgen.UI.PullToRefreshView;
import com.newgen.UI.PullToRefreshView.OnFooterRefreshListener;
import com.newgen.UI.PullToRefreshView.OnHeaderRefreshListener;
import com.newgen.UI.RunImageViewPager;
import com.newgen.adapter.MPublicNewsAdapter;
import com.newgen.adapter.MRunImageViewPageAdapter;
import com.newgen.domain.NewsPub;
import com.newgen.domain.RunImage;
import com.newgen.server.NewsServer;
import com.newgen.tools.PublicValue;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.mw.MImgNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MLinkDetailActivity;
import com.newgen.xj_app.detail.mw.MLiveDetailActivity;
import com.newgen.xj_app.detail.mw.MNewsDetailActivity;
import com.newgen.xj_app.detail.mw.MSubjectDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MIndexFragmentItem extends Fragment implements OnHeaderRefreshListener,
        OnFooterRefreshListener {

    MyGridView listView;
    int startid = 0;
    String score = "";
    int currentPoint;
    private PullToRefreshView mPullDownScrollView;
    private FrameLayout runImgBlock;

    LoadDateTask task;
    Handler myHandler;

    String uuid = "";

    List<NewsPub> tempList = null;
    List<NewsPub> newsList = new ArrayList<NewsPub>();
    List<RunImage> imgtempList = null;
    List<RunImage> imgsList = new ArrayList<RunImage>();
//	List<Image> runImages;

    int cid, index;
    String cname;
    int showType;

    private final String cate = "cate_";

    MPublicNewsAdapter adapter;
    RunImageViewPager image_wall_gallery;
    MongolTextView txt_gallerytitle, txt_gallerycount;

    private Receiver mReceiver;

    public int firstVisible = 0, visibleCount = 0, totalCount = 0;

    private long flushTime = 0;
    boolean isFrist = true;
    boolean isNight = false;

    private ScrollView scroll_parent;

    View v;

    public void setCategory(int cid, int position, String cname) {
        this.cid = cid;
        this.index = position;
        this.cname = cname;
    }

    public void setCategory(int cid, int position, String cname, int showType) {
        // TODO Auto-generated method stub
        this.cid = cid;
        this.index = position;
        this.cname = cname;
        this.showType = showType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (savedInstanceState != null) {
            this.cid = savedInstanceState.getInt("cid");
            this.index = savedInstanceState.getInt("index");
            this.cname = savedInstanceState.getString("cname");
            this.showType = savedInstanceState.getInt("showType");
        }

        uuid = SharedPreferencesTools.getValue(getActivity(),
                SharedPreferencesTools.KEY_DEVICE_ID,
                SharedPreferencesTools.KEY_DEVICE_ID);

        v = inflater.inflate(R.layout.index_fragment_item_m, container,
                false);

        runImgBlock = (FrameLayout) v.findViewById(R.id.runImgBlock);
        image_wall_gallery = (RunImageViewPager) v.findViewById(R.id.image_wall_gallery);
        txt_gallerycount = (MongolTextView) v.findViewById(R.id.txt_gallerycount);
        txt_gallerytitle = (MongolTextView) v.findViewById(R.id.txt_gallerytitle);
        txt_gallerytitle.setTextColor(Color.parseColor("#FFFFFF"));
        scroll_parent = (ScrollView) v.findViewById(R.id.scroll_parent);

        mPullDownScrollView = (PullToRefreshView) v.findViewById(R.id.refresh_root);
        mPullDownScrollView.setEnablePullTorefresh(true);
        mPullDownScrollView.setEnablePullLoadMoreDataStatus(true);
        mPullDownScrollView.setOnHeaderRefreshListener(this);
        mPullDownScrollView.setOnFooterRefreshListener(this);
        adapter = new MPublicNewsAdapter(getActivity(), newsList, true);
        listView = (MyGridView) v.findViewById(R.id.gridView);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new ListItemClick());
        listView.setAdapter(adapter);
        initHandle();

        //接收广播
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.newgen.sg_news.activity.RETURNTOP");
            mReceiver = new Receiver();
            getActivity().registerReceiver(mReceiver, filter);//注册广播接收者
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }


    private void initHandle() {
        // TODO Auto-generated method stub
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        runImgBlock.setVisibility(View.VISIBLE);

                        MRunImageViewPageAdapter adapter = new MRunImageViewPageAdapter(getActivity(),
                                imgsList);

                        image_wall_gallery.setAdapter(adapter);
                        txt_gallerytitle.setText(imgsList.get(0).getSummary());
                        txt_gallerycount.setText("1/" + imgsList.size());

                        currentPoint = 0;

                        image_wall_gallery
                                .setOnPageChangeListener(new OnPageChangeListener() {
                                    @Override
                                    public void onPageSelected(int arg0) {
//								pointers.get(arg0).setImageResource(
//										R.drawable.feature_point_cur);
//								pointers.get(currentPoint).setImageResource(
//										R.drawable.feature_point);
                                        currentPoint = arg0;
                                        txt_gallerycount.setText(currentPoint + 1 + "/" + imgsList.size());
                                        txt_gallerytitle.setText(imgsList.get(currentPoint).getSummary());
                                    }

                                    @Override
                                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int arg0) {
                                        // TODO Auto-generated method stub

                                    }
                                });

                        image_wall_gallery.setOnTouchListener(new OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                return false;
                            }
                        });

                        break;
                    case 2:
                        runImgBlock.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.cid = savedInstanceState.getInt("cid");
            this.index = savedInstanceState.getInt("index");
            this.cname = savedInstanceState.getString("cname");
            this.showType = savedInstanceState.getInt("showType");
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (isFrist) {
//			getCacheData();// 获取缓存数据
            flushTime = new Date().getTime();
//			onRefresh();
            onHeaderRefresh(null);

            isFrist = false;
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        // TODO Auto-generated method stub
        /* 下拉刷新数据 */
        JCVideoPlayer.releaseAllVideos();
        startid = 0;
        score = "";
        GetRunImgList m = new GetRunImgList();
        new Thread(m).start();

        task = new LoadDateTask();
        task.execute();
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        // TODO Auto-generated method stub
        JCVideoPlayer.releaseAllVideos();
        if (null != newsList && newsList.size() > 0)
            startid = newsList.get(newsList.size() - 1).getId();
        if (showType == PublicValue.RecommendshowType && null != newsList && newsList.size() > 0)
            score = newsList.get(newsList.size() - 1).getScore();

        task = new LoadDateTask();
        task.execute();
    }


//	@Override
//	public void onRefresh() {
//		JCVideoPlayer.releaseAllVideos();
//		startid = -1;
//		score ="";
//		long tempTime = new Date().getTime();
//		String notice = Tools.getTimeInterval(flushTime, tempTime);
//		listView.setRefreshTime(notice);
//		flushTime = tempTime;
//		task = new LoadDateTask();
//		task.execute();
//	}
//
//	@Override
//	public void onLoadMore() {
//		JCVideoPlayer.releaseAllVideos();
//		if (null != newsList && newsList.size() > 0)
//			startid = newsList.get(newsList.size() - 1).getId();
//		if (showType==PublicValue.RecommendshowType&&null != newsList && newsList.size() > 0)
//			score = newsList.get(newsList.size() - 1).getScore();
//		
//		task = new LoadDateTask();
//		task.execute();
//	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt("cid", cid);
        outState.putInt("index", index);
        outState.putString("cname", cname);
        outState.putInt("showType", showType);
    }


    class ListItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            NewsPub news = newsList.get(position);
            Intent intent = null;
            if (news.getNewsPubExt().getInfotype() == 3) {//推广
                if (news.getNewsPubExt().getUrl() != null
                        && !news.getNewsPubExt().getUrl().equals("")) {
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(news.getNewsPubExt().getUrl());
                    intent.setData(content_url);
                } else {
                    intent = new Intent(getActivity(), MNewsDetailActivity.class);
                }
            } else if (news.getNewsPubExt().getInfotype() == PublicValue.NEWS_TAG_SUBJECT) {//专题
                intent = new Intent(getActivity(), MSubjectDetailActivity.class);
                intent.putExtra("summary", news.getDigest());
                intent.putExtra("faceImage", news.getNewsPubExt()
                        .getFaceimgpath()
                        + PublicValue.IMG_SIZE_M
                        + news.getNewsPubExt().getFaceimgname());
            } else if (news.getNewsPubExt().getInfotype() == PublicValue.NEWS_TAG_LIVE) {//图文直播
                intent = new Intent(getActivity(),
                        MLiveDetailActivity.class);
                intent.putExtra("title", news.getTitle());
                intent.putExtra("liveid", news.getNewsPubExt().getLiveId());
                intent.putExtra("createtime", "");
            } else
                switch (news.getNewsPubExt().getType()) {
                    case PublicValue.NEWS_STYLE_WORD:
                        intent = new Intent(getActivity(), MNewsDetailActivity.class);
                        intent.putExtra("newsObject", news);
                        break;
                    case PublicValue.NEWS_STYLE_IMG:
                        intent = new Intent(getActivity(),
                                MImgNewsDetailActivity.class);
                        break;
                    case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
                        intent = new Intent(getActivity(), MLinkDetailActivity.class);
                        intent.putExtra("url", news.getNewsPubExt().getUrl());
                        intent.putExtra("shareimg", news.getNewsPubExt()
                                .getFaceimgpath()
                                + PublicValue.IMG_SIZE_M
                                + news.getNewsPubExt().getFaceimgname());
                        break;
                    default:
                        intent = new Intent(getActivity(), MNewsDetailActivity.class);
                        intent.putExtra("newsObject", news);
                        break;
                }
            if (null != intent) {
                intent.putExtra("newsId", news.getId());
                intent.putExtra("title", news.getShorttitle());
                JCVideoPlayer.releaseAllVideos();
                startActivity(intent);
            }

        }
    }


    private class LoadDateTask extends AsyncTask<Object, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            JCVideoPlayer.releaseAllVideos();
            listView.setEnabled(false);
        }

        @Override
        protected Integer doInBackground(Object... arg0) {
            int ret = -1;
            try {
                NewsServer server = new NewsServer();

                if (showType != PublicValue.RecommendshowType)
                    if (cid > 0)
                        tempList = (server.getNewsList(cid, 9, startid));
                    else
                        tempList = (server.getLocationNewsList(PublicValue.LOCATIONCITY.getProvince() + "_"
                                + PublicValue.LOCATIONCITY.getCity(), 9, startid));
                else
                    tempList = (server.getRecommendNewsList(uuid, score));

                if (tempList == null) {
                    ret = -1;
                } else if (tempList.size() <= 0) {
                    ret = 0;
                } else {
                    ret = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                switch (result) {
                    case 1:
                        if (startid <= 0) {// 刷新动作
                            newsList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        if (showType != PublicValue.RecommendshowType)
                            newsList.addAll(tempList);
                        else
                            addNoRepeatData(tempList);
//					newsList.addAll(tempList);
                        adapter.notifyDataSetChanged();
//					Tools.setListViewHeightBasedOnChildren(listView);
                        tempList.clear();
                        tempList = null;
                        startid = newsList.get(newsList.size() - 1).getId();
                        break;
                    case -1:
                        break;
                    case 0:
                        if (startid <= 0) {// 刷新动作
                            newsList.clear();
                            adapter.notifyDataSetChanged();
                        }
                        MyToast.showToast(getActivity(), "没有更多数据", 3);
                        break;
                }
                stopLoadOrRefresh();
                listView.setEnabled(true);
            } catch (Exception e) {

            }
        }


        @Override
        protected void onCancelled() {
            stopLoadOrRefresh();
            listView.setEnabled(true);
        }

    }

    /**
     * 视频停止、音频停止
     */
    public void stopLoadOrRefresh() {
        JCVideoPlayer.releaseAllVideos();
//		listView.stopRefresh();
//		listView.stopLoadMore();
        mPullDownScrollView.onHeaderRefreshComplete();
        mPullDownScrollView.onFooterRefreshComplete();
    }


    /**
     * 判断是否有视频item在 如果有在超出视图的时候暂停视频播放
     */
    public void autoPlayVideo(AbsListView view) {

        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.videocontroller1) != null) {

                JCVideoPlayerStandard videoPlayerStandard1 = (JCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.videocontroller1);
                Rect rect = new Rect();
                videoPlayerStandard1.getLocalVisibleRect(rect);
                int videoheight3 = videoPlayerStandard1.getHeight();
                Log.e("HomePageVideoRelease", "i=" + i + "===" + "videoheight3:" + videoheight3 + "===" + "rect.top:" + rect.top + "===" + "rect.bottom:" + rect.bottom);
                if (rect.top == 0 && rect.bottom == videoheight3) {
                } else {
                    //部分不在视图中
                    Log.e("HomePageVideoRelease", "======================releaseAllVideos=====================");
                    JCVideoPlayer.releaseAllVideos();
                }
            }
        }
    }

    /***
     * 获取缓存数据
     */
    public void getCacheData() {
        String jStr = SharedPreferencesTools.getValue(getActivity(),
                cate + cid, SharedPreferencesTools.CACHEDATA);
        if (null != jStr && !"".equals(jStr)) {
            try {
                Gson gson = new Gson();
                JSONArray json = new JSONArray(jStr);
                for (int i = 0; i < json.length(); i++) {
                    NewsPub news = gson.fromJson(json.getString(i),
                            NewsPub.class);
                    if (null != news)
                        newsList.add(news);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void addNoRepeatData(List<NewsPub> tempList) {
        // TODO Auto-generated method stub
        newsList.addAll(tempList);
        for (int i = 0; i < newsList.size(); i++) {
            for (int j = i + 1; j < newsList.size(); j++) {
                if (newsList.get(j).getId() == newsList.get(i).getId()) {
                    newsList.remove(j);
                    break;
                }
            }
        }
    }


    class GetRunImgList implements Runnable {
        Message msg = new Message();

        @Override
        public void run() {

            NewsServer server = new NewsServer();
            imgtempList = server.getImgsList(cid);
            imgsList.clear();
            if (imgtempList != null && imgtempList.size() != 0) {
                msg.what = 1;
                imgsList.addAll(imgtempList);
            } else
                msg.what = 2;

            myHandler.sendMessage(msg);

        }

    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播后的处理
//        	listView.setSelection(0);
        }
    }


}
