package com.example.user.sensortest;

import android.content.res.Resources;
import android.widget.TextView;

public class AppUtility {

    TextView i;

    public static String getResourceKeyValue(String keyName) {

        int id = Resources.getSystem().getIdentifier(keyName, "string", "com.example.user.sensortest");
        return Resources.getSystem().getString(id);
    }
}