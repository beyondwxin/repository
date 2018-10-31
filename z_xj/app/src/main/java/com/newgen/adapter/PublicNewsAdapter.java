package com.newgen.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;

import com.newgen.UI.RoundImageView;
import com.newgen.domain.Leader;
import com.newgen.domain.NewsFile;
import com.newgen.domain.NewsPub;
import com.newgen.domain.RunImage;
import com.newgen.tools.BitmapTools;
import com.newgen.tools.DisplayTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * 公共的适配器
 * 
 * @author hrd
 * 
 */
public class PublicNewsAdapter extends BaseAdapter {
	static Context context;
	List<NewsPub> list;
	LayoutInflater layoutInflater;
	ImageLoader imageLoader = null;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private ImageLoadingListener animateFourListener = new AnimateFourtDisplayListener();
	private ImageLoadingListener animateBigListener = new AnimateBigDisplayListener();
	private ImageLoadingListener expandFirstDisplayListener = new ExpandFirstDisplayListener();

//	List<ImageView> pointers;
	int currentPoint;
	TextView imageState,imageCount;
	List<RunImage> runImages; 
	List<Leader> listLeader;
	boolean tag = true;
	boolean isNight = false;

	// 加两个类型
	// public final static int TYPENUMBER = 10;// item 类型数量 没增加图片多种新闻
	public final static int TYPENUMBER = 13;// item 类型数量

	public PublicNewsAdapter(Context context, List<NewsPub> list,boolean isNight) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
		this.isNight = isNight;
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
		NewsWordHolder holder = null;
		RunImageHolder imageHolder = null;
		ADHolder adHolder = null;
		VideoHolder videoHolder = null;

		ExpandHolder expandHolder = null;
		ImageHolder imageHoldernew = null;
		int type = getItemViewType(position);
		
