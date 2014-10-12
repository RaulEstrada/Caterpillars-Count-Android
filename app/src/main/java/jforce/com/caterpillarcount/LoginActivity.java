package jforce.com.caterpillarcount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class LoginActivity extends Activity {

    public static final String EMAIL_REGEX = "^[-\\w_\\.+]*[-\\w_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_login);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String email = extras.getString("email");
            String password = extras.getString("password");

            EditText emailField = (EditText) findViewById(R.id.login_emailfield);
            emailField.setText(email);

            EditText passwordField = (EditText) findViewById(R.id.login_passwordfield);
            passwordField.setText(password);

            //open dialog telling the person to check their email
            RegistrationDialogFragment dialog = new RegistrationDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("email", email);

            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "registrationDialog");
        }
    }

    @Override
    protected void onResume (){
        super.onResume();
        getActionBar().show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view)throws JSONException, UnsupportedEncodingException{
        //activityCircle.setVisibility(View.VISIBLE);

        progressDialog = getProgressDialog().show(this, "Logging in", "Please wait...");


        //new Thread(new LoginCredentialsRunnable(this, progressDialog)).start();

        EditText emailField = (EditText) findViewById(R.id.login_emailfield);
        String emailAddress = emailField.getText().toString();
        EditText passwordField = (EditText) findViewById(R.id.login_passwordfield);
        String password = passwordField.getText().toString();

        //ensure valid email format
        boolean validEmail = emailAddress.matches(LoginActivity.EMAIL_REGEX);

        if(!validEmail){
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.login_emailfield));
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }


        //password is entered
        if (password != null && !password.isEmpty()) {


            //construct the JSON object to be POSTed
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email", emailAddress);
            jsonParams.put("password", password);
            StringEntity entity = new StringEntity(jsonParams.toString());

            LoginCredentialsResponseHandler loginResponseHandler = new LoginCredentialsResponseHandler(this);

            RestClient.post(this, "users.php", entity, "application/json", loginResponseHandler);



//            JsonObject json = new JsonObject();
//            json.addProperty("email", emailAddress);
//            json.addProperty("password", password);
//
//            //POST to backend
//            Ion.with(this)
//                    .load(RegistrationActivity.BASE_URL + "users.php")
//                    .setJsonObjectBody(json)
//                    .asJsonObject()
//                    .withResponse()
//                    .setCallback(new LoginCredentialsFutureCallback<Response<JsonObject>>(this));

        }
        else{//password is not entered
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.login_passwordfield));
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }


    }

    public void downloadUser() throws JSONException, UnsupportedEncodingException {

        //getProgressDialog().dismiss();
        //activityCircle.setVisibility(View.VISIBLE);

        EditText emailField = (EditText) findViewById(R.id.login_emailfield);
        String emailAddress = emailField.getText().toString();

        //construct the JSON object to be POSTed
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", emailAddress);
        StringEntity entity = new StringEntity(jsonParams.toString());

        LoginUserResponseHandler loginResponseHandler = new LoginUserResponseHandler(this);

        RestClient.post(this, "users.php", entity, "application/json", loginResponseHandler);


//        EditText emailField = (EditText) findViewById(R.id.login_emailfield);
//        String emailAddress = emailField.getText().toString();
//
//        JsonObject json = new JsonObject();
//        json.addProperty("email", emailAddress);
//
//        //POST to backend
//        Ion.with(this)
//                .load(RegistrationActivity.BASE_URL + "users.php")
//                .setJsonObjectBody(json)
//                .asJsonObject()
//                .withResponse()
//                .setCallback(new LoginCredentialsFutureCallback<Response<JsonObject>>(this));

    }

    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }

    public void shake(){

        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.login_emailfield));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.login_passwordfield));

    }

}
