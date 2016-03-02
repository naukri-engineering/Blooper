package com.naukri.newmonk;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.naukri.newmonk.annotation.NewMonkCrashReport;
import com.naukri.newmonk.config.NewMonkConfiguration;
import com.naukri.newmonk.database.DatabaseOpenHelper;
import com.naukri.newmonk.database.ExceptionLogger;
import com.naukri.newmonk.exception.ExceptionHandler;
import com.naukri.newmonk.exception.NewMonkException;
import com.naukri.newmonk.factory.NewMonkFactory;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;
import com.naukri.newmonk.metadata.ExceptionModel;
import com.naukri.newmonk.monkservice.ExceptionSendingScheduler;
import com.naukri.newmonk.network.WebServiceManager;
import com.naukri.newmonk.util.Util;

/**
 * Class to initialize crash/exception reporting
 */
public class Monk {
    private static ExceptionHandler exceptionHandler = null;
    private static Context context;
    private static NewMonkConfiguration newMonkConfiguration;
    private static boolean isNewMonkInitialized = false;

    private Monk() {
    }

    /**
     * Initialize new monk in a given application.
     * Put this call in Application onCreate() method
     *
     * @param application your application class
     * @throws NewMonkException if the mandatory params are missing
     */
    public static void initNewMonk(Application application) throws NewMonkException {
        if (isNewMonkInitialized) {
            NewMonkLogger.info(LogMessage.MONK_SDK_REINIT);
            return;
        }
        NewMonkCrashReport newMonkCrashReport = application.getClass().getAnnotation(NewMonkCrashReport.class);
        if (newMonkCrashReport != null) {
            initNewMonk(application, new NewMonkConfiguration(newMonkCrashReport));
        } else {
            throw new NewMonkException(LogMessage.NO_ANNOTATION);
        }
    }

    /**
     * Initialize new monk in a given application.
     * Put this call in Application onCreate() method
     *
     * @param application application
     * @param config      NewMonkConfiguration to set up the configuration manually
     * @throws NewMonkException if the mandatory params are missing
     */
    public static void initNewMonk(Application application, NewMonkConfiguration config) throws NewMonkException {
        if (isNewMonkInitialized) {
            NewMonkLogger.info(LogMessage.MONK_SDK_REINIT);
            return;
        }
        boolean isValidConfiguration = validateConfiguration(config);
        if (isValidConfiguration) {
            newMonkConfiguration = config;
            context = application.getApplicationContext();
            // if this is debug build then log enable. else disable logs for release build.
            Boolean debuggable = isDebuggableMode(context);
            NewMonkLogger.isLogEnabled = (newMonkConfiguration.debug && debuggable);
            DatabaseOpenHelper.getDbInstance(context);
            NewMonkLogger.info(LogMessage.DATABASE_INIT);
            WebServiceManager.setUrl(newMonkConfiguration.uri);

            if (exceptionHandler == null)
                exceptionHandler = new ExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());

            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
            NewMonkLogger.info(LogMessage.EXCEPTION_HANDLER_INIT);
            ExceptionSendingScheduler.scheduleExceptionSending(getContext(), newMonkConfiguration.interval);
            NewMonkLogger.info(LogMessage.MONK_SDK_INIT);
            isNewMonkInitialized = true;
        }
    }

    @NonNull
    /**
     * check app is in debuggable mode or not.
     */
    protected static Boolean isDebuggableMode(Context ctx) {
        Boolean debuggable = true;
        PackageManager pm = ctx.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Exception e) {
            NewMonkLogger.info(LogMessage.ERROR_DEBUG_FLAG);
        }
        return debuggable;
    }

    /**
     * Validate mandatory params in configuration
     *
     * @param config NewMonkConfiguration which contains the config params
     * @return validation status
     * @throws NewMonkException if any of the params is invalid
     */
    private static boolean validateConfiguration(NewMonkConfiguration config) throws NewMonkException {
        if (TextUtils.isEmpty(config.uri)) {
            throw new NewMonkException(LogMessage.NO_URI);
        } else if (TextUtils.isEmpty(config.appId)) {
            throw new NewMonkException(LogMessage.NO_APPID);
        }
        return true;
    }

    /**
     * log caught exception into NewMonk.
     *
     * @param className Name of class where exception occured.
     * @param exception caught exception object
     */
    public static void logException(String className, Exception exception) {

        if (isNewMonkInitialized) {
            try {
                if (exception != null) {
                    ExceptionModel exceptionModel = Util.prepareExceptionModel(exception, className);
                    ExceptionLogger exceptionLogger = NewMonkFactory.createExceptionLogger();
                    exceptionLogger.logException(exceptionModel);
                } else {
                    NewMonkLogger.error(LogMessage.NO_EXCEPTION_TO_SEND);
                }
            } catch (Exception ex) {
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, ex);
            }
        } else {
            NewMonkLogger.error(LogMessage.ERROR_INIT_MONK_SDK);
        }
    }

    /**
     * log Message into NewMonk.
     *
     * @param className current class name
     * @param msg       any type of message log in NewMonk
     */
    public static void logMessage(String className, String msg) {

        if (isNewMonkInitialized) {
            try {
                if (msg != null) {
                    ExceptionModel exceptionModel = Util.prepareExceptionModel(msg, className);
                    ExceptionLogger exceptionLogger = NewMonkFactory.createExceptionLogger();
                    exceptionLogger.logException(exceptionModel);
                }
            } catch (Exception ex) {
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, ex);
            }
        } else {
            NewMonkLogger.error(LogMessage.ERROR_INIT_MONK_SDK);
        }
    }

    /**
     * Access the new monk configuration to read and write config params
     *
     * @return NewMonkConfiguration which holds the config params
     */
    public static NewMonkConfiguration getNewMonkConfiguration() {
        if (!isNewMonkInitialized) {
            NewMonkLogger.error(LogMessage.ERROR_INIT_MONK_SDK);
        }
        return newMonkConfiguration;
    }

    public static Context getContext() {
        return context;
    }
}
