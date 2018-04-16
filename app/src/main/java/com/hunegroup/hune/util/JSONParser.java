package com.hunegroup.hune.util;

import com.hunegroup.hune.app.App;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

public class JSONParser {

	static String json = "";
	// constructor
	public JSONParser() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public String makeHttpRequest(String url, String method, List<NameValuePair> params) {
		String result = null;
		// Making HTTP request
		try {
			
			// check for request method
			if(method == "POST"){
				// request method is POST
				// defaultHttpClient
				HttpClient client = Utilities.getClient();
				//DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = client.execute(httpPost);
				result=EntityUtils.toString(httpResponse.getEntity());
//				//DefaultHttpClient httpClient = new DefaultHttpClient();//pending
//				HttpPost httpPost = new HttpPost(url);
//				//httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;charset=UTF-8");
//				HttpClient client = Utilities.getClient();
//				httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
//
//				HttpResponse httpResponse = client.execute(httpPost);
//				//HttpResponse httpResponse = httpClient.execute(httpPost);////pending
//				result=EntityUtils.toString(httpResponse.getEntity());
////				if(android.os.Build.VERSION.SDK_INT < 11){
////					httpResponse.getEntity().consumeContent();
////				}
				
			}else if(method == "GET"){
				// request method is GET
                SessionUser sessionUser = new SessionUser(App.getInstance());
				HttpClient client = Utilities.getClient();
				//DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);
                if(sessionUser.getLanguage() ==0){
                    httpGet.addHeader(Common.JsonKey.KEY_CATEGORY_LANGUAGE, "en");
                }
				else if(sessionUser.getLanguage() == 1){
                    httpGet.addHeader(Common.JsonKey.KEY_CATEGORY_LANGUAGE, "vi");
                }
                else httpGet.addHeader(Common.JsonKey.KEY_CATEGORY_LANGUAGE, Locale.getDefault().getLanguage());
				HttpResponse httpResponse = client.execute(httpGet);
				result=EntityUtils.toString(httpResponse.getEntity());
			} else if (method == "PUT") {

				// request method is PUT
				HttpClient client = Utilities.getClient();
				//DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpPut httpPut = new HttpPut(url);

				HttpResponse httpResponse = client.execute(httpPut);
				result=EntityUtils.toString(httpResponse.getEntity());
			}
			else if (method == "DELETE") {

				// request method is DELETE
				HttpClient client = Utilities.getClient();
				//DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpDelete httpDelete = new HttpDelete(url);

				HttpResponse httpResponse = client.execute(httpDelete);
				result=EntityUtils.toString(httpResponse.getEntity());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		return  result;

		
	}
}
