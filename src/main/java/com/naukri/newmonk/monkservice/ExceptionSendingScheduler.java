package com.naukri.newmonk.monkservice;

import android.content.Context;

import com.naukri.newmonk.util.Util;


public class ExceptionSendingScheduler {
    /**
     * Set alarm for sync with server.
     */
    public static void scheduleExceptionSending(Context context, long interval) {
        Util.setInexactAlarm(context, SendExceptionLog.class, interval);

        /*AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long time =  System.currentTimeMillis();

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time,
                AlarmManager.INTERVAL_HOUR, pendingIntent);*/
    }
}
