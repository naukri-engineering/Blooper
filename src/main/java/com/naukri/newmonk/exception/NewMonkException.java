package com.naukri.newmonk.exception;

/**
 * Exception thrown when manadatory configuration params are missing
 *
 * @author minni
 */
public class NewMonkException extends Exception {
    public NewMonkException(String message) {
        super(message);
    }
}
