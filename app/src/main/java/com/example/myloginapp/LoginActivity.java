package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);

        // Set a listener for the login button
        loginButton.setOnClickListener(v -> redirectToMainActivity());
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
        finish();  // Finish the current activity to remove it from the back stack
    }
}

