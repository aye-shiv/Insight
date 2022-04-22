package com.trivtech.insight.activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.trivtech.insight.R;
import com.trivtech.insight.util.Utils;

public class SplashScreenActivity extends AppCompatActivity {
    boolean PREPARED = false;
    VideoView splash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Utils.changeBarColors(this, Color.BLACK);

        splash = findViewById(R.id.video);
        String URLPath = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        splash.setVideoURI(Uri.parse(URLPath));
        splash.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                PREPARED = true;
            }
        });
        splash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.reset();
                Utils.nextActivity(SplashScreenActivity.this, MainActivity.class);
            }
        });
        splash.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PREPARED && !splash.isPlaying())
            Utils.nextActivity(SplashScreenActivity.this, MainActivity.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        splash.pause();
    }
}
