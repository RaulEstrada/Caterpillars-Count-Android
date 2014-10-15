package jforce.com.caterpillarcount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class OrderActivity extends Activity {

    private String order;
    private Bitmap pic = null;


    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String NUMBERS_ONLY = "^[0-9]*$";

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

    public Integer getLength() {

        EditText et = (EditText) findViewById(R.id.length_edittext);

        String string = et.getText().toString();

        if(!string.matches(NUMBERS_ONLY)){
            return null;
        }

        Integer length = Integer.parseInt(et.getText().toString());

        return length;



    }


    public Integer getCount() {

        EditText et = (EditText) findViewById(R.id.count_edittext);

        String string = et.getText().toString();

        if(!string.matches(NUMBERS_ONLY)){
            return null;
        }

        Integer count = Integer.parseInt(et.getText().toString());
        return count;


    }

    public String getNotes() {

        EditText et = (EditText) findViewById(R.id.notes_order_edittext);
        return et.getText().toString();


    }


    public void submit(View view){



        Integer length = getLength();
        Integer count = getCount();



        if(length == null){

            EditText et = (EditText) findViewById(R.id.count_edittext);

            YoYo.with(Techniques.Shake)
                    .playOn(et);


            Toast toast = Toast.makeText(this, getResources().getString(R.string.order_invalidCount), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return;

        }



        if(count == null){

            EditText et = (EditText) findViewById(R.id.count_edittext);

            YoYo.with(Techniques.Shake)
                    .playOn(et);


            Toast toast = Toast.makeText(this, getResources().getString(R.string.order_invalidCount), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            return;

        }

        Intent data = new Intent();

        data.putExtra("order", getOrder());
        data.putExtra("count", getCount());
        data.putExtra("length", getLength());
        data.putExtra("notes", getNotes());
        data.putExtra("pic", getPic());

        setResult(RESULT_OK, data);
        finish();

    }

    public String getOrder(){
        return order;
    }

    public void setOrder(String order){
        this.order = order;
    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setPic(imageBitmap);
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), imageBitmap);

            Button button = (Button) findViewById(R.id.capture_order_photo_button);
            button.setText("");

            button.setBackgroundDrawable(drawable);
        }
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }



}
