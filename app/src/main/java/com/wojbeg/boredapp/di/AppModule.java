package com.wojbeg.boredapp.di;


import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;


@Module(includes = {ApiModule.class})
public abstract class AppModule {

    @Binds
    abstract Context bindContext(Application application);

}