		if (convertView == null) {
			if (type == PublicValue.NEWS_TYPE_RUNIMAGE) {// 如果是轮换图
				imageHolder = new RunImageHolder();
				convertView = layoutInflater.inflate(R.layout.run_img_itme,
						null);
				this.initRunImage(convertView, imageHolder);
			} else if (type == PublicValue.NEWS_TYPE_IMG) { // 图片新闻
				if(isNight)
					convertView = layoutInflater.inflate(
							R.layout.news_list_ad_item_night, null);
				else
					convertView = layoutInflater.inflate(
							R.layout.news_list_ad_item, null);
				adHolder = new ADHolder();
				this.initImgNews(convertView, adHolder);
			} else if (type == PublicValue.NEWS_TYPE_IMG_TWO) { // 两张图
				convertView = layoutInflater.inflate(
							R.layout.img_news_list_item2, null);
				imageHoldernew = new ImageHolder();
				this.initNewTwoImgNews(convertView, imageHoldernew);
				
			} else if (type == PublicValue.NEWS_TYPE_IMG_FOUR) {
				convertView = layoutInflater.inflate(
						R.layout.img_news_list_item4, null);
				imageHoldernew = new ImageHolder();
				this.initNewFourImgNews(convertView, imageHoldernew);
			} else if (type == PublicValue.NEWS_TYPE_COMMON) {// 通用新闻
				convertView = layoutInflater.inflate(R.layout.news_list, null);
				holder = new NewsWordHolder();
				this.initCommonNews(convertView, holder);
				
			} else if (type == PublicValue.NEWS_TYPE_BIG_IMA) { // 大图新闻
				if(isNight)
					convertView = layoutInflater.inflate(R.layout.news_big_list_night,null);
				else
					convertView = layoutInflater.inflate(R.layout.news_big_list,null);
				
				holder = new NewsWordHolder();
				
				this.initCommonNews(convertView, holder);
				
			} else if (type == PublicValue.NEWS_TYPE_VIDEO) {
				
				if(isNight)
					convertView = layoutInflater.inflate(
							R.layout.video_news_two_item_night, null);
				else
					convertView = layoutInflater.inflate(
							R.layout.video_news_two_item, null);
				
				videoHolder = new VideoHolder();
				this.initVideo(convertView, videoHolder);
				
			}  else if (type == PublicValue.NEWS_TYPE_EXPAND) {
				convertView = layoutInflater.inflate(
						R.layout.adapter_item_expand, null);
				expandHolder = new ExpandHolder();
				this.initexpand(convertView, expandHolder);
			}
		} else {
			if (type == PublicValue.NEWS_TYPE_RUNIMAGE) {
				imageHolder = (RunImageHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_COMMON) {
				holder = (NewsWordHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_BIG_IMA) {
				holder = (NewsWordHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_IMG) {
				adHolder = (ADHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_IMG_TWO) {
				imageHoldernew = (ImageHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_IMG_FOUR) {
				imageHoldernew = (ImageHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_VIDEO) {
				videoHolder = (VideoHolder) convertView.getTag();
			} else if (type == PublicValue.NEWS_TYPE_EXPAND) {
				expandHolder = (ExpandHolder) convertView.getTag();
			}

		}

		if (type == PublicValue.NEWS_TYPE_RUNIMAGE) {// 轮换图
			this.showRunImage(convertView, imageHolder, position);
		}  else if (type == PublicValue.NEWS_TYPE_COMMON) {// 小图文字新闻
			this.showCommonNews(convertView, holder, position);
		} else if (type == PublicValue.NEWS_TYPE_BIG_IMA) { // 大图文字新闻
			this.showCommonBigImgNews(convertView, holder, position);
		} else if (type == PublicValue.NEWS_TYPE_IMG) {// 图片新闻
			this.showImgNews(convertView, adHolder, position);
		} else if (type == PublicValue.NEWS_TYPE_IMG_TWO) { // 两张图
			this.showNewTwoImgNews(convertView, imageHoldernew, position);
		} else if (type == PublicValue.NEWS_TYPE_IMG_FOUR) { // 四张图
			this.showNewFourImgNews(convertView, imageHoldernew, position);
		}  else if (type == PublicValue.NEWS_TYPE_VIDEO) { // 视频
			this.ShowVideodata(convertView, videoHolder, position);
		} else if (type == PublicValue.NEWS_TYPE_EXPAND) { // 推广
			this.Showexpanddata(convertView, expandHolder, position);
		}
		return convertView;
	}

	private void initRunImage(View convertView, RunImageHolder imageHolder) {

//		android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
//				PublicValue.WIDTH, PublicValue.WIDTH / 2);
		imageHolder.imageState = (TextView) convertView
				.findViewById(R.id.txt_gallerytitle);
		imageHolder.imageCount = (TextView) convertView
				.findViewById(R.id.txt_gallerycount);
		imageHolder.viewPage = (ViewPager) convertView
				.findViewById(R.id.image_wall_gallery);
//		imageHolder.points = (LinearLayout) convertView
//				.findViewById(R.id.gallery_point_linear);
//		imageHolder.viewPage.setLayoutParams(params);
	}

	private void showRunImage(View convertView, RunImageHolder imageHolder,
			int position) {
		NewsPub news = list.get(position);
		runImages = news.getListrunimage();
		//翻转list
		runImages = TurnOver(runImages);
		
		if (null == runImages || runImages.size() <= 0)
			return;
		RunImageViewPageAdapter adapter = new RunImageViewPageAdapter(context,
				runImages);
		imageHolder.viewPage.setAdapter(adapter);
		imageHolder.viewPage.setCurrentItem(runImages.size()-1);
		
		imageHolder.imageState.setText(runImages.get(runImages.size()-1).getSummary());
		
		imageHolder.imageCount.setText("1/"+runImages.size());
		
//		pointers = new ArrayList<ImageView>();
//		imageHolder.points.removeAllViews();
//		for (int i = 0; i < runImages.size(); i++) {
//			ImageView iv = new ImageView(context);
//			iv.setImageResource(R.drawable.feature_point);
//			iv.setPadding(4, 3, 4, 3);
//			imageHolder.points.addView(iv);
//			pointers.add(iv);
//		}
//		pointers.get(0).setImageResource(R.drawable.feature_point_cur);
		currentPoint = 0;
		imageState = imageHolder.imageState;
		imageCount = imageHolder.imageCount;
		imageHolder.viewPage
				.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {
//						pointers.get(arg0).setImageResource(
//								R.drawable.feature_point_cur);
//						pointers.get(currentPoint).setImageResource(
//								R.drawable.feature_point);
						currentPoint = arg0;
						imageCount.setText(runImages.size()-currentPoint+"/"+runImages.size());
						imageState.setText(runImages.get(currentPoint)
								.getSummary());
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
		imageHolder.viewPage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		convertView.setTag(imageHolder);
	}

	private void initCommonNews(View convertView, NewsWordHolder holder) {

		holder.comment = (TextView) convertView.findViewById(R.id.newsCommen);
		holder.title = (TextView) convertView.findViewById(R.id.newsTitle);
//		holder.digest = (TextView) convertView.findViewById(R.id.newsDigest);
//		holder.mark = (ImageView) convertView.findViewById(R.id.newsMark);
		holder.commen_num = (TextView)convertView.findViewById(R.id.commen_num);
		holder.commen_time = (TextView)convertView.findViewById(R.id.commen_time);
		holder.faceImg = (RoundImageView) convertView.findViewById(R.id.newsPic);
		holder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
		holder.news_comment = (LinearLayout) convertView
				.findViewById(R.id.news_commen);
		holder.news_tag = (TextView)convertView.findViewById(R.id.news_tag);
		
	}

	private void showCommonNews(View convertView, NewsWordHolder holder,
			int position) {
		NewsPub news = list.get(position);

//		if (news.getNewsPubExt().getReviewcount() == 0) {
//			holder.news_comment.setVisibility(View.GONE);
//		} else
//			holder.news_comment.setVisibility(View.GONE);
		
//		holder.comment.setText(news.getNewsPubExt().getReviewcount() + "");
		
		String result = "";
		if(news.getPublishtime()!=null)
			result = formatTime(news.getPublishtime());
		
		holder.commen_time.setText(result);
		
		holder.news_tag.setText(news.getNewsPubExt().getInfoTypeString());
		
		if (news.getShorttitle() != null && !"".equals(news.getShorttitle())) {
			// holder.title.setText(Tools.cutString(news.getShorttitle(), 0,
			// 13));
			holder.title.setText(news.getShorttitle());
		} else {
			holder.title.setText(news.getTitle());
		}
		
		holder.commen_num.setText(news.getNewsPubExt().getReviewcount()+"");
		

//		if (news.getDigest() == null || "".equals(news.getDigest())) {
//			holder.digest.setText(Tools.cutString(news.getBody(), 0, 50));
//		} else
//			holder.digest.setText(news.getDigest());
		
		// Log.i("infoadater", typetag);
		// if (getTypetag().equals("领导动态")) {
		// holder.faceImg.setVisibility(View.GONE);
		// } else {
		
		
		if (news.getNewsPubExt().getFaceimgname() == null
				|| "".equals(news.getNewsPubExt().getFaceimgname())
				|| news.getNewsPubExt().getFaceimgpath() == null
				|| "".equals(news.getNewsPubExt().getFaceimgpath())) {// 没有封面图
			holder.faceImg.setVisibility(View.GONE);
		} else {
			holder.faceImg.setVisibility(View.VISIBLE);
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_M
					+ news.getNewsPubExt().getFaceimgname(), holder.faceImg,
					options, animateFirstListener);
			Log.i("info", PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_M
					+ news.getNewsPubExt().getFaceimgname());

			// }
		}
		setBaseHodertag(holder, news);
		convertView.setTag(holder);
	}

	private void showCommonBigImgNews(View convertView, NewsWordHolder holder,
			int position) {
		NewsPub news = list.get(position);

		if (news.getNewsPubExt().getReviewcount() == 0) {
			holder.news_comment.setVisibility(View.GONE);
		} else
			holder.news_comment.setVisibility(View.GONE);
		
		String result = formatTime(news.getPublishtime());
		
		holder.commen_time.setText(result);
		
		holder.commen_num.setText(news.getNewsPubExt().getReviewcount() + "");
		
		
		if (news.getShorttitle() != null && !"".equals(news.getShorttitle())) {
			// holder.title.setText(Tools.cutString(news.getShorttitle(), 0,
			// 13));
			holder.title.setText(news.getShorttitle());
		} else {
			holder.title.setText(news.getTitle());
		}

//		if (news.getDigest() == null || "".equals(news.getDigest())) {
//			holder.digest.setText(Tools.cutString(news.getBody(), 0, 50));
//		} else
//			holder.digest.setText(news.getDigest());
		
		// Log.i("infoadater", typetag);
		// if (getTypetag().equals("领导动态")) {
		// holder.faceImg.setVisibility(View.GONE);
		// } else {
		if (news.getNewsPubExt().getFaceimgname() == null
				|| "".equals(news.getNewsPubExt().getFaceimgname())
				|| news.getNewsPubExt().getFaceimgpath() == null
				|| "".equals(news.getNewsPubExt().getFaceimgpath())) {// 没有封面图
			holder.faceImg.setVisibility(View.GONE);
		} else {
			holder.faceImg.setVisibility(View.VISIBLE);
//			holder.faceImg
			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_M
					+ news.getNewsPubExt().getFaceimgname(), holder.faceImg,
					options, animateBigListener);
			Log.i("info", PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_M
					+ news.getNewsPubExt().getFaceimgname());

			// }
		}
		convertView.setTag(holder);
	}

	// 图片新闻
	private void initImgNews(View convertView, ADHolder adHolder) {

		adHolder.ad1 = (RoundImageView) convertView.findViewById(R.id.ad1);
		adHolder.ad2 = (RoundImageView) convertView.findViewById(R.id.ad2);
		adHolder.ad3 = (RoundImageView) convertView.findViewById(R.id.ad3);
		adHolder.adTitle = (TextView) convertView.findViewById(R.id.ad_title);
		adHolder.publish_num = (TextView) convertView.findViewById(R.id.publish_num);
		adHolder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
//		adHolder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
	}

	private void showImgNews(View convertView, ADHolder adHolder, int position) {
		try {
			// 过滤文件
			NewsPub news = list.get(position);
			List<NewsFile> imgs = news.getListpic();
			if (imgs == null)
				imgs = new ArrayList<NewsFile>();
			this.reSize(adHolder.ad1);
			this.reSize(adHolder.ad2);
			this.reSize(adHolder.ad3);
			if (imgs.size() <= 0) {
				imageLoader.displayImage("", adHolder.ad1, options,
						animateFirstListener);
				imageLoader.displayImage("", adHolder.ad2, options,
						animateFirstListener);
				imageLoader.displayImage("", adHolder.ad3, options,
						animateFirstListener);

			} else if (imgs.size() == 1) {
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad1, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad2, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad3, options,
						animateFirstListener);
			} else if (imgs.size() == 2) {
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad1, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(1).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(1).getFileName(), adHolder.ad2, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad3, options,
						animateFirstListener);
			} else if (imgs.size() >= 3) {
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(0).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(0).getFileName(), adHolder.ad1, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(1).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(1).getFileName(), adHolder.ad2, options,
						animateFirstListener);
				imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
						+ imgs.get(2).getFilePath() + PublicValue.IMG_SIZE_S
						+ imgs.get(2).getFileName(), adHolder.ad3, options,
						animateFirstListener);
			}
			
			String[] firstArray = news.getPublishtime().split(" ");
			String publishTime= firstArray[0];
			adHolder.adTitle.setText(news.getShorttitle());
			adHolder.publish_time.setText(publishTime);
			adHolder.publish_num.setText(news.getNewsPubExt().getReviewcount()+"");
//			setBaseHodertag(adHolder, news);
			
		} catch (EnumConstantNotPresentException e) {
			imageLoader.displayImage("", adHolder.ad1, options,
					animateFirstListener);
			imageLoader.displayImage("", adHolder.ad2, options,
					animateFirstListener);
			imageLoader.displayImage("", adHolder.ad3, options,
					animateFirstListener);
			adHolder.adTitle.setText("");
		}

		convertView.setTag(adHolder);
	}

