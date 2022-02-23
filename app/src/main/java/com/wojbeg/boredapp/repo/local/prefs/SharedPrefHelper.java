package com.wojbeg.boredapp.repo.local.prefs;

public interface SharedPrefHelper {

    void setValue(String key, int value);
    void setValue(String key, String value);
    void setValue(String key, double value);
    void setValue(String key, long value);
    void setValue(String key, boolean value);
    String getStringValue(String key, String defaultValue);
    int getIntValue(String key, int defaultValue);
    long getLongValue(String key, long defaultValue);
    boolean getBooleanValue(String key, boolean defaultValue);
    void removeKey(String key);
    void clearPreferences();


}
