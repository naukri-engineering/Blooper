package com.naukri.blooper.exception;

/**
 * Exception thrown when manadatory configuration params are missing
 *
 * @author minni
 */
public class BlooperException extends Exception {
    public BlooperException(String message) {
        super(message);
    }
}
