package com.newgen.fragment.szb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.yoojia.imagemap.ImageMap;
import net.yoojia.imagemap.core.PolyShape;
import net.yoojia.imagemap.core.Shape;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.newgen.domain.szb.Article;
import com.newgen.domain.szb.PaperPage;
import com.newgen.tools.ArticlesTools;
import com.newgen.tools.BitmapUtil;
import com.newgen.tools.FileUtils;
import com.newgen.tools.MD5;
import com.newgen.tools.SharedPreferencesTools;
import com.newgen.tools.Tools;
import com.newgen.xj_app.R;
import com.newgen.xj_app.detail.ww.EpaperArticleDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 电子报显示Fragment
 * 
 * @author hrd
 * 
 */
public class DZBPicFragment extends Fragment {
	int screenWidth;
	int screenHeight;
	private ImageMap map;
	private PaperPage picArt;
	private String paperName;
	// 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;
	private Map<Object, Shape> shapesCache;
	// private int transAction = 0;

	/**
	 * Image 下载器
	 */

	private FileUtils utils;

	ImageLoader loader;
	DisplayImageOptions options;
	ImageLoaderConfiguration configuration;

	// private LoadNewsAsyncTask task;

	private int with_b;
	// int hight = map.getHeight();
	private int hight_b;
	private float min;
	private Bitmap bitmap;

	boolean isNight = false;
	
	private List<Article> mList = null;
	// private AbHttpUtil mAbHttpUtil = null;
	private View contextView = null;
	// private FinalBitmapTools bitmaoTools=null;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 11:
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Shape shape_new = (Shape) msg.obj;
				shape_new.setAlaph(0);
				shape_new.onScale((float) (1 / min));
				map.addShape(shape_new);

				break;
			case 12:
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Shape shape_new_test = (Shape) msg.obj;
				shape_new_test.setAlaph(0);
				shape_new_test.onScale((float) (1 / min));
				map.addShape(shape_new_test);

				break;

			case 13:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Shape shape_new_test1 = (Shape) msg.obj;
				shape_new_test1.setAlaph(0);
				shape_new_test1.onScale((float) (1 / min));
				map.addShape(shape_new_test1);

				break;

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (null != savedInstanceState) {
			picArt = (PaperPage) savedInstanceState.getSerializable("data");
			paperName = savedInstanceState.getString("paperName");
			Log.i("savedInstanceState", "diyici");
		}
		
		contextView = inflater.inflate(R.layout.dzb_fragment_pic, container,false);
		
