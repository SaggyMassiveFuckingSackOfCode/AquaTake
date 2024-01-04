package com.example.aquatake;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.Manifest;

import androidx.core.app.ActivityCompat;

public class PermissionRequestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[]{Manifest.permission.VIBRATE},
                123);
    }
}
