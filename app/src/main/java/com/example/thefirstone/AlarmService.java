package com.example.thefirstone;

import static com.example.thefirstone.App.CHANNEL_1_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    boolean vibrate;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Creating media player component and starting it
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        vibrate = intent.getBooleanExtra("VIBRATE", false);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        String title = intent.getStringExtra("ALARM_TITLE");
        String snoozeVal = intent.getStringExtra("SNOOZE_VAL");
        boolean snooze = intent.getBooleanExtra("SNOOZE", true);

        Random random = new Random();
        int id = random.nextInt();
        Intent activityIntent = new Intent(this, AlarmActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.putExtra("ALARM_TITLE", title);
        activityIntent.putExtra("VIBRATE", vibrate);
        activityIntent.putExtra("SNOOZE", snooze);
        activityIntent.putExtra("SNOOZE_VAL", snoozeVal);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_simple_alarm)
                .setContentTitle(title)
                .setContentText("Wake Up! Wake Up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(id, notification);

        if(vibrate) {
            Toast.makeText(this, "Vibrating...", Toast.LENGTH_SHORT).show();
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        if(vibrate)
            vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