	// 推广
	private void initexpand(View convertView, ExpandHolder expandHolder) {
		
		expandHolder.expand_ima = (ImageView) convertView
				.findViewById(R.id.image_item);
		expandHolder.newsTag = (TextView) convertView
				.findViewById(R.id.newsTag);

		convertView.setTag(expandHolder);
	}

	private void Showexpanddata(View convertView, ExpandHolder expandHolder,
			int position) {
		NewsPub news = list.get(position);

		if (news.getNewsPubExt().getFaceimgname() == null
				|| "".equals(news.getNewsPubExt().getFaceimgname())
				|| news.getNewsPubExt().getFaceimgpath() == null
				|| "".equals(news.getNewsPubExt().getFaceimgpath())) {// 没有封面图
			// expandHolder.expand_ima.setVisibility(View.GONE);
			expandHolder.expand_ima
					.setImageResource(R.drawable.image_load_error);
		} else {

			expandHolder.expand_ima.setVisibility(View.VISIBLE);

			imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_M
					+ news.getNewsPubExt().getFaceimgname(),
					expandHolder.expand_ima, options,
					expandFirstDisplayListener);
			Log.i("info", PublicValue.IMAGE_SERVER_PATH
					+ news.getNewsPubExt().getFaceimgpath()
					+ PublicValue.IMG_SIZE_S
					+ news.getNewsPubExt().getFaceimgname());

			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) expandHolder.expand_ima
					.getLayoutParams();
			
			
			Log.i("width", "" + expandHolder.expand_ima.getWidth());
			
//			expandHolder.expand_item_relative.setLayoutParams(new RelativeLayout.LayoutParams(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getWidth() / 4));
			
