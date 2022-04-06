package com.trivtech.insight;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {

    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //Device
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    public static int getScreenHeightPercent(double percent){ return (int)(screenHeight*percent); }
    public static int getScreenWidthPercent(double percent){
        return (int)(screenWidth*percent);
    }

    public static void changeStatusBarColor(Activity activity, int color){
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static int getRandom(int max){ return getRandom(0, max); }
    public static int getRandom(int min, int max){
        return new Random().nextInt(max - min + 1) + min;
    }

    public static String getRandomText(){ return getRandomText(getRandom(110)); }
    public static String getRandomText(int length){
        final String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for(int i=0; i<length; i++){
            sb.append(ABC.charAt(rnd.nextInt(ABC.length())));
        }
        return sb.toString();
    }




    public static void nextActivity(AppCompatActivity currentActivity, Class nextActivity){
        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }
}
