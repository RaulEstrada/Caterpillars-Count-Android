package com.jforce.caterpillarscount;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.io.CopyStreamAdapter;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends FragmentActivity implements RadialTimePickerDialog.OnTimeSetListener{

    private String site;
    private int circle;
    private String survey;
    private String species;
    private int herbivory;
    private int temperature;
    private ArrayList<Card> cards;
    private int cardCount;
    private CardArrayAdapter mCardArrayAdapter;
    private TextView cardHint;
    private SharedPreferences sharedPreferences;
    private Bundle picBundle;

    private String surveyPhotoName;
    public String leafPictureName;
    private ArrayList<String> orderPhotoNames;


    private String time24hrFormat;
    private String dateLongFormat;

    private ProgressDialog uploadMetaDataProgressDialog;
    private ProgressDialog uploadImagesProgressDialog;

    public final int NOTIFICATION_ID = 1;

    boolean uploadSuccess = false;

    final int REQUEST_IMAGE_CAPTURE = 5;

    private String mCurrentPhotoPath;
    private String mLastPhotoPath;
    private boolean hasPhoto;
    private boolean speciesEditTextIsVisible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

        if(!loggedIn){
            firstTimeSetup();

        }else{
            setContentView(R.layout.activity_home);
            populateSpinners();
            populateDateAndTime();
            init();

        }


    }

    public void firstTimeSetup(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        //disables activity start animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
        //disables activity finish animation
        overridePendingTransition(0, 0);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView, Context c) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            //totalHeight += view.getMeasuredHeight() + 50;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        Toast toast = Toast.makeText(c, "Divider height: " + listView.getDividerHeight(), Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
        //listview.getDividerHeigt() was yielding 0, so I hardcoded it so bottoms of cards
        //don't get cut off

        if((listAdapter.getCount() - 1) == 0){
            params.height = totalHeight + 50;
        }
        else{
            params.height = totalHeight + (100 * (listAdapter.getCount() - 1));
        }

        listView.setLayoutParams(params);
        listView.requestLayout();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        firstTimeSetup();

    }



    public void populateSpinners(){

        Spinner spinner0 = (Spinner) findViewById(R.id.temp_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this,
                R.array.temp_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner0.setAdapter(adapter0);

        spinner0.setOnItemSelectedListener(new TemperatureSpinnerController());
        spinner0.setSelection(3);



        Spinner spinner1 = (Spinner) findViewById(R.id.site_spinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
//                R.array.site_array, android.R.layout.simple_spinner_item);
//    // Specify the layout to use when the list of choices appears
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    // Apply the adapter to the spinner
//        spinner1.setAdapter(adapter1);

        Gson gson = new Gson();

        ArrayList<Site> emptySites = new ArrayList<Site>();
        String jsonEmptySites = gson.toJson(emptySites);

        String jsonSites = sharedPreferences.getString("sites", jsonEmptySites);


        Type type = new TypeToken<ArrayList<Site>>(){}.getType();
        ArrayList<Site> sites = gson.fromJson(jsonSites, type);


        spinner1.setAdapter(new SiteSpinnerAdapter(this, sites));

        spinner1.setOnItemSelectedListener(new SiteSpinnerController(this));




        Spinner spinner2 = (Spinner) findViewById(R.id.circle_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.circle_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        Spinner spinner3 = (Spinner) findViewById(R.id.survey_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.survey_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);

        Site site = (Site) spinner1.getSelectedItem();
        if(site != null){
            populateSpeciesSpinnerByState(site.getStateCode());
        }




        Spinner spinner5 = (Spinner) findViewById(R.id.herbivory_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
//                R.array.herbivory_array, android.R.layout.simple_spinner_item);

        //HerbivorySpinnerAdapter adapter5 = new HerbivorySpinnerAdapter(this);
        // Specify the layout to use when the list of choices appears
        //adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner5.setAdapter(new HerbivorySpinnerAdapter(this));

    }

    public void populateDateAndTime(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat timeFormatLong = new SimpleDateFormat("kk:mm:ss");
        String time = timeFormat.format(calendar.getTime());
        time24hrFormat = timeFormatLong.format(calendar.getTime());
        EditText timeET = (EditText) findViewById(R.id.time_picker_edittext);
        timeET.setText(time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        dateLongFormat = dateFormatLong.format(calendar.getTime());
        EditText dateET = (EditText) findViewById(R.id.date_picker_edittext);
        dateET.setText(date);

    }

    // dd/MM/yyyy to MM/dd
    public String reformatDateShort(String date){

        return date.substring(3, 5) + "/" + date.substring(0, 2);
    }

    // dd/MM/yyyy to MM/dd/yy
    public String reformatDateLong(String date){

        return date.substring(3, 5) + "/" + date.substring(0, 2) + "/" + date.substring(8);
    }

    public void setSite(String site){
        this.site = site;
    }

    public void setCircle(int circle){
        this.circle = circle;
    }


    public void setSurvey(String survey){
        this.survey = survey;
    }

    public void setSpecies(String species){
        this.species = species;
    }

    public void setHerbivory(int herbivory){
        this.herbivory = herbivory;
    }

    public void setTemperature(int temperature) {this.temperature = temperature;}

    public void addOrder(View view){
        Intent intent = new Intent(this, OrderActivity.class);
        startActivityForResult(intent, 1);
    }

    public void pickDate(View view){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(new DatePickerController(), year, month, day);
        calendarDatePickerDialog.show(getSupportFragmentManager(), "datePickerDialog");
    }

    public void pickTime(View view){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                .newInstance(this, hour, minute, false);

        timePickerDialog.show(getSupportFragmentManager(), "timePickerDialog");


    }

    @Override
    public void onTimeSet(RadialPickerLayout layout, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String time = timeFormat.format(calendar.getTime());
        SimpleDateFormat timeFormatLong = new SimpleDateFormat("kk:mm:ss");
        time24hrFormat = timeFormatLong.format(calendar.getTime());

        EditText timeET = (EditText) findViewById(R.id.time_picker_edittext);


        timeET.setText(time);
//
//
//        if(hourOfDay > 12){
//            if (minute >= 10) {
//
//                timeET.setText((hourOfDay-12) + ":" + minute + " PM");
//
//            }
//            else{
//
//                timeET.setText((hourOfDay-12) + ":" + "0" + minute + " PM");
//
//            }
//        }
//        else if (hourOfDay == 12){
//            if (minute >= 10) {
//
//                timeET.setText(hourOfDay + ":" + minute + " PM");
//
//            }
//            else{
//
//                timeET.setText(hourOfDay + ":" + "0" + minute + " PM");
//
//            }
//
//
//        }
//        else if (hourOfDay == 0){
//            if (minute >= 10) {
//
//                timeET.setText(12 + ":" + minute + " AM");
//
//            }
//            else{
//
//                timeET.setText(12 + ":" + "0" + minute + " AM");
//
//            }
//        }
//        else{
//
//            if (minute >= 10) {
//
//                timeET.setText(hourOfDay + ":" + minute + " AM");
//
//            }
//            else{
//
//                timeET.setText(hourOfDay + ":" + "0" + minute + " AM");
//
//            }
//        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){

                if(cardCount == 0){
                    LinearLayout layout = (LinearLayout) findViewById(R.id.cards_linear_layout);
                    cardHint = (TextView) layout.findViewById(R.id.order_card_hint_text);
                    layout.removeView(cardHint);

                }


                cardCount++;

                //Create a Card
                OrderCard card = new OrderCard(this, data);

                cards.add(card);

                mCardArrayAdapter.notifyDataSetChanged();

                ListView cardList = (ListView) findViewById(R.id.myList);
                setListViewHeightBasedOnChildren(cardList, this);


//                Toast toast = Toast.makeText(this, "Adapter count: " + mCardArrayAdapter.getCount(), Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (resultCode == RESULT_OK) {
                hasPhoto = true;


                galleryAddPic();


                Button button = (Button) findViewById(R.id.capture_plant_photo_button);


                // Get the dimensions of the View
                int targetW = button.getWidth();
                int targetH = button.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

                bitmap = OrderActivity.rotateBitmap(bitmap, 90);

                BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);


                button.setText("");

                button.setBackgroundDrawable(drawable);
            }
            else{

                File file = new File(mCurrentPhotoPath);
                file.delete();

                if(hasPhoto){
                    mCurrentPhotoPath = mLastPhotoPath;
                }
                else{
                    mCurrentPhotoPath = null;
                }

            }
        }


    }

    public void init(){
        cardCount = 0;
        cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(this,cards);
        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        mCardArrayAdapter.setEnableUndo(true);

        orderPhotoNames = new ArrayList<String>();

        uploadSuccess = false;

        hasPhoto = false;

        speciesEditTextIsVisible = false;


    }


    public ArrayList<Card> getCardList() {
        return cards;
    }

    public void setCardList(ArrayList<Card> cards){
        this.cards = cards;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount){
        this.cardCount = cardCount;
    }

    public CardArrayAdapter getCardArrayAdapter() {
        return mCardArrayAdapter;
    }

    public void setCardArrayAdapter(CardArrayAdapter adapter){
        this.mCardArrayAdapter = adapter;
    }

    public void takePictureLeaf(View view){
        File f = takePicture(view);
        this.leafPictureName = f.getPath();
    }

    public File takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;


            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

                Toast toast = Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            return photoFile;
        }
        return null;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mLastPhotoPath = mCurrentPhotoPath;
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void addSite(View view){

        final View v = view;

        final Activity activity = this;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Plant Infomation");
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_add_site, null));
        builder.setCustomTitle(getLayoutInflater().inflate(R.layout.dialog_add_site_title, null));
        builder.setCancelable(true)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        AlertDialog alertDialog = (AlertDialog) dialog;



                        EditText etID = (EditText) alertDialog.findViewById(R.id.siteid_edittext);
                        EditText etPassword = (EditText) alertDialog.findViewById(R.id.sitepassword_edittext);

                        int siteID = Integer.parseInt(etID.getText().toString());
                        String sitePassword = etPassword.getText().toString();


                        //construct the JSON object to be POSTed
                        JSONObject jsonParams = new JSONObject();
                        StringEntity entity = null;
                        try {
                            jsonParams.put("action", "checkSitePassword");
                            jsonParams.put("siteID", siteID);
                            jsonParams.put("sitePasswordCheck", sitePassword);
                            entity = new StringEntity(jsonParams.toString());
                        } catch (Exception e) {
                            Toast toast1 = Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT);
                            toast1.setGravity(Gravity.CENTER, 0, 0);
                            toast1.show();
                        }



                        SitePasswordResponseHandler handler = new SitePasswordResponseHandler(activity, siteID, sitePassword);

                        if (entity == null) {
                            Toast toast1 = Toast.makeText(v.getContext(), "Something went wrong", Toast.LENGTH_SHORT);
                            toast1.setGravity(Gravity.CENTER, 0, 0);
                            toast1.show();
                        }

                        RestClient.postJson(activity, "sites.php", entity, "application/json", handler);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();


    }

    public void downloadSite(int siteID, String sitePassword){

        //construct the JSON object to be POSTed
        JSONObject jsonParams = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParams.put("action", "getOneByID");
            jsonParams.put("siteID", siteID);
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception e) {
            Toast toast1 = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
        }

        SiteDownloadResponseHandler handler = new SiteDownloadResponseHandler(this, sitePassword);

        if (entity == null) {
            Toast toast1 = Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
        }

        RestClient.postJson(this, "sites.php", entity, "application/json", handler);

    }

    public void saveSite(Site site){



        Gson gson = new Gson();

        ArrayList<Site> emptySites = new ArrayList<Site>();
        String jsonEmptySites = gson.toJson(emptySites);

        String jsonSites = sharedPreferences.getString("sites", jsonEmptySites);


        Type type = new TypeToken<ArrayList<Site>>(){}.getType();
        ArrayList<Site> sites = gson.fromJson(jsonSites, type);

        for(Site s: sites){

            if(s.compareTo(site) == 0){
                Toast toast1 = Toast.makeText(this, "Requested site already in site list!", Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.show();
                return;
            }

        }

        sites.add(site);


        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();


        String serializedSites = gson.toJson(sites);
        prefsEditor.putString("sites", serializedSites);
        prefsEditor.commit();



        Toast toast1 = Toast.makeText(this, site.getName() + " successfully added!", Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER, 0, 0);
        toast1.show();

        Spinner spinner1 = (Spinner) findViewById(R.id.site_spinner);
        SiteSpinnerAdapter adapter = new SiteSpinnerAdapter(this, sites);
        spinner1.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner1.setSelection(sites.size() -1);
        populateSpeciesSpinnerByState(site.getStateCode());



    }






    public Bundle getPicBundle() {
        return picBundle;
    }

    public void setPicBundle(Bundle pic) {
        this.picBundle = pic;
    }

    public void setDateLongFormat(String dateLongFormat) {
        this.dateLongFormat = dateLongFormat;
    }

    public void setTime24hrFormat(String time24hrFormat) {
        this.time24hrFormat = time24hrFormat;
    }

    public ArrayList<String> getOrderPhotoNames() {
        return orderPhotoNames;
    }

    public void setOrderPhotoNames(ArrayList<String> orderPhotoNames) {
        this.orderPhotoNames = orderPhotoNames;
    }

    public String getSurveyPhotoName() {
        return surveyPhotoName;
    }

    public void setSurveyPhotoName(String surveyPhotoName) {
        this.surveyPhotoName = surveyPhotoName;
    }

    public void submit(View view) throws JSONException, UnsupportedEncodingException {



//        uploadMetaDataProgressDialog = getUploadMetaDataProgressDialog().show(this, "Uploading Data", "Please wait...");
//        uploadMetaDataProgressDialog.setCancelable(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        String email1 = sharedPreferences.getString("email", null);

        int length = email1.length();


        String substring = email1.substring(length - 7, length);

        if(!substring.equals("unc.edu")){
            Toast toast1 = Toast.makeText(this, "Only unc.edu email addresses may enter data at this time", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return;

        }


        //siteID
        Spinner siteSpinner = (Spinner) findViewById(R.id.site_spinner);
        Site site = (Site) siteSpinner.getSelectedItem();

        if(site == null){

            Toast toast = Toast.makeText(this, "Please select a site", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.site_spinner));
            return;
        }


        if(!hasPhoto){

            Toast toast1 = Toast.makeText(this, "Leaf Photo is required", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();

            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.capture_plant_photo_button));

            return;



        }



        notificationStart();

        //clear old photo name values
        surveyPhotoName = null;
        orderPhotoNames.clear();


        Toast toast1 = Toast.makeText(this, "Starting Upload...", Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER, 0, 0);
        toast1.show();



        //First check that user is still valid and hasn't been invalidated by an admin
        String email = sharedPreferences.getString("email", "");

        if(email.equals("")){

            //getUploadMetaDataProgressDialog().dismiss();
            notificationFailure();
            Toast toast = Toast.makeText(this, "Internal error: please log out and log back in", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        //construct the JSON object to be POSTed
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("email", email);
        StringEntity entity = new StringEntity(jsonParams.toString());


        SubmitValidResponseHandler handler = new SubmitValidResponseHandler(this);

        RestClient.postJson(this, "users.php", entity, "application/json", handler);

    }

    public void submitSurvey() throws JSONException, UnsupportedEncodingException{




        //then submit the survey

        //construct the JSON object to be POSTed
        JSONObject jsonParams = new JSONObject();
        String type = "survey";
        jsonParams.put("type",type);
        //Log.d("caterpillars", type);

        //siteID
        Spinner siteSpinner = (Spinner) findViewById(R.id.site_spinner);
        Site site = (Site) siteSpinner.getSelectedItem();

        if(site == null){
            notificationFailure();
            Toast toast = Toast.makeText(this, "Please select a site", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        jsonParams.put("siteID", site.getSiteID());
        //Log.d("caterpillars", Integer.toString(siteID + 3));


        //userID
        int userID = sharedPreferences.getInt("userID", -1);
        if(userID == -1){
            //getUploadMetaDataProgressDialog().dismiss();
            notificationFailure();
            Toast toast = Toast.makeText(this, "Internal error: please log out and log back in", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        jsonParams.put("userID", userID);
        //Log.d("caterpillars", Integer.toString(userID));

        //circle

        Spinner circleSpinner = (Spinner) findViewById(R.id.circle_spinner);
        int circle = circleSpinner.getSelectedItemPosition() + 1;
        jsonParams.put("circle", circle);
        //Log.d("caterpillars", Integer.toString(circle));

        //survey
        Spinner surveySpinner = (Spinner) findViewById(R.id.survey_spinner);
        String survey = (String) surveySpinner.getSelectedItem();
        jsonParams.put("survey", survey);
        //Log.d("caterpillars", survey);

        //timeStart

        String timeStart = dateLongFormat + " " + time24hrFormat;
        jsonParams.put("timeStart", timeStart);
        //Log.d("caterpillars", timeStart);

        //temperatureMin
        Spinner tempSpinner = (Spinner) findViewById(R.id.temp_spinner);
        int tempIndex = tempSpinner.getSelectedItemPosition();
        int[] minArray = getResources().getIntArray(R.array.temp_array_min);
        int temperatureMin = minArray[tempIndex];
        jsonParams.put("temperatureMin", temperatureMin);
        //Log.d("caterpillars", Integer.toString(temperatureMin));

        //temperatureMax

        int[] maxArray = getResources().getIntArray(R.array.temp_array_max);
        int temperatureMax = maxArray[tempIndex];
        jsonParams.put("temperatureMax", temperatureMax);
        Log.d("caterpillars", Integer.toString(temperatureMax));

        //siteNotes

        EditText siteNotesET = (EditText) findViewById(R.id.sitenotes_edittext);
        String siteNotes = siteNotesET.getText().toString();

        jsonParams.put("siteNotes", siteNotes);



        //Log.d("caterpillars", siteNotes);

        //plantSpecies
        Spinner plantSpeciesSpinner = (Spinner) findViewById(R.id.plantspecies_spinner);
        String spinnerItem = (String) plantSpeciesSpinner.getSelectedItem();

        if(spinnerItem.equals("Other")){
            EditText plantSpeciesET = (EditText) findViewById(R.id.plantspecies_edittext);
            String plantSpecies = plantSpeciesET.getText().toString();
            jsonParams.put("plantSpecies", plantSpecies);
        }
        else{
            String plantSpecies = spinnerItem;
            jsonParams.put("plantSpecies", plantSpecies);

        }


        //Log.d("caterpillars", plantSpecies);

        //herbivory
        Spinner herbivorySpinner = (Spinner) findViewById(R.id.herbivory_spinner);
        int herbivoryIndex = herbivorySpinner.getSelectedItemPosition();
        int[] herbivoryVals = getResources().getIntArray(R.array.herbivory_array_values);
        int herbivory = herbivoryVals[herbivoryIndex];
        jsonParams.put("herbivory", herbivory);
        //Log.d("caterpillars", Integer.toString(herbivory));

        //construct the JSON object to be POSTed

        StringEntity entity = new StringEntity(jsonParams.toString());
        SubmitSurveyResponseHandler handler = new SubmitSurveyResponseHandler(this);
        RestClient.postJson(this,"submission_full.php",entity,"application/json",handler);


    }

    public void submitOrders(int surveyID) throws JSONException{
        if(cards.size() == 0){
            notificationImages(null, null, null);
            return;
        }

        for (int i = 0; i < cards.size(); i++){

            //construct the JSON object to be POSTed
            JSONObject jsonParams = new JSONObject();

            //type
            String type = "order";
            jsonParams.put("type",type);
            Log.d("caterpillars", type);


            //userID
            int userID = sharedPreferences.getInt("userID", -1);
            if(userID == -1){
                notificationFailure();
                Toast toast = Toast.makeText(this, "Internal error: please log out and log back in", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            jsonParams.put("userID", userID);
            Log.d("caterpillars", Integer.toString(userID));


            //surveyID
            jsonParams.put("surveyID", surveyID);
            Log.d("caterpillars", Integer.toString(surveyID));


            OrderCard card = (OrderCard) cards.get(i);


            //orderArthropod
            String orderArthropod = card.getOrder();
            jsonParams.put("orderArthropod", orderArthropod);
            Log.d("caterpillars", orderArthropod);

            //orderLength
            int orderLength = card.getLength();
            jsonParams.put("orderLength", orderLength);
            Log.d("caterpillars", Integer.toString(orderLength));

            //orderNotes
            String orderNotes = card.getNotes();
            jsonParams.put("orderNotes", orderNotes);
            Log.d("caterpillars", orderNotes);

            //orderCount
            int orderCount = card.getCount();
            jsonParams.put("orderCount", orderCount);
            Log.d("orderCount", Integer.toString(orderCount));

            if(card.getPhotoPath()!=null){
                try{
                    postImage(card.getPhotoPath());
                } catch (Exception e){}
            }

            StringEntity entity;
            try {
                entity = new StringEntity(jsonParams.toString());
            }
            catch (Exception e){
                notificationFailure();
                Toast toast = Toast.makeText(this, "Internal error: please retry", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            SubmitOrderResponseHandler handler = new SubmitOrderResponseHandler(this, cards.size(), i);
            RestClient.postJson(this,"submission_full.php",entity,"application/json",handler);
        }
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }

    public ProgressDialog getUploadMetaDataProgressDialog() {
        return uploadMetaDataProgressDialog;
    }

    public ProgressDialog getUploadImagesProgressDialog(){
        return uploadImagesProgressDialog;
    }

    public void openSiteInfoDialog(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(getLayoutInflater().inflate(R.layout.dialog_site_info_title, null));
        builder.setMessage("Enter the Site, Circle, and Survey associated with where you are currently recording data.\n\n" +
                "Enter any notes that you may deem relevant to data reviewers");
        builder.setCancelable(true)
                .setNegativeButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

    public void openOrderInfoDialog(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(getLayoutInflater().inflate(R.layout.dialog_order_info_title, null));
        builder.setMessage("Add a record for each Arthropod Order that you see, estimating its length in mm " +
                "and noting how many you saw.\n\n" +
                "If necessary, submit observations for an Order using multiple records " +
                "(e.g., you saw 10 ants of size 4 mm, and 3 ants of size 12 mm).\n\n" +
                "You can add multiple Arthropod Orders to each submission.");
        builder.setCancelable(true)
                .setNegativeButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }

    public void openPlantInfoDialog(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Select the name of the plant species that you surveyed from the drop-down menu below. " +
                "If the desired plant species does not appear in the list, select " +
                "'Other' and enter the plant species manually into the text field that appears.\n\n" +
                "Choose the herbivory score that best characterizes the average level of herbivory across all leaves examined in the survey.");
        builder.setCustomTitle(getLayoutInflater().inflate(R.layout.dialog_plant_info_title, null));
        builder.setCancelable(true)
                .setNegativeButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();

    }


    public void openLeafPhotoDialog(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_leaf_photo, null));
        builder.setCustomTitle(getLayoutInflater().inflate(R.layout.dialog_leaf_photo_title, null));
        builder.setCancelable(true)
                .setNegativeButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

    public void postImage(String photoPath) throws FileNotFoundException{
        if (photoPath != null){
            RestClient.postFile(this, photoPath);
        }
    }

    public void notificationStart(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_file_file_upload)
                        .setContentTitle("Verifying Credentials")
                        .setContentText("Please wait...")
                        .setProgress(0, 0, true)
                        .setOngoing(true);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(this.NOTIFICATION_ID, mBuilder.build());
    }

    public void notificationData(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_file_file_upload)
                        .setContentTitle("Uploading Data")
                        .setContentText("Upload is in progress")
                        .setProgress(0, 0, true)
                        .setOngoing(true);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(this.NOTIFICATION_ID, mBuilder.build());
    }

    public void notificationImages(Integer progress, Integer current, Integer total){
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(this)
                                        .setSmallIcon(R.drawable.ic_stat_file_file_upload)
                                        .setContentTitle("Uploading Images")
                                        .setOngoing(true);

                        if(progress != null){
                            mBuilder.setContentText("Image " + current + " of " + total + " - " + progress + "%")
                                    .setProgress(100, progress, false);

                        }
                        else{

                            mBuilder.setContentText("Getting files ready")
                                    .setProgress(0, 0, true);

                        }

                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }


    public void notificationFailure(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_file_cloud_off)
                        .setContentTitle("Upload Failed")
                        .setProgress(0, 0, false)
                        .setOngoing(false);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(this.NOTIFICATION_ID, mBuilder.build());

    }

    public void notificationComplete(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_file_cloud_done)
                        .setContentTitle("Upload complete")
                        .setContentText("Thank you for your submission!")
                        .setProgress(0, 0, false)
                        .setOngoing(false);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(this.NOTIFICATION_ID, mBuilder.build());
    }

    public String trimDomainPrefix(String string){
        final int trimLength = "uploads/".length();
        return string.substring(trimLength);
    }

    public String trimAbsolutePath(String string){
        String[] parts = string.split("/");
        int length = parts.length;
        return parts[length - 1];
    }

    public void populateSpeciesSpinnerByState(String stateCode){

        new ParseSpeciesTask(this, stateCode).execute();

    }


    public void setSpecies(ArrayList<String> species){

        Spinner spinner4 = (Spinner) findViewById(R.id.plantspecies_spinner);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.addAll(species);
        adapter4.notifyDataSetChanged();
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new SpeciesSpinnerController());
    }


    public class ParseSpeciesTask extends AsyncTask<Void, Integer, Boolean> {

        Activity activity;
        String stateCode;
        ArrayList<String> values;

        public ParseSpeciesTask(Activity activity, String stateCode){
            this.activity = activity;
            this.stateCode = stateCode;
            this.values = new ArrayList<String>();


        }

        @Override
        protected Boolean doInBackground(Void... v) {

            InputStream is = getResources().openRawResource(R.raw.cc_tree_species_by_state);

            Scanner scanner = new Scanner(is, "UTF-8");


            while(scanner.hasNextLine()){
                String line = scanner.nextLine();


                String[] cells = line.split(",");

                String state = cells[0].replace("\"", "");
                String species = cells[1].replace("\"", "");

                if(stateCode.equals(state)){

                    values.add(species);
                }
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                HomeActivity homeActivity = (HomeActivity) activity;

                homeActivity.setSpecies(values);
            }
            else{
                Toast toast = Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }


    public void showSpeciesEditText(boolean animate){

        EditText et = (EditText) findViewById(R.id.plantspecies_edittext);

        et.setVisibility(View.VISIBLE);


        if(animate){
            YoYo.with(Techniques.FadeIn)
                    .duration(500)
                    .playOn(et);
        }
        speciesEditTextIsVisible = true;
    }

    public void hideSpeciesEditText(boolean animate){
        final EditText et = (EditText) findViewById(R.id.plantspecies_edittext);
        if(animate){
            YoYo.with(Techniques.FadeOut)
                    .duration(500)
                    .playOn(et);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                et.setVisibility(View.GONE);

            }
        }, 500);

        speciesEditTextIsVisible = false;
    }


    public boolean speciesEditTextIsVisible() {
        return speciesEditTextIsVisible;
    }

}
