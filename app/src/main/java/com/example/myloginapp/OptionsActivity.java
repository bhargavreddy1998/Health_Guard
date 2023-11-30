package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    private Button watchButton;
    private Button phoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        watchButton = findViewById(R.id.watchButton);
        phoneButton = findViewById(R.id.phoneButton);

        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For now, do nothing when Watch is selected
            }
        });

        phoneButton.setOnClickListener(v -> redirectToMainActivity());
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();  // Finish the current activity to remove it from the back stack
    }
}

