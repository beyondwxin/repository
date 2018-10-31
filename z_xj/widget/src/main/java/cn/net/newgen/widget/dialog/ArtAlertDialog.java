package cn.net.newgen.widget.dialog;

import cn.net.newgen.widget.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 自定义AlertDialog
 * @author yilang version 1.0.0.0
 */
public class ArtAlertDialog extends AlertDialog implements OnClickListener{
	//private Context context;
	private String msg, okStr, cancelStr,title_Str;
	private OnArtClickListener mListener;
	private TextView msgTxt, okTxt, cancelTxt,title_text;
	
	public ArtAlertDialog(Context context, String msg,
			String okStr, String cancelStr, OnArtClickListener mListener) {
		super(context);
		//this.context = context;
		this.msg = msg;
		this.okStr = okStr;
		this.cancelStr = cancelStr;
		this.mListener = mListener;
	}
	
	public ArtAlertDialog(Context context, String msg,String title_Str,
			String okStr, String cancelStr, OnArtClickListener mListener) {
		super(context);
		//this.context = context;
		this.title_Str=title_Str;
		this.msg = msg;
		this.okStr = okStr;
		this.cancelStr = cancelStr;
		this.mListener = mListener;
	}
	public ArtAlertDialog(Context context) {
		super(context);
		//this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.art_alert_dialog_layout);//设置 dialog布局文件
		
		msgTxt = (TextView) findViewById(R.id.alert_txt);
		okTxt = (TextView) findViewById(R.id.ok_txt);
		cancelTxt = (TextView) findViewById(R.id.cancel_txt);
		title_text=(TextView) findViewById(R.id.title_text);
		if (null !=title_Str) {
			title_text.setText(title_Str);
		}
		if(null != msg)
			msgTxt.setText(msg);
		if(null != okStr && !"".equals(okStr))
			okTxt.setText(okStr);
		if(null != cancelStr && !"".equals(cancelStr))
			cancelTxt.setText(cancelStr);
		
		okTxt.setOnClickListener(this);
		cancelTxt.setOnClickListener(this);
	}

	public interface OnArtClickListener {

		public void okButtonClick();

		public void cancelButtonClick();
	}

	@Override
	public void onClick(View v) {
		if(null != mListener){
			if(v == okTxt)
				mListener.okButtonClick();
			else
				mListener.cancelButtonClick();
		}
		this.cancel();
	}
}
