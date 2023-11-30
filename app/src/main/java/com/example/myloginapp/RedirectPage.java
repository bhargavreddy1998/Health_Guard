package com.example.myloginapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RedirectPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);

        Button buttonMeasureRate = findViewById(R.id.buttonMeasureRate);
        Button buttonAddProfile = findViewById(R.id.buttonAddProfile);
        Button buttonTrackSteps = findViewById(R.id.trackStepsButton);
        Button buttonExercise = findViewById(R.id.exerciseButton);

        buttonMeasureRate.setOnClickListener(view -> navigateToMeasureRate());
        buttonAddProfile.setOnClickListener(view -> navigateToAddProfile());
        buttonTrackSteps.setOnClickListener(view -> navigateToStepCounter());
        buttonExercise.setOnClickListener(view -> navigateToExercise());
    }

    private void navigateToMeasureRate() {
        Intent intent = new Intent(this, MeasureHeartAndRespRateActivity.class);
        startActivity(intent);
    }

    private void navigateToAddProfile() {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        startActivity(intent);
    }

    private void navigateToStepCounter() {
        Intent intent = new Intent(this, StepCounterActivity.class);
        startActivity(intent);
    }

    private void navigateToExercise() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
}
