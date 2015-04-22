package com.jforce.caterpillarscount;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostImage extends AsyncTask<Void, Integer, Boolean> {

    private String photoPath;
    private HomeActivity activity;

    public PostImage(String photoPath, HomeActivity activity)
    {
        this.photoPath = photoPath;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(photoPath);
        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL urlUpload = new URL("http://caterpillarscount.unc.edu/api/uploads.php");

            // Open a HTTP  connection to  the URL
            HttpURLConnection conn = (HttpURLConnection) urlUpload.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("image", photoPath);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""
                    + photoPath + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necessary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (Exception e) {
            Log.w("err", e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        activity.notificationImages(progress[0], progress[1], progress[2]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            activity.notificationComplete();
            Toast toast = Toast.makeText(activity, "Upload complete!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else{
            activity.notificationFailure();
        }
    }
}
