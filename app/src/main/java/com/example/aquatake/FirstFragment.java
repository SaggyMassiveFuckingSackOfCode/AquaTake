package com.example.aquatake;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstFragment extends Fragment {
    DatabaseManager db;
    private EditText etAmount;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private int totalIntake , intakeGoal;

    public FirstFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        db = new DatabaseManager(getContext());
        etAmount = view.findViewById(R.id.etAmount);
        tvProgress = view.findViewById(R.id.tvProgress);
        progressBar = view.findViewById(R.id.progressBar);
        updateDisplay();

        view.findViewById(R.id.btnDrink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(TextUtils.isEmpty(etAmount.getText().toString())){
                        Toast.makeText(getActivity(), "Please fill empty field(s)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Calendar calendar = Calendar.getInstance();
                    Date currentDate = calendar.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    String date = dateFormat.format(currentDate);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    String time = timeFormat.format(currentDate);
                    Home.Database.insertIntakeRecord(date, time, Integer.parseInt(etAmount.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Invalid number format for age, height, or weight", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                etAmount.setText("");
                updateDisplay();
            }
        });



        return view;
    }
    private void updateDisplay(){
        totalIntake = db.getTotalWaterIntakeForToday();
        intakeGoal = db.getRecommendedIntake();
        tvProgress.setText(totalIntake + "ml / "+ intakeGoal +" ml");
        int progress = (100 * totalIntake)/intakeGoal;

        animateProgressBar(progress);

        progressBar.setProgress(progress);
    }
    private void animateProgressBar(int newProgress) {
        // Use ObjectAnimator to animate the progress bar
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", newProgress);
        progressAnimator.setDuration(500);  // Adjust the duration as needed (in milliseconds)
        progressAnimator.setInterpolator(new AccelerateInterpolator());
        progressAnimator.start();
    }
}
