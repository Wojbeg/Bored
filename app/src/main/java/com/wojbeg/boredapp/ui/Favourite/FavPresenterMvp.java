package com.wojbeg.boredapp.ui.Favourite;

import com.wojbeg.boredapp.model.Idea;

import java.util.List;

public interface FavPresenterMvp {

    void deleteIdea(Idea idea) ;
    void undoDeleteIdea(Idea idea);
}
