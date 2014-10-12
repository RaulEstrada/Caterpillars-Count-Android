package jforce.com.caterpillarcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.Response;

/**
 * Created by justinforsyth on 10/1/14.
 */
public class LoginCredentialsFutureCallback<T> implements FutureCallback<T> {

    private Activity activity;

    public LoginCredentialsFutureCallback(Activity activity){

        this.activity = activity;


    }



    public void onCompleted(Exception e, T result) {

        LoginActivity loginActivity = (LoginActivity) activity;

        // do stuff with the result or error
        if(e != null){
            loginActivity.getProgressDialog().dismiss();
            Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT).show();
            return;
        }



        Response<JsonObject> castedResult = (Response<JsonObject>) result;

        RawHeaders status = castedResult.getHeaders();

        int code = status.getResponseCode();


        if(code == 200){ //HTTP1.1/200 OK

            String validUserString;
            String validPwString;
            String activeString;


            try {
                validUserString = castedResult.getResult().get("validUser").getAsString();
                validPwString = castedResult.getResult().get("validPw").getAsString();
                activeString = castedResult.getResult().get("active").getAsString();
            } catch (Exception exception) {
                // TODO Auto-generated catch block
                loginActivity.getProgressDialog().dismiss();
                Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            Boolean validUser = "1".equals(validUserString);
            Boolean validPw = "1".equals(validPwString);
            Boolean active = "1".equals(activeString);

            if(validUser && validPw && active){//successful login
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(loginActivity);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", true);
                editor.commit();

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
        else if(code == 404){//HTTP1.1/404 Not found
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

        loginActivity.getProgressDialog().dismiss();
        Toast.makeText(loginActivity, loginActivity.getResources().getString(R.string.loginException), Toast.LENGTH_SHORT).show();
        return;



    }
}
