package com.naukri.newmonk.factory;

import com.naukri.newmonk.Monk;
import com.naukri.newmonk.database.DatabaseOpenHelper;
import com.naukri.newmonk.database.ExceptionLogger;
import com.naukri.newmonk.metadata.ExceptionLog;
import com.naukri.newmonk.network.WebServiceManager;

/**
 * Factory for creating instances.
 *
 * @author minni
 */
public class NewMonkFactory {

    /**
     * Create instance of exception logger
     *
     * @return ExceptionLogger
     */
    public static ExceptionLogger createExceptionLogger() {
        return new ExceptionLogger(createDatabaseOpenHelper(), new ExceptionLog());
    }

    /**
     * Create instance of database open helper
     *
     * @return DatabaseOpenHelper
     */
    public static DatabaseOpenHelper createDatabaseOpenHelper() {
        return DatabaseOpenHelper.getDbInstance(Monk.getContext());
    }

    /**
     * Create instance of web service manager
     *
     * @return WebServiceManager
     */
    public static WebServiceManager createWebServiceManager() {
        return new WebServiceManager();
    }
}
