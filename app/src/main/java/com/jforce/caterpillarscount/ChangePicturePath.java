package com.jforce.caterpillarscount;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Raul Estrada on 18/04/2015.
 */
public class ChangePicturePath extends JsonHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response){}
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
}