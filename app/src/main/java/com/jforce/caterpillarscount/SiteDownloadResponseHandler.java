package com.jforce.caterpillarscount;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justinforsyth on 12/5/14.
 */
public class SiteDownloadResponseHandler extends JsonHttpResponseHandler{


    Activity activity;
    String sitePassword;


    public SiteDownloadResponseHandler(Activity activity, String sitePassword){
        super();
        this.activity = activity;
        this.sitePassword = sitePassword;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response){


        HomeActivity homeActivity = (HomeActivity) activity;


        Integer siteID = null;
        String siteName = null;
        String siteState = null;

        try {
            siteID = response.getInt("siteID");
            siteName = response.getString("siteName");
            siteState = response.getString("siteState");
        } catch (JSONException e) {

            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        Site site = new Site(siteID, sitePassword, siteName, siteState);


        homeActivity.saveSite(site);


        return;



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HomeActivity homeActivity = (HomeActivity) activity;

        Toast toast = Toast.makeText(activity, "Connection Error", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }
}
