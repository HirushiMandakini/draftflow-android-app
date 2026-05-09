package com.mandakini.draftflow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mandakini.draftflow.R;

public class Onboarding1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding1);

        Button btnNext = findViewById(R.id.btnNext);
        TextView btnSkip = findViewById(R.id.btnSkip);

        btnNext.setOnClickListener(v ->
                startActivity(new Intent(this, Onboarding2Activity.class))
        );

        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}