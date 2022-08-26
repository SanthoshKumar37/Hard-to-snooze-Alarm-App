package com.example.thefirstone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {

    private Button dismissButton, submitButton, snoozeButton;
    private TextView question, showMistake, screenTitle;
    private EditText answerValue;
    int answer = 0;
    int snoozeVal = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alarm);

        dismissButton = findViewById(R.id.dismissButton);
        submitButton = findViewById(R.id.submitButton);
        snoozeButton = findViewById(R.id.snoozeButton);
        screenTitle = findViewById(R.id.screenTitle);
        question = findViewById(R.id.questionValue);
        showMistake = findViewById(R.id.mistakeArea);
        answerValue = findViewById(R.id.userAnswer);

        //getting values from the AlarmReceiver class
        Intent intent = getIntent();
        String alarmVal = intent.getStringExtra("ALARM_TITLE");
        String minVal = intent.getStringExtra("SNOOZE_VAL");
        boolean vibrate = intent.getBooleanExtra("VIBRATE", false);
        boolean snooze = intent.getBooleanExtra("SNOOZE", true);

        screenTitle.setText(alarmVal);

        //setting default value when snooze value is empty
        if(snooze) {
            snoozeVal = 1;
            if (!minVal.equals(""))
                snoozeVal = Integer.parseInt(minVal);
        }

        Random random = new Random();

        int a = random.nextInt(10);
        int b = random.nextInt(20) + 1;
        int choice = random.nextInt(4);
        switch (choice){
            case 0:
                question.setText(String.format("%d + %d = ?", a, b));
                answer = a + b;
                break;
            case 1:
                question.setText(String.format("%d * %d = ?", a, b));
                answer = a * b;
                break;
            case 2:
                question.setText(String.format("%d - %d = ?", a, b));
                answer = a - b;
                break;
            case 3:
                question.setText(String.format("%d // %d = ?", a, b));
                answer = a / b;
                break;
            default:
                break;
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usertxt = answerValue.getText().toString();
                int userAns;
                if(usertxt.equals(""))
                    userAns = 0;
                else
                    userAns = Integer.parseInt(usertxt);
                if(userAns == answer) {
                    dismissButton.setVisibility(View.VISIBLE);
                    showMistake.setVisibility(View.GONE);
                    if(snooze)
                        snoozeButton.setVisibility(View.VISIBLE);
                }
                else{
                    showMistake.setVisibility(View.VISIBLE);
                    dismissButton.setVisibility(View.GONE);
                    snoozeButton.setVisibility(View.GONE);
                    answerValue.setText("");
                }
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(getApplicationContext(), AlarmService.class);
                //Stopping the background services(AlarmService class)
                getApplicationContext().stopService(closeIntent);
                //closing the activity
                finishAndRemoveTask();
            }
        });

        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.MINUTE, snoozeVal);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                int id = random.nextInt();

                Toast.makeText(AlarmActivity.this, "Alarm Snoozed!!", Toast.LENGTH_SHORT).show();

                //setting new alarm when teh snooze button clicked
                Alarm snoozeAlarm = new Alarm(id, alarmVal, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), vibrate, snooze, false, true, true, true, true, true, true, true, true, true, minVal);
                snoozeAlarm.schedule(getApplicationContext());

                Intent closeIntent = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(closeIntent);
                finishAndRemoveTask();
            }
        });
    }
}