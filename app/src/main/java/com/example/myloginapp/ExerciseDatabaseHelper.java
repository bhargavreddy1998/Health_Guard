package com.example.myloginapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "water_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WATER_CONSUMPTION = "water_consumption";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CONSUMPTION = "consumption_ml";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_WATER_CONSUMPTION + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_CONSUMPTION + " INTEGER NOT NULL);";

    public ExerciseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades if needed
    }
}

