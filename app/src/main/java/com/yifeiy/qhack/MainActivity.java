package com.yifeiy.qhack;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout entry_container;
    static String newFile;
    static boolean makeFile = false;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public Uri imageUri;
    static ImageView receipt_view;
    static Bitmap bmp;
    ArrayList<String> filesContent;
    String file_replacement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button camera_button = findViewById(R.id.camera_button);
        receipt_view = findViewById(R.id.receipt_view);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });

        file_replacement = readFiles();

        String[] entries = file_replacement.split("!");

        final UrlHandler urlHandler = new UrlHandler();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        entry_container = findViewById(R.id.entry_container);

        for (String entry : entries) {
            Log.v("splits", entry);
            final Entry e = new Entry(entry.split("#"));
            final LinearLayout l = new LinearLayout(this);
            l.setLayoutParams(lp);
            l.setOrientation(LinearLayout.HORIZONTAL);

            ImageView img = new ImageView(this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveToInternalStorage(urlHandler.getBitmapFromURL(e.img_addr), e.name);
                }
            }).start();
            ContextWrapper cw = new ContextWrapper(this);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, e.name);
            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                img.setImageBitmap(b);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            img.setLayoutParams(getWeightParams(3f));

            TextView t = new TextView(this);
            t.setText(e.toString());
            t.setLayoutParams(getWeightParams(1f));
            t.setPadding(50, 100, 50, 100);

            l.addView(img);
            l.addView(t);

            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.setVisibility(View.GONE);
                }
            });
            entry_container.addView(l);

        }


    }


    private String readFiles(){
        filesContent = new ArrayList<>();
        for (int i=0;true;i++){
            String filename = "entries_file" + Integer.toString(i);
            File file = new File(this.getFilesDir(), filename);
            if (!(file.exists()|i>100)) {
                break;
            }
            try {
                Scanner sc = new Scanner(file);
                filesContent.add(sc.next());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        String s = "";

        for (int i=0;i<filesContent.size();i++){
            String filename = "entries_file" + Integer.toString(i);
            File file = new File(this.getFilesDir(), filename);
            FileOutputStream outputStream;
            if (file.exists()) {
                file.delete();
            }
            s += filesContent.get(i);
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(filesContent.get(i).getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (s.length() == 0){
            s = "iPhone#02/Jun/2019#http://i5.walmartimages.com/asr/c7447d99-c90c-4039-b098-7ec0949e85ea_1.d6bf6d15f16849b61e85deeac75035c7.jpeg#012341242123#999.99!Tomato Pretz#03/Jun/2019#http://i5.walmartimages.ca/images/Large/087/3v2/999999-999999-6289150873v2.jpg#123123123123#2.59!K cups#15/Jan/2018#http://i5.walmartimages.ca/images/Large/960/023/999999-UPC_762111960023.jpg#102938471823#12.29";
        }
        return s;
    }




    private static LinearLayout.LayoutParams getWeightParams(float x) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, x
        );
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String filename) {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (makeFile) {
            String[] entries = "Always Pad#03/Feb/2019#https://i5.walmartimages.ca/images/Large/469/447/6000198469447.jpg#003700030495#5.47!Always Pad#03/Feb/2019#https://i5.walmartimages.ca/images/Large/469/447/6000198469447.jpg#003700030495#5.47!FerreroChocolate Collection#03/Feb/2019#https://i5.walmartimages.ca/images/Large/404/092/6000199404092.jpg#007874212365#13.47!FerreroChocolate Collection#03/Feb/2019#https://i5.walmartimages.ca/images/Large/404/092/6000199404092.jpg#007874212365#13.47".split("!");
            final UrlHandler urlHandler = new UrlHandler();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            for (String entry : entries) {
                Log.v("splits", entry);
                final Entry e = new Entry(entry.split("#"));
                final LinearLayout l = new LinearLayout(this);
                l.setLayoutParams(lp);
                l.setOrientation(LinearLayout.HORIZONTAL);

                ImageView img = new ImageView(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveToInternalStorage(urlHandler.getBitmapFromURL(e.img_addr), e.name);
                    }
                }).start();
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File f = new File(directory, e.name);
                try {
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    img.setImageBitmap(b);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                img.setLayoutParams(getWeightParams(3f));

                TextView t = new TextView(this);
                t.setText(e.toString());
                t.setLayoutParams(getWeightParams(1f));
                t.setPadding(50, 100, 50, 100);

                l.addView(img);
                l.addView(t);

                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        l.setVisibility(View.GONE);
                    }
                });
                entry_container.addView(l);

            }
        }
        makeFile = false;

    }
    public static void feedResponse(String s){
        newFile = s;
        makeFile = true;
        update();
    }

    public static void update(){

    }
}