			layoutParams.height = wm.getDefaultDisplay().getWidth() / 4;
			expandHolder.expand_ima.setLayoutParams(layoutParams);

		}
		setBaseHodertag(expandHolder, news);
	}

	// 音频
//	private void initVoice(View convertView, VoiceHolder voiceHolder) {
//		voiceHolder.voice_title = (TextView) convertView
//				.findViewById(R.id.voice_title);
//		voiceHolder.voice_play = (ImageView) convertView
//		.findViewById(R.id.voice_play);
//		voiceHolder.voice_item = (RelativeLayout)convertView
//		.findViewById(R.id.voice_item);
////		voiceHolder.voice_view = (View) convertView
////				.findViewById(R.id.voice_view);// 点击播放时隐藏此View
////		voiceHolder.voice_time_text = (TextView) convertView
////				.findViewById(R.id.voice_timer_text);
////		voiceHolder.voice_img_play = (ImageView) convertView
////				.findViewById(R.id.voice_img_play);
////		voiceHolder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
//		convertView.setTag(voiceHolder);
//	}
//
//	private void ShowVoicedata(View convertView, final VoiceHolder voiceHolder,
//			int position) {
//		int voiceposition = position;
//
//		final NewsPub news = list.get(position);
////		setBaseHodertag(voiceHolder, news);
//		voiceHolder.voice_title.setText(news.getShorttitle());// 设置音频条目标题
//		
//		if(news.getStop())
//			voiceHolder.voice_play
//			.setBackgroundResource(R.drawable.play_button);
//		else 
//			voiceHolder.voice_play
//			.setBackgroundResource(R.drawable.pause_button);
//		
//		
//		//		voiceHolder.voice_img_play
////				.setBackgroundResource(R.drawable.listen_list_play);
////		if (null != news.getListaudio() && news.getListaudio().size() > 0) {
////			voiceHolder.voice_time_text.setText(news.getListaudio().get(0)
////					.getPlaytime());// 设置音频时间大小
////		} else {
////			voiceHolder.voice_time_text.setText("00:00");// 设置音频时间大小
////			convertView.setVisibility(View.GONE);
////		}
////
////		Log.i("info", news.getListaudio().size() + "");
//		voiceHolder.voice_play.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) { // 点击播放、暂停音频
//				// TODO
//				// NewsPub news = newsList.get(position);
//				
//				JCVideoPlayer.releaseAllVideos();
//				String path = news.getListaudio().get(0).getFilePath() + "/"
//						+ news.getListaudio().get(0).getFileName();
//				String title = news.getTitle();
//				Intent broadcast = new Intent();
//
//				broadcast.putExtra("path", path);
//				broadcast.putExtra("title", title);
//				broadcast.putExtra("newsId", news.getId());
//				
//				if (news.getStop()) { // 控制音频的播放与暂停
//					
//					//在播放前把所有type为音频的都设置为暂停播放
//					for(NewsPub a:list){
//						if(a.getNewsPubExt().getShowstyle()==6){
//							a.setStop(true);
//						}
//					}
//					
//					broadcast.putExtra("isStop", true);
//					setTag(false);
//					news.setStop(false);
//					broadcast.setAction("com.newgen.sjdb");// 和service中的receiver接收的action一致
//					Toast.makeText(context, "音频已播放！", Toast.LENGTH_SHORT)
//							.show();
//					
//					voiceHolder.voice_play
//					.setBackgroundResource(R.drawable.pause_button);
//
//				} else {
//					
//					broadcast.setAction("com.newgen.voice");
//					Toast.makeText(context, "音频已暂停！", Toast.LENGTH_SHORT)
//							.show();
//					setTag(true);
//					news.setStop(true);
//					voiceHolder.voice_play
//					.setBackgroundResource(R.drawable.play_button);
//				}
//
//				context.sendBroadcast(broadcast);// 发广播
//				// Toast.makeText(getActivity(), "播放音频",
//				// Toast.LENGTH_SHORT).show();
//			}
//		});
//		voiceHolder.voice_item.setOnClickListener(new OnClickListener() {// 点击进入音频新闻详情
//																			// 并播放此音頻
//					@Override
//					public void onClick(View v) {
//						// TODO
//
//						// 没有播放时播放音频
//						String path = news.getListaudio().get(0).getFilePath()
//								+ "/"
//								+ news.getListaudio().get(0).getFileName();
//						String title = news.getTitle();
//						// 进入新闻详情
//						Intent intent = new Intent(context,
//								VoiceNewsDetailActivity.class);
//						Gson gson = new Gson();
//						String newsStr = gson.toJson(news);
//						Tools.log(newsStr);
//						intent.putExtra("news", newsStr);
//						intent		.putExtra("title", title);
//						intent.putExtra("newsId", news.getId());
//						intent.putExtra("tag", isTag());
//						context.startActivity(intent);
//					}
//				});
//	}

	// 视频
	private void initVideo(View convertView, VideoHolder videoHolder) {
		videoHolder.title = (TextView) convertView
				.findViewById(R.id.video_title);
		videoHolder.commentCountNumber = (TextView) convertView
				.findViewById(R.id.commentCountNumber);
		videoHolder.video = (JCVideoPlayerStandard) convertView
				.findViewById(R.id.videocontroller1);
		videoHolder.publish_time = (TextView)convertView
				.findViewById(R.id.publish_time);
		videoHolder.comment_num = (TextView)convertView
				.findViewById(R.id.comment_num);
		videoHolder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
		convertView.setTag(videoHolder);
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
		NewsPub news = list.get(position);

		try { // 以免news.getListvedio()数组越界

			NewsFile newsFile = news.getListvedio().get(0);
			videoHolder.title.setText(news.getShorttitle() + "");
			videoHolder.publish_time.setText("");
			
			String result = formatTime(news.getPublishtime());
			
			videoHolder.publish_time.setText(result);
			
			videoHolder.comment_num.setText(news.getNewsPubExt()
					.getReviewcount()+"");
			
//			if (news.getNewsPubExt().getReviewcount() > 0) {
//				videoHolder.commentCountNumber.setText(news.getNewsPubExt()
//						.getReviewcount() + "");
//			} else
//				videoHolder.commentCountNumber.setText("");

			videoHolder.video.setUp(
					newsFile.getFilePath() + "/" + newsFile.getFileName(),
					JCVideoPlayer.SCREEN_LAYOUT_LIST, news.getShorttitle());
			
			Log.i("info", newsFile.getFilePath() + "/" + newsFile.getFileName());
			Log.i("info", news.getShorttitle());

//			setBaseHodertag(videoHolder, news);	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		imageLoader.displayImage(news.getNewsPubExt().getFaceimgpath()
				+ PublicValue.IMG_SIZE_M
				+ news.getNewsPubExt().getFaceimgname(),
				videoHolder.video.thumbImageView, options, animateBigListener);

	}

	// 两张图展示
	private void initNewTwoImgNews(View convertView, ImageHolder imageHolder) {
		imageHolder.commentCount = (TextView) convertView
				.findViewById(R.id.comment_count);
		imageHolder.imgState = (TextView) convertView
				.findViewById(R.id.image_state);
		imageHolder.pic1 = (ImageView) convertView
				.findViewById(R.id.image_item1);
		imageHolder.pic2 = (ImageView) convertView
				.findViewById(R.id.image_item2);
		imageHolder.degist = (TextView) convertView
				.findViewById(R.id.newsDigest);
		imageHolder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
		convertView.setTag(imageHolder);
	}
	

	/**
	 * 两张图片显示
	 * 
	 * @param convertView
	 * @param imageHolder
	 * @param position
	 */
	private void showNewTwoImgNews(View convertView, ImageHolder imageHolder,
			int position) {
		NewsPub news = list.get(position);

		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(0).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(0).getFileName(), imageHolder.pic1,
				options, animateFourListener);

		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(1).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(1).getFileName(), imageHolder.pic2,
				options, animateFourListener);
		imageHolder.commentCount.setText(news.getNewsPubExt().getReviewcount()
				+ "跟帖");
		imageHolder.imgState.setText(news.getShorttitle());
		imageHolder.degist.setText(news.getDigest());
