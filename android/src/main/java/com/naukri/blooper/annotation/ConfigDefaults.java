package com.naukri.blooper.annotation;

import com.naukri.blooper.util.Constants;

/**
 * Defaults for required config params
 *
 * @author minni
 */
public interface ConfigDefaults {
    String URI = "";
    String APPID = "";
    boolean DEBUG = false;
    long SEND_EXCEPTION_INTERVAL = Constants.SEND_EXCEPTION_INTERVAL;
}
