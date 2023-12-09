package com.example.aquatake;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstFragment extends Fragment {
    private EditText etAmount;


    public FirstFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        view.findViewById(R.id.btnDrink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(TextUtils.isEmpty(etAmount.getText().toString())){
                        Toast.makeText(getActivity(), "Please enter empty field(s)", Toast.LENGTH_SHORT).show();
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
            }
        });
        etAmount = view.findViewById(R.id.etAmount);
        return view;
    }
    public void onClickDrink(View view) {
    }
}
