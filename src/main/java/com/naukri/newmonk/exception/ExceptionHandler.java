package com.naukri.newmonk.exception;


import com.naukri.newmonk.database.ExceptionLogger;
import com.naukri.newmonk.factory.NewMonkFactory;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;
import com.naukri.newmonk.metadata.ExceptionModel;
import com.naukri.newmonk.util.Util;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;

    public ExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler)
    {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        try {
            ExceptionModel exceptionModel = Util.prepareExceptionModel(ex, null);

            //ExceptionLogger exceptionLogger = new ExceptionLogger();
            ExceptionLogger exceptionLogger = NewMonkFactory.createExceptionLogger();
            exceptionLogger.logException(exceptionModel);

//            send to default handler.
//            uncaughtExceptionHandler.uncaughtException(thread, ex);

            // Kill App.
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        catch(Exception exception)
        {
            //Monk.sendException(ExceptionHandler.class.getName().toString(), exception);
            NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, exception);
        }
    }
}

