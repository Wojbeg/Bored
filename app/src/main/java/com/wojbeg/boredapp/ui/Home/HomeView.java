package com.wojbeg.boredapp.ui.Home;

import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.mvp.BaseView;

public interface HomeView extends BaseView {

    void showNetworkUnavailable();
    void showEmptyIdeaMessage();
    void showErrorMessage();
    void showErrorSavingMessage();
    void showSavedInfo();

    void showFrame();
    void hideFrame();
    void clearFrame();

    void showLoading();
    void hideLoading();

    void showSaving();
    void hideSaving();

    void showSettingsFrame();
    void hideSettingsFrame();

    void showIdeaDetails(Idea idea, boolean wasSaved);

    void showParticipantsError();
    void hideParticipantsError();
}
