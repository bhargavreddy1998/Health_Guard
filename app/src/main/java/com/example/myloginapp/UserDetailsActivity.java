package com.example.myloginapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {

    private UserDetailsDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        dbHelper = new UserDetailsDatabaseHelper(this);

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(view -> saveUserDetails());
    }

    private void saveUserDetails() {
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextAge = findViewById(R.id.editTextAge);
        EditText editTextGender = findViewById(R.id.editTextGender);
        EditText editTextMobile = findViewById(R.id.editTextMobile);
        EditText editTextHeight = findViewById(R.id.editTextHeight);
        EditText editTextWeight = findViewById(R.id.editTextWeight);

        String name = editTextName.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());
        String gender = editTextGender.getText().toString();
        String mobile = editTextMobile.getText().toString();
        double height = Double.parseDouble(editTextHeight.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());

        // Save to the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDetailsDatabaseHelper.COLUMN_NAME, name);
        values.put(UserDetailsDatabaseHelper.COLUMN_AGE, age);
        values.put(UserDetailsDatabaseHelper.COLUMN_GENDER, gender);
        values.put(UserDetailsDatabaseHelper.COLUMN_MOBILE, mobile);
        values.put(UserDetailsDatabaseHelper.COLUMN_HEIGHT, height);
        values.put(UserDetailsDatabaseHelper.COLUMN_WEIGHT, weight);

        long newRowId = db.insert(UserDetailsDatabaseHelper.TABLE_NAME, null, values);

        // Clear text boxes
        editTextName.setText("");
        editTextAge.setText("");
        editTextGender.setText("");
        editTextMobile.setText("");
        editTextHeight.setText("");
        editTextWeight.setText("");

        if (newRowId != -1) {
            showToast("Insertion successful");
            Intent intent = new Intent(UserDetailsActivity.this, RedirectPage.class);
            startActivity(intent);
        } else {
            showToast("Insertion failed");
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
