package com.example.thefirstone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm {
    private int alarmId;
    private String title;
    private int hour, minute;
    private boolean vibrate, snooze, recurring, started, isSnoozeAlarm;
    private boolean mon, tue, wed, thu, fri, sat, sun;
    private String snoozeVal;

    public Alarm(int alarmId, String title, int hour, int minute, boolean vibrate, boolean snooze, boolean recurring, boolean started, boolean isSnoozeAlarm, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun, String snoozeVal) {
        this.alarmId = alarmId;
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.vibrate = vibrate;
        this.snooze = snooze;
        this.recurring = recurring;
        this.started = started;
        this.isSnoozeAlarm = isSnoozeAlarm;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sun = sun;
        this.sat = sat;
        this.snoozeVal = snoozeVal;
    }

    public void schedule(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_TITLE", title);
        intent.putExtra("VIBRATE", vibrate);
        intent.putExtra("SNOOZE", snooze);

        intent.putExtra("MONDAY", mon);
        intent.putExtra("TUESDAY", tue);
        intent.putExtra("WEDNESDAY", wed);
        intent.putExtra("THURSDAY", thu);
        intent.putExtra("FRIDAY", fri);
        intent.putExtra("SATURDAY", sat);
        intent.putExtra("SUNDAY", sun);

        intent.putExtra("SNOOZE_ALARM", isSnoozeAlarm);

        if(snooze){
            intent.putExtra("SNOOZE_VAL", snoozeVal);
        }
        if(!isSnoozeAlarm)
            intent.putExtra("POSITION", alarmId);

        //creating pending intent which executes the AlarmReceiver class when it receives the broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        myCalendar.set(Calendar.HOUR_OF_DAY, hour);
        myCalendar.set(Calendar.MINUTE, minute);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);

        //Recycler View
//        alarms.add(new AlarmItem(title, recurring, true, i++, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), alarmManager, pendingIntent));
//        adapter.setAlarms(alarms);
//        listLayout.setAdapter(adapter);

        //If alarm time has already passed, increment day by 1
        if (myCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(context, "Alarm Set Tomorrow", Toast.LENGTH_SHORT).show();
            myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.get(Calendar.DAY_OF_MONTH) + 1);
        }


        //Setting Recurrent alarm for daily usage
        if(recurring) {
            Toast.makeText(context, title + " Recurring Alarm Set!!", Toast.LENGTH_SHORT).show();
            final long RUN_DAILY = 24 * 60 * 60 * 1000;
//            alarmManager.setRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    myCalendar.getTimeInMillis(),
//                    RUN_DAILY,
//                    pendingIntent
//            );
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent);
        }
        else {
            //Setting a Temporary Alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(context, title + " Alarm Set!!!", Toast.LENGTH_SHORT).show();
        }

        this.started = true;
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        this.started = false;

        Toast.makeText(context, title + " Alarm OFF", Toast.LENGTH_SHORT).show();
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isSnooze() {
        return snooze;
    }

    public void setSnooze(boolean snooze) {
        this.snooze = snooze;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isSnoozeAlarm() {
        return isSnoozeAlarm;
    }

    public void setSnoozeAlarm(boolean snoozeAlarm) {
        isSnoozeAlarm = snoozeAlarm;
    }

    public String getSnoozeVal() {
        return snoozeVal;
    }

    public void setSnoozeVal(String snoozeVal) {
        this.snoozeVal = snoozeVal;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThu() {
        return thu;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }
}
