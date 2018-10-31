package com.newgen.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpTools {

	/****
	 * http Post 请求方法
	 * 
	 * @param params
	 *            参数<key, value> 对，其他可加入File 对象用于文件上传
	 * @param url
	 *            请求地址
	 * @param files
	 *            如果 params File在磁盘上的地址， 请填写与key对应的值
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(Map<String, Object> params, String url,
			boolean isUrlCoding, String codingChar, String... fileParams)
			throws Exception {
		HttpContext httpContext = new BasicHttpContext();
		HttpPost post = new HttpPost(url);
		Set<String> keys = params.keySet();
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			boolean isContiune = false;
			for (String key : keys) {
				isContiune = false;
				if (null != fileParams && fileParams.length > 0) {// 如果有传文参数过来
					for (String fileParam : fileParams) {
						if (fileParam.equals(key)) {// 如果当前为文件
							isContiune = true;
							File file = null;
							@SuppressWarnings("unchecked")
							List<String> list = (List<String>) params.get(key);
							for (String dir : list) {
								file = new File(dir);
							}
							if (!file.exists())
								throw new Exception("文件不存在");
							entity.addPart(key, new FileBody(file));
						}
					}
				}
				if (isContiune)
					continue;
				if (isUrlCoding) {
					StringBody value = new StringBody((String) params.get(key),
							Charset.forName(codingChar));
					entity.addPart(key, value);
				} else {
					entity.addPart(key,
							new StringBody((String) params.get(key)));
				}

			}
			post.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post, httpContext);
			String reslutStr = null;
			if (response.getStatusLine().getStatusCode() == 200) {
				reslutStr = EntityUtils.toString(response.getEntity());
			}
			return reslutStr;
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * http get 请求
	 * 
	 * @param requestPath
	 *            请求地址
	 * @param timeOut
	 *            超时时间，以秒为单位，值为0时将使用默认超时时间
	 * @return
	 */
	public static String httpGet(String requestPath, int timeOut) {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(requestPath);
			connection = (HttpURLConnection) url.openConnection();
			if (timeOut > 0) {
				connection.setConnectTimeout(timeOut * 1000);
				connection.setReadTimeout(timeOut * 1000);
			}
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
}
