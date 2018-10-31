package com.newgen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.BaseAdapter;

import com.newgen.UI.MongolTextView;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsPub;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MVideoNewsAdapter extends BaseAdapter {

    static Context context;
    List<NewsPub> list;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = null;
    DisplayImageOptions options;

    VideoHolder videoHolder = null;
    boolean tag = true;

    public MVideoNewsAdapter(Context context, List<NewsPub> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_load_error)
                .showImageForEmptyUri(R.drawable.image_load_error)
                .showImageOnFail(R.drawable.image_load_error)
                .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        imageLoader = ImageLoader.getInstance();
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
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.video_news_two_item_m, null);
            videoHolder = new VideoHolder();
            videoHolder.video_title_html = (WebView) convertView.
                    findViewById(R.id.video_title_html);
            videoHolder.commen_time = convertView.findViewById(R.id.commen_time);
            videoHolder.video = (JCVideoPlayerStandard) convertView
                    .findViewById(R.id.videocontroller1);

        } else
            videoHolder = (VideoHolder) convertView.getTag();


        try { // 以免news.getListvedio()数组越界

            NewsPub news = list.get(position);

            videoHolder.video_title_html.loadUrl("file:///android_asset/title/mwContentComponent.html");
            videoHolder.video_title_html.getSettings().setJavaScriptEnabled(true);
            videoHolder.video_title_html.addJavascriptInterface(new JSInterface(position), "jsObj");

            NewsFile newsFile = news.getListvedio().get(0);

            videoHolder.commen_time.setTextSize(13);
            String re = news.getPublishtime().replace('-', '.');
            String[] re2 = re.split(" ");
            String re3 = re2[1].substring(0, re2[1].length() - 7);
            String result = re2[0] + "\t\t" + re3;

            videoHolder.commen_time.setText(result);

            videoHolder.video.setUp(
                    newsFile.getFilePath() + "/" + newsFile.getFileName(),
                    JCVideoPlayer.SCREEN_LAYOUT_LIST, news.getShorttitle());

            imageLoader.displayImage(news.getNewsPubExt().getFaceimgpath()
                            + PublicValue.IMG_SIZE_M
                            + news.getNewsPubExt().getFaceimgname(),
                    videoHolder.video.thumbImageView, options, null);

        } catch (Exception e) {
            // TODO: handle exception
        }
        convertView.setTag(videoHolder);

        return convertView;
    }


    class VideoHolder {
        WebView video_title_html;
        MongolTextView commen_time;
        JCVideoPlayerStandard video;
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
                return "{'content':'" + list.get(position).getShorttitle() + "','fontSize':'18'}";
            else
                return "{'content':'" + list.get(position).getTitle() + "','fontSize':'18'}";

        }

        ;
    }


    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }


}
