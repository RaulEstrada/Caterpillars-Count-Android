package com.jforce.caterpillarscount;

import android.app.ProgressDialog;

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
