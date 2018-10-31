package cn.net.newgen.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import cn.net.newgen.widget.R;

public class ArtInputDialog extends Dialog implements OnClickListener {
	
	private String title, okStr, cancelStr;
	private OnInputClickListener mListener;
	private TextView titleTxt, okTxt, cancelTxt;
	private EditText inputTxt;
	
	public ArtInputDialog(Context context, String title, String okStr,
			String cancelStr, OnInputClickListener mListener) {
		super(context);
		this.title = title;
		this.okStr = okStr;
		this.cancelStr = cancelStr;
		this.mListener = mListener;
	}

	public ArtInputDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.setContentView(R.layout.art_input_dialog_layout);
		window.setBackgroundDrawable(new ColorDrawable(0));
		
		titleTxt = (TextView) findViewById(R.id.title_txt);
		okTxt = (TextView) findViewById(R.id.ok_txt);
		cancelTxt = (TextView) findViewById(R.id.cancel_txt);
		inputTxt = (EditText) findViewById(R.id.input_edit);
		if(null != title)
			titleTxt.setText(title);
		if(null != okStr && !"".equals(okStr))
			okTxt.setText(okStr);
		if(null != cancelStr && !"".equals(cancelStr))
			cancelTxt.setText(cancelStr);
		
		okTxt.setOnClickListener(this);
		cancelTxt.setOnClickListener(this);
	}

	
	public interface OnInputClickListener{
		
		public void okButtonClick(final String inputStr);
		
		public void cancelButtonClick();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(null != mListener){
			if(v == okTxt){
				final String txt = inputTxt.getText().toString();
				mListener.okButtonClick(txt);
			}else{
				mListener.cancelButtonClick();
			}
		}
		this.cancel();
	}
}
