package com.wojbeg.boredapp.ui.Home;

import static com.wojbeg.boredapp.utils.Constants.DEFAULT_PARTICIPANTS;
import static com.wojbeg.boredapp.utils.Constants.MAX_PARTICIPANTS;

import android.util.Log;

import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.di.scopes.ActivityScope;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.mvp.BaseMvpPresenter;
import com.wojbeg.boredapp.repo.local.Repository;
import com.wojbeg.boredapp.repo.remote.BoredApi;
import com.wojbeg.boredapp.utils.onlineChecker.OnlineChecker;

import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@ActivityScope
public class HomePresenter extends BaseMvpPresenter<HomeView> implements HomePresenterMvp {

    private static final String TAG = "HomePresenter";

    private final Repository repository;
    private Idea currentIdea = null;
    private boolean isCurrentSaved = false;

    @Inject
    public HomePresenter(CompositeDisposable presenterCompositeDisposable, Repository repository) {
        this.presenterCompositeDisposable = presenterCompositeDisposable;
        this.repository = repository;

    }

    public boolean isNetworkAvailable(){
        return repository.isNetworkAvailable();
    }

    @Override
    public void getIdeaFromApi(){
        if(isNetworkAvailable()){

            view.showFrame();
            view.showLoading();

            repository.getIdea()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Observer<Idea>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    presenterCompositeDisposable.add(d);
                                }

                                @Override
                                public void onNext(@NonNull Idea idea) {
                                    handleResult(idea);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    handleError(e);
                                }

                                @Override
                                public void onComplete() {
                                    presenterCompositeDisposable.clear();
                                }
                            }
                    );

        }else{
            view.showNetworkUnavailable();
            view.hideLoading();
        }
    }

    @Override
    public void validateInputs(String type, String numberOfParticipants, float accessibilityMin, float accessibilityMax,  float priceMin, float priceMax, boolean isChecked) {

        int numberOfParticipantsInt = DEFAULT_PARTICIPANTS;
        if(numberOfParticipants.compareToIgnoreCase("") != 0){
            numberOfParticipantsInt = Integer.parseInt(numberOfParticipants);
        }

        if(isChecked){
            Random r = new Random();
            numberOfParticipantsInt = r.nextInt(MAX_PARTICIPANTS) + 1;
        }

        if(numberOfParticipantsInt <=0 ){
            view.showParticipantsError();
        }else{
            getCustomIdeaFromApi(type.toLowerCase(Locale.ROOT), numberOfParticipantsInt, accessibilityMin, accessibilityMax, priceMin, priceMax);
        }

    }

    public void getCustomIdeaFromApi(String type, int participants, double accessibilityMin, double accessibilityMax,  double priceMin, double priceMax){

        if(isNetworkAvailable()){

            view.showFrame();
            view.showLoading();

            repository.getCustomIdea(type, participants, accessibilityMin, accessibilityMax, priceMin, priceMax)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Idea>() {
                                   @Override
                                   public void onSubscribe(@NonNull Disposable d) {
                                       presenterCompositeDisposable.add(d);
                                   }

                                   @Override
                                   public void onNext(@NonNull Idea idea) {
                                       /*
                                       We handle a response that has an empty idea
                                       (error no activity found with the specified parameters).
                                       This can also be solved by adding an error field to the
                                       idea class or creating a response wrapper class. However, this idea seems
                                       simple and effective to me, every idea must have an activity.
                                       */

                                        if(idea.activity == null){
                                            handleEmptyIdea();
                                        }else{
                                            handleResult(idea);
                                        }

                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                        handleError(e);
                                   }

                                   @Override
                                   public void onComplete() {
                                        presenterCompositeDisposable.clear();
                                   }
                               }
                    );

        }else{
            view.showNetworkUnavailable();
            view.hideLoading();
        }

    }

    @Override
    public void subscribe(HomeView view) {
        super.subscribe(view);

        if(currentIdea != null){
            view.showFrame();
            view.showIdeaDetails(currentIdea, isCurrentSaved);
        }else{
            isCurrentSaved = false;
        }
    }

    private void handleResult(Idea idea){
        isCurrentSaved = false;
        view.clearFrame();
        currentIdea = idea;
        view.hideLoading();
        view.showIdeaDetails(idea, isCurrentSaved);
    }

    private void handleError(Throwable t){
        view.hideLoading();
        view.showErrorMessage();
    }

    private void handleEmptyIdea(){
        view.hideLoading();

        if(currentIdea==null){
            view.hideFrame();
        }
        view.showEmptyIdeaMessage();
    }

    @Override
    public void saveIdea() {

        if(currentIdea!=null){
            view.showSaving();

            repository.insert(currentIdea)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            presenterCompositeDisposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            isCurrentSaved = true;
                            view.hideSaving();
                            view.showSavedInfo();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            view.hideSaving();
                            view.showErrorSavingMessage();
                        }
                    });
        }else{
            view.hideSaving();
            view.showErrorSavingMessage();
        }

    }

    public boolean haveCurrentIdea(){
        return (currentIdea!=null);
    }

    public void retrieveData(Idea retrievedIdea, boolean retrievedSaved){
        if(currentIdea == null){
            currentIdea = retrievedIdea;
            isCurrentSaved = retrievedSaved;
        }
    }

    public Idea getCurrentIdea() {
        return currentIdea;
    }

    public boolean isCurrentSaved() {
        return isCurrentSaved;
    }
}
