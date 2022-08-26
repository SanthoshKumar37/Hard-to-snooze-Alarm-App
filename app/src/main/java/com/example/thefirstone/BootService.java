package com.example.thefirstone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BootService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        ArrayList<Alarm> alarms = dbHandler.readAllAlarms();
        for(Alarm a : alarms){
            if(a.isStarted())
                a.schedule(getApplicationContext());
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
