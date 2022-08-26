package com.example.thefirstone;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "SIMPLE_ALARM";
    public static final String CHANNEL_2_ID = "CALENDAR_ALARM";
    public static final String CHANNEL_3_ID = "NOTIFICATION_ALARM";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Simple Alarm",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is the notification channel created for the simple alarms.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
