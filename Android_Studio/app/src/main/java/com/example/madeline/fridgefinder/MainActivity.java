package com.example.madeline.fridgefinder;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madeline.fridgefinder.db.ItemContract;
import com.example.madeline.fridgefinder.db.ItemDbHelper;
import com.google.android.gms.vision.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * handles the activity shown on app opening
 * this activity contains the camera option
 *
 * */

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    String pathToFile;
    Bitmap mine;
    protected static ImageProcessor processor;
    protected static boolean pictureTaken = false;

    private final int requestCode = 20;
    /**
     * creates option to navigate to the other activity
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            /**
             * Starts the next activity if the button is pressed
             * */

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        //Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        Intent activity2Intent = new Intent(getApplicationContext(), LstActivity.class);
                        startActivity(activity2Intent);
                        break;
                    /*case R.id.action_favorites:
                        Intent activity3Intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(activity3Intent);
                        break;*/
                }
                return true;
            }
        });


        //handles camera and input
        button = (Button) findViewById(R.id.button);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]
                    {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        imageView = (ImageView) findViewById(R.id.image_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }


        });
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();

        }
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;

    }
    /**
     * occurs after a picture is taken
     * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(this.requestCode == requestCode && resultCode == RESULT_OK){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        String partFilename = currentDateFormat();
        //storeCameraPhotoInSDCard(bitmap, partFilename);

        // display the image from SD Card to ImageView Control
        //String storeFilename = "photo_" + partFilename + ".jpg";
        //Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
        imageView.setImageBitmap(bitmap);

        mine = bitmap;

        //creates a processor
        processor = new ImageProcessor(bitmap);

        //set true when picture is taken
        //pictureTaken = true;

        //switch activities to listview after a picture is taken
        Intent activity3Intent = new Intent(getApplicationContext(), LstActivity.class);
        startActivity(activity3Intent);


    }


    /**
     * stores the image if an SD card is included
     * */
    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }*/
    /**
     * method that gets the current date
     * */
    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void dispatchPictureTakerAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                startActivityForResult(takePic, requestCode);

            }
        }
    }

    /**
     * creates a photo file
     * */
    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("mylog", "Excep : " + e.toString());
        }
        return image;
    }

}
