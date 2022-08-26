package com.example.thefirstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "alarm_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "my_alarms";

    //16 COLUMNS
    private static final String ID_COL = "alarm_id";
    private static final String TITLE_COL = "title";
    private static final String HOUR_COL = "hour";
    private static final String MIN_COL = "minute";
    private static final String VIBRATE_COL = "vibrate";
    private static final String SNOOZE_COL = "snooze";
    private static final String RECURRING_COL = "recurring";
    private static final String STARTED_COL = "started";
    private static final String MONDAY_COL = "mon";
    private static final String TUESDAY_COL = "tue";
    private static final String WEDNESDAY_COL = "wed";
    private static final String THURSDAY_COL = "thu";
    private static final String FRIDAY_COL = "fri";
    private static final String SATURDAY_COL = "sat";
    private static final String SUNDAY_COL = "sun";
    private static final String SNOOZE_VAL_COL = "snooze_val";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, "
                + TITLE_COL + " TEXT,"
                + HOUR_COL + " INTEGER,"
                + MIN_COL + " INTEGER,"
                + VIBRATE_COL + " INTEGER,"
                + SNOOZE_COL + " INTEGER,"
                + RECURRING_COL + " INTEGER,"
                + STARTED_COL + " INTEGER,"
                + MONDAY_COL + " INTEGER,"
                + TUESDAY_COL + " INTEGER,"
                + WEDNESDAY_COL + " INTEGER,"
                + THURSDAY_COL + " INTEGER,"
                + FRIDAY_COL + " INTEGER,"
                + SATURDAY_COL + " INTEGER,"
                + SUNDAY_COL + " INTEGER,"
                + SNOOZE_VAL_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewAlarm(Alarm alarm) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL, alarm.getAlarmId());
        values.put(TITLE_COL, alarm.getTitle());
        values.put(HOUR_COL, alarm.getHour());
        values.put(MIN_COL, alarm.getMinute());
        values.put(VIBRATE_COL, boolToInt(alarm.isVibrate()));
        values.put(SNOOZE_COL, boolToInt(alarm.isSnooze()));
        values.put(RECURRING_COL, boolToInt(alarm.isRecurring()));
        values.put(STARTED_COL, boolToInt(alarm.isStarted()));
        values.put(MONDAY_COL, boolToInt(alarm.isMon()));
        values.put(TUESDAY_COL, boolToInt(alarm.isTue()));
        values.put(WEDNESDAY_COL, boolToInt(alarm.isWed()));
        values.put(THURSDAY_COL, boolToInt(alarm.isThu()));
        values.put(FRIDAY_COL, boolToInt(alarm.isFri()));
        values.put(SATURDAY_COL, boolToInt(alarm.isSat()));
        values.put(SUNDAY_COL, boolToInt(alarm.isSun()));
        values.put(SNOOZE_VAL_COL, alarm.getSnoozeVal());

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<Alarm> readAllAlarms() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor alarmCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (alarmCursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                alarmArrayList.add(new Alarm(alarmCursor.getInt(0), alarmCursor.getString(1), alarmCursor.getInt(2), alarmCursor.getInt(3), intToBool(alarmCursor.getInt(4)), intToBool(alarmCursor.getInt(5)), intToBool(alarmCursor.getInt(6)), intToBool(alarmCursor.getInt(7)), false, intToBool(alarmCursor.getInt(8)), intToBool(alarmCursor.getInt(9)), intToBool(alarmCursor.getInt(10)), intToBool(alarmCursor.getInt(11)), intToBool(alarmCursor.getInt(12)), intToBool(alarmCursor.getInt(13)), intToBool(alarmCursor.getInt(14)), alarmCursor.getString(15)));
            } while (alarmCursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        alarmCursor.close();
        return alarmArrayList;
    }
    public void updateAlarm(Alarm alarm) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL, alarm.getAlarmId());
        values.put(TITLE_COL, alarm.getTitle());
        values.put(HOUR_COL, alarm.getHour());
        values.put(MIN_COL, alarm.getMinute());
        values.put(VIBRATE_COL, boolToInt(alarm.isVibrate()));
        values.put(SNOOZE_COL, boolToInt(alarm.isSnooze()));
        values.put(RECURRING_COL, boolToInt(alarm.isRecurring()));
        values.put(STARTED_COL, boolToInt(alarm.isStarted()));
        values.put(MONDAY_COL, boolToInt(alarm.isMon()));
        values.put(TUESDAY_COL, boolToInt(alarm.isTue()));
        values.put(WEDNESDAY_COL, boolToInt(alarm.isWed()));
        values.put(THURSDAY_COL, boolToInt(alarm.isThu()));
        values.put(FRIDAY_COL, boolToInt(alarm.isFri()));
        values.put(SATURDAY_COL, boolToInt(alarm.isSat()));
        values.put(SUNDAY_COL, boolToInt(alarm.isSun()));
        values.put(SNOOZE_VAL_COL, alarm.getSnoozeVal());

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_NAME, values, "alarm_id=?", new String[]{String.valueOf(alarm.getAlarmId())});
        db.close();
    }

    public void deleteAlarm(Alarm alarm) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "alarm_id=?", new String[]{String.valueOf(alarm.getAlarmId())});
        db.close();
    }

    public boolean intToBool(int var){
        return var == 1;
    }

    public int boolToInt(boolean b){
        return b ? 1 : 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
