package com.example.thefirstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean snooze = intent.getBooleanExtra("SNOOZE", true);
        boolean isSnoozeAlarm = intent.getBooleanExtra("SNOOZE_ALARM", false);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = "Alarm Reboot";
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            Intent intentService = new Intent(context, BootService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentService);
            } else {
                context.startService(intentService);
            }
        }
        else {
            //it only executes the service when the scheduled alarm is on today or when it is a snoozed Alarm
            if (checkToday(intent) || (snooze && isSnoozeAlarm)) {

                //getting the passed values
                String title = intent.getStringExtra("ALARM_TITLE");
                boolean vibrate = intent.getBooleanExtra("VIBRATE", false);
                int pos = intent.getIntExtra("POSITION", -1);
                String snoozeVal = intent.getStringExtra("SNOOZE_VAL");

                //if the title edit text is empty
                if (title.equals(""))
                    title = "Alarm";

                //Passing Values to Main Activity so we can switch off the once type alarm through broadcast
                //when the app is open
                Intent backToMain = new Intent("broadcastSample");
                backToMain.putExtra("TEST", "Hello from Alarm Receiver");
                backToMain.putExtra("POSITION", pos);
                context.sendBroadcast(backToMain);

                //Changing values in the DB when the alarm is closed
                DBHandler dbHandler = new DBHandler(context);
                ArrayList<Alarm> alarms = dbHandler.readAllAlarms();

                Alarm temp = null;
                for (Alarm a : alarms)
                    if (a.getAlarmId() == pos)
                        temp = a;
                if (temp != null && !temp.isRecurring()) {
                    temp.setStarted(false);
                    dbHandler.updateAlarm(temp);
                }
                //Setting Recurring Alarms
                if(temp != null && temp.isRecurring()){
                    temp.schedule(context);
                }

                //passing required values to the Alarm Activity
//            Intent i = new Intent();
//            i.setClassName("com.example.thefirstone", "com.example.thefirstone.AlarmActivity");
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra("ALARM_TITLE", title);
//            i.putExtra("VIBRATE", vibrate);
//            i.putExtra("SNOOZE", snooze);
//            if (snooze) {
//                i.putExtra("SNOOZE_VAL", snoozeVal);
//            }
//            context.startActivity(i);

                //passing required values to the AlarmService class(for Background usage purpose)
                Toast.makeText(context, title + "! Wake UP!", Toast.LENGTH_LONG).show();
                Intent intentService = new Intent(context, AlarmService.class);
                intentService.putExtra("ALARM_TITLE", title);
                intentService.putExtra("VIBRATE", vibrate);
                intentService.putExtra("POSITION", pos);
                intentService.putExtra("SNOOZE", snooze);
                intentService.putExtra("SNOOZE_VAL", snoozeVal);
                //starting the background service for the application
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentService);
                } else {
                    context.startService(intentService);
                }
            } else //when the scheduled alarm is not supposed to ring today
                Toast.makeText(context, "NOT today", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkToday(Intent intent){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                return intent.getBooleanExtra("MONDAY", false);
            case Calendar.TUESDAY:
                return intent.getBooleanExtra("TUESDAY", false);
            case Calendar.WEDNESDAY:
                return intent.getBooleanExtra("WEDNESDAY", false);
            case Calendar.THURSDAY:
                return intent.getBooleanExtra("THURSDAY", false);
            case Calendar.FRIDAY:
                return intent.getBooleanExtra("FRIDAY", false);
            case Calendar.SATURDAY:
                return intent.getBooleanExtra("SATURDAY", false);
            case Calendar.SUNDAY:
                return intent.getBooleanExtra("SUNDAY", false);
        }
        return false;
    }
}
