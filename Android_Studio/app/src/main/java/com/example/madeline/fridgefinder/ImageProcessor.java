package com.example.madeline.fridgefinder;

import android.graphics.*;

import com.google.android.gms.tasks.*;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.*;

// Contains everything for recognizing text from images
/**
 * class that processes an image and provides
 * a method that processes the lines read
 * from a bitmap image
 *
 * */

public class ImageProcessor {

    private FirebaseVisionImage image;
    private FirebaseVisionTextRecognizer textReader;
    public List<String> processedLines;
    //protected static int counter = 0;

    /**
     * constructor
     * @param bm the image being processed
     * */
    public ImageProcessor(Bitmap bm) {
        image = FirebaseVisionImage.fromBitmap(bm);
        textReader = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        processedLines = new ArrayList<>();

        Task<FirebaseVisionText> result = textReader.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    /**
                     * store the lines parsed from the image in
                     * the instance variable processedLines
                     * */
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                processedLines.add(line.getText());
                                //System.out.println(line.getText());
                            }
                        }
                        //System.out.println("Image processed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Image processing failed :(");
                    }
                });
    }
    /**
     * method that returns the lines taken from text
     * @return the lines read from the image or
     *          an empty list if no image has been read
     * */

    public List<String> getProcessedLines() {
        //return an empty arraylist if processedLines hasn't been initialized
        if (processedLines == null){
            processedLines = new ArrayList<>();
            return processedLines;
        }

        //return the lines and clear out the list
        List<String> temp = new ArrayList<>(processedLines);
        processedLines.clear();
        return temp;
    }
}