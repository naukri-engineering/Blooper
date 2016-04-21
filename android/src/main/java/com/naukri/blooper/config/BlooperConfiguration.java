package com.naukri.blooper.config;

import com.naukri.blooper.annotation.BlooperCrashReport;
import com.naukri.blooper.util.PreferencesManager;

import java.util.Map;

/**
 * This class holds all required configuration params
 *
 * @author minni
 */
public class BlooperConfiguration {
    public String uri;
    public String appId;
    public boolean debug;
    public Map<String, String> httpHeaders;
    public long interval;

    public BlooperConfiguration() {

    }

    /**
     * Initialize config params from annotation
     *
     * @param blooperCrashReport the configuration instance
     */
    public BlooperConfiguration(BlooperCrashReport blooperCrashReport) {
        this.uri = blooperCrashReport.uri();
        this.appId = blooperCrashReport.appId();
        this.debug = blooperCrashReport.debug();
        //this.httpHeaders = blooperCrashReport.httpHeaders;
        this.interval = blooperCrashReport.interval();

    }

    /**
     * Get user id
     *
     * @return userId
     */
    public String getUserId() {
        return PreferencesManager.getUserId();
    }

    /**
     * Store user id that is to be sent in all requests
     *
     * @param userId user id
     */
    public void setUserId(String userId) {
        PreferencesManager.saveUserId(userId);
    }
}
