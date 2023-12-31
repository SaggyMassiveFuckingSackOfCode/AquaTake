package com.example.aquatake;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment {

    DatabaseManager Database;

    private TextView tvName, tvGender, tvAge, tvHeight, tvWeight, tvWakeUpTime, tvBedTime, tvActivityLevel;
    private String[] profileData;
    public SecondFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        Database = new DatabaseManager(getContext());
        tvName = view.findViewById(R.id.tvName);
        tvGender = view.findViewById(R.id.tvGender);
        tvAge = view.findViewById(R.id.tvAge);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvWakeUpTime = view.findViewById(R.id.tvWakeUpTime);
        tvBedTime = view.findViewById(R.id.tvBedTime);
        tvActivityLevel = view.findViewById(R.id.tvActivityLevel);
        profileData = Database.getProfileData();
        view.findViewById(R.id.btnSetUp).setOnClickListener(view1 -> startActivity(new Intent(getActivity(), SetupProfile.class)));
        updateProfile();
        return view;
    }
    public void updateProfile(){
        tvName.setText("Name: " + profileData[0]);
        tvGender.setText("Gender: " + profileData[1]);
        tvAge.setText("Age: " + profileData[2]);
        tvHeight.setText("Height: " + profileData[3]);
        tvWeight.setText("Weight: " + profileData[4]);
        tvWakeUpTime.setText("Wake Up \nTime: " + profileData[5]);
        tvBedTime.setText("Bed Time: " + profileData[6]);
        tvActivityLevel.setText("Activity Level: " + profileData[7]);
    }
}
