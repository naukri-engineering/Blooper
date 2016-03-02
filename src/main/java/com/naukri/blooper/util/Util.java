package com.naukri.blooper.util;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.metadata.ExceptionModel;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.StringTokenizer;

import static android.app.AlarmManager.RTC_WAKEUP;

public class Util {
    /**
     * Check string is null or length is greater than 0.
     *
     * @param str
     * @return trimmed string or blank string.
     */
    public static String getString(String str) {
        if (str != null && str.length() != 0)
            return str.trim();

        return "";
    }

    /**
     * Prepare Exception model from Throwable object.
     *
     * @param ex the throwable
     * @param className name of class
     * @return ExceptionModel
     */
    public static ExceptionModel prepareExceptionModel(Throwable ex, String className) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));

        ExceptionModel exceptionModel = new ExceptionModel();

        String clsName = getString(className);

        exceptionModel.tag = getTag(clsName);
        exceptionModel.count = "1";
        exceptionModel.timestamp = (System.currentTimeMillis() / 1000);
        exceptionModel.type = (ex != null) ? ex.getClass().getName() : Constants.UNKNOWN_TYPE;
        exceptionModel.message = ex.getMessage();
        exceptionModel.code = "0";
        exceptionModel.file = clsName;
        exceptionModel.line = 0;
        exceptionModel.stackTrace = errors.toString();

        //LogHandler.showLogs(errors.toString());
        BlooperLogger.info(errors.toString());

        return exceptionModel;
    }

    /**
     * Prepare Exception model from Throwable object.
     *
     * @param error
     * @param className
     * @return
     */
    public static ExceptionModel prepareExceptionModel(String error, String className) {
        ExceptionModel exceptionModel = new ExceptionModel();

        String clsName = getString(className);

        exceptionModel.tag = getTag(clsName);
        exceptionModel.count = "1";
        exceptionModel.timestamp = (System.currentTimeMillis() / 1000);
        exceptionModel.type = Constants.MESSAGE_TYPE;
        exceptionModel.message = "";
        exceptionModel.code = "";
        exceptionModel.file = clsName;
        exceptionModel.line = 0;
        exceptionModel.stackTrace = error;

        //LogHandler.showLogs(error);
        BlooperLogger.info(error);

        return exceptionModel;
    }

    /**
     * Get tag from class name.
     *
     * @param clsName take class name.
     * @return String tag.
     */
    private static String getTag(String clsName) {
        String tag = Constants.UNKNOWN_TAG;

        if (!clsName.isEmpty()) {
            StringTokenizer stringTokenizerTag = new StringTokenizer(clsName, ".");
            int count = stringTokenizerTag.countTokens();

            if (count != 1) {
                for (int i = 0; i < (count - 1); i++) {
                    if (stringTokenizerTag.hasMoreTokens())
                        stringTokenizerTag.nextToken();
                }
            }

            if (stringTokenizerTag.hasMoreTokens())      // Get Tag.
                tag = stringTokenizerTag.nextToken();
        }
        return tag;
    }

    /**
     * Initiate service to sync the database logged exception with server
     *
     * @param context  app level context.
     * @param cls      give class name that extends IntentService.
     * @param interval sync re-start time. sync will restart after this given interval
     */
    public static void setInexactAlarm(Context context, Class<? extends Service> cls,
                                       long interval) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null) {
            pendingIntent = PendingIntent
                    .getService(context, 0, intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alaramManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alaramManager.setInexactRepeating(RTC_WAKEUP,
                    System.currentTimeMillis(), interval, pendingIntent);
        }
    }

    public static boolean isSuccessResponse(int responseCode)
    {
        if(responseCode == HttpURLConnection.HTTP_ACCEPTED || responseCode == HttpURLConnection.HTTP_NO_CONTENT)
            return true;
        else
            return false;
    }



}
