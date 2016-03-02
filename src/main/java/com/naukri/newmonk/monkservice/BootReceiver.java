package com.naukri.newmonk.monkservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naukri.newmonk.Monk;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Set alarm service, after restarting phone.
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            NewMonkLogger.info(LogMessage.MONK_START_BOOT_COMPLETE);
            //ExceptionSendingScheduler.scheduleExceptionSending(Monk.getContext());

//            ExceptionSendingScheduler.scheduleExceptionSending(context, Monk.getNewMonkConfiguration().interval);
        }
        /*else    // Start service to sync with server.
        {
            LogHandler.showLogs(LogMessage.SET_ALARM);
            Intent intent1 = new Intent(context, SendExceptionLog.class);
            //intent1.putExtra("Config", )
            context.startService(intent1);
        }*/
    }
}
