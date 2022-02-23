package com.wojbeg.boredapp.ui.Favourite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.adapters.IdeaAdapter;

import javax.inject.Inject;
import  com.wojbeg.boredapp.adapters.IdeaAdapter.IdeaAdapterListener;
import com.wojbeg.boredapp.model.Idea;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class FavouriteFragment extends DaggerFragment implements FavView {

    private static final String TAG = "FavouriteFragment";

    @Inject
    FavPresenter favPresenter;

    @BindView(R.id.ideasRecyclerView)
    RecyclerView ideasRecyclerView;
    @BindView(R.id.noIdeasImage)
    ImageView noIdeasImage;
    @BindView(R.id.noIdeasInfo)
    TextView noIdeasInfo;
    @BindView(R.id.noIdeasMoreInfo)
    TextView noIdeasMoreInfo;

    @BindView(R.id.favContainer)
    RelativeLayout favContainer;

    @BindView(R.id.loadingIdeasProgressBar)
    ProgressBar progressBar;

    private IdeaAdapter ideaAdapter;

    @Inject
    public FavouriteFragment(){
        // Requires empty constructor

    }

    private IdeaAdapterListener ideaAdapterListener = new IdeaAdapterListener() {
        @Override
        public void onIdeaShareClick(Idea idea) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            StringBuilder intentString = new StringBuilder(
                    idea.activity + "\n" +
                    getString(R.string.type) + ": " + idea.type + ", " +
                    getString(R.string.participants) + ": " + idea.participants + "\n" +
                    getString(R.string.price) + ": " + idea.price + ", "+ getString(R.string.accessibility) + ": " + idea.accessibility);

            if(!idea.link.equalsIgnoreCase("")){
                intentString.append("\n" + getString(R.string.ideaLinkTitle) + idea.getLink());
            }

            shareIntent.putExtra(Intent.EXTRA_TEXT, intentString.toString());

            shareIntent.putExtra(Intent.EXTRA_TITLE, idea.activity);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, idea.activity);

            startActivity(Intent.createChooser(shareIntent,null));
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ideaAdapter = new IdeaAdapter(ideaAdapterListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        showLoading();
        hideIdeas();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        favPresenter.subscribe(this);
        favPresenter.checkIdeasCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favPresenter.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favPresenter.onDetach();
    }

    @Override
    public void showIdeas() {
        ideasRecyclerView.setVisibility(View.VISIBLE);

        noIdeasImage.setVisibility(View.GONE);
        noIdeasInfo.setVisibility(View.GONE);
        noIdeasMoreInfo.setVisibility(View.GONE);
    }

    @Override
    public void hideIdeas() {
        ideasRecyclerView.setVisibility(View.GONE);

        noIdeasImage.setVisibility(View.VISIBLE);
        noIdeasInfo.setVisibility(View.VISIBLE);
        noIdeasMoreInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIdeas(List<Idea> ideas) {
        ideaAdapter.differ.submitList(ideas);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(getContext(), getString(R.string.errorLoadingMessage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeletedIdea(Idea idea) {
        Snackbar snackbar = Snackbar.make(favContainer, getString(R.string.deletedIdea), Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favPresenter.undoDeleteIdea(idea);
            }
        });
        snackbar.show();
    }

    @Override
    public void showIdeaNotDeleted() {
        Toast.makeText(getContext(), getString(R.string.errorDeleteIdea), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showUndoSuccess() {
        ideaAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), getString(R.string.retrievedIdea), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUndoError() {
        Toast.makeText(getContext(), getString(R.string.unableToRetrieve), Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView(){
        ideasRecyclerView.setAdapter(ideaAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(ideasRecyclerView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        favPresenter.onDetach();
        super.onSaveInstanceState(outState);
    }

    private ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Idea idea = (Idea) ideaAdapter.differ.getCurrentList().get(position);
            favPresenter.deleteIdea(idea);
        }
    };



}
