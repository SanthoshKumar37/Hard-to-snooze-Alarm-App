package com.example.thefirstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class AlarmFragment extends Fragment {

    private TextView infoText;
    private ImageButton setAlarmButton;
    private RecyclerView listLayout;
    private Random random = new Random();
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private AlarmRecViewAdapter adapter;
    private LinearLayout listLayoutWrapper;
    private DBHandler dbHandler;
    int i = 0;

    private final BroadcastReceiver offReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("POSITION", -1);
            boolean empty = intent.getBooleanExtra("ALARM_EMPTY", false);
            Toast.makeText(context, intent.getStringExtra("TEST") + pos, Toast.LENGTH_SHORT).show();

            if(pos != -1) {
                Alarm temp = null;
                for(Alarm a : alarms)
                    if(a.getAlarmId() == pos)
                        temp = a;
                if (temp != null && !temp.isRecurring()) {
                    temp.setStarted(false);
//                    dbHandler.updateAlarm(temp);
                }
                adapter.setAlarms(alarms);

                listLayout.setAdapter(adapter);
            }
            else if(empty){
                infoText.setVisibility(View.VISIBLE);
                listLayoutWrapper.setGravity(Gravity.CENTER);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

       adapter = new AlarmRecViewAdapter(requireContext());

        setAlarmButton = view.findViewById(R.id.setAlarmButton);
        listLayout = view.findViewById(R.id.listLayout);
        infoText = view.findViewById(R.id.infoText);
        listLayoutWrapper = view.findViewById(R.id.listLayoutWrapper);

        dbHandler = new DBHandler(requireContext());

        //Setting the Layout of the RecyclerView
        listLayout.setLayoutManager(new LinearLayoutManager(requireContext()));

        //Displaying the already set Alarms
        displaySetAlarms();



        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarms.isEmpty())
                    i = 0;
                else
                    i = alarms.get(alarms.size() - 1).getAlarmId() + 1;
                Intent activityIntent = new Intent(requireContext(), MainActivity.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.putExtra("ALARM_ID", i);
//                getContext().startActivity(activityIntent);
                startActivity(activityIntent);
            }
        });

        return view;
    }

    private void displaySetAlarms(){
        alarms = dbHandler.readAllAlarms();
        if(!alarms.isEmpty()) {
            infoText.setVisibility(View.GONE);
            listLayoutWrapper.setGravity(Gravity.NO_GRAVITY);
            i = alarms.get(alarms.size() - 1).getAlarmId() + 1;
            Toast.makeText(requireContext(), "LAST id : " + i, Toast.LENGTH_SHORT).show();
            adapter.setAlarms(alarms);
            listLayout.setAdapter(adapter);
        }
        else {
            infoText.setVisibility(View.VISIBLE);
            listLayoutWrapper.setGravity(Gravity.CENTER);
            Toast.makeText(requireContext(), "Empty db", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            requireContext().unregisterReceiver(offReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("broadcastSample");
        requireContext().registerReceiver(offReceiver , filter);
    }

}
