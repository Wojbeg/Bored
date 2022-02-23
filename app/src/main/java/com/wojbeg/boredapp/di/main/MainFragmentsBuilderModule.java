package com.wojbeg.boredapp.di.main;

import com.wojbeg.boredapp.di.scopes.ActivityScope;
import com.wojbeg.boredapp.di.scopes.FragmentScope;
import com.wojbeg.boredapp.ui.Favourite.FavPresenter;
import com.wojbeg.boredapp.ui.Favourite.FavPresenterMvp;
import com.wojbeg.boredapp.ui.Favourite.FavouriteFragment;
import com.wojbeg.boredapp.ui.Home.HomeFragment;
import com.wojbeg.boredapp.ui.Home.HomePresenter;
import com.wojbeg.boredapp.ui.Home.HomePresenterMvp;
import com.wojbeg.boredapp.ui.Main.MainPresenter;
import com.wojbeg.boredapp.ui.Main.MainPresenterMvp;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@Module(includes = {MainFragmentsBuilderModule.MainAbstractModule.class})
public class MainFragmentsBuilderModule {

    @ActivityScope
    @Provides
    CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Module
    public interface MainAbstractModule {

        @Binds
        @ActivityScope
        MainPresenterMvp getMainPresenter(MainPresenter mainPresenter);

        @FragmentScope
        @ContributesAndroidInjector
        HomeFragment contributeHomeFragment();

        @Binds
        @ActivityScope
        HomePresenterMvp getHomePresenter(HomePresenter homePresenter);

        @FragmentScope
        @ContributesAndroidInjector
        FavouriteFragment contributeFavFragment();

        @Binds
        @ActivityScope
        FavPresenterMvp getFavPresenter(FavPresenter favPresenter);
    }
}
