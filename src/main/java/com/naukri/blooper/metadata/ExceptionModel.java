package com.naukri.blooper.metadata;

import com.naukri.blooper.util.Constants;

public class ExceptionModel
{
    public String tag = Constants.UNKNOWN_TAG;
    public String count = null;
    public long  timestamp = 0l;
    public String type = Constants.UNKNOWN_TYPE;
    public String message = null;
    public String code = null;
    public String file = null;
    public int line = 0;
    public String stackTrace = null;
}
