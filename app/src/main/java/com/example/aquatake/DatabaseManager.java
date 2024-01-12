package com.example.aquatake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "waterIntake.db";
    private static final int DATABASE_VERSION = 2;

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

    //CREDENTIAL TABLE
    private static final String TABLE_CREDENTIALS = "credentials";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    //SQL STATEMENTS FOR CREATING TABLES
    private static final String CREATE_TABLE_USERPROFILE =
            "CREATE TABLE " + TABLE_USERPROFILE + " ("
                    + COLUMN_USERNAME           + " TEXT, "
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
                    + COLUMN_USERNAME       + " TEXT, "
                    + COLUMN_DATE           + " TEXT, "
                    + COLUMN_TIME           + " TEXT, "
                    + COLUMN_AMOUNT         + " INTEGER);";

    private static final String CREATE_TABLE_CREDENTIALS =
            "CREATE TABLE " + TABLE_CREDENTIALS + " ("
                    + COLUMN_USERNAME       + " TEXT PRIMARY KEY, "
                    + COLUMN_PASSWORD       + " TEXT);";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERPROFILE);
        sqLiteDatabase.execSQL(CREATE_TABLE_INTAKE_RECORD);
        sqLiteDatabase.execSQL(CREATE_TABLE_CREDENTIALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERPROFILE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INTAKE_RECORD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
        onCreate(sqLiteDatabase);
    }


    public boolean hasExistingProfile() {
        SQLiteDatabase db = this.getReadableDatabase();

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        Cursor cursor = db.query(
                TABLE_USERPROFILE,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{loggedInUsername},
                null, null, null
        );

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public void UpdateUserProfile(String username, String name, String gender, int age, int height, int weight, String wakeupTime, String bedtime, String activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the profile for the current username exists
        Cursor cursor = db.query(TABLE_USERPROFILE,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_WAKEUP_TIME, wakeupTime);
        contentValues.put(COLUMN_BEDTIME, bedtime);
        contentValues.put(COLUMN_ACTIVITY_LEVEL, activityLevel);

        if (cursor.moveToFirst()) {
            // Update the existing row for the current username
            db.update(TABLE_USERPROFILE, contentValues, COLUMN_USERNAME + "=?", new String[]{username});
        } else {
            // Insert a new row if the profile for the current username doesn't exist
            contentValues.put(COLUMN_USERNAME, username);
            db.insert(TABLE_USERPROFILE, null, contentValues);
        }

        cursor.close();
        db.close();
    }

    public void insertIntakeRecord(String date, String time, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, loggedInUsername);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_AMOUNT, amount);

        db.insert(TABLE_INTAKE_RECORD, null, contentValues);
        db.close();
    }


    public String[] getProfileData() {
        SQLiteDatabase db  = this.getReadableDatabase();
        String[] profileData = new String[8];

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        Cursor cursor = db.query(
                TABLE_USERPROFILE,
                new String[]{
                        COLUMN_NAME,
                        COLUMN_GENDER,
                        COLUMN_AGE,
                        COLUMN_HEIGHT,
                        COLUMN_WEIGHT,
                        COLUMN_WAKEUP_TIME,
                        COLUMN_BEDTIME,
                        COLUMN_ACTIVITY_LEVEL
                },
                COLUMN_USERNAME + "=?",
                new String[]{loggedInUsername},
                null, null, null
        );

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

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        Cursor cursor = db.query(
                TABLE_INTAKE_RECORD,
                new String[]{COLUMN_DATE, COLUMN_TIME, COLUMN_AMOUNT},
                COLUMN_USERNAME + "=?",
                new String[]{loggedInUsername},
                null, null, null
        );

        if(cursor.moveToFirst()){
            do{
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                });
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list.toArray(new String[0][0]);
    }

    public String[][] getIntakeRecordForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String[]> list = new ArrayList<>();

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        Cursor cursor = db.query(
                TABLE_INTAKE_RECORD,
                new String[]{COLUMN_DATE, COLUMN_TIME, COLUMN_AMOUNT},
                COLUMN_USERNAME + "=? AND " + COLUMN_DATE + "=?",
                new String[]{loggedInUsername, date},
                null, null, null
        );

        if(cursor.moveToFirst()) {
            do {
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))
                });
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list.toArray(new String[0][0]);
    }

    public int getTotalWaterIntakeForToday() {
        int totalIntake = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        String[][] record = getIntakeRecordForDate(todayDate);
        int[] amounts = new int[record.length];

        for (int i = 0; i < record.length; i++) {
            amounts[i] = Integer.parseInt(record[i][2]);
        }

        for (int amount : amounts) {
            totalIntake += amount;
        }

        Log.d("Debug", "Today's Date: " + todayDate);
        Log.d("Debug", "Logged-In Username: " + loggedInUsername);
        Log.d("Debug", "Number of Records: " + record.length);

        return totalIntake;
    }




    public int getRecommendedIntake() {
        SQLiteDatabase db = this.getReadableDatabase();
        int recommendedIntake = 0;

        String loggedInUsername = AmbotSaImongLubot.loggedInUsername;

        String[] profileData = getProfileData();

        if (profileData != null && profileData.length >= 3) {
            int age = Integer.parseInt(profileData[2]);
            int weight = Integer.parseInt(profileData[4]);
            String activityLevel = profileData[7];

            double ageFactor = 0.5;
            double activityFactor = activityLevel.equals("Sedentary") ? 30 :
                    activityLevel.equals("Lightly Active") ? 40 :
                            activityLevel.equals("Moderately Active") ? 50 :
                                    activityLevel.equals("Very Active") ? 60 : 35;

            recommendedIntake = (int) ((weight * activityFactor) + (age * ageFactor));
        }

        db.close();
        return recommendedIntake;
    }

    public boolean isUsernameTaken(Context context, String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CREDENTIALS,
                new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        if(count > 0){
            Toast.makeText(context, "Username is already taken", Toast.LENGTH_SHORT).show();
        }
        return count > 0;
    }
    public void registerCredential(Context context,String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(isUsernameTaken(context, username)){
            return;
        }

        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_CREDENTIALS, null, contentValues);
        db.close();
    }
    public boolean Login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CREDENTIALS,
                new String[]{COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null, null);

        boolean isValid = cursor.getCount() > 0;

        cursor.close();
        db.close();
        AmbotSaImongLubot.loggedInUsername = username;
        AmbotSaImongLubot.isLoggedIn = true;
        return isValid;
    }
    public void logout() {
        AmbotSaImongLubot.loggedInUsername = null;
        AmbotSaImongLubot.isLoggedIn = false;
    }
}