		Tools.debugLog("DZBPicFragment onCreateView");
		initView();
		initData();
		Loading();
		return contextView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putSerializable("data", picArt);
		outState.putString("paperName", paperName);
		super.onSaveInstanceState(outState);
	}

	public void setData(PaperPage page, String paperName) {
		this.picArt = page;
		this.paperName = paperName;
		Log.i("setData", picArt.getFaceImg());
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		shapesCache = new HashMap<Object, Shape>();
		// mImageDownLoader = new ImageDownLoader(getActivity());
		utils = new FileUtils(getActivity());
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		mList = new ArrayList<Article>();

	}

	/**
	 * 界面初始化
	 */
	private void initView() {
		// TODO Auto-generated method stub
		map = (ImageMap) contextView.findViewById(R.id.imagemap);
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.image_load_error)
				.showImageForEmptyUri(R.drawable.image_load_error)
				.showImageOnFail(R.drawable.image_load_error)
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(0)).build();
		configuration = ImageLoaderConfiguration.createDefault(getActivity());
	}

	private void initValidata() {
		// TODO Auto-generated method stub
		// Bitmap bitmap;
		try {
			Log.e("http_url_error", picArt.getFaceImg());
			// Intent intent = new Intent("com.newgen.faceimgpath");
			// String string =picArt.getFaceImg();
			// String replace = string.replace('S', 'M');
			// intent.putExtra("imgpath", replace);
			// Log.i("info", replace);
			// Log.i("info", picArt.getFaceImg());
			// getActivity().sendBroadcast(intent);
			// String[] time = picArt.getFaceImg().split("/");
			// String date = time[time.length - 2];
			// String newName = (date + time[time.length - 1]);
			// 将图片地址的MD5加密码作为图片的缓存名
			String newName = MD5.md5(picArt.getFaceImg());
			// String newName = picArt.getFaceImg();

			// FinalBitmapToolsForLoacl tools=new
			// FinalBitmapToolsForLoacl(getActivity());
			// tools.
			// //tools.LoadingPic(newName, view);
			//

			bitmap = utils.getBitmap(newName);

			if (bitmap == null) {
				bitmap = BitmapUtil.returnBitMap(picArt.getFaceImg());
				utils.savaBitmap(newName, bitmap);
			}

		} catch (Exception e) {
			// TODO: handle exception
			// bitmap = BitmapUtil.ReadBitmapById(getActivity(),
			// R.drawable.loading);
			try {

			} catch (Exception e1) {
				// TODO Auto-generated catch block

			}
			// ReadBitmapById(getActivity(),
			// R.drawable.loading);
		}

	}

	private void initImagView() {
		try {
			with_b = bitmap.getWidth();
			// int hight = map.getHeight();
			hight_b = bitmap.getHeight();
			float num_1 = ((float) (screenWidth - 40) / (float) with_b);
			float num_2 = ((float) (screenHeight - 100) / (float) hight_b);

			if (num_1 > num_2) {
				min = num_2;
			} else {
				min = num_1;
			}
			min = min * 0.97f;

			map.setMapBitmap(bitmap, min);
			/**
			 * 个个新闻的在此复制
			 */
			if (null == mList || mList.size() <= 0) {
				Shape shape = new PolyShape(0, R.color.transparent);// 透明色
				shape.setValues("0,0,0,0,0,0,0,0");
				shape.setAlaph(0);// 数值越小，越透
				map.addShape(shape);
				shapesCache.put(shape.tag, shape);
			} else
				for (int i = 0; i < mList.size(); i++) {
					Article p = mList.get(i);
					Shape shape = new PolyShape(p.getId(), R.color.transparent);// 透明色
					String num = null;
					String coordinate = p.getCoordinate().replaceAll(";", ",");
					num = ArticlesTools.getNums(coordinate, with_b, hight_b);
					shape.setValues(num);
					shape.setAlaph(0);// 数值越小，越透
					map.addShape(shape);
					shapesCache.put(shape.tag, shape);
				}
		} catch (Exception e) {
			// TODO: handle exception
//			Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
//			Log.e("e", e.getMessage());
		}
	}

	/**
	 * 数据的再次获取
	 */
	private void Loading() {
		// TODO Auto-generated method stub
		mList = picArt.getArticles();
		// initImagView();
		// initListener();
		new LoadData().execute();

	}

	/**
	 * 将String数据转化为json数据
	 * 
	 * @param content
	 * @param is_saveCache
	 */
	// private void Str_toJSon(String content, boolean is_saveCache) {
	// // TODO Auto-generated method stub
	// try {
	// JSONObject obj = new JSONObject(content);
	// int re = obj.getInt("ret");
	// if (re == 1) {
	// JSONArray json_array = obj.getJSONArray("data");
	// List<Article> list_obj = new ArrayList<Article>();
	// for (int i = 0; i < json_array.length(); i++) {
	//
	// Gson gson = new Gson();
	// // 解析不成功呀,全部是null
	// Article news_obj = new Article();
	// String json_str = json_array.getString(i);
	// // 特殊情况
	// if (json_str.equals("null")) {
	// // ViewInject.toast(SystemString.no_news);
	// // mAbPullToRefreshView.setVisibility(View.GONE);
	// Toast.makeText(getActivity(), R.string.not_paper,
	// Toast.LENGTH_SHORT).show();
	// return;
	// }
	//
	// news_obj = gson.fromJson(json_array.getString(i),
	// Article.class);
	// if (news_obj != null)
	// list_obj.add(news_obj);
	// }
	// if (list_obj.size() == 0) {
	// Toast.makeText(getActivity(), R.string.not_paper,
	// Toast.LENGTH_SHORT).show();
	// } else {
	//
	// mList.clear();
	// // 吧所有的值付值到alldata里面去
	// mList.addAll(list_obj);
	//
	// //
	// }
	//
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // 如果json格式有异常应该怎么处理呢？
	// mList.clear();
	//
	// }
	//
	// }

	private void initListener() {
		// TODO Auto-generated method stub
		map.getHighlightImageView().setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					// shape_temp.setAlaph(100);
					// 当手指按下的时候
					x1 = event.getX();
					y1 = event.getY();

					for (Shape shape : shapesCache.values()) {
						if (shape.inArea(x1, y1)) {

							Shape shape_new = shapesCache.get(shape.tag);
							shape_new.setAlaph(80);
							// shape_new.onTranslate(0, 0);
							shape_new.onScale((float) (1 / min));
							map.addShape(shape_new);

							Message msg = new Message();
							msg.what = 12;
							msg.obj = shape_new;
							handler.sendMessage(msg);
							break;
							//

						}
					}

				}

				if (event.getAction() == MotionEvent.ACTION_MOVE) {

					// shape_temp.setAlaph(100);
					// 当手指按下的时候
					float x3 = event.getX();
					float y3 = event.getY();

					for (Shape shape : shapesCache.values()) {
						if (shape.inArea(x3, y3)) {

							Shape shape_new = shapesCache.get(shape.tag);
							shape_new.setAlaph(100);
							// shape_new.onTranslate(0, 0);
							shape_new.onScale((float) (1 / min));
							map.addShape(shape_new);

							Message msg = new Message();
							msg.what = 13;
							msg.obj = shape_new;
							handler.sendMessage(msg);
							break;
							//

						}
					}

				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					x2 = event.getX();
					y2 = event.getY();

					if (((x1 - x2 <= 3) || (x1 - x2 >= -3))
							&& ((y1 - y2 <= 3) || (y1 - y2 >= -3))) {

						// float xOnView = x1;
						// float yOnView = y2;
						for (Shape shape : shapesCache.values()) {
							if (shape.inArea(x2, y2)) {

								Shape shape_new = shapesCache.get(shape.tag);
								shape_new.setAlaph(100);
								// shape_new.onTranslate(0, 0);
								shape_new.onScale((float) (1 / min));
								map.addShape(shape_new);

								Article obj = null;
								int id = (Integer) shape.tag;
								for (int i = 0; i < mList.size(); i++) {
									if (id == mList.get(i).getId()) {
										obj = mList.get(i);
									}
								}
								Intent intent = new Intent(getActivity(),
										EpaperArticleDetailActivity.class);
								intent.putExtra("aid", id);
								intent.putExtra("paperName", paperName);
								intent.putExtra("articleObject", obj);
								startActivityForResult(intent, 12);
								Message msg = new Message();
								msg.what = 11;
								msg.obj = shape_new;
								handler.sendMessage(msg);

								// shape.setAlaph(0);
								// map.removeShape(shape);
								break;
								//

							}
						}
					}
				}

				return true;
			}

		});

	}

	class LoadData extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			// TODO Auto-generated method stub
			// int ret = -1;
			// content = Tools.executeHttpGet(url);
			// if (content != null) {

			initValidata();
			return 0;
			// ret = 0;
			// }
			// return ret;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			switch (result) {
			case -1:
				// Toast.makeText(getActivity(), "网络请求错误", 0).show();
				break;
			case 0:
				// Str_toJSon(content, true);
				initImagView();
				initListener();
				break;
			default:
				break;
			}
		}

	}

}