package com.wojbeg.boredapp.di;


import com.wojbeg.boredapp.widget.ListRemoteViewsFactory;
import com.wojbeg.boredapp.widget.ListWidgetService;
import com.wojbeg.boredapp.widget.WidgetService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract WidgetService contributeWidgetService();

    @ContributesAndroidInjector
    abstract ListWidgetService contributeListWidgetService();

}
