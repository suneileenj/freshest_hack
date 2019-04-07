package com.example.madeline.fridgefinder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView imageView;
    String pathToFile;
    static final int CAM_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        if(Build.VERSION.SDK_INT >=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        imageView = (ImageView) findViewById(R.id.image_view);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchPictureTakerAction();

            }


        });
    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");

        if(!folder.exists()){

            folder.mkdir();

        }
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

       super.onActivityResult(requestCode,resultCode,data);
       if(resultCode == RESULT_OK){
           if(requestCode == 1){
               Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
               imageView.setImageBitmap(bitmap);

           }
       }

    }

    private void dispatchPictureTakerAction(){
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            photoFile = createPhotoFile();
            if(photoFile!=null){

                String pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic,1);
            }

//            try{
//
//                photoFile = createPhotoFile();
//            }
//            catch(Exception){



        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
             image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch(IOException e){
            Log.d("mylog", "Excep : " + e.toString());
        }
        return image;
    }

}
