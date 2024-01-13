package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    String username;
    String password;
    DatabaseManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseManager(this);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);

        findViewById(R.id.btnRegisterRegister).setOnClickListener(view -> {
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            String confirmPassword = ((EditText)findViewById(R.id.etRegisterConfirmPassword)).getText().toString();

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill in empty fields", Toast.LENGTH_SHORT).show();
            } else if (db.isUsernameTaken(this, username)) {
                Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show();
            } else if (password.equals(confirmPassword)) {
                db.registerCredential(this, username, password);
                etUsername.setText("");
                etPassword.setText("");
                ((EditText)findViewById(R.id.etRegisterConfirmPassword)).setText("");
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tvLogin).setOnClickListener(view -> startActivity(new Intent(Register.this, Login.class)));
    }


}