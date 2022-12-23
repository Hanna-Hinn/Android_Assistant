package com.example.ass_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_logo extends AppCompatActivity {

    private ImageView logo;
    private TextView txtTitle;
    private Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        setUpVariables();

        // this code is responsible for making the activity full screen, by removing the very top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logo.setAnimation(top);
        txtTitle.setAnimation(bottom);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_logo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }


    private void setUpVariables() {
        logo = findViewById(R.id.imgLogo);
        txtTitle = findViewById(R.id.txtTitle);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }
}