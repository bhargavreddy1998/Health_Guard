package com.example.myloginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper2 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_database";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "health";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_HEART_RATE = "heartRate";
    public static final String COLUMN_RESPIRATORY_RATE = "respiratoryRate";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TIMESTAMP + " TIMESTAMP PRIMARY KEY, " +
                    COLUMN_HEART_RATE + " TEXT, " +
                    COLUMN_RESPIRATORY_RATE + " TEXT" +
                    ")";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertData(String heartBeat, String respiratoryRate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        values.put(COLUMN_TIMESTAMP, currentTimeStamp);
        values.put(COLUMN_HEART_RATE, heartBeat);
        values.put(COLUMN_RESPIRATORY_RATE, respiratoryRate);
        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public Cursor queryAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void clearOldData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