//		setBaseHodertag(imageHolder, news);
	}

	// 四张图显示
	private void initNewFourImgNews(View convertView, ImageHolder imageHolder) {
		imageHolder.commentCount = (TextView) convertView
				.findViewById(R.id.comment_count);
		imageHolder.imgState = (TextView) convertView
				.findViewById(R.id.image_state);
		imageHolder.pic1 = (ImageView) convertView
				.findViewById(R.id.image_item1);
		imageHolder.pic2 = (ImageView) convertView
				.findViewById(R.id.image_item2);
		imageHolder.pic3 = (ImageView) convertView
				.findViewById(R.id.image_item3);
		imageHolder.pic4 = (ImageView) convertView
				.findViewById(R.id.image_item4);
		imageHolder.degist = (TextView) convertView
				.findViewById(R.id.newsDigest);
		imageHolder.newsTag = (TextView) convertView.findViewById(R.id.newsTag);
		convertView.setTag(imageHolder);
	}

	/**
	 * 四张图显示
	 * 
	 * @param convertView
	 * @param imageHolder
	 * @param position
	 */
	private void showNewFourImgNews(View convertView, ImageHolder imageHolder,
			int position) {
		NewsPub news = list.get(position);

		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(0).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(0).getFileName(), imageHolder.pic1,
				options, animateFourListener);

		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(1).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(1).getFileName(), imageHolder.pic2,
				options, animateFourListener);
		
		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(2).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(2).getFileName(), imageHolder.pic3,
				options, animateFourListener);
		
		imageLoader.displayImage(PublicValue.IMAGE_SERVER_PATH
				+ news.getListpic().get(3).getFilePath()
				+ PublicValue.IMG_SIZE_M
				+ news.getListpic().get(3).getFileName(), imageHolder.pic4,
				options, animateFourListener);
		
		imageHolder.commentCount.setText(news.getNewsPubExt().getReviewcount()
				+ "跟帖");
		imageHolder.imgState.setText(news.getShorttitle());
		imageHolder.degist.setText(news.getDigest());
