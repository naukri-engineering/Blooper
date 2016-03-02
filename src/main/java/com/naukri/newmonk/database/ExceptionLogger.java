package com.naukri.newmonk.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.naukri.newmonk.Monk;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;
import com.naukri.newmonk.metadata.ExceptionLog;
import com.naukri.newmonk.metadata.ExceptionModel;

import java.sql.SQLException;

/**
 * This class handle all New Monk database CRUD operations.
 *
 * @author akash
 */
public class ExceptionLogger {
    private final int DATABASE_LOG_LIMIT = 20;
    private DatabaseOpenHelper databaseOpenHelper;
    private ExceptionLog exceptionLog;


    public ExceptionLogger(DatabaseOpenHelper databaseOpenHelper, ExceptionLog exceptionLog) {
        this.databaseOpenHelper = databaseOpenHelper;
        this.exceptionLog = exceptionLog;
    }

    /**
     * Save unhandled exceptions(Caught by Exception handler)
     *
     * @param exceptionModel
     * @throws SQLException
     */
    public void logException(ExceptionModel exceptionModel) throws SQLException {
        //DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getDbInstance();

        Cursor cursor = null;

        try {
            DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getDbInstance(Monk.getContext());

            if (databaseOpenHelper != null) {
                SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getWritableDatabase();

                cursor = getCursor(sqLiteDatabase);
                int count = 0;

                if (cursor != null)
                    count = cursor.getCount();

                if (count < DATABASE_LOG_LIMIT) {

                    if (sqLiteDatabase != null && exceptionModel != null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DbConstants.COLUMN_NAME_CODE, exceptionModel.code);
                        contentValues.put(DbConstants.COLUMN_NAME_COUNT, exceptionModel.count);
                        contentValues.put(DbConstants.COLUMN_NAME_FILE, exceptionModel.file);
                        contentValues.put(DbConstants.COLUMN_NAME_LINE, exceptionModel.line);
                        contentValues.put(DbConstants.COLUMN_NAME_MESSAGE, exceptionModel.message);
                        contentValues.put(DbConstants.COLUMN_NAME_STACK_TRACE, exceptionModel.stackTrace);
                        contentValues.put(DbConstants.COLUMN_NAME_TAG, exceptionModel.tag);
                        contentValues.put(DbConstants.COLUMN_NAME_TIMESTAMP, exceptionModel.timestamp);
                        contentValues.put(DbConstants.COLUMN_NAME_TYPE, exceptionModel.type);

                        long id = sqLiteDatabase.insertOrThrow(DbConstants.TABLE_NAME_EXCEPTION, null, contentValues);

                        if (id != 0)     // Check record inserted successfully or not.
                        {
                            NewMonkLogger.info(LogMessage.DATABASE_EXCEPTION_INSERTED);
                        }
                    } else        // Database insertion error.
                    {
                        //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_INSERTION);
                        NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_INSERTION);
                    }
                } else {
                    NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_LIMIT_ERROR);
                }
            } else        // Database writable permission error.
            {
                //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_WRITABLE_PERMISSION);
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_WRITABLE_PERMISSION);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * Get Exception log cursor.
     *
     * @param sqLiteDatabase
     * @return
     */
    private Cursor getCursor(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = null;

        cursor = sqLiteDatabase.query(DbConstants.TABLE_NAME_EXCEPTION, null, null, null, null, null, null);

        return cursor;
    }

    /**
     * Get Exception logs from DB.
     *
     * @return ExceptionLog
     * @throws SQLiteException
     */
    public ExceptionLog getExceptionLog() throws SQLiteException {
        //ExceptionLog exceptionLog = new ExceptionLog();

        //DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getDbInstance(Monk.getContext());

        ExceptionLog exceptionLog = new ExceptionLog();

        Cursor cursor = null;

        try {
            DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getDbInstance(Monk.getContext());

            if (databaseOpenHelper != null) {
                SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getReadableDatabase();

                if (sqLiteDatabase != null) {
                    cursor = getCursor(sqLiteDatabase);

                    if (cursor != null && cursor.moveToFirst()) {
                        int cursorCount = cursor.getCount();
                        exceptionLog.exceptionModel = new ExceptionModel[cursorCount];

                        ExceptionModel exceptionModel = null;

                        for (int i = 0; i < cursorCount; i++) {
                            exceptionModel = new ExceptionModel();
                            exceptionModel.code = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_CODE));
                            exceptionModel.count = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_COUNT));
                            exceptionModel.file = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_FILE));
                            exceptionModel.line = cursor.getInt(cursor.getColumnIndex(DbConstants.COLUMN_NAME_LINE));
                            exceptionModel.message = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_MESSAGE));
                            exceptionModel.stackTrace = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_STACK_TRACE));
                            exceptionModel.type = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_TYPE));
                            exceptionModel.timestamp = cursor.getLong(cursor.getColumnIndex(DbConstants.COLUMN_NAME_TIMESTAMP));
                            exceptionModel.tag = cursor.getString(cursor.getColumnIndex(DbConstants.COLUMN_NAME_TAG));

                            exceptionLog.exceptionModel[i] = exceptionModel;
                            cursor.moveToNext();
                        }

                        //LogHandler.showLogs(LogMessage.DATABSE_EXCEPTION_FETCH + cursorCount);
                        NewMonkLogger.info(LogMessage.DATABSE_EXCEPTION_FETCH + cursorCount);
                    }
                } else        // Database fetch error.
                {
                    //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_FETCH);
                    NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_FETCH);
                }
            } else        // Database readable permission error.
            {
                //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_READABLE_PERMISSION);
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_READABLE_PERMISSION);

            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return exceptionLog;
    }

    /**
     * Clear exception logs from database.
     */
    public void clearData() {
        //DatabaseOpenHelper databaseOpenHelper = DatabaseOpenHelper.getDbInstance(Monk.getContext());

        if (databaseOpenHelper != null) {
            SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getWritableDatabase();

            if (sqLiteDatabase != null) {
                sqLiteDatabase.delete(DbConstants.TABLE_NAME_EXCEPTION, null, null);
            } else {
                //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_INSERTION);
                NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_DELETION);
            }
        } else        // Database writable permission error.
        {
            //Monk.sendException(ExceptionLogger.class.getName().toString(), LogMessage.ERROR_DATABASE_WRITABLE_PERMISSION);
            NewMonkLogger.error(LogMessage.ERROR_EXCEPTION + LogMessage.ERROR_DATABASE_WRITABLE_PERMISSION);
        }
    }

}
