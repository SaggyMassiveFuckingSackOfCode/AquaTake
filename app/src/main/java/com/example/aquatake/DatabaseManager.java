package com.example.aquatake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private static final String COLUMN_ACTIVITY_LEVEL = "activity_level";

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
                    + COLUMN_BEDTIME        + " TEXT, "
                    + COLUMN_ACTIVITY_LEVEL + " TEXT);";

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

    public boolean hasExistingProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERPROFILE, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    public void insertSingleUserProfile(String name, String gender, int age, int height, int weight, String wakeupTime, String bedtime, String activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERPROFILE, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_WAKEUP_TIME, wakeupTime);
        contentValues.put(COLUMN_BEDTIME, bedtime);
        contentValues.put(COLUMN_ACTIVITY_LEVEL, activityLevel);
        db.insert(TABLE_USERPROFILE, null, contentValues);
        db.close();
    }
    public void insertIntakeRecord(String date, String time, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_AMOUNT, amount);
        db.insert(TABLE_INTAKE_RECORD, null, contentValues);
        db.close();
    }

    public String[] getProfileData() {
        SQLiteDatabase db  = this.getReadableDatabase();
        String[] profileData = new String[8];
        Cursor cursor      = db.rawQuery("SELECT * FROM " + TABLE_USERPROFILE, null);
        if (cursor.moveToFirst()) {
            profileData[0] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            profileData[1] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
            profileData[2] = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
            profileData[3] = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)));
            profileData[4] = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
            profileData[5] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WAKEUP_TIME));
            profileData[6] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BEDTIME));
            profileData[7] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_LEVEL));
        }
        cursor.close();
        db.close();
        return profileData;
    }
    public String[][] getIntakeRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String[]> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INTAKE_RECORD, null);
        if(cursor.moveToFirst()){
            do{
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                });
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list.toArray(new String[0][0]);
    }
    public String[][] getIntakeRecordForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String[]> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_INTAKE_RECORD +
                        " WHERE " + COLUMN_DATE + " = ?",
                new String[]{date});

        if(cursor.moveToFirst()) {
            do {
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                });
            } while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return list.toArray(new String[0][0]);
    }
    public int getTotalWaterIntakeForToday() {
        int totalIntake = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());
        String[][] record = getIntakeRecordForDate(todayDate);
        int[] amounts = new int[record.length];
        for (int i = 0; i < record.length; i++) {
            amounts[i] = Integer.parseInt(record[i][2]);
        }
        for (int amount : amounts) {
            totalIntake += amount;
        }
        return totalIntake;
    }


    public int getRecommendedIntake() {
        SQLiteDatabase db = this.getReadableDatabase();
        int recommendedIntake = 0;

        String[] profileData = getProfileData();

        if (profileData != null && profileData.length >= 3) {
            int age = Integer.parseInt(profileData[2]);
            int weight = Integer.parseInt(profileData[4]);
            String activityLevel = profileData[7];

            double ageFactor = 0.5;
            double activityFactor = activityLevel == "Sedentary" ? 30 :
                    activityLevel == "Lightly Active" ? 40 :
                    activityLevel == "Moderately Active" ? 50 :
                    activityLevel == "Very Active" ? 60 : 35;

            recommendedIntake = (int) ((weight * activityFactor) + (age * ageFactor));
        }

        db.close();
        return recommendedIntake;
    }
}