//		setBaseHodertag(imageHolder, news);
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
		int type = msg.getNewsPubExt().getType();
		int showstyle = msg.getNewsPubExt().getShowstyle();
		int infotype = msg.getNewsPubExt().getInfotype();
		List<NewsFile> listpic = msg.getListpic();
		if (showstyle <= 0) {
			if (type == -1)// 轮换图
				return PublicValue.NEWS_TYPE_RUNIMAGE;
			else if (type == 0)// 文字新闻
				return PublicValue.NEWS_TYPE_COMMON;
			else if (type == 1)// 图片新闻
				return PublicValue.NEWS_TYPE_IMG;
			else if (type == 2)// 视频
				return PublicValue.NEWS_TYPE_VIDEO;
			else
				return PublicValue.NEWS_TYPE_COMMON;
		} else {
			// 1.普通2.大图3.三图4.四图5.视频6.音频7.窄图
			if (showstyle == 1) {
				return PublicValue.NEWS_TYPE_COMMON;
			} else if (showstyle == 2) {
				return PublicValue.NEWS_TYPE_BIG_IMA;
			} else if (showstyle == 3) {
				if (listpic.size() > 2) {
					return PublicValue.NEWS_TYPE_IMG;
				} else {
					return PublicValue.NEWS_TYPE_BIG_IMA;
				}
			} else if (showstyle == 4) {
				if (listpic.size() >= 4) {
					return PublicValue.NEWS_TYPE_IMG_FOUR;
				} else {
					return PublicValue.NEWS_TYPE_BIG_IMA;
				}

			} else if (showstyle == 5) {
				return PublicValue.NEWS_TYPE_VIDEO;
			} else if (showstyle == 7) {
				return PublicValue.NEWS_TYPE_EXPAND;
			} else
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

	class LeaderHolder {
		GridView leaderTable;
	}

	class NewsWordHolder extends BaseHolder {
		TextView title;
		TextView digest;
		TextView comment;
		TextView commen_num;
		TextView commen_time;
		TextView news_tag;
		// TextView newsTag;// 新闻标志
		ImageView mark;
		RoundImageView faceImg;
		LinearLayout news_comment;
	}

	class ADHolder extends BaseHolder {
		RoundImageView ad1, ad2, ad3;
		TextView adTitle;
		TextView publish_time;
		TextView publish_num;
	}

	class RunImageHolder {
		ViewPager viewPage;
		TextView imageState;
		TextView imageCount;
//		LinearLayout points;
		LinearLayout runImageLayout;
	}

	class VideoHolder extends BaseHolder {
		ImageView faceImage;
		TextView title, commentCountNumber;
		TextView publish_time,comment_num;
		JCVideoPlayerStandard video;
	}

	class VoiceHolder extends BaseHolder {
		TextView voice_title;
		ImageView voice_play;
		RelativeLayout voice_item;
//		TextView voice_time_text;
//		ImageView voice_img_play;
//		View voice_view;
	}

	class ExpandHolder extends BaseHolder {
		RelativeLayout expand_item_relative;
		ImageView expand_ima;
		// TextView expand_tag;
	}

	class ImageHolder extends BaseHolder {
		ImageView pic1, pic2, pic3, pic4;
		TextView imgState;
		TextView commentCount;
		TextView degist;
	}

	class FaceImageHolder {
		ImageView faceImg;
		TextView summary;
	}

	private void reSize(ImageView button) {
		LayoutParams params = (LayoutParams) button.getLayoutParams();// new
																		// LayoutParams(height,
																		// height);
		int width = (PublicValue.WIDTH - DisplayTools.dip2px(context, 20)) / 3;
		int height = (int) (width * 0.75);
		params.height = height;
		button.setLayoutParams(params);
		button.setScaleType(ScaleType.FIT_CENTER);
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
					Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3/ 4f);
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
					params.height = width*3/5;
					imageView.setLayoutParams(params);
					Bitmap bm = BitmapTools.cutBitmap(loadedImage,3/5f);
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
	 * 四张图 展示样式
	 */
	private static class AnimateFourtDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				try {
					LayoutParams params = Tools
							.getLinearLayoutParams((PublicValue.WIDTH - Tools
									.dip2px(context, 15)) / 2, 3 / 4f);
					ImageView imageView = (ImageView) view;
					Bitmap bm = BitmapTools.cutBitmap(loadedImage, 3 / 4f);
					imageView.setImageBitmap(bm);
					imageView.setLayoutParams(params);
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
	

	private static class ExpandFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				try {
					ImageView imageView = (ImageView) view;

					Bitmap bm = BitmapTools.cutBitmap(loadedImage, 1 / 4f);
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

	private class AnimateFirstDisplayListener1 extends
			SimpleImageLoadingListener {

		final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				LayoutParams params = Tools.getLinearLayoutParams(
						PublicValue.WIDTH, 1 / 2f);
				ImageView imageView = (ImageView) view;
				Bitmap bm = BitmapTools.cutBitmap(loadedImage, 1 / 2f);
				imageView.setImageBitmap(bm);
				imageView.setLayoutParams(params);
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	private String formatTime(String publishtime) {
		// TODO Auto-generated method stub
		//[2017-12-05, 08:07:16.847]
		String result = "";
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");       
		String date = sDateFormat.format(new java.util.Date()); 
		SimpleDateFormat sTimeFormat = new SimpleDateFormat("HH:mm");       
		String time = sTimeFormat.format(new java.util.Date());
		String[] firstArray = publishtime.split(" ");
		String publishTime= firstArray[0];
		if(publishTime.equals(date)){
			firstArray[1] = firstArray[1].substring(0, 5);
			if(firstArray[1].substring(0,2).equals(time.substring(0, 2))){//一个小时内
				try {
					int nowMin = Integer.parseInt(time.substring(3,5));
					int oldMin = Integer.parseInt(firstArray[1].substring(3,5));
					int chaMin = nowMin - oldMin;
					if(chaMin>0)
						result = chaMin+"分钟前";
					else
						result ="刚刚";
				}catch (NumberFormatException e) {
				    e.printStackTrace();
				}
			}else{
				try {
					int nowHour = Integer.parseInt(time.substring(0,2));
					int oldHour = Integer.parseInt(firstArray[1].substring(0,2));
					int chaHour = nowHour - oldHour;
					result = chaHour+"小时前";
				}catch (NumberFormatException e) {
				    e.printStackTrace();
				}
			}
		}else{
			result = firstArray[0];
		}
		return result;
	}
	
	private List<RunImage> TurnOver(List<RunImage> list) {
		// TODO Auto-generated method stub
		List<RunImage> temp = new ArrayList<RunImage>();
		for(int i = list.size()-1;i>=0;i--){
			temp.add(list.get(i));
		}
		return temp;
	}
	
}
