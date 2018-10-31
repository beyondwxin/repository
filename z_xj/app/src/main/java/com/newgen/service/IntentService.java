package com.newgen.service;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.newgen.xj_app.detail.ww.ImgNewsDetailActivity;
import com.newgen.xj_app.detail.ww.LinkDetailActivity;
import com.newgen.xj_app.detail.ww.NewsDetailActivity;
import com.newgen.tools.PublicValue;
import com.newgen.xj_app.MainActivity;
import com.newgen.xj_app.R;



/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */

public class IntentService extends GTIntentService {
	
	
	// 获取通知栏管理类 通知栏对象
	NotificationManager mNotificationManager = null;
	Notification mNotification = null;
	boolean isAppRuning = false;

	public IntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
    	
    	mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification = new Notification();
		
		byte[] payload = msg.getPayload();
		JSONObject jsonObject = null;
		if (payload != null) {
			String data = new String(payload);
			
			try {
				jsonObject = new JSONObject(data);
				
				CharSequence contentTitle = jsonObject.getString("title");// 标题
				CharSequence contentText = null;
				int id = 0;
				int type = 0;
				int infoType = 0;
				int liveId = 0;
				String url = null;
				String title = null;
				String summary = null;
				String faceImgName = null;
				String faceImgPath = null;
				String faceImage = null;
				
				id = jsonObject.getInt("id");
				type = jsonObject.getInt("type");
				infoType = jsonObject.getInt("infoType");
				liveId = jsonObject.getInt("liveId");
				url = jsonObject.getString("url");
				title = jsonObject.getString("title");
				contentText = jsonObject.getString("digest");
				summary = jsonObject.getString("digest");
				faceImgName = jsonObject.getString("faceImgName");
				faceImgPath = jsonObject.getString("faceImgPath");
				faceImage = faceImgPath+PublicValue.IMG_SIZE_M+faceImgName;
				
				
				NotificationManager manager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder builder = new NotificationCompat.Builder(
						context);
				builder.setSmallIcon(R.drawable.icon_app);
				builder.setDefaults(Notification.DEFAULT_ALL);
				builder.setAutoCancel(true);// 点击后自动消失
				builder.setContentTitle(contentTitle);
				builder.setContentText(contentText);
				
				/* 判断本应用是否在运行 */
				ActivityManager am = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningTaskInfo> list = am.getRunningTasks(1000);

				String MY_PKG_NAME = "com.newgen.sg_news.activity";
				for (RunningTaskInfo info : list) {
					if (info.topActivity.getPackageName().equals(
							MY_PKG_NAME)
							|| info.baseActivity.getPackageName().equals(
									MY_PKG_NAME)) {
						isAppRuning = true;
						break;
					}
				}
				 
				Intent _intent = new Intent();
				_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				_intent.putExtra("newsID", id);
				_intent.putExtra("newsId", id);
				_intent.putExtra("type", type);
				_intent.putExtra("infoType", infoType);
				_intent.putExtra("url",url);
				_intent.putExtra("liveid", liveId);
				_intent.putExtra("title", title);
				_intent.putExtra("faceImage", faceImage);
				_intent.putExtra("summary", summary);
				_intent.putExtra("image_url", faceImage);
				_intent.putExtra("shareimg", faceImage);
				
				if (isAppRuning == false) {
					// 没有运行
					_intent.setClass(context, MainActivity.class);
				} else {
					switch (type) {
					case PublicValue.NEWS_STYLE_WORD:
						_intent.setClass(context, NewsDetailActivity.class);
						break;
					case PublicValue.NEWS_STYLE_IMG:
						_intent.setClass(context, ImgNewsDetailActivity.class);
						break;
					case PublicValue.NEWS_STYLE_LINK: // 视频直播和超链url
						_intent.putExtra("url", url);
						_intent.putExtra("shareimg",faceImage);
						_intent.setClass(context, LinkDetailActivity.class);
						break;
					default:
						_intent.setClass(context, NewsDetailActivity.class);
						break;
					}
				}
				
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, _intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(pendingIntent);
				manager.notify(0, builder.build());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
    
    
}
