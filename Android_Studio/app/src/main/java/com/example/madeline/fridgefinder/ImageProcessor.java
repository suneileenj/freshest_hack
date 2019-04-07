package com.example.madeline.fridgefinder;

import android.graphics.*;

import com.google.android.gms.tasks.*;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.*;

// Contains everything for recognizing text from images

public class ImageProcessor {

    private FirebaseVisionImage image;
    private FirebaseVisionTextRecognizer textReader;
    public List<String> processedLines;
    protected static int counter = 0;

    public ImageProcessor(Bitmap bm) {
        counter = 0;
        image = FirebaseVisionImage.fromBitmap(bm);
        textReader = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        processedLines = new ArrayList<>();

        Task<FirebaseVisionText> result = textReader.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                            for (FirebaseVisionText.Line line : block.getLines()) {
                                processedLines.add(line.getText());
                                System.out.println(line.getText());
                            }
                        }
                        System.out.println("Image processed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Image processing failed :(");
                    }
                });
    }

    public List<String> getProcessedLines() {
        if (counter == 1){
            counter++;
            return processedLines;
        }
        return null;
    }
}