package com.wojbeg.boredapp.adapters;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdeaAdapter extends RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder> {

    public AsyncListDiffer differ = new AsyncListDiffer<Idea>(this, diffCallback);
    private IdeaAdapterListener ideaAdapterListener;

    public IdeaAdapter(IdeaAdapterListener ideaAdapterListener){
        this.ideaAdapterListener = ideaAdapterListener;
    }


    private static final DiffUtil.ItemCallback<Idea> diffCallback = new DiffUtil.ItemCallback<Idea>() {
        @Override
        public boolean areItemsTheSame(@NonNull Idea oldItem, @NonNull Idea newItem) {
            return oldItem.getKey().compareToIgnoreCase(newItem.getKey()) == 0;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Idea oldItem, @NonNull Idea newItem) {
            return oldItem.activity.compareToIgnoreCase(newItem.activity) == 0;
        }
    };

    public interface IdeaAdapterListener {
        void onIdeaShareClick(Idea idea);
    }

    @NonNull
    @Override
    public IdeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView =
               LayoutInflater.from(parent.getContext()).inflate(
                       R.layout.idea_preview,
                       parent,
                       false
               );

        return new IdeaViewHolder(itemView, ideaAdapterListener);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class IdeaViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.Title)
        TextView ideaTitle;

        @BindView(R.id.shareIdea)
        ImageView shareIdea;

        @BindView(R.id.hasLink)
        ImageView hasLink;

        @BindView(R.id.personAmount)
        TextView personAmount;
        @BindView(R.id.ideaTypeText)
        TextView ideaTypeText;
        @BindView(R.id.priceText)
        TextView priceText;

        @BindView(R.id.accessibilityText)
        TextView accessibilityText;

        @BindView(R.id.LinkTitle)
        TextView linkTitle;
        @BindView(R.id.linkText)
        TextView linkText;

        IdeaAdapterListener ideaAdapterListener;
        boolean isExpanded;

        public IdeaViewHolder(@NonNull View itemView, IdeaAdapterListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ideaAdapterListener = listener;

            isExpanded = false;
            linkText.setVisibility(View.GONE);
            linkTitle.setVisibility(View.GONE);
            hasLink.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull IdeaViewHolder holder, int position) {

        Idea idea = (Idea) differ.getCurrentList().get(position);

        holder.ideaTitle.setText(idea.activity);
        holder.personAmount.setText(String.valueOf(idea.participants));
        holder.ideaTypeText.setText(idea.type);
        holder.priceText.setText(String.valueOf(idea.price));
        holder.accessibilityText.setText(String.valueOf(idea.accessibility));

        holder.linkText.setVisibility(View.GONE);
        holder.linkTitle.setVisibility(View.GONE);
        holder.hasLink.setVisibility(View.GONE);

        if(!(idea.link.trim().compareToIgnoreCase("")==0)){
            holder.linkText.setText(idea.link);
            holder.linkText.setMovementMethod(LinkMovementMethod.getInstance());
            holder.linkText.setClickable(true);

            holder.linkText.setVisibility(View.VISIBLE);
            holder.linkTitle.setVisibility(View.VISIBLE);
            holder.hasLink.setVisibility(View.VISIBLE);
        }

        holder.shareIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ideaAdapterListener.onIdeaShareClick(idea);
            }
        });

    }


}
