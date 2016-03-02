package com.naukri.blooper.monkservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.logger.LogMessage;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Set alarm service, after restarting phone.
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            BlooperLogger.info(LogMessage.BLOOPER_START_BOOT_COMPLETE);
            //ExceptionSendingScheduler.scheduleExceptionSending(Blooper.getContext());

//            ExceptionSendingScheduler.scheduleExceptionSending(context, Blooper.getBlooperConfiguration().interval);
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
