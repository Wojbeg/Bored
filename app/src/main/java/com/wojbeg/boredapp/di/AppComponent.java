package com.wojbeg.boredapp.di;

import android.app.Application;
import com.wojbeg.boredapp.App;
import com.wojbeg.boredapp.di.scopes.AppScope;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@AppScope
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
                AppModule.class,
                OnlineCheckerModule.class,
                LocalRepositoryModule.class,
                ServiceModule.class,
        }
)
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
