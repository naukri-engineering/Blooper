package com.naukri.newmonk.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation for config params
 *
 * @author minni
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NewMonkCrashReport {
    /**
     * @return Your newmonk endpoint which will receive reports
     */
    String uri() default ConfigDefaults.URI;

    /**
     * @return The newmonk defined application id for you
     */
    String appId() default ConfigDefaults.APPID;

    /**
     * @return The interval at which the reports will be sent
     */
    long interval() default ConfigDefaults.SEND_EXCEPTION_INTERVAL;

    /**
     * @return To enable/disable debug logs from this library
     */
    boolean debug() default ConfigDefaults.DEBUG;
}
