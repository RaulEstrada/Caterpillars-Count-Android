package jforce.com.caterpillarcount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.doomonafireball.betterpickers.widget.AccessibleTextView;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

/**
 * Created by justinforsyth on 10/2/14.
 */
public class LoginCredentialsRunnable implements Runnable {

    private LoginActivity activity;
    private ProgressDialog progressDialog;

    public LoginCredentialsRunnable(LoginActivity activity, ProgressDialog progressDialog){
        super();
        this.activity = activity;
        this.progressDialog = progressDialog;

    }

    @Override
    public void run() {

        try {

            // Here you should write your time consuming task...

//            EditText emailField = (EditText) activity.findViewById(R.id.login_emailfield);
//            String emailAddress = emailField.getText().toString();
//            EditText passwordField = (EditText) activity.findViewById(R.id.login_passwordfield);
//            String password = passwordField.getText().toString();
//
//            //ensure valid email format
//            boolean validEmail = emailAddress.matches(LoginActivity.EMAIL_REGEX);
//
//            if(!validEmail){
//                activity.getProgressDialog().dismiss();
//                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.login_emailfield));
//                Toast toast = Toast.makeText(activity.getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                return;
//            }
//
//
//            //password is entered
//            if (password != null && !password.isEmpty()) {
//
//                JsonObject json = new JsonObject();
//                json.addProperty("email", emailAddress);
//                json.addProperty("password", password);
//
//                //POST to backend
//                Ion.with(activity)
//                        .load(RegistrationActivity.BASE_URL + "users.php")
//                        .setJsonObjectBody(json)
//                        .asJsonObject()
//                        .withResponse()
//                        .setCallback(new LoginCredentialsFutureCallback<Response<JsonObject>>(activity));
//
//            }
//            else{//password is not entered
//                activity.getProgressDialog().dismiss();
//                YoYo.with(Techniques.Shake).playOn(activity.findViewById(R.id.login_passwordfield));
//                Toast toast = Toast.makeText(activity.getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                return;
//            }
//
//
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);
//
//            if (loggedIn){
//
//                activity.downloadUser();
//            }


        } catch (Exception e) {



        }
        progressDialog.dismiss();
    }
}
