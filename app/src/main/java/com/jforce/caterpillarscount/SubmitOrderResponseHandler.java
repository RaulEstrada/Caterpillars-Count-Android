package com.jforce.caterpillarscount;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by justinforsyth on 10/27/14.
 */
public class SubmitOrderResponseHandler extends JsonHttpResponseHandler {


    private Activity activity;
    private int cardCount;
    private int i;

    public SubmitOrderResponseHandler(Activity activity, int cardCount, int i){
        super();
        this.activity = activity;
        this.cardCount = cardCount;
        this.i = i;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response){


        HomeActivity homeActivity = (HomeActivity) activity;


        String insectPhoto;

        try {
            insectPhoto = response.getString("insectPhoto");
        } catch (JSONException e) {

            //homeActivity.getUploadMetaDataProgressDialog().dismiss();
            homeActivity.notificationFailure();
            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        ArrayList<String> orderPhotoNames = homeActivity.getOrderPhotoNames();

        try{
            orderPhotoNames.add(insectPhoto);
        }
        catch (Exception e){
            //homeActivity.getUploadMetaDataProgressDialog().dismiss();
            homeActivity.notificationFailure();
            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;

        }



        homeActivity.setOrderPhotoNames(orderPhotoNames);
        //Log.d("caterpillars", insectPhoto);

        if(i + 1 == cardCount){

            //homeActivity.getUploadMetaDataProgressDialog().dismiss();

            try{
                homeActivity.ftpUploadImages();
                homeActivity.notificationImages(null, null, null);
            }
            catch (Exception e){
                homeActivity.notificationFailure();
                Toast toast = Toast.makeText(homeActivity, "Something went wrong", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }


        }

//        Toast toast = Toast.makeText(homeActivity, "Success!", Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();




    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HomeActivity homeActivity = (HomeActivity) activity;

        //homeActivity.getUploadMetaDataProgressDialog().dismiss();
        homeActivity.notificationFailure();
        //Log.d("caterpillars", responseString);
        Toast toast = Toast.makeText(activity, "Error submitting Arthropod Order", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }







}
