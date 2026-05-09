package com.mandakini.draftflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;

public class Onboarding2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding2);

        Button btnNext = findViewById(R.id.btnNext);
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView btnSkip = findViewById(R.id.btnSkip);

        btnBack.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v ->
                startActivity(new Intent(this, Onboarding3Activity.class))
        );

        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}