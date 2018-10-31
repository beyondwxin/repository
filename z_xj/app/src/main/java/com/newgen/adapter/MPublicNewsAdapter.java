package com.newgen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newgen.UI.MongolTextView;
import com.newgen.UI.MyWebView;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsPub;
import com.newgen.tools.BitmapTools;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 公共的适配器
 *
 * @author suny
 */
public class MPublicNewsAdapter extends BaseAdapter {
    static Context context;
    List<NewsPub> list;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = null;
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private ImageLoadingListener animateBigListener = new AnimateBigDisplayListener();

    //	List<ImageView> pointers;
    int currentPoint;
    boolean tag = true;
    int currentPosition = 0;
    NewsWordHolder holder = null;
    VideoHolder videoHolder = null;
    boolean isIndex = false;

    // 加两个类型
    // public final static int TYPENUMBER = 10;// item 类型数量 没增加图片多种新闻
    public final static int TYPENUMBER = 2;// item 类型数量

    public MPublicNewsAdapter(Context context, List<NewsPub> list, boolean isIndex) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.isIndex = isIndex;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_load_error)
                .showImageForEmptyUri(R.drawable.image_load_error)
                .showImageOnFail(R.drawable.image_load_error)
                .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        imageLoader = ImageLoader.getInstance();
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null || list.size() <= 0)
            return 0;
        else
            return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);

        if (convertView == null) {
            if (type == PublicValue.NEWS_TYPE_COMMON) {// 通用新闻
                convertView = layoutInflater.inflate(R.layout.news_list_m, null);
                holder = new NewsWordHolder();
                this.initCommonNews(convertView, holder);
            } else if (type == PublicValue.NEWS_TYPE_VIDEO) {
                convertView = layoutInflater.inflate(R.layout.video_news_two_item_m, null);
                videoHolder = new VideoHolder();
                this.initVideoView(convertView, videoHolder);
            }
        } else {
            if (type == PublicValue.NEWS_TYPE_COMMON) {
                holder = (NewsWordHolder) convertView.getTag();
            } else if (type == PublicValue.NEWS_TYPE_VIDEO) {
                videoHolder = (VideoHolder) convertView.getTag();
            }
        }

        if (type == PublicValue.NEWS_TYPE_COMMON) {// 小图文字新闻
            this.showCommonNews(convertView, holder, position, parent);
        } else if (type == PublicValue.NEWS_TYPE_VIDEO) { // 视频
            this.ShowVideodata(convertView, videoHolder, position);
        }

        return convertView;
    }

    private void initCommonNews(View convertView, NewsWordHolder holder) {
        holder.faceImg = (ImageView) convertView.findViewById(R.id.newsPic);
        holder.commen_time = (MongolTextView) convertView.findViewById(R.id.commen_time);
        holder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
        holder.newsTitle_html = (MyWebView) convertView.findViewById(R.id.newsTitle_html);
    }


    private void showCommonNews(View convertView, final NewsWordHolder holder,
                                int position, ViewGroup parent) {
        final NewsPub news = list.get(position);

        currentPosition = position;

        holder.commen_time.setTextSize(13);

        int width = (parent.getWidth() - 20) / 3;
        int height = (width-50) * 3;
        Log.e("height", height + "");
        //item的layoutparams用GridView.LayoutParams或者  AbsListView.LayoutParams设置，不能用LinearLayout.LayoutParams
        //convertView.setLayoutParams(new    GridView.LayoutParams(width,height));
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        holder.newsTitle_html.loadUrl("file:///android_asset/title/mwContentComponent.html");
        holder.newsTitle_html.getSettings().setJavaScriptEnabled(true);
        holder.newsTitle_html.addJavascriptInterface(new JSInterface(position), "jsObj");
        String re = news.getPublishtime().replace('-', '.');
        String[] re2 = re.split(" ");
        String re3 = re2[1].substring(0, re2[1].length() - 7);
        String result = re2[0] + "\t\t" + re3;

        holder.commen_time.setText(result);

        if (news.getNewsPubExt().getFaceimgname() == null
                || "".equals(news.getNewsPubExt().getFaceimgname())
                || news.getNewsPubExt().getFaceimgpath() == null
                || "".equals(news.getNewsPubExt().getFaceimgpath())) {// 没有封面图
            holder.faceImg.setVisibility(View.GONE);

            Log.e("commonImgUrl", PublicValue.IMAGE_SERVER_PATH
                    + news.getNewsPubExt().getFaceimgpath()
                    + PublicValue.IMG_SIZE_M
                    + news.getNewsPubExt().getFaceimgname());

        } else {
            holder.faceImg.setVisibility(View.VISIBLE);

            imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
                            + news.getNewsPubExt().getFaceimgpath()
                            + PublicValue.IMG_SIZE_M
                            + news.getNewsPubExt().getFaceimgname(), holder.faceImg,
                    options, animateFirstListener);
        }
        convertView.setTag(holder);
    }


    // 视频
    private void initVideoView(View convertView, VideoHolder videoHolder) {
        videoHolder.video_title_html = (WebView) convertView.
                findViewById(R.id.video_title_html);
        videoHolder.video = (JCVideoPlayerStandard) convertView
                .findViewById(R.id.videocontroller1);
    }

    /**
     * 视频显示列表
     *
     * @param convertView
     * @param videoHolder
     * @param position
     */
    private void ShowVideodata(View convertView, VideoHolder videoHolder,
                               int position) {


        try { // 以免news.getListvedio()数组越界

            NewsPub news = list.get(position);

            videoHolder.video_title_html.loadUrl("file:///android_asset/title/mwContentComponent.html");
            videoHolder.video_title_html.getSettings().setJavaScriptEnabled(true);
            videoHolder.video_title_html.addJavascriptInterface(new JSInterface(position), "jsObj");

            NewsFile newsFile = news.getListvedio().get(0);

//			String result = formatTime(news.getPublishtime());

            videoHolder.video.setUp(
                    newsFile.getFilePath() + "/" + newsFile.getFileName(),
                    JCVideoPlayer.SCREEN_LAYOUT_LIST, news.getShorttitle());

            imageLoader.displayImage(news.getNewsPubExt().getFaceimgpath()
                            + PublicValue.IMG_SIZE_M
                            + news.getNewsPubExt().getFaceimgname(),
                    videoHolder.video.thumbImageView, options, animateBigListener);

//			setBaseHodertag(videoHolder, news);	
        } catch (Exception e) {
            // TODO: handle exception
        }
        convertView.setTag(videoHolder);
    }

    @Override
    public int getViewTypeCount() {
        return TYPENUMBER;
    }

    @Override
    public int getItemViewType(int position) {
        NewsPub msg = list.get(position);
        // TODO 使用NewsPubExt中的type 替换 msg.getStype()
        // //图文|图片|视频|音频|投票
        // 1.普通2.大图3.三图4.四图5.视频6.音频

        if (isIndex)
            return PublicValue.NEWS_TYPE_COMMON;
        else {
            int showstyle = msg.getNewsPubExt().getShowstyle();

            // 1.普通2.大图3.三图4.四图5.视频6.音频7.窄图
            if (showstyle == 1)
                return PublicValue.NEWS_TYPE_COMMON;
            else if (showstyle == 5)
                return PublicValue.NEWS_TYPE_VIDEO;
            else
                return PublicValue.NEWS_TYPE_COMMON;
        }
    }

    /**
     * 设置tag
     *
     * @param baseholder
     * @param news
     */
    public void setBaseHodertag(BaseHolder baseholder, NewsPub news) {
        switch (news.getNewsPubExt().getInfotype()) {

            default:
                baseholder.newsTag.setVisibility(View.GONE);
                break;
        }
    }

    class BaseHolder {
        TextView newsTag;
    }

    class NewsWordHolder extends BaseHolder {
        ImageView faceImg;
        MongolTextView commen_time;
        MyWebView newsTitle_html;
    }

    class VideoHolder extends BaseHolder {
        WebView video_title_html;
        JCVideoPlayerStandard video;
    }


    /*
     * 普通新闻图  展示样式
     */
    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                try {
                    ImageView imageView = (ImageView) view;
                    Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 4f);
                    imageView.setImageBitmap(bm);
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 500);
                        displayedImages.add(imageUri);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    /*
     * 大图和视频图  展示样式
     */
    private static class AnimateBigDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                try {
                    ImageView imageView = (ImageView) view;
                    android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    int width = imageView.getWidth();
                    params.height = width * 3 / 5;
                    imageView.setLayoutParams(params);
                    Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 5f);
                    imageView.setImageBitmap(bm);
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 500);
                        displayedImages.add(imageUri);
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private String formatTime(String publishtime) {
        // TODO Auto-generated method stub
        //[2017-12-05, 08:07:16.847]
        String result = "";
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String date = sDateFormat.format(new java.util.Date());
        SimpleDateFormat sTimeFormat = new SimpleDateFormat("HH:mm");
        String time = sTimeFormat.format(new java.util.Date());
        String[] firstArray = publishtime.split(" ");
        String publishTime = firstArray[0];
        if (publishTime.equals(date)) {
            firstArray[1] = firstArray[1].substring(0, 5);
            if (firstArray[1].substring(0, 2).equals(time.substring(0, 2))) {//一个小时内
                try {
                    int nowMin = Integer.parseInt(time.substring(3, 5));
                    int oldMin = Integer.parseInt(firstArray[1].substring(3, 5));
                    int chaMin = nowMin - oldMin;
                    if (chaMin > 0)
                        result = chaMin + "分钟前";
                    else
                        result = "刚刚";
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int nowHour = Integer.parseInt(time.substring(0, 2));
                    int oldHour = Integer.parseInt(firstArray[1].substring(0, 2));
                    int chaHour = nowHour - oldHour;
                    result = chaHour + "小时前";
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else {
            result = firstArray[0] + firstArray[1];
        }
        return result;
    }

    /***
     * js 调用接口类
     *
     * @author sy
     *
     */
    private class JSInterface {

        int position = 0;

        public JSInterface(int position) {
            this.position = position;
        }

        @JavascriptInterface
        public String getInitContent() {
            if (list.get(position).getShorttitle() != null
                    && !"".equals(list.get(position).getShorttitle()))
                return "{'content':'" + list.get(position).getShorttitle() + "','fontSize':'18','line':'2'}";
            else
                return "{'content':'" + list.get(position).getTitle() + "','fontSize':'18','line':'2'}";

        }

        ;
    }

}
