package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;

    String username;
    String password;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        db = new DatabaseManager(this);

        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            Log.d("DEBUG", ""+ db.Login(username, password));
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill in empty fields", Toast.LENGTH_SHORT).show();
            } else if(db.Login(username, password)){
                if(!db.hasExistingProfile()){
                    startActivity(new Intent(Login.this, SetupProfile.class));
                } else {
                    startActivity(new Intent(Login.this, Home.class));
                }
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(view -> {
            startActivity((new Intent(Login.this, Register.class)));
        });
    }

}