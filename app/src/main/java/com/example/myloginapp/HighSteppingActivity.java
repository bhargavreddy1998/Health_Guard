package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HighSteppingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_stepping);
        String sidePlankBenefits = getString(R.string.high_stepping);
        String sidePlankDuration = getString(R.string.high_stepping_duration);
        String sidePlankCaloriesBurned = getString(R.string.high_stepping_calories_burned);

        TextView benefitsTextView = findViewById(R.id.highSteppingBenefitsTextView);
        TextView durationTextView = findViewById(R.id.highSteppingDurationTextView);
        TextView caloriesTextView = findViewById(R.id.highSteppingCaloriesTextView);

        benefitsTextView.setText(sidePlankBenefits);
        durationTextView.setText(sidePlankDuration);
        caloriesTextView.setText(sidePlankCaloriesBurned);
        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(view -> {
            // Redirect to ExerciseActivity
            Intent intent = new Intent(HighSteppingActivity.this, ExerciseActivity.class);
            startActivity(intent);
        });
    }
}
