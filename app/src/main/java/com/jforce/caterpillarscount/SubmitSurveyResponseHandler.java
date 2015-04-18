package com.jforce.caterpillarscount;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by justinforsyth on 10/20/14.
 */
public class SubmitSurveyResponseHandler extends JsonHttpResponseHandler {


    private Activity activity;

    public SubmitSurveyResponseHandler(Activity activity){
        super();

        this.activity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
        HomeActivity homeActivity = (HomeActivity) activity;

        int surveyID;
        String leavePhoto;

        try {
            surveyID = response.getInt("surveyID");
            leavePhoto = response.getString("leavePhoto");
        } catch (JSONException e) {
            homeActivity.notificationFailure();
            //"Something went wrong"
            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        try{
            homeActivity.setSurveyPhotoName(leavePhoto);
            homeActivity.submitOrders(surveyID);
            homeActivity.postImage(homeActivity.leafPictureName);
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

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HomeActivity homeActivity = (HomeActivity) activity;

        homeActivity.notificationFailure();
        Log.d("caterpillars", responseString);
        Toast toast = Toast.makeText(activity, responseString + statusCode, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }



}
