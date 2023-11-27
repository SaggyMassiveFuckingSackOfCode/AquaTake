package com.example.aquatake;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseManager(this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(!db.CheckExistingProfile()){
                    startActivity(new Intent(MainActivity.this, SetupProfile.class));
                } else {
                    startActivity(new Intent(MainActivity.this, Home.class));
                }
                finish();
            }
        }, 3000);
    }
}