package com.jforce.caterpillarscount;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.jforce.caterpillarscount.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;


public class OrderActivity extends Activity {

    private String order;
    String mCurrentPhotoPath;
    String mLastPhotoPath;
    boolean hasPhoto;


    final int REQUEST_IMAGE_CAPTURE = 5;

    public static final String NUMBERS_ONLY = "^[0-9]*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        populateSpinners();

        hasPhoto = false;
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

        if(string.length() == 0){
            return null;
        }

        if(!string.matches(NUMBERS_ONLY)){
            return null;
        }


        Integer length;
        try {
            length = Integer.parseInt(et.getText().toString());
        }
        catch (Exception e){
            return null;
        }

        if(length > 300){
            return null;
        }

        return length;



    }


    public Integer getCount() {

        EditText et = (EditText) findViewById(R.id.count_edittext);

        String string = et.getText().toString();

        if(string.length() == 0){
            return null;
        }

        if(!string.matches(NUMBERS_ONLY)){
            return null;
        }

        Integer count;
        try {
            count = Integer.parseInt(et.getText().toString());
        }
        catch (Exception e){
            return null;
        }

        if(count > 1000){
            return null;
        }


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

            EditText et = (EditText) findViewById(R.id.length_edittext);

            YoYo.with(Techniques.Shake)
                    .playOn(et);


            Toast toast = Toast.makeText(this, getResources().getString(R.string.order_invalidLength), Toast.LENGTH_SHORT);
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
        if(mCurrentPhotoPath!=null){
            data.putExtra("photoPath", mCurrentPhotoPath);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    public String getOrder(){
        return order;
    }

    public void setOrder(String order){
        this.order = order;
    }

    public void takePicture(View view) throws IOException{
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//
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
        }






    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            hasPhoto = true;


            galleryAddPic();


            Button button = (Button) findViewById(R.id.capture_order_photo_button);


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

            bitmap = rotateBitmap(bitmap, 90);

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


    public static Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



}
