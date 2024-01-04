package com.example.aquatake;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class WaterReminderManager extends JobIntentService {

    private final String CHANNEL_ID = "WaterReminderChannel";
    private final int notificationId = 1;
    public static final String PERMISSION_GRANTED_ACTION = "com.example.aquatake.PERMISSION_GRANTED";

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, WaterReminderManager.class, 123, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Check and request the VIBRATE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            // If not granted, request the VIBRATE permission
            requestVibrationPermission();
            return;
        }

        createNotificationChannel();
        showNotification();
    }

    private void requestVibrationPermission() {
        // Send a broadcast to request the VIBRATE permission
        Intent permissionIntent = new Intent(PERMISSION_GRANTED_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(permissionIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "Water Reminder";
            String descriptionText = "Reminds you to drink water";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionText);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        // Generate a unique notification ID using the current timestamp
        long uniqueId = System.currentTimeMillis();

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.alarm_clock_svgrepo_com__1_)
                    .setContentTitle("Drink Water Reminder")
                    .setContentText("Stay hydrated! It's time to drink water.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify((int) uniqueId, builder.build());
        } catch (SecurityException e) {
            // Handle SecurityException, for example, log the error
            Log.e("NotificationError", "SecurityException: " + e.getMessage());
        }
    }
}
