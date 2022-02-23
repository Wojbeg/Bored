package com.wojbeg.boredapp.ui.Main;

import static com.wojbeg.boredapp.utils.FragmentsEnum.*;

import com.wojbeg.boredapp.di.scopes.ActivityScope;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.mvp.BaseMvpPresenter;
import com.wojbeg.boredapp.ui.Home.HomePresenter;
import com.wojbeg.boredapp.utils.FragmentsEnum;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

@ActivityScope
public class MainPresenter extends BaseMvpPresenter<MainView> implements MainPresenterMvp {

    private FragmentsEnum selectedFragment = HOME_FRAGMENT;

    @Inject
    public MainPresenter(CompositeDisposable presenterCompositeDisposable){
        this.presenterCompositeDisposable = presenterCompositeDisposable;
    }

    public FragmentsEnum getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(FragmentsEnum selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Override
    public void selectFragment(FragmentsEnum selected){
        setSelectedFragment(selected);
        switch (selected){
            case HOME_FRAGMENT:
                view.switchToHomeFragment();
                break;

            case FAVOURITE_FRAGMENT:
                view.switchToFavFragment();
                break;
        }

    }

}
