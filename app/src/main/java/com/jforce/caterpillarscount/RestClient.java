package com.jforce.caterpillarscount;
import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.loopj.android.http.*;

public class RestClient {
	
	private static final String BASE_URL = "https://secure28.webhostinghub.com/~pocket14/forsyth.im/caterpillars/";

	  private static AsyncHttpClient client = new AsyncHttpClient();

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static void post(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
	      client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }

}
