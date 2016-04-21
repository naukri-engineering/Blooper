package com.naukri.blooper;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.naukri.blooper.annotation.BlooperCrashReport;
import com.naukri.blooper.config.BlooperConfiguration;
import com.naukri.blooper.database.DatabaseOpenHelper;
import com.naukri.blooper.database.ExceptionLogger;
import com.naukri.blooper.exception.ExceptionHandler;
import com.naukri.blooper.exception.BlooperException;
import com.naukri.blooper.factory.BlooperFactory;
import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.logger.LogMessage;
import com.naukri.blooper.metadata.ExceptionModel;
import com.naukri.blooper.monkservice.ExceptionSendingScheduler;
import com.naukri.blooper.network.WebServiceManager;
import com.naukri.blooper.util.Util;

/**
 * Class to initialize crash/exception reporting
 */
public class Blooper {
    private static ExceptionHandler exceptionHandler = null;
    private static Context context;
    private static BlooperConfiguration blooperConfiguration;
    private static boolean isBlooperInitialized = false;

    private Blooper() {
    }

    /**
     * Initialize blooper in a given application.
     * Put this call in Application onCreate() method
     *
     * @param application your application class
     * @throws BlooperException if the mandatory params are missing
     */
    public static void initBlooper(Application application) throws BlooperException {
        if (isBlooperInitialized) {
            BlooperLogger.info(LogMessage.BLOOPER_SDK_REINIT);
            return;
        }
        BlooperCrashReport blooperCrashReport = application.getClass().getAnnotation(BlooperCrashReport.class);
        if (blooperCrashReport != null) {
            initBlooper(application, new BlooperConfiguration(blooperCrashReport));
        } else {
            throw new BlooperException(LogMessage.NO_ANNOTATION);
        }
    }

    /**
     * Initialize blooper in a given application.
     * Put this call in Application onCreate() method
     *
     * @param application application
     * @param config      BlooperConfiguration to set up the configuration manually
     * @throws BlooperException if the mandatory params are missing
     */
    public static void initBlooper(Application application, BlooperConfiguration config) throws BlooperException {
        if (isBlooperInitialized) {
            BlooperLogger.info(LogMessage.BLOOPER_SDK_REINIT);
            return;
        }
        boolean isValidConfiguration = validateConfiguration(config);
        if (isValidConfiguration) {
            blooperConfiguration = config;
            context = application.getApplicationContext();
            // if this is debug build then log enable. else disable logs for release build.
            Boolean debuggable = isDebuggableMode(context);
            BlooperLogger.isLogEnabled = (blooperConfiguration.debug && debuggable);
            DatabaseOpenHelper.getDbInstance(context);
            BlooperLogger.info(LogMessage.DATABASE_INIT);
            WebServiceManager.setUrl(blooperConfiguration.uri);

            if (exceptionHandler == null)
                exceptionHandler = new ExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());

            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
            BlooperLogger.info(LogMessage.EXCEPTION_HANDLER_INIT);
            ExceptionSendingScheduler.scheduleExceptionSending(getContext(), blooperConfiguration.interval);
            BlooperLogger.info(LogMessage.BLOOPER_SDK_INIT);
            isBlooperInitialized = true;
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
            BlooperLogger.info(LogMessage.ERROR_DEBUG_FLAG);
        }
        return debuggable;
    }

    /**
     * Validate mandatory params in configuration
     *
     * @param config BlooperConfiguration which contains the config params
     * @return validation status
     * @throws BlooperException if any of the params is invalid
     */
    private static boolean validateConfiguration(BlooperConfiguration config) throws BlooperException {
        if (TextUtils.isEmpty(config.uri)) {
            throw new BlooperException(LogMessage.NO_URI);
        } else if (TextUtils.isEmpty(config.appId)) {
            throw new BlooperException(LogMessage.NO_APPID);
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

        if (isBlooperInitialized) {
            try {
                if (exception != null) {
                    ExceptionModel exceptionModel = Util.prepareExceptionModel(exception, className);
                    ExceptionLogger exceptionLogger = BlooperFactory.createExceptionLogger();
                    exceptionLogger.logException(exceptionModel);
                } else {
                    BlooperLogger.error(LogMessage.NO_EXCEPTION_TO_SEND);
                }
            } catch (Exception ex) {
                BlooperLogger.error(LogMessage.ERROR_EXCEPTION, ex);
            }
        } else {
            BlooperLogger.error(LogMessage.ERROR_INIT_BLOOPER_SDK);
        }
    }

    /**
     * log Message into NewMonk.
     *
     * @param className current class name
     * @param msg       any type of message log in NewMonk
     */
    public static void logMessage(String className, String msg) {

        if (isBlooperInitialized) {
            try {
                if (msg != null) {
                    ExceptionModel exceptionModel = Util.prepareExceptionModel(msg, className);
                    ExceptionLogger exceptionLogger = BlooperFactory.createExceptionLogger();
                    exceptionLogger.logException(exceptionModel);
                }
            } catch (Exception ex) {
                BlooperLogger.error(LogMessage.ERROR_EXCEPTION, ex);
            }
        } else {
            BlooperLogger.error(LogMessage.ERROR_INIT_BLOOPER_SDK);
        }
    }

    /**
     * Access the blooper configuration to read and write config params
     *
     * @return BlooperConfiguration which holds the config params
     */
    public static BlooperConfiguration getBlooperConfiguration() {
        if (!isBlooperInitialized) {
            BlooperLogger.error(LogMessage.ERROR_INIT_BLOOPER_SDK);
        }
        return blooperConfiguration;
    }

    public static Context getContext() {
        return context;
    }
}
