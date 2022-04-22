package com.trivtech.insight.activities;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.trivtech.insight.adapters.FragmentAdapter;
import com.trivtech.insight.R;
import com.trivtech.insight.ml.SignRecog;
import com.trivtech.insight.util.Utils;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static String logTag = "MainActivity";

    TabLayout tabLayout;
    ViewPager2 tabPages;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.changeBarColors(this, Color.BLACK);


        ViewGroup t = findViewById(R.id.main);
        LayoutTransition lt = new LayoutTransition();
        lt.enableTransitionType(LayoutTransition.CHANGING);
        t.setLayoutTransition(lt);

        tabLayout = findViewById(R.id.tabLayout);
        tabPages = findViewById(R.id.pages);


		//Style
        ConstraintLayout.LayoutParams params;
        params = (ConstraintLayout.LayoutParams) tabLayout.getLayoutParams();
        params.height = Utils.getScreenHeightPercent(.05); //5%
        tabLayout.setLayoutParams(params);


        params = (ConstraintLayout.LayoutParams) tabPages.getLayoutParams();
        params.height = Utils.getScreenHeightPercent(.95); //95%
        tabPages.setLayoutParams(params);
        tabPages.setClipToOutline(true);


        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        tabPages.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Translate"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPages.setCurrentItem(tab.getPosition());
            }
        });

        tabPages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public static boolean checkCameraPermission(Context context){
        String permission = Manifest.permission.CAMERA;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity){
        String permission = Manifest.permission.CAMERA;
        ActivityCompat.requestPermissions(activity, new String[]{permission}, 100);
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        if(requestCode == 100){
            Log.i(logTag, "Camera Permission Granted");
        } else {
            Toast.makeText(this, "Please allow access to the camera, in android settings", Toast.LENGTH_SHORT).show();
            Log.e(logTag, "Camera Permission Granted");
        }
    }

}