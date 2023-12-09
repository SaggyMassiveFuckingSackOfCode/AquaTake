package com.example.aquatake;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ThirdFragment extends Fragment {
    Spinner spinner;
    DatabaseManager db;
    LinearLayout loView;

    public ThirdFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        spinner = view.findViewById(R.id.DateSelect);
        loView = view.findViewById(R.id.loView);
        db = new DatabaseManager(requireContext());
        populateSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDate = (String) parentView.getItemAtPosition(position);
                updateLoView(selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        return view;
    }

    private void populateSpinner() {
        String[][] intakeRecords = db.getIntakeRecord();
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Set<String> uniqueDates = new HashSet<>();
        for (String[] record : intakeRecords) {
            uniqueDates.add(record[0]);
        }
        dateList.addAll(uniqueDates);

        // Sort the dateList in ascending order
        Collections.sort(dateList, new Comparator<String>() {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

            @Override
            public int compare(String date1, String date2) {
                try {
                    return sdf.parse(date1).compareTo(sdf.parse(date2));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                dateList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void updateLoView(String selectedDate) {
        loView.removeAllViews();
        String[][] intakeRecords = db.getIntakeRecordForDate(selectedDate);
        for (String[] record : intakeRecords) {
            LinearLayout recordLayout = new LinearLayout(requireContext());
            recordLayout.setOrientation(LinearLayout.VERTICAL);

            TextView timeTextView = new TextView(requireContext());
            String formattedTime = record[1].substring(0, 5);
            timeTextView.setText(String.format("%s", formattedTime));

            TextView amountTextView = new TextView(requireContext());
            amountTextView.setText(String.format("      Amount: %s mL", record[2]));

            recordLayout.addView(timeTextView);
            recordLayout.addView(amountTextView);
            loView.addView(recordLayout);
        }

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(loView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.start();
    }
}
