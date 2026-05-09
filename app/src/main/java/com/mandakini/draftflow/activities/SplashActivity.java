package com.mandakini.draftflow.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View progressFill = findViewById(R.id.progressFill);

        ValueAnimator animator = ValueAnimator.ofInt(0, 180);
        animator.setDuration(SPLASH_DELAY);
        animator.addUpdateListener(animation -> {
            int width = (int) animation.getAnimatedValue();
            progressFill.getLayoutParams().width = width;
            progressFill.requestLayout();
        });
        animator.start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}