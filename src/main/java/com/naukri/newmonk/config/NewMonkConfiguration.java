package com.naukri.newmonk.config;

import com.naukri.newmonk.annotation.NewMonkCrashReport;
import com.naukri.newmonk.util.PreferencesManager;

import java.util.Map;

/**
 * This class holds all required configuration params
 *
 * @author minni
 */
public class NewMonkConfiguration {
    public String uri;
    public String appId;
    public boolean debug;
    public Map<String, String> httpHeaders;
    public long interval;

    public NewMonkConfiguration() {

    }

    /**
     * Initialize config params from annotation
     *
     * @param newMonkCrashReport the configuration instance
     */
    public NewMonkConfiguration(NewMonkCrashReport newMonkCrashReport) {
        this.uri = newMonkCrashReport.uri();
        this.appId = newMonkCrashReport.appId();
        this.debug = newMonkCrashReport.debug();
        //this.httpHeaders = newMonkCrashReport.httpHeaders;
        this.interval = newMonkCrashReport.interval();

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
