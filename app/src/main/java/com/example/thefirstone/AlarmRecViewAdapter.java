package com.example.thefirstone;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmRecViewAdapter extends RecyclerView.Adapter<AlarmRecViewAdapter.ViewHolder> {

    private ArrayList<Alarm> alarms = new ArrayList<>();
    private DBHandler dbHandler;

    private final Context context;
    private int i = 1;

    public AlarmRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        dbHandler = new DBHandler(context);
        
        Alarm tempAlarm = alarms.get(position);
        String alarmName = tempAlarm.getTitle();

        if(alarmName.equals(""))
            alarmName = "Default";

        //Getting Hour and Minute Values from the passed Calendar
        String meridian;
        Calendar myCalendar = Calendar.getInstance();
        int hourVal, minVal;
        hourVal = tempAlarm.getHour();
        minVal = tempAlarm.getMinute();

        myCalendar.set(Calendar.HOUR_OF_DAY, hourVal);
        myCalendar.set(Calendar.MINUTE, minVal);
        myCalendar.set(Calendar.SECOND, 0);

        if(hourVal >= 12) {
            meridian = "PM";
            hourVal = hourVal- 12;
        }
        else
            meridian = "AM";
        if(hourVal == 0)
            hourVal = 12;
        holder.listTime.setText(String.format("%02d : %02d ", hourVal, minVal) + meridian);
        //End

        //Setting Alarm Name
        holder.listAlarm.setText(alarmName);

        //Setting the Switch button
        holder.cancelAlarm.setChecked(tempAlarm.isStarted());

        //Setting Alarm Type
        String type;
        if(tempAlarm.isRecurring())
            type = "  |  Recurring";
        else
            type = "  |  Once";
        holder.listType.setText(type);

        holder.cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!holder.cancelAlarm.isChecked()){
                   tempAlarm.cancelAlarm(context);
                   tempAlarm.setStarted(false);
               }
               else{
                   //If alarm time has already passed, increment day by 1
                   tempAlarm.setStarted(true);
                   tempAlarm.schedule(context);
               }
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
               dbHandler.updateAlarm(tempAlarm);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "clicked Delete Button", Toast.LENGTH_SHORT).show();

                if (holder.cancelAlarm.isChecked()){
                    tempAlarm.cancelAlarm(context);
                    tempAlarm.setStarted(false);
                }

                dbHandler.deleteAlarm(tempAlarm);
                alarms.remove(tempAlarm);

                if(alarms.isEmpty()){
                    Intent backToHome = new Intent("broadcastSample");
                    Toast.makeText(context, "Alarm Empty", Toast.LENGTH_SHORT).show();
                    backToHome.putExtra("TEST", "Created from Delete Button");
                    backToHome.putExtra("ALARM_EMPTY", true);
                    context.sendBroadcast(backToHome);
                }

                notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }


    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView listAlarm, listType, listTime;
        private SwitchMaterial cancelAlarm;
        private ImageButton deleteButton;
        //TODO create delete button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            listAlarm = itemView.findViewById(R.id.listAlarmName);
            listTime = itemView.findViewById(R.id.listAlarmTime);
            listType = itemView.findViewById(R.id.listAlarmType);
            cancelAlarm = itemView.findViewById(R.id.cancelButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
