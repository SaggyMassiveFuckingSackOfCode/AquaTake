package com.example.aquatake;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
                + COLUMN_AGE            + " TEXT, "
                + COLUMN_HEIGHT         + " REAL, "
                + COLUMN_WEIGHT         + " REAL, "
                + COLUMN_WAKEUP_TIME    + " TEXT, "
                + COLUMN_BEDTIME        + " TEXT);";
    private static final String CREATE_TABLE_INTAKE_RECORD =
            "CREATE TABLE " + TABLE_INTAKE_RECORD + " ("
                + COLUMN_INTAKE_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE           + " TEXT, "
                + COLUMN_TIME           + " TEXT, "
                + COLUMN_AMOUNT         + " REAL);";

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
}
