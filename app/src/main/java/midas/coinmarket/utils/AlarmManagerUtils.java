package midas.coinmarket.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import midas.coinmarket.controller.service.AlarmReceiver;

public class AlarmManagerUtils {
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    private Context mContext;

    public AlarmManagerUtils(Context context) {
        if (alarmMgr == null)
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mContext = context;
    }

    public static AlarmManagerUtils instanceAlarm(Context context) {
        return new AlarmManagerUtils(context);
    }

    public void setAlarmDaily() {
        Log.e("Alarm", "Create alarm");
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        // 8Am && 8PM. Call alarm.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }


    public void cancelAlarm() {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
