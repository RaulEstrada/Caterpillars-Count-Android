package com.jforce.caterpillarscount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import com.jforce.caterpillarscount.R;


public class RegistrationActivity extends Activity {

    public static final String LETTERS_ONLY = "^[a-zA-Z]+$";
    public static final String BASE_URL = "https://secure28.webhostinghub.com/~pocket14/forsyth.im/caterpillars/";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().show();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_registration);
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
        getMenuInflater().inflate(R.menu.registration, menu);
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

    public void register(View view) throws JSONException, UnsupportedEncodingException {

        progressDialog = getProgressDialog().show(this, "Registering", "Please wait...");
        progressDialog.setCancelable(true);

        EditText emailField = (EditText) findViewById(R.id.registration_emailfield);
        String emailAddress = emailField.getText().toString();
        EditText fnameField = (EditText) findViewById(R.id.registration_fname);
        String fname = fnameField.getText().toString();
        EditText lnameField = (EditText) findViewById(R.id.registration_lname);
        String lname = lnameField.getText().toString();

        boolean validFName = fname.matches(LETTERS_ONLY);
        boolean validLName = lname.matches(LETTERS_ONLY);

        if (!validFName){
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_fname));
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid first name -- Letters only!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if (!validLName){
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_lname));
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid last name -- Letters Only!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        //ensure valid email format
        boolean validEmail = emailAddress.matches(LoginActivity.EMAIL_REGEX);

        if(!validEmail){
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_emailfield));
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        EditText passwordField = (EditText) findViewById(R.id.registration_passwordfield);
        String password = passwordField.getText().toString();

        EditText passwordConfirmField = (EditText) findViewById(R.id.registration_passwordconfirm);
        String passwordConfirm = passwordConfirmField.getText().toString();

        if(!password.equals(passwordConfirm)){
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_passwordfield));
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_passwordconfirm));
            Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }



        //make sure a password is actually entered
        if (password != null && !password.isEmpty()) {

            JsonObject json = new JsonObject();
            json.addProperty("email", emailAddress);
            json.addProperty("password", password);
            json.addProperty("name", fname + " " + lname);

            //POST to backend
            Ion.with(this)
                    .load(BASE_URL + "users.php")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new RegistrationFutureCallback<Response<JsonObject>>(this, emailAddress, password));

        }
        else{
            getProgressDialog().dismiss();
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_passwordfield));
            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.registration_passwordconfirm));
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }


    }

    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }
}
