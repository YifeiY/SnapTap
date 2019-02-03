package com.yifeiy.qhack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlHandler extends AsyncTask<String,Void,Bitmap> {

    public Bitmap getBitmapFromURL(String src) {
        String [] arr = new String [1];
        arr[0] = src;
        return doInBackground(arr);
    }
    protected Bitmap doInBackground(String... params) {

        try {
            java.net.URL url = new java.net.URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, null);

        } catch (Exception e) {
            return null;
        }
    }


}
