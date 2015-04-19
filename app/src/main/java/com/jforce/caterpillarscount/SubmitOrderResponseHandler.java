package com.jforce.caterpillarscount;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
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
        String orderId;
        try {
            insectPhoto = ((OrderCard)homeActivity.getCardList().get(i)).getPhotoPath();
            String[] parts = insectPhoto.split("/");
            insectPhoto = parts[parts.length-1];
            orderId = response.getString("orderID");
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("type","order");
            jsonParams.put("order", orderId);
            jsonParams.put("picture", insectPhoto);
            try{
                RestClient.postJson(activity,"updatePicture.php",new StringEntity(jsonParams.toString()),"application/json", new ChangePicturePath());
            } catch (Exception e){}
        } catch (JSONException e) {
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
            homeActivity.notificationFailure();
            Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;

        }

        homeActivity.setOrderPhotoNames(orderPhotoNames);

        if(i + 1 == cardCount){
            try{
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
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        HomeActivity homeActivity = (HomeActivity) activity;
        homeActivity.notificationFailure();
        Toast toast = Toast.makeText(activity, "Error submitting Arthropod Order", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }
}