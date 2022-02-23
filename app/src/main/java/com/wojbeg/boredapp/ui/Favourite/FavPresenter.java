package com.wojbeg.boredapp.ui.Favourite;

import android.util.Log;

import com.wojbeg.boredapp.adapters.IdeaAdapter;
import com.wojbeg.boredapp.di.scopes.ActivityScope;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.mvp.BaseMvpPresenter;
import com.wojbeg.boredapp.repo.local.Repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.schedulers.Schedulers;

@ActivityScope
public class FavPresenter extends BaseMvpPresenter<FavView> implements FavPresenterMvp {
    private static final String TAG = "FavPresenter";

    private final Repository repository;
    private IdeaAdapter adapter;

    @Inject
    public FavPresenter(CompositeDisposable presenterCompositeDisposable, Repository repository) {
        this.presenterCompositeDisposable = presenterCompositeDisposable;
        this.repository = repository;
    }

    public void checkIdeasCount(){

        repository.getCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        presenterCompositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        view.hideLoading();

                        if(integer > 0){
                            view.showIdeas();
                            getIdeasFromDb();

                        }else{
                            view.hideIdeas();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideLoading();
                        view.hideIdeas();
                        view.showErrorMessage();
                    }
                });
    }


    private void getIdeasFromDb(){
        presenterCompositeDisposable.add(repository.getSavedIdeas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleGetIdeasSuccess, this::handleGetIdeasFailure));
    }

    private void handleGetIdeasSuccess(List<Idea> ideas){
        view.setIdeas(ideas);
        view.showIdeas();
    }

    private void handleGetIdeasFailure(Throwable error){
        view.hideLoading();
        view.hideIdeas();
        view.showErrorMessage();
    }

    @Override
    public void deleteIdea(Idea idea) {

        repository.deleteIdea(idea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>(){
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        presenterCompositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        view.showDeletedIdea(idea);
                        checkIdeasCount();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showIdeaNotDeleted();
                    }
                });

    }


    @Override
    public void undoDeleteIdea(Idea idea) {

        repository.insert(idea)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        presenterCompositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        view.showUndoSuccess();
                        checkIdeasCount();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showUndoError();
                    }
                });

    }




}
