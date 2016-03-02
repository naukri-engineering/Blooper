package com.naukri.newmonk.metadata;

import android.os.Build;

public interface DeviceInfo {

    int SDK_VERSION = Build.VERSION.SDK_INT;
    String BRAND = Build.BRAND;
    String DEVICE = Build.DEVICE;
    String ID = Build.ID;
    String PRODUCT = Build.PRODUCT;
    String MANUFACTURER = Build.MANUFACTURER;
    String MODEL = Build.MODEL;
    String USER = Build.USER;
}
