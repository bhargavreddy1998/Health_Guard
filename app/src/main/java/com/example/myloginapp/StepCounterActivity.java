package com.example.myloginapp;

import android.content.ContentValues;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView headingTextView;
    private Button finishWalkButton;
    private TextView stepCountTextView;
    private Button playPauseButton;
    private int stepCount = 0;
    private boolean isTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        // Find the views by their IDs
        headingTextView = findViewById(R.id.headingTextView);
        stepCountTextView = findViewById(R.id.stepCountTextView);
        playPauseButton = findViewById(R.id.playPauseButton);

        // Set initial texts and button state
        headingTextView.setText("Step Counter");
        updateStepCount();
        updatePlayPauseButton();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Handle the case where the device doesn't have a step counter sensor
            // You may want to use other methods for step counting in such cases
        }

        // Set click listener for the play/pause button
        playPauseButton.setOnClickListener(v -> toggleTracking());
        finishWalkButton = findViewById(R.id.finishWalkButton);
        finishWalkButton.setOnClickListener(v -> finishWalk());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && isTracking) {
            stepCount = (int) event.values[0];
            updateStepCount();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    private void updateStepCount() {
        // Set the text of the step count TextView with the current step count
        stepCountTextView.setText("Steps: " + stepCount);
    }

    private void updatePlayPauseButton() {
        playPauseButton.setText(isTracking ? "Pause" : "Play");
    }

    private void toggleTracking() {
        isTracking = !isTracking;
        updatePlayPauseButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-register the sensor listener when the activity is resumed
        if (sensorManager != null) {
            Sensor stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener when the activity is paused to save resources
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void finishWalk() {
        // Assuming dbHelper3 is an instance of DatabaseHelper3
        StepCounterDatabaseHelper dbHelper3 = new StepCounterDatabaseHelper(this);

        // Upload step count to the database
        uploadStepCount(dbHelper3, stepCount);

        // Reset step count to 0
        stepCount = 0;
        updateStepCount();

        // Show toast indicating success
        Toast.makeText(this, "Step count uploaded successfully", Toast.LENGTH_SHORT).show();

        // Return to the redirect page
        finish();
    }

    private void uploadStepCount(StepCounterDatabaseHelper dbHelper3, int stepCount) {
        // Assuming "steps" is the table name and "step_count" is the column name in the database
        ContentValues values = new ContentValues();
        values.put("step_count", stepCount);

        // Insert the step count into the database
        dbHelper3.getWritableDatabase().insert("steps", null, values);
    }
}
