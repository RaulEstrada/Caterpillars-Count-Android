package com.jforce.caterpillarscount;

import android.graphics.Bitmap;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by justinforsyth on 10/20/14.
 */
public class ThumbnailCustomSource implements CardThumbnail.CustomSource{

    private String tag;
    private Bitmap bitmap;



    public ThumbnailCustomSource(String tag, Bitmap bitmap){

        this.tag = tag;
        this.bitmap = bitmap;
    }


    public String getTag(){

        return tag;

    }


    /**
     * @return the bitmap from custom source
     */
    public Bitmap getBitmap(){

        return bitmap;
    }



}
