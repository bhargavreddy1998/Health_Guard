package com.example.myloginapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StepCounterDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "step_counter_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STEPS = "steps";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STEP_COUNT = "step_count";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STEPS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_STEP_COUNT
            + " integer not null);";

    public StepCounterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add any upgrade logic here
    }
}