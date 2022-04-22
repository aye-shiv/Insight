package com.trivtech.insight.misc;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutions.hands.HandsResult;
import com.trivtech.insight.ml.SignRecog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Interpreter {
    public static String logTag = "Interpreter";

    private final SignClassifier classifier = new SignClassifier();
    public String prediction = "";
    public String predictionToCheck = "";
    public String match = "";

    public Interpreter(){ }

    public void open(Context context){
        classifier.open(context);
    }
    public void close() {
        classifier.close();
    }

    public boolean feed(HandsResult result){ //Returns false if no landmarks given, true otherwise
        prediction = "";
        if(result.multiHandLandmarks().isEmpty()){
            return false;
        } else if(!classifier.isAvailable){
            Log.e(logTag, "Classifier is not available.. run open()");
            return false;
        }

        List<Float> landmarks = parseResult(result);
        normalize(landmarks);
        prediction = list[classifier.classify(landmarks)];
        if(predictionToCheck.equals("")){
            predictionToCheck = prediction;
            checkerDelayPhase = true;
            checker.postDelayed(checkDelay, checkerDelay);
        }
        return true;
    }

    private List<Float> parseResult(HandsResult result){
        List<Float> landmarks = new ArrayList<>();
        List<LandmarkProto.NormalizedLandmark> landmarkList = result.multiHandLandmarks().get(0).getLandmarkList();
        for(LandmarkProto.NormalizedLandmark landmark : landmarkList){
            //Log.e("test", "x=" + landmark.getX() + " | y=" + landmark.getY());
            landmarks.add(landmark.getX());
            landmarks.add(landmark.getY());
        }
        return landmarks;
    }

    public void normalize(List<Float> landmarks){
        float baseX = 0f, baseY = 0f;
        float temp1 = 0f, temp2 = 0f;
        for(int i=0; i < landmarks.size(); i++){
            if(i==0){
                temp1 = landmarks.get(i);
                landmarks.set(i, baseX);
            } else if(i==1){
                temp2 = landmarks.get(i);
                landmarks.set(i, baseY);
            } else if(i % 2 == 0){
                landmarks.set(i, landmarks.get(i)-temp1);
            } else {
                landmarks.set(i, landmarks.get(i)-temp2);
            }
        }

        float max = Math.abs(landmarks.get(0));
        for(float landmark: landmarks){
            if(Math.abs(landmark) > max){
                max = Math.abs(landmark);
            }
        }
        for(int i=0; i<landmarks.size(); i++){
            float temp = landmarks.get(i);
            landmarks.set(i, temp/max);
        }
    }


    /*==== =====*/

    static String[] list = new String[] {
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };

    public boolean matchFound = false;
    public boolean matchFoundDelayPhase = false;
    public boolean checkerDelayPhase = false;

    int checkerDelay = 1500;
    int matchDelay = 1500;
    Handler checker = new Handler();
    Handler matcher = new Handler();

    Runnable checkDelay = new Runnable() {
        @Override
        public void run() {
            if(prediction.equals(predictionToCheck)){
                match = predictionToCheck;
                matchFound = true;
                matchFoundDelayPhase = true;
                matcher.postDelayed(matchFoundDelay, matchDelay);
            }
            Log.e("CHECK", "Compared " + prediction + " to " + predictionToCheck);
            predictionToCheck = "";
            checkerDelayPhase = false;
        }
    };

    Runnable matchFoundDelay = new Runnable() {
        @Override
        public void run() {
            matchFoundDelayPhase = false;
        }
    };

}
