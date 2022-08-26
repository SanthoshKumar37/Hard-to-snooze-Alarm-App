package com.example.thefirstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TimePickerDialog timePickerDialog;
    private EditText alarmTitle, minuteValue;
    private TextView alarmTime, listAlarmTitle;
    private Spinner repeatOptions;
    private Calendar myCalendar = Calendar.getInstance();
    private RelativeLayout customLayout;
    private Button setAlarm;
    private ToggleButton setVibrate, setSnooze;
    private RecyclerView listLayout;
    private Random random = new Random();
//    private ArrayList<Alarm> alarms = new ArrayList<>();
//    private AlarmRecViewAdapter adapter = new AlarmRecViewAdapter(this);
    private CheckBox mon, tue, wed, thu, fri, sat, sun;
    private DBHandler dbHandler;
    int currentHour;
    int currentMinute;
    int i = 0;

//    private final BroadcastReceiver offReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int pos = intent.getIntExtra("POSITION", -1);
//            Toast.makeText(context, intent.getStringExtra("TEST") + pos, Toast.LENGTH_SHORT).show();
//
//            if(pos != -1) {
//                Alarm temp = null;
//                for(Alarm a : alarms)
//                    if(a.getAlarmId() == pos)
//                        temp = a;
//                if (temp != null && !temp.isRecurring()) {
//                    temp.setStarted(false);
////                    dbHandler.updateAlarm(temp);
//                }
//                adapter.setAlarms(alarms);
//
//                listLayout.setAdapter(adapter);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Initialization
        alarmTitle = findViewById(R.id.alarmTitle);
        alarmTime = findViewById(R.id.alarmTime);
        repeatOptions = findViewById(R.id.repeatOptions);
        customLayout = findViewById(R.id.customLayout);
        setAlarm = findViewById(R.id.addAlarmButton);
        setVibrate = findViewById(R.id.vibrateButton);
        setSnooze = findViewById(R.id.snoozeToggleButton);
        minuteValue = findViewById(R.id.minuteValue);
//        listLayout = findViewById(R.id.listLayout);
//        listAlarmTitle = findViewById(R.id.listAlarmTitle);
        mon = findViewById(R.id.cboxMon);
        tue = findViewById(R.id.cboxTue);
        wed = findViewById(R.id.cboxWed);
        thu = findViewById(R.id.cboxThu);
        fri = findViewById(R.id.cboxFri);
        sat = findViewById(R.id.cboxSat);
        sun = findViewById(R.id.cboxSun);

        dbHandler = new DBHandler(getApplicationContext());

        //Setting the Layout of the RecyclerView
//        listLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Displaying the already set Alarms
//        displaySetAlarms();

        //Spinner Values Initialization
        ArrayList<String> options = new ArrayList<>();
        options.add("Once");
        options.add("Daily");
        options.add("Weekdays");
        options.add("Custom");

        ArrayAdapter<String> optionAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                options
        );
        repeatOptions.setAdapter(optionAdapter);

        //Showing Custom checkboxes when it is selected
        repeatOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, repeatOptions.getSelectedItem().toString() + " Selected", Toast.LENGTH_SHORT).show();
                String type = repeatOptions.getSelectedItem().toString();

                if(type.equals("Custom"))
                    customLayout.setVisibility(View.VISIBLE);
                else
                    customLayout.setVisibility(View.GONE);

                if(!type.equals("Once")) {
                    if(type.equals("Daily"))
                        setCheckBox(true, true, true, true, true, true,true);
                    else if(type.equals("Weekdays"))
                        setCheckBox(true, true, true, true, true, false,false);
                    else
                        setCheckBox(false, false, false, false, false, false,false);
                }
                else
                    setCheckBox(true, true, true, true, true, true,true);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Setting Default Time Values When the time is not Clicked
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        alarmTime.setText(getTimeValue(currentHour, currentMinute));


        //Getting time values(hour, minute) when the clock time text view is clicked
        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar temp = Calendar.getInstance();
                currentHour = temp.get(Calendar.HOUR_OF_DAY);
                currentMinute = temp.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //setting the alarm time values in myCalendar
                        alarmTime.setText(getTimeValue(hourOfDay, minute));
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });

        //Toggling snooze or not
        setSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setSnooze.isChecked()){
                    minuteValue.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Snooze ON!", Toast.LENGTH_SHORT).show();
                }
                else{
                    minuteValue.setVisibility(View.GONE);
                }
            }
        });

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            if(offReceiver != null){
//                unregisterReceiver(offReceiver);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    protected void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("broadcastSample");
//        registerReceiver(offReceiver , filter);
//    }

    //When the Add button clicked
    public void addAlarmClicked(View view){
        Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();

        boolean recurring = !repeatOptions.getSelectedItem().toString().equals("Once");
        String title = alarmTitle.getText().toString();

//        if(alarms.isEmpty())
//            i = 0;
//        else
//            i = alarms.get(alarms.size() - 1).getAlarmId() + 1;

        Intent homeIntent = getIntent();
        i = homeIntent.getIntExtra("ALARM_ID", -1);
        Toast.makeText(this, "I: " + i, Toast.LENGTH_SHORT).show();

        Alarm newAlarm = new Alarm(i++, title, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), setVibrate.isChecked(), setSnooze.isChecked(), recurring, true, false, mon.isChecked(), tue.isChecked(), wed.isChecked(), thu.isChecked(),fri.isChecked(), sat.isChecked(), sun.isChecked(), minuteValue.getText().toString());
        newAlarm.schedule(getApplicationContext());

        //Adding value to the database
        dbHandler.addNewAlarm(newAlarm);

        //Recycler View
//        alarms.add(newAlarm);
//        adapter.setAlarms(alarms);
//        listLayout.setAdapter(adapter);
        Intent activity = new Intent(this, HomeActivity.class);
        activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(activity);
        finish();
    }

//    private void displaySetAlarms(){
//        alarms = dbHandler.readAllAlarms();
//        if(!alarms.isEmpty()) {
//            listAlarmTitle.setVisibility(View.VISIBLE);
//            i = alarms.get(alarms.size() - 1).getAlarmId() + 1;
//            Toast.makeText(this, "LAST id : " + i, Toast.LENGTH_SHORT).show();
//            adapter.setAlarms(alarms);
//            listLayout.setAdapter(adapter);
//        }
//        else
//            Toast.makeText(this, "Empty db", Toast.LENGTH_SHORT).show();
//
//    }

    private String getTimeValue(int hourVal, int minVal){
        String meridian;

        myCalendar.set(Calendar.HOUR_OF_DAY, hourVal);
        myCalendar.set(Calendar.MINUTE, minVal);
        myCalendar.set(Calendar.SECOND, 0);
        myCalendar.set(Calendar.MILLISECOND, 0);

        if(hourVal >= 12) {
            meridian = "PM";
            hourVal = hourVal - 12;
        }
        else
            meridian = "AM";
        if(hourVal == 0)
            hourVal = 12;

       return String.format("%02d : %02d ", hourVal, minVal) + meridian;
    }

    private void setCheckBox(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g){
        mon.setChecked(a);
        tue.setChecked(b);
        wed.setChecked(c);
        thu.setChecked(d);
        fri.setChecked(e);
        sat.setChecked(f);
        sun.setChecked(g);
    }

}