package jforce.com.caterpillarcount;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

        if(!loggedIn){
            firstTimeSetup();

        }else{
            setContentView(R.layout.activity_home);
            populateSpinners();
            populateDateAndTime();
            initCards();
        }


    }

    public void firstTimeSetup(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        //disables activity start animation
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        //disables activity finish animation
        overridePendingTransition(0, 0);
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
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
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
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
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("loggedIn", false);
            editor.commit();
            firstTimeSetup();
        }
        return super.onOptionsItemSelected(item);
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



        Spinner spinner1 = (Spinner) findViewById(R.id.site_spinner);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.site_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new SiteSpinnerController());


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

        Spinner spinner4 = (Spinner) findViewById(R.id.herbivory_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.herbivory_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner4.setAdapter(adapter4);

    }

    public void populateDateAndTime(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a");
        String time = timeFormat.format(calendar.getTime());
        EditText timeET = (EditText) findViewById(R.id.time_picker_edittext);
        timeET.setText(time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        EditText dateET = (EditText) findViewById(R.id.date_picker_edittext);
        dateET.setText(reformatDateLong(date));

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

        //Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        //calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        //calendar.set(Calendar.MINUTE, minute);

        //SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a");
        //String time = timeFormat.format(calendar.getTime());

        EditText timeET = (EditText) findViewById(R.id.time_picker_edittext);


        //timeET.setText(time);


        if(hourOfDay > 12){
            if (minute >= 10) {

                timeET.setText((hourOfDay-12) + ":" + minute + " PM");

            }
            else{

                timeET.setText((hourOfDay-12) + ":" + "0" + minute + " PM");

            }
        }
        else if (hourOfDay == 12){
            if (minute >= 10) {

                timeET.setText(hourOfDay + ":" + minute + " PM");

            }
            else{

                timeET.setText(hourOfDay + ":" + "0" + minute + " PM");

            }


        }
        else if (hourOfDay == 0){
            if (minute >= 10) {

                timeET.setText(12 + ":" + minute + " AM");

            }
            else{

                timeET.setText(12 + ":" + "0" + minute + " AM");

            }
        }
        else{

            if (minute >= 10) {

                timeET.setText(hourOfDay + ":" + minute + " AM");

            }
            else{

                timeET.setText(hourOfDay + ":" + "0" + minute + " AM");

            }
        }

    }

    public void capturePhoto(View view){

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){


                String order = data.getStringExtra("order");


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
                setListViewHeightBasedOnChildren(cardList);


//                Toast toast = Toast.makeText(this, "Adapter count: " + mCardArrayAdapter.getCount(), Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void initCards(){
        cardCount = 0;
        cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayAdapter(this,cards);
        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        mCardArrayAdapter.setEnableUndo(true);


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



}
