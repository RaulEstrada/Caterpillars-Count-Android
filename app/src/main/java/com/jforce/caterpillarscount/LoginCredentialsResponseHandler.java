package com.jforce.caterpillarscount;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import com.jforce.caterpillarscount.R;

public class LoginCredentialsResponseHandler extends JsonHttpResponseHandler{
	
	private Activity activity;
	
	public LoginCredentialsResponseHandler(Activity activity){
		super();
		this.activity = activity;
	}
	
	@Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		LoginActivity loginActivity = (LoginActivity) activity;
		
		
        // Pull out the first event on the public timeline
        String validUserString;
        String validPwString;
        String activeString;
    
			try {
				validUserString = response.getString("validUser");
				validPwString = response.getString("validPw");
                activeString = response.getString("active");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
                loginActivity.getProgressDialog().dismiss();
                Toast toast = Toast.makeText(activity,  loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
			}

        Boolean validUser = "1".equals(validUserString);
        Boolean validPw = "1".equals(validPwString);
        Boolean active = "1".equals(activeString);

        // Do something with the response

        if(validUser && validPw && active){//successful login
            try {
                loginActivity.downloadUser();
            } catch (JSONException e) {
                loginActivity.getProgressDialog().dismiss();
                Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT).show();
                return;
            } catch (UnsupportedEncodingException e) {
                loginActivity.getProgressDialog().dismiss();
                Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT).show();
                return;
            }

            return;

        }
        else if (validUser && validPw && !active){//account not active yet
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_notActive), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return;

        }
        else if (validUser && !validPw && active){//invalid password
            loginActivity.getProgressDialog().dismiss();
            loginActivity.shake();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_invalidCredentials), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;

        }
        else if (!validUser && validPw && active){//deactivated account

            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_deactivated), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        else if (!validUser && !validPw && active) {//invalid password
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_invalidCredentials), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            loginActivity.shake();
            return;
        }
        else if (validUser && !validPw && !active){//invalid password
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_invalidCredentials), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            loginActivity.shake();
            return;
        }
        else if(!validUser && validPw && !active){//account not active yet
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_notActive), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        else if(!validUser && !validPw && !active){//invalid password
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_invalidCredentials), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            loginActivity.shake();
            return;
        }
        else{

            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
			
			
    }
	
	@Override
	public void onFailure(int code, Header[] headers, String string, Throwable throwable){
        LoginActivity loginActivity = (LoginActivity) activity;


        if(code == 404){//HTTP1.1/404 Not found
            loginActivity.getProgressDialog().dismiss();
            Toast toast = Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_invalidCredentials), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            loginActivity.shake();
            return;
        }
        else if(code == 500){//HTTP1.1/500 Internal Server Error
            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_credentials500), Toast.LENGTH_SHORT).show();
            return;

        }
        else if(code == 400){//HTTP1.1/400 Bad Request
            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.login_credentials400), Toast.LENGTH_SHORT).show();
            return;

        }
        else{
            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT).show();
            return;
        }

    }


}
