package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SetupProfile extends AppCompatActivity {

    private String name;
    private String gender;
    private String age;
    private String height;
    private String weight;
    private String wakeUpTime;
    private String bedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
    }


    public void onClickConfirm(View view) {
        name        = ((EditText)findViewById(R.id.etName)).getText().toString();
        gender      = ((EditText)findViewById(R.id.etGender)).getText().toString();
        age         = ((EditText)findViewById(R.id.etAge)).getText().toString();
        height      = ((EditText)findViewById(R.id.etHeight)).getText().toString();
        weight      = ((EditText)findViewById(R.id.etWeight)).getText().toString();
        wakeUpTime  = ((EditText)findViewById(R.id.etWakeUpTime)).getText().toString();
        bedTime     = ((EditText)findViewById(R.id.etBedTime)).getText().toString();
        if(name.isEmpty() || gender.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty() || wakeUpTime.isEmpty() || bedTime.isEmpty())
        {
            return;
        }
        ((TextView)findViewById(R.id.textView)).setText(name + gender + age + height + weight + wakeUpTime + bedTime);
    }
}