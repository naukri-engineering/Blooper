package com.naukri.newmonk.monkservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.naukri.newmonk.Monk;
import com.naukri.newmonk.database.ExceptionLogger;
import com.naukri.newmonk.factory.NewMonkFactory;
import com.naukri.newmonk.logger.LogMessage;
import com.naukri.newmonk.logger.NewMonkLogger;
import com.naukri.newmonk.metadata.App;
import com.naukri.newmonk.metadata.Device;
import com.naukri.newmonk.metadata.DeviceInfo;
import com.naukri.newmonk.metadata.Environment;
import com.naukri.newmonk.metadata.ExceptionLog;
import com.naukri.newmonk.metadata.OS;
import com.naukri.newmonk.network.NetworkRequest;
import com.naukri.newmonk.network.WebServiceManager;
import com.naukri.newmonk.util.Constants;
import com.naukri.newmonk.util.Util;


public class SendExceptionLog extends IntentService {

    private final static String NAME = "service";

    public SendExceptionLog() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // Get all exceptions from DB.
            //ExceptionLogger exceptionLogger = new ExceptionLogger();
            ExceptionLogger exceptionLogger = NewMonkFactory.createExceptionLogger();
            ExceptionLog exceptionLog = exceptionLogger.getExceptionLog();

            if (exceptionLog != null && exceptionLog.exceptionModel != null && exceptionLog.exceptionModel.length > 0) {
                NewMonkLogger.info(LogMessage.WEB_SERVICE_HIT);
                // Preapare Network request.
                NetworkRequest networkRequest = requestIntialization(exceptionLog);
                // Hit Web service.
                WebServiceManager webServiceManager = NewMonkFactory.createWebServiceManager();
                int responseCode = webServiceManager.sendException(networkRequest);
                // all is well
                if (Util.isSuccessResponse(responseCode)) {
                    //ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.clearData();
                }
            } else    //  No exception in db.
            {
                NewMonkLogger.warn(LogMessage.NO_EXCEPTION);
            }
        } catch (Exception exception) {
            // Monk.sendException(SendExceptionLog.class.getName().toString(), exception);
            //LogHandler.showLogs(LogMessage.ERROR_EXCEPTION + exception.toString());
            NewMonkLogger.error(LogMessage.ERROR_EXCEPTION, exception);
        }
    }

    /**
     * Prepare Request.
     *
     * @param exceptionLog exception log instance
     * @return NetworkRequest
     * @throws PackageManager.NameNotFoundException
     */
    private NetworkRequest requestIntialization(ExceptionLog exceptionLog) throws PackageManager.NameNotFoundException {
        NetworkRequest networkRequest = new NetworkRequest();

        networkRequest.source = Constants.SOURCE;

        // User id.
        networkRequest.uId = Util.getString(Monk.getNewMonkConfiguration().getUserId());

        // App id.
        networkRequest.appId = Util.getString(Monk.getNewMonkConfiguration().appId);

        Environment environment = new Environment();
        OS os = new OS();
        os.name = Constants.OS_NAME;
        os.version = DeviceInfo.SDK_VERSION;

        // Get app version name.
        App app = new App();
        app.version = Monk.getContext().getPackageManager().getPackageInfo(Monk.getContext().getPackageName(), 0).versionName;

        Device device = new Device();
        device.name = DeviceInfo.MODEL;

        environment.app = app;
        environment.os = os;
        environment.device = device;

        networkRequest.environment = environment;

        networkRequest.exceptionLog = exceptionLog;
        return networkRequest;
    }
}
