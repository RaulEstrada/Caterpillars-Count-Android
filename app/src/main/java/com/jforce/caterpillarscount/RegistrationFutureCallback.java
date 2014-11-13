//package com.jforce.caterpillarscount;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.widget.Toast;
//
//import com.google.gson.JsonObject;
//import com.koushikdutta.async.future.FutureCallback;
//import com.koushikdutta.async.http.libcore.RawHeaders;
//import com.koushikdutta.ion.Response;
//
//import com.jforce.caterpillarscount.R;
//
///**
// * Created by justinforsyth on 9/30/14.
// */
//public class RegistrationFutureCallback<T> implements FutureCallback<T> {
//
//    private String emailAddress;
//    private String password;
//    private Activity activity;
//
//    public RegistrationFutureCallback(Activity activity, String emailAddress, String password){
//
//        this.activity = activity;
//        this.emailAddress = emailAddress;
//        this.password = password;
//
//
//    }
//
//
//    public void onCompleted(Exception e, T result) {
//
//        // do stuff with the result or error
//        if(e != null){
//            Toast.makeText(activity, activity.getResources().getString(R.string.registerException), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        RegistrationActivity registrationActivity = (RegistrationActivity) activity;
//
//        Response<JsonObject> castedResult = (Response<JsonObject>) result;
//
//        RawHeaders status = castedResult.getHeaders();
//
//        int code = status.getResponseCode();
//
//
//        if(code == 200){ //HTTP1.1/200 OK, successful registration
//
//            Intent data = new Intent();
//
//            data.putExtra("email", emailAddress);
//            data.putExtra("password", password);
//            registrationActivity.setResult(registrationActivity.RESULT_OK, data);
//            registrationActivity.finish();
//
//        }
//        else if(code == 409){//HTTP1.1/409 Conflict
//
//            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register409), Toast.LENGTH_SHORT).show();
//        }
//        else if(code == 500){//HTTP1.1/500 Internal Server Error
//
//            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register500), Toast.LENGTH_SHORT).show();
//
//
//        }
//        else if(code == 400){//HTTP1.1/400 Bad Request
//
//            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register400), Toast.LENGTH_SHORT).show();
//
//        }
//
//
//
//    }
//}
