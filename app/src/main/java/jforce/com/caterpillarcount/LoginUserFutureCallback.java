package jforce.com.caterpillarcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.Response;

/**
 * Created by justinforsyth on 10/2/14.
 */
public class LoginUserFutureCallback<T> implements FutureCallback<T> {

    private Activity activity;

    public LoginUserFutureCallback(Activity activity){

        this.activity = activity;


    }

    public void onCompleted(Exception e, T result) {

        LoginActivity loginActivity = (LoginActivity) activity;

        // do stuff with the result or error
        if(e != null){
            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(activity, loginActivity.getResources().getString(R.string.login_userDownloadFailure), Toast.LENGTH_SHORT).show();
            return;
        }



        Response<JsonObject> castedResult = (Response<JsonObject>) result;

        RawHeaders status = castedResult.getHeaders();

        int code = status.getResponseCode();


        if(code == 200){ //HTTP1.1/200 OK

            int userID = castedResult.getResult().get("userID").getAsInt();
            String email = castedResult.getResult().get("email").getAsString();
            String name = castedResult.getResult().get("name").getAsString();
            int validUserInt = castedResult.getResult().get("validUser").getAsInt();

            Boolean validUser = (validUserInt == 1);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(loginActivity);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //save user data
            editor.putInt("userID", userID);
            editor.putString("email", email);
            editor.putString("name", name);
            editor.putBoolean("validUser", validUser);

            //Change status to loggedIn
            editor.putBoolean("loggedIn", true);
            editor.commit();

            loginActivity.getProgressDialog().dismiss();

            //start HomeActivity, finish those below in in the back stack

            //disables activity start animation
            Intent intent = new Intent(loginActivity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            loginActivity.setResult(Activity.RESULT_OK);
            loginActivity.finish();
            loginActivity.startActivity(intent);
            //disables activity finish animation
            loginActivity.overridePendingTransition(0, 0);


            return;



        }else{

            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_userDownloadFailure), Toast.LENGTH_SHORT).show();
            return;

        }





    }


}
