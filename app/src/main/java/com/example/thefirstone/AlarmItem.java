package com.example.thefirstone;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.Calendar;

public class AlarmItem {
    private String name;
    private boolean recurring, started;
    private int id, hour, minute;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public AlarmItem(String name, boolean recurring, boolean started, int id, int hour, int minute, AlarmManager alarmManager, PendingIntent pendingIntent) {
        this.name = name;
        this.recurring = recurring;
        this.started = started;
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.alarmManager = alarmManager;
        this.pendingIntent = pendingIntent;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }


    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public void setAlarmManager(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
}
