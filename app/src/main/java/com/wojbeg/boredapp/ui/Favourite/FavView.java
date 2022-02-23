package com.wojbeg.boredapp.ui.Favourite;

import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.mvp.BaseView;

import java.util.List;

public interface FavView  extends BaseView {
    void showIdeas();
    void hideIdeas();

    void setIdeas(List<Idea> ideas);

    void showLoading();
    void hideLoading();

    void showErrorMessage();

    void showDeletedIdea(Idea idea);
    void showIdeaNotDeleted();

    void showUndoSuccess();
    void showUndoError();

}
