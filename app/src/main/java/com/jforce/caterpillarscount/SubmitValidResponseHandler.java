package com.jforce.caterpillarscount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justinforsyth on 10/20/14.
 */
public class SubmitValidResponseHandler extends JsonHttpResponseHandler {

    private Activity activity;

    public SubmitValidResponseHandler(Activity activity){
        //super();
        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


        HomeActivity homeActivity = (HomeActivity) activity;

        // Pull out the first event on the public timeline
        int validUserInt;

        try {
            validUserInt = response.getInt("validUser");
        } catch (JSONException e) {

            homeActivity.notificationFailure();
            //"Something went wrong"
            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        Boolean validUser = (validUserInt == 1);

        if(validUser){
            try {
                homeActivity.notificationData();
                homeActivity.submitSurvey();
                return;
            }
            catch (Exception e){
                homeActivity.notificationFailure();
                //"Something went wrong"
                Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
        }
        else{
            homeActivity.notificationFailure();
            Toast toast = Toast.makeText(activity, "Account is invalid. Please contact an administrator", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HomeActivity homeActivity = (HomeActivity) activity;
        homeActivity.notificationFailure();
        Toast toast = Toast.makeText(activity, responseString + " " + statusCode, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }
}
