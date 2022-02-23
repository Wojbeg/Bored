package com.wojbeg.boredapp;

import android.app.Service;

import com.wojbeg.boredapp.di.DaggerAppComponent;
//import com.wojbeg.boredapp.widget.HasServiceInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;

public class App extends DaggerApplication  {

    //@Inject
   // DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

   // @Override
   // public AndroidInjector<Service> serviceInjector(){
   //     return dispatchingServiceInjector;
    //}
//
}
