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
        etName          = findViewById(R.id.etName);
        etGender        = findViewById(R.id.etGender);
        etAge           = findViewById(R.id.etAge);
        etHeight        = findViewById(R.id.etHeight);
        etWeight        = findViewById(R.id.etWeight);
        etWakeUpTime    = findViewById(R.id.etWakeUpTime);
        etBedTime       = findViewById(R.id.etBedTime);
        db = new DatabaseManager(this);
        if (db.hasExistingProfile()) {
            String[] profileData = db.getProfileData();
            etName.setText(profileData[0]);
            etGender.setText(profileData[1]);
            etAge.setText(profileData[2]);
            etHeight.setText(profileData[3]);
            etWeight.setText(profileData[4]);
            etWakeUpTime.setText(profileData[5]);
            etBedTime.setText(profileData[6]);
        }
    }

    public void onClickConfirm(View view) {
        name        = etName.getText().toString().trim();
        gender      = etGender.getText().toString().trim();
        age         = etAge.getText().toString().trim();
        height      = etHeight.getText().toString().trim();
        weight      = etWeight.getText().toString().trim();
        wakeUpTime  = etWakeUpTime.getText().toString().trim();
        bedTime     = etBedTime.getText().toString().trim();
        try {
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(age)
                    || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)
                    || TextUtils.isEmpty(wakeUpTime) || TextUtils.isEmpty(bedTime)) {
                Toast.makeText(this, "Please fill in missing field(s)", Toast.LENGTH_SHORT).show();
                return;
            }
            db.insertSingleUserProfile(
                    name,
                    gender,
                    Integer.parseInt(age),
                    Integer.parseInt(height),
                    Integer.parseInt(weight),
                    wakeUpTime,
                    bedTime);
            startActivity(new Intent(SetupProfile.this, Home.class));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format for age, height, or weight", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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