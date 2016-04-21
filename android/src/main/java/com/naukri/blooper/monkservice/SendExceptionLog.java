package com.naukri.blooper.monkservice;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.naukri.blooper.Blooper;
import com.naukri.blooper.database.ExceptionLogger;
import com.naukri.blooper.factory.BlooperFactory;
import com.naukri.blooper.logger.BlooperLogger;
import com.naukri.blooper.logger.LogMessage;
import com.naukri.blooper.metadata.App;
import com.naukri.blooper.metadata.Device;
import com.naukri.blooper.metadata.DeviceInfo;
import com.naukri.blooper.metadata.Environment;
import com.naukri.blooper.metadata.ExceptionLog;
import com.naukri.blooper.metadata.OS;
import com.naukri.blooper.network.NetworkRequest;
import com.naukri.blooper.network.WebServiceManager;
import com.naukri.blooper.util.Constants;
import com.naukri.blooper.util.Util;


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
            ExceptionLogger exceptionLogger = BlooperFactory.createExceptionLogger();
            ExceptionLog exceptionLog = exceptionLogger.getExceptionLog();

            if (exceptionLog != null && exceptionLog.exceptionModel != null && exceptionLog.exceptionModel.length > 0) {
                BlooperLogger.info(LogMessage.WEB_SERVICE_HIT);
                // Preapare Network request.
                NetworkRequest networkRequest = requestIntialization(exceptionLog);
                // Hit Web service.
                WebServiceManager webServiceManager = BlooperFactory.createWebServiceManager();
                int responseCode = webServiceManager.sendException(networkRequest);
                // all is well
                if (Util.isSuccessResponse(responseCode)) {
                    //ExceptionLogger exceptionLogger = new ExceptionLogger();
                    exceptionLogger.clearData();
                }
            } else    //  No exception in db.
            {
                BlooperLogger.warn(LogMessage.NO_EXCEPTION);
            }
        } catch (Exception exception) {
            // Blooper.sendException(SendExceptionLog.class.getName().toString(), exception);
            //LogHandler.showLogs(LogMessage.ERROR_EXCEPTION + exception.toString());
            BlooperLogger.error(LogMessage.ERROR_EXCEPTION, exception);
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
        networkRequest.uId = Util.getString(Blooper.getBlooperConfiguration().getUserId());

        // App id.
        networkRequest.appId = Util.getString(Blooper.getBlooperConfiguration().appId);

        Environment environment = new Environment();
        OS os = new OS();
        os.name = Constants.OS_NAME;
        os.version = DeviceInfo.SDK_VERSION;

        // Get app version name.
        App app = new App();
        app.version = Blooper.getContext().getPackageManager().getPackageInfo(Blooper.getContext().getPackageName(), 0).versionName;

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
