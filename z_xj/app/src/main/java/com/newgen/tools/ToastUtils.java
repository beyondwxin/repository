package com.newgen.tools;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
	
	public static Toast getToast(Context con ,String msg){
		Toast toast=Toast.makeText(con, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		return toast;
		//toast.show();
	}

}
