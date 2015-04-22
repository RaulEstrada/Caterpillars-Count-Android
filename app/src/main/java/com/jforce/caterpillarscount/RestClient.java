package com.jforce.caterpillarscount;
import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {
	
	private static final String BASE_URL = "http://caterpillarscount.unc.edu/api/";

	private static AsyncHttpClient client = new AsyncHttpClient();


	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	public static void post(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {

	      client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
	  }

    public static void postJson(Context context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Content-Type", "application/json");
        client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
    }

    public static void postFile(HomeActivity activity, String photoPath) {
        new PostImage(photoPath, activity).execute();
    }


	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }

}
