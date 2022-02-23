package com.wojbeg.boredapp.di;

import com.wojbeg.boredapp.di.scopes.AppScope;
import com.wojbeg.boredapp.repo.local.Repo;
import com.wojbeg.boredapp.repo.local.Repository;
import com.wojbeg.boredapp.repo.local.db.dao.IdeaDao;
import com.wojbeg.boredapp.repo.local.prefs.SharedPreferenceUtils;
import com.wojbeg.boredapp.repo.remote.BoredApi;
import com.wojbeg.boredapp.utils.onlineChecker.OnlineChecker;

import dagger.Module;
import dagger.Provides;

@Module(includes = {LocalDataModule.class})
public class LocalRepositoryModule {

    @Provides
    @AppScope
    Repo provideLocalRepository(IdeaDao ideaDao, BoredApi boredApi, OnlineChecker onlineChecker, SharedPreferenceUtils sharedPreferences){
        return new Repository(ideaDao, boredApi, onlineChecker, sharedPreferences);
    }

}
