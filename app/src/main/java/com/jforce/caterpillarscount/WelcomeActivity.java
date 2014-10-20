package com.jforce.caterpillarscount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.jforce.caterpillarscount.R;


public class WelcomeActivity extends Activity {

    public final static int REGISTER = 1;
    public final static int LOGIN = 2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_welcome);
        revealCenterIcon();

        //Wait 1 second to reveal the rest of the UI
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                revealUI();

            }
        }, 2000);

    }

    public void revealCenterIcon(){
        TextView title1 = (TextView)findViewById(R.id.textview_welcometitle);
        TextView title2 = (TextView)findViewById(R.id.textview_welcometitle2);
        //set title font
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/CooperBlack.ttf");

        title1.setTypeface(myTypeface);
        title2.setTypeface(myTypeface);

        ImageView centerIcon = (ImageView)findViewById(R.id.image_centericon);



        //fade in center icon
        centerIcon.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(centerIcon);

    }

    public void revealUI(){
        TextView title1 = (TextView)findViewById(R.id.textview_welcometitle);
        TextView title2 = (TextView)findViewById(R.id.textview_welcometitle2);
        Button loginButton = (Button)findViewById(R.id.button_welcome_signin);
        Button registerButton = (Button)findViewById(R.id.button_welcome_register);
        TextView privacy = (TextView)findViewById(R.id.textview_privacy);



        title1.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(title1);

        title2.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(title2);

        loginButton.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(loginButton);

        registerButton.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(registerButton);

        privacy.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .playOn(privacy);


    }


    @Override
    protected void onResume (){
        super.onResume();
        getActionBar().hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void welcomeLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);

    }

    public void welcomeRegister(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTER);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REGISTER) {
            if(resultCode == RESULT_OK){

                String emailAddress = "";
                String password = "";

                if (data != null){
                    emailAddress = data.getStringExtra("email");
                    password = data.getStringExtra("password");
                }

                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("email", emailAddress);
                intent.putExtra("password", password);
                startActivityForResult(intent, LOGIN);

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == LOGIN){
            if(resultCode == RESULT_OK){

                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
