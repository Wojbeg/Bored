package com.wojbeg.boredapp.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.wojbeg.boredapp.di.scopes.AppScope;
import com.wojbeg.boredapp.utils.onlineChecker.AppOnlineChecker;
import com.wojbeg.boredapp.utils.onlineChecker.OnlineChecker;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineCheckerModule {

    @Provides
    @AppScope
    ConnectivityManager provideConnectivityManager(Application context){
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @AppScope
    OnlineChecker onlineChecker(ConnectivityManager connectivityManager){
        return new AppOnlineChecker(connectivityManager);
    }

}
