package com.example.aquatake;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupProfile extends AppCompatActivity {

    private String name;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String wakeUpTime;
    private String bedTime;
    private EditText etName;
    private EditText etGender;
    private EditText etAge;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etWakeUpTime;
    private EditText etBedTime;

    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        etName      = findViewById(R.id.etName);
        etGender    = findViewById(R.id.etGender);
        etAge       = findViewById(R.id.etAge);
        etHeight    = findViewById(R.id.etHeight);
        etWeight    = findViewById(R.id.etWeight);
        etWakeUpTime = findViewById(R.id.etWakeUpTime);
        etBedTime   = findViewById(R.id.etBedTime);

        db = new DatabaseManager(this);

        if (db.CheckExistingProfile()) {
            String[][] profileInfo = db.getProfileData();
            etName.setText(profileInfo[0][0]);
            etGender.setText(profileInfo[0][1]);
            etAge.setText(profileInfo[0][2]);
            etHeight.setText(profileInfo[0][3]);
            etWeight.setText(profileInfo[0][4]);
            etWakeUpTime.setText(profileInfo[0][5]);
            etBedTime.setText(profileInfo[0][6]);
        }
    }
    public void onClickConfirm(View view) {
        this.name        = etName.getText().toString();
        this.gender      = etGender.getText().toString();
        this.age         = etAge.getText().toString();
        this.height      = etHeight.getText().toString();
        this.weight      = etWeight.getText().toString();
        this.wakeUpTime  = etWakeUpTime.getText().toString();
        this.bedTime     = etBedTime.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age) ||
                TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) ||
                TextUtils.isEmpty(wakeUpTime) || TextUtils.isEmpty(bedTime)) {
            Toast.makeText(this, "PLEASE FILL EMPTY FIELDS", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            db.onProfileSetup(this,
                    this.name,
                    this.gender,
                    Integer.parseInt(this.age),
                    Integer.parseInt(this.height),
                    Integer.parseInt(this.weight),
                    this.wakeUpTime,
                    this.bedTime);
        } catch (NumberFormatException e){
        }

        startActivity(new Intent(SetupProfile.this, Home.class));
    }
    public void setTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(SetupProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String text = hourOfDay + ":" + (minutes < 10 ? "0" + minutes : minutes);
                ((EditText) view).setText(text);
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }
}