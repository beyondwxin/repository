package com.newgen.server;

import android.util.Log;

import com.newgen.tools.HttpTools;
import com.newgen.tools.PublicValue;
import com.newgen.tools.Tools;

public class ServiceServer {

	public String getServiceJson() {
		// TODO Auto-generated method stub
		
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("/getLifeList.do");
		
		Tools.log(url.toString());
		Log.i("info", url.toString());
		String result = HttpTools.httpGet(url.toString(), 6);
		
		return result;
		
	}
	
	public String getOfficeHallJson() {
		// TODO Auto-generated method stub
		
		StringBuffer url = new StringBuffer(PublicValue.BASEURL);
		url.append("/getOfficeList.do");
		
		Tools.log(url.toString());
		Log.i("info", url.toString());
		String result = HttpTools.httpGet(url.toString(), 6);
		
		return result;
		
	}

}
