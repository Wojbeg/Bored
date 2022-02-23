package com.wojbeg.boredapp.repo.local;


import com.wojbeg.boredapp.di.scopes.AppScope;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.repo.local.db.dao.IdeaDao;
import com.wojbeg.boredapp.repo.local.prefs.SharedPreferenceUtils;
import com.wojbeg.boredapp.repo.remote.BoredApi;
import com.wojbeg.boredapp.utils.onlineChecker.OnlineChecker;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


@AppScope
public class Repository implements Repo {

    private final IdeaDao ideaDao;
    private final BoredApi boredApi;
    private final OnlineChecker onlineChecker;
    private final SharedPreferenceUtils sharedPreferences;

    @Inject
    public Repository(IdeaDao ideaDao, BoredApi boredApi, OnlineChecker onlineChecker, SharedPreferenceUtils sharedPreferences) {
        this.ideaDao = ideaDao;
        this.boredApi = boredApi;
        this.onlineChecker = onlineChecker;
        this.sharedPreferences = sharedPreferences;
    }

    //BoredApi
    @Override
    public Observable<Idea> getIdea() {
        return boredApi.getIdea();
    }

    @Override
    public Observable<Idea> getCustomIdea(String type, int participants, double accessibilityMin, double accessibilityMax, double priceMin, double priceMax) {
        return boredApi.getCustomIdea(type, participants, accessibilityMin, accessibilityMax, priceMin, priceMax);
    }

    //OnlineChecker

    @Override
    public boolean isNetworkAvailable() {
        return onlineChecker.isNetworkAvailable();
    }

    //Database

    @Override
    public Completable insert(Idea idea) {
        return ideaDao.insert(idea);
    }

    @Override
    public Single<Integer> deleteIdea(Idea idea) {
        return ideaDao.deleteIdea(idea);
    }

    @Override
    public Flowable<List<Idea>> getSavedIdeas() {
        return ideaDao.getSavedIdeas();
    }

    @Override
    public Single<Integer> getCount() {
        return ideaDao.getCount();
    }

    //SharedPreferences

    @Override
    public void setValue(String key, int value) {
        sharedPreferences.setValue(key,value);
    }

    @Override
    public void setValue(String key, String value) {
        sharedPreferences.setValue(key, value);
    }

    @Override
    public void setValue(String key, double value) {
        sharedPreferences.setValue(key, value);
    }

    @Override
    public void setValue(String key, long value) {
        sharedPreferences.setValue(key, value);
    }

    @Override
    public void setValue(String key, boolean value) {
        sharedPreferences.setValue(key, value);
    }

    @Override
    public String getStringValue(String key, String defaultValue) {
        return sharedPreferences.getStringValue(key, defaultValue);
    }

    @Override
    public int getIntValue(String key, int defaultValue) {
        return sharedPreferences.getIntValue(key, defaultValue);
    }

    @Override
    public long getLongValue(String key, long defaultValue) {
        return sharedPreferences.getLongValue(key, defaultValue);
    }

    @Override
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return sharedPreferences.getBooleanValue(key, defaultValue);
    }

    @Override
    public void removeKey(String key) {
        sharedPreferences.removeKey(key);
    }

    @Override
    public void clearPreferences() {
        sharedPreferences.clearPreferences();
    }


}
