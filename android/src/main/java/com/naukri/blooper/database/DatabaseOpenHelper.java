package com.naukri.blooper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.logger.LogMessage;

/**
 * This class is creating database for New monk.
 *
 * @author akash
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static DatabaseOpenHelper databaseOpenHelper = null;

    /**
     * DatabaseOpenHelper constructor
     *
     * @param context app level context
     */
    private DatabaseOpenHelper(Context context) {
        super(context, com.naukri.blooper.database.DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION);
    }

    /**
     * Get database instance.
     *
     * @param context take application context.
     * @return DatabaseOpenHelper
     */
    public static DatabaseOpenHelper getDbInstance(Context context) {
        if (context != null) {
            if (databaseOpenHelper == null) {
                synchronized (DatabaseOpenHelper.class)     // Thread safe singleton.
                {
                    if (databaseOpenHelper == null) {
                        databaseOpenHelper = new DatabaseOpenHelper(context);
                    }
                }
            }
        } else {
            //Blooper.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_CONTEXT_NULL);
            BlooperLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_CONTEXT_NULL);
        }
        return databaseOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.CREATE_TABLE_EXCEPTION);
        BlooperLogger.info(LogMessage.DATABASE_CREATED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
