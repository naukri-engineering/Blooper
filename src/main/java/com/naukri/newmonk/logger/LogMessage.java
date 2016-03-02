package com.naukri.newmonk.logger;

public interface LogMessage
{
    String DATABASE_INIT = "Database initialization complete";
    String MONK_SDK_INIT = "Monk Sdk initialization complete";
    String MONK_SDK_REINIT = "Monk already initialized. Nothing to do";
    String EXCEPTION_HANDLER_INIT = "Exception Handler initialization complete";
    String ERROR_INIT_MONK_SDK = "Initialize New Monk First";
    String HIT_WEB_SERVICE = "Hit Webservice";
    String NWK_REQUEST = "Request : ";
    String NWK_RESPONSE = "Response : ";
    String ERROR_CONNECTION_TIME_OUT = "Connection Time Out Exception";
    String ERROR_SOCKET_TIMEOUT = "Socket Time Out Exception";
    String ERROR_EXCEPTION = "Exception : ";
    String MONK_START_BOOT_COMPLETE = "Monk Sdk Start After Boot Complete";
    String ERROR_DATABASE_WRITABLE_PERMISSION = "Data base writable permission Error";
    String ERROR_DATABASE_INSERTION = "Database Insertion Error";
    String ERROR_DATABASE_FETCH = "Database Fetch Error";
    String ERROR_DATABASE_READABLE_PERMISSION = "Data base readable permission Error";
    String ERROR_CONTEXT_NULL = "Context Null, Sdk not initialized";
    String DATABASE_CREATED = "Database Created";
    String DATABASE_EXCEPTION_INSERTED = "Exception Inserted";
    String DATABSE_EXCEPTION_FETCH = "Exception count : ";
    String ERROR_DATABASE_DELETION = "Database deletion Error";
    String WEB_SERVICE_HIT = "Web service hit";
    String NO_EXCEPTION = "No Exception in db, Web service cancel";
    String NO_ANNOTATION = "NewMonkCrashReport annotation missing";
    String NO_URI = "Missing Uri in configuration";
    String NO_APPID = "Missing app id in configuration";
    String NO_EXCEPTION_TO_SEND = "No exception to send";
    String ERROR_DEBUG_FLAG = "Error fetching debug flag";
    String ERROR_DATABASE_LIMIT_ERROR = "Database insertion limit exceeded.";
    String INCORRECT_RESPONSE_CODE = "Response code must be either 202 or 204. Received response code is ";
    String SEND_SUCCESS = "Sent Successfully. Response code received ";
}
