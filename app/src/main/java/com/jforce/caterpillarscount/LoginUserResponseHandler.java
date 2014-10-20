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

import com.jforce.caterpillarscount.R;

public class LoginUserResponseHandler extends JsonHttpResponseHandler{

	private Activity activity;

	public LoginUserResponseHandler(Activity activity){
		super();
		this.activity = activity;
	}
	
	@Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		LoginActivity loginActivity = (LoginActivity) activity;
		
		
        // Pull out the first event on the public timeline
        int userID;
        String email;
        String name;
        int validUserInt;
    
			try {
				userID = response.getInt("userID");
				email = response.getString("email");
                name = response.getString("name");
                validUserInt = response.getInt("validUser");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
                loginActivity.getProgressDialog().dismiss();
                Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
			}

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
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        loginActivity.setResult(Activity.RESULT_OK);
        loginActivity.finish();
        loginActivity.startActivity(intent);
        //disables activity finish animation
        Toast toast = Toast.makeText(loginActivity, "Successfully logged in!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        loginActivity.overridePendingTransition(0, 0);


        return;
			
			
    }
	
	@Override
	public void onFailure(int code, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        LoginActivity loginActivity = (LoginActivity) activity;

        loginActivity.getProgressDialog().dismiss();
        Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_userDownloadFailure), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;

    }


}
