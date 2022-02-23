package com.wojbeg.boredapp.repo.local.prefs;

import static com.wojbeg.boredapp.utils.Constants.PREF_NAME;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.wojbeg.boredapp.di.scopes.AppScope;

import javax.inject.Inject;
import javax.inject.Singleton;

@AppScope
public class SharedPreferenceUtils implements SharedPrefHelper {

    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    public SharedPreferenceUtils(Application context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setValue(String key, int value){
        sharedPreferencesEditor.putInt(key, value);
        sharedPreferencesEditor.commit();
    }

    @Override
    public void setValue(String key, String value){
        sharedPreferencesEditor.putString(key, value);
        sharedPreferencesEditor.commit();
    }

    @Override
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    @Override
    public void setValue(String key, long value) {
        sharedPreferencesEditor.putLong(key, value);
        sharedPreferencesEditor.commit();
    }

    @Override
    public void setValue(String key, boolean value) {
        sharedPreferencesEditor.putBoolean(key, value);
        sharedPreferencesEditor.commit();
    }

    @Override
    public String getStringValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public int getIntValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public long getLongValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    @Override
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void removeKey(String key) {
        if (sharedPreferencesEditor != null) {
            sharedPreferencesEditor.remove(key);
            sharedPreferencesEditor.commit();
        }
    }

    @Override
    public void clearPreferences() {
        sharedPreferencesEditor.clear().commit();
    }

}
