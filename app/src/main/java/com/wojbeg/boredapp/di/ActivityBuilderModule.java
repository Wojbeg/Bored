package com.wojbeg.boredapp.di;


import com.wojbeg.boredapp.di.scopes.ActivityScope;
import com.wojbeg.boredapp.di.main.MainFragmentsBuilderModule;
import com.wojbeg.boredapp.ui.Main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = MainFragmentsBuilderModule.class
    )
    abstract MainActivity contributeMainActivity();


}
