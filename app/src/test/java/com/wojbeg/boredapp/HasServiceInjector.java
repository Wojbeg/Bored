package com.wojbeg.boredapp;


import android.app.Service;

import dagger.android.AndroidInjector;

public interface HasServiceInjector {
    AndroidInjector<? extends Service> serviceInjector();
}

