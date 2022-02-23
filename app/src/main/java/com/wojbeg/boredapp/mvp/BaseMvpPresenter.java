package com.wojbeg.boredapp.mvp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseMvpPresenter<T extends BaseView> implements PresenterMvp {

    protected T view;
    protected CompositeDisposable presenterCompositeDisposable;

    public void subscribe(T view){
        this.view = view;
    }

    public void onDetach(){
        presenterCompositeDisposable.clear();
        view = null;
    }

    public boolean isViewAttached(){
        return view!=null;
    }

}
