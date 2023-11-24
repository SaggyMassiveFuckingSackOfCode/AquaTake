package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        etName          = (EditText)findViewById(R.id.etName);
        etGender        = (EditText)findViewById(R.id.etName);
        etAge           = (EditText)findViewById(R.id.etName);
        etHeight        = (EditText)findViewById(R.id.etName);
        etWeight        = (EditText)findViewById(R.id.etName);
        etWakeUpTime    = (EditText)findViewById(R.id.etName);
        etBedTime       = (EditText)findViewById(R.id.etName);
    }

    public void onClickConfirm(View view) {
        name        = etName.getText().toString();
        gender      = etGender.getText().toString();
        age         = etAge.getText().toString();
        height      = etHeight.getText().toString();
        weight      = etWeight.getText().toString();
        wakeUpTime  = etWakeUpTime.getText().toString();
        bedTime     = etBedTime.getText().toString();
        if(name.isEmpty() || gender.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty() || wakeUpTime.isEmpty() || bedTime.isEmpty())
        {
            return;
        }
    }
    public void setTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(SetupProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                ((EditText) view).setText(hourOfDay + ":" + (minutes < 10 ? "0" + minutes : minutes));
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }
}