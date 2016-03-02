package com.naukri.blooper.factory;

import com.naukri.blooper.Blooper;
import com.naukri.blooper.database.DatabaseOpenHelper;
import com.naukri.blooper.database.ExceptionLogger;
import com.naukri.blooper.metadata.ExceptionLog;
import com.naukri.blooper.network.WebServiceManager;

/**
 * Factory for creating instances.
 *
 * @author minni
 */
public class BlooperFactory {

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
        return DatabaseOpenHelper.getDbInstance(Blooper.getContext());
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
