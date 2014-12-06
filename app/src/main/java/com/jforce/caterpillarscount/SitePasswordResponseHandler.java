package com.jforce.caterpillarscount;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by justinforsyth on 12/5/14.
 */
public class SitePasswordResponseHandler extends JsonHttpResponseHandler {

    Activity activity;
    int siteID;
    String sitePassword;


    public SitePasswordResponseHandler(Activity activity, int siteID, String sitePassword){
        super();
        this.activity = activity;
        this.siteID = siteID;
        this.sitePassword = sitePassword;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response){


        HomeActivity homeActivity = (HomeActivity) activity;


        Integer validSitePassword = null;

        try {
            validSitePassword = response.getInt("validSitePassword");
        } catch (JSONException e) {

            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if(validSitePassword != null){

            if(validSitePassword == 1){

                homeActivity.downloadSite(siteID, sitePassword);

            }else{

                Toast toast = Toast.makeText(activity, "Invalid Site ID or Password", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }

        }

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
