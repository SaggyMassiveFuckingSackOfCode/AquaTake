package com.example.aquatake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "waterIntake.db";
    private static final int DATABASE_VERSION = 1;

    //USER TABLE
    private static final String TABLE_USERPROFILE   = "user_profile";
    private static final String COLUMN_NAME         = "name";
    private static final String COLUMN_GENDER       = "gender";
    private static final String COLUMN_AGE          = "age";
    private static final String COLUMN_HEIGHT       = "height";
    private static final String COLUMN_WEIGHT       = "weight";
    private static final String COLUMN_WAKEUP_TIME  = "wakeup_time";
    private static final String COLUMN_BEDTIME      = "bedtime";

    //WATER INTAKE TABLE
    private static final String TABLE_INTAKE_RECORD = "intake_record";
    private static final String COLUMN_INTAKE_ID    = "id";
    private static final String COLUMN_DATE         = "date";
    private static final String COLUMN_TIME         = "time";
    private static final String COLUMN_AMOUNT       = "amount";

    //SQL STATEMENTS FOR CREATING TABLES
    private static final String CREATE_TABLE_USERPROFILE =
            "CREATE TABLE " + TABLE_USERPROFILE + " ("
                + COLUMN_NAME           + " TEXT, "
                + COLUMN_GENDER         + " TEXT, "
                + COLUMN_AGE            + " INTEGER, "
                + COLUMN_HEIGHT         + " INTEGER, "
                + COLUMN_WEIGHT         + " INTEGER, "
                + COLUMN_WAKEUP_TIME    + " TEXT, "
                + COLUMN_BEDTIME        + " TEXT);";
    private static final String CREATE_TABLE_INTAKE_RECORD =
            "CREATE TABLE " + TABLE_INTAKE_RECORD + " ("
                + COLUMN_INTAKE_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE           + " TEXT, "
                + COLUMN_TIME           + " TEXT, "
                + COLUMN_AMOUNT         + " INTEGER);";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERPROFILE);
        sqLiteDatabase.execSQL(CREATE_TABLE_INTAKE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERPROFILE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INTAKE_RECORD);
        onCreate(sqLiteDatabase);
    }

    public void onProfileSetup(Context context, String name, String gender, int age, int height, int weight, String bedtime, String wakeupTime) {
        SQLiteDatabase db = getWritableDatabase();

        // Check if a profile already exists
        if (CheckExistingProfile()) {
            // Update the existing profile
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, name);
            contentValues.put(COLUMN_GENDER, gender);
            contentValues.put(COLUMN_AGE, age);
            contentValues.put(COLUMN_HEIGHT, height);
            contentValues.put(COLUMN_WEIGHT, weight);
            contentValues.put(COLUMN_BEDTIME, bedtime);
            contentValues.put(COLUMN_WAKEUP_TIME, wakeupTime);

            // Update the existing row in the user_profile table
            db.update(TABLE_USERPROFILE, contentValues, null, null);
            Toast.makeText(context, "PROFILE UPDATED", Toast.LENGTH_LONG).show();
        } else {
            // Insert a new profile if none exists
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, name);
            contentValues.put(COLUMN_GENDER, gender);
            contentValues.put(COLUMN_AGE, age);
            contentValues.put(COLUMN_HEIGHT, height);
            contentValues.put(COLUMN_WEIGHT, weight);
            contentValues.put(COLUMN_BEDTIME, bedtime);
            contentValues.put(COLUMN_WAKEUP_TIME, wakeupTime);

            db.insert(TABLE_USERPROFILE, null, contentValues);
            Toast.makeText(context, "PROFILE CREATED", Toast.LENGTH_LONG).show();
        }

        // Close the database after completing the operations
        db.close();
    }



    public boolean CheckExistingProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_USERPROFILE);
        db.close();
        return count > 0;
    }

    public String[][] getProfileData() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERPROFILE, null);
        if(cursor.moveToFirst()){
            do{
                list.add(new String[]{
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                });
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list.toArray(new String[0][0]);
    }
}
