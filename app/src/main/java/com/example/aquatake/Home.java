package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    DatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DatabaseManager(this);

    }


    public void onClickGoToSetUp(View view) {
        startActivity(new Intent(Home.this, SetupProfile.class));
    }
    public void checkProfiles(View view) {
        Toast.makeText(this, "" + db.CheckExistingProfile(), Toast.LENGTH_SHORT).show();
    }
}