package nl.everlutions.myradar.utils;

import android.provider.Settings;

public interface Constants {

    boolean DEBUG_MODE = true; // set to FALSE for release
    boolean DEBUG_FRAGMENTS_LIFECYCLE = false; // set to FALSE for release

    long TIME_SECOND = 1000;
    long TIME_MINUTE = TIME_SECOND * 60;
    long TIME_HOUR = 60 * TIME_MINUTE;
    long TIME_DAY = 24 * TIME_HOUR;
    long TIME_WEEK = 7 * TIME_DAY;

    int API_CONNECTION_TIMEOUT = 15000;

    int IMAGE_PREFERRED_WIDTH = 480;
    int IMAGE_MAX_WIDTH = 600;

    String API_PRODUCTION_URL = "http://api.buienradar.nl/";
    String API_TEST_URL = "http://api.buienradar.nl/";
    String API_BASE_URL = DEBUG_MODE ? API_TEST_URL : API_PRODUCTION_URL;

    String API_GET_TEST_OBJECT = "blackbird/";
    String API_PUT_REGISTER_DEVICE = "register-device/" + Settings.Secure.ANDROID_ID;
    ;


    String PREF_SETTINGS = "PREF_SETTINGS";

    String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
    String PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD";
    String PREF_KEY_GCM_SUBSCRIBING_SUCCESSFUL = "PREF_KEY_GCM_SUBSCRIBING_SUCCESSFUL";


}
