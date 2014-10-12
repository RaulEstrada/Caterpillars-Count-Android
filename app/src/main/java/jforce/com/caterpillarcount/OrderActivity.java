package jforce.com.caterpillarcount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class OrderActivity extends Activity {

    private String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        populateSpinners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void populateSpinners(){
        Spinner spinner0 = (Spinner) findViewById(R.id.order_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter0 = ArrayAdapter.createFromResource(this,
                R.array.order_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner0.setAdapter(adapter0);

        spinner0.setOnItemSelectedListener(new OrderSpinnerController());
    }

    public int getLength() {

        EditText et = (EditText) findViewById(R.id.length_edittext);
        int length = Integer.parseInt(et.getText().toString());

        return length;



    }


    public int getCount() {

        EditText et = (EditText) findViewById(R.id.count_edittext);
        int count = Integer.parseInt(et.getText().toString());
        return count;


    }

    public String getNotes() {

        EditText et = (EditText) findViewById(R.id.notes_order_edittext);
        return et.getText().toString();


    }


    public void submit(View view){

        Intent data = new Intent();

        data.putExtra("order", getOrder());
        data.putExtra("count", getCount());
        data.putExtra("length", getLength());
        data.putExtra("notes", getNotes());
        setResult(RESULT_OK, data);
        finish();

    }

    public String getOrder(){
        return order;
    }

    public void setOrder(String order){
        this.order = order;
    }
}
