package com.example.aquatake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity {

    private TextView tvProgress;
    private Button btnDrink;
    private EditText etAmount;
    private DatabaseManager db;

    private LinearLayout viewLayout;

    private Fade fade;
    private static int progress, goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = new DatabaseManager(this);
        progress = 0;
        goal = 0;
        tvProgress = findViewById(R.id.tvProgress);
        btnDrink = findViewById(R.id.btnDrink);
        etAmount = findViewById(R.id.etAmount);
        tvProgress.setText( progress + " / " + goal);
        viewLayout = findViewById(R.id.loRecords);
        fade = new Fade();
        fade.setDuration(300);

        displayRecords();
    }

    public void onClickDrink(View view) {
        try{
            if(TextUtils.isEmpty(etAmount.getText().toString())){
                Toast.makeText(this, "Please enter empty field(s)", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            String date = dateFormat.format(currentDate);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            String time = timeFormat.format(currentDate);
            db.insertIntakeRecord(date, time, Integer.parseInt(etAmount.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format for age, height, or weight", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        displayRecords();
    }

    public void displayRecords() {
        viewLayout.removeAllViews();
        String[][] intakeRecords = db.getIntakeRecordForToday();

        for (String[] record : intakeRecords) {
            LinearLayout recordLayout = new LinearLayout(this);
            recordLayout.setBackgroundColor(Color.parseColor("#808080"));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 20);
            recordLayout.setOrientation(LinearLayout.VERTICAL);
            recordLayout.setPadding(20, 20, 20, 20);
            recordLayout.setLayoutParams(layoutParams);
            TransitionManager.beginDelayedTransition(viewLayout, fade);

            TextView dateTextView = new TextView(this);
            dateTextView.setText("Date: " + record[0]);
            dateTextView.setTextColor(Color.parseColor("#000000"));
            recordLayout.addView(dateTextView);

            TextView timeTextView = new TextView(this);
            timeTextView.setText("Time: " + record[1]);
            timeTextView.setTextColor(Color.parseColor("#000000"));
            recordLayout.addView(timeTextView);

            TextView amountTextView = new TextView(this);
            amountTextView.setText("Amount: " + record[2]);
            amountTextView.setTextColor(Color.parseColor("#000000"));
            recordLayout.addView(amountTextView);

            viewLayout.addView(recordLayout);
        }
    }

    public void onClickClear(View view) {
        db.clearRecords();
    }
}