package com.naukri.newmonk.database;


public interface DbConstants {

    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "new_monk.db";

    String TABLE_NAME_EXCEPTION = "monk_exception";
    String COLUMN_NAME_ID = "id";
    String COLUMN_NAME_TAG = "tag";
    String COLUMN_NAME_COUNT = "count";
    String COLUMN_NAME_TIMESTAMP = "timestamp";
    String COLUMN_NAME_TYPE = "type";
    String COLUMN_NAME_MESSAGE = "message";
    String COLUMN_NAME_CODE = "code";
    String COLUMN_NAME_FILE = "file";
    String COLUMN_NAME_LINE = "line";
    String COLUMN_NAME_STACK_TRACE = "stacktrace";


    String CREATE_TABLE_EXCEPTION = "CREATE TABLE " + TABLE_NAME_EXCEPTION
            + "("
            + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME_TAG + " TEXT, "
            + COLUMN_NAME_TIMESTAMP + " TEXT, "
            + COLUMN_NAME_TYPE + " TEXT, "
            + COLUMN_NAME_MESSAGE + " TEXT, "
            + COLUMN_NAME_CODE + " TEXT, "
            + COLUMN_NAME_COUNT + " INTEGER, "
            + COLUMN_NAME_FILE + " TEXT, "
            + COLUMN_NAME_LINE + " INTEGER, "
            + COLUMN_NAME_STACK_TRACE + " TEXT"
            + ")";


    // Shared preference Contants
    String PREF_UID = "uid";

}
