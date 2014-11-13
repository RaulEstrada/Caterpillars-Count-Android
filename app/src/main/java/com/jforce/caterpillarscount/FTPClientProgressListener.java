package com.jforce.caterpillarscount;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import org.apache.commons.net.io.CopyStreamAdapter;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import java.io.File;

/**
 * Created by justinforsyth on 11/10/14.
 */
public class FTPClientProgressListener implements CopyStreamListener {

    private File file;
    private HomeActivity.UploadImagesTask task;
    private Integer current;
    private Integer total;

    public FTPClientProgressListener(HomeActivity.UploadImagesTask task, File file, Integer current, Integer total){

        this.task = task;
        this.file = file;
        this.current = current;
        this.total = total;
    }

    public void bytesTransferred(CopyStreamEvent event){

    }

    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
        //this method will be called everytime some bytes are transferred

        final long bytes = totalBytesTransferred;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final int percent = (int)(bytes*100/file.length());

                        task.myPublishProgress(percent, current, total);

                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();







    }


}
