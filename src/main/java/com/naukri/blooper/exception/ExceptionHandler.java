package com.naukri.blooper.exception;


import com.naukri.blooper.database.ExceptionLogger;
import com.naukri.blooper.factory.BlooperFactory;
import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.logger.LogMessage;
import com.naukri.blooper.metadata.ExceptionModel;
import com.naukri.blooper.util.Util;

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
            ExceptionLogger exceptionLogger = BlooperFactory.createExceptionLogger();
            exceptionLogger.logException(exceptionModel);

//            send to default handler.
//            uncaughtExceptionHandler.uncaughtException(thread, ex);

            // Kill App.
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        catch(Exception exception)
        {
            //Blooper.sendException(ExceptionHandler.class.getName().toString(), exception);
            BlooperLogger.error(LogMessage.ERROR_EXCEPTION, exception);
        }
    }
}

