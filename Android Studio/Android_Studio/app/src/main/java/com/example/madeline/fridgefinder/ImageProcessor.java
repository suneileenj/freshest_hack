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

    public ImageProcessor(Bitmap bm) {
        image = FirebaseVisionImage.fromBitmap(bm);
        textReader = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        processedLines = new ArrayList<>();

        Task<FirebaseVisionText> result = textReader.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                            for (FirebaseVisionText.Line line: block.getLines()) {
                                processedLines.add(line.getText());
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
        return processedLines;
    }

}