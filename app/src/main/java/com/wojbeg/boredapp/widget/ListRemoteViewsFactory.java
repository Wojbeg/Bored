package com.wojbeg.boredapp.widget;

import static com.wojbeg.boredapp.widget.IdeasListWidget.LINK_ITEM;

import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wojbeg.boredapp.App;
import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.repo.local.Repository;
import com.wojbeg.boredapp.ui.Main.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.subscribers.BlockingSubscriber;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private List<Idea> ideasList = new ArrayList<Idea>();
    private CompositeDisposable disposable = new CompositeDisposable();

    Repository repository;

    Context context;

    private int appWidgetId;

    public ListRemoteViewsFactory(Context context, Repository repository, Intent intent) {
        this.context = context;
        this.repository = repository;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        getIdeas();
    }

    private void getIdeas(){
        ideasList.clear();

        ideasList = repository.getSavedIdeas().blockingFirst();

    }

    @Override
    public void onDataSetChanged() {
        getIdeas();
    }

    @Override
    public void onDestroy() {
        disposable.clear();
        ideasList.clear();
    }

    @Override
    public int getCount() {
        return ideasList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.idea_widget_preview);

        Idea idea = ideasList.get(position);

        if(idea != null){

            views.setTextViewText(R.id.ideaTitleWidget, idea.activity);
            views.setTextViewText(R.id.personAmountWidget, ": "+String.valueOf(idea.participants));
            views.setTextViewText(R.id.ideaTypeTextWidget, idea.type);
            views.setTextViewText(R.id.priceTextWidget, String.valueOf(idea.price));
            views.setTextViewText(R.id.accessibilityTextWidget, String.valueOf(idea.accessibility));

            if(idea.link.trim().compareToIgnoreCase("")==0){
                views.setViewVisibility(R.id.hasLinkWidget, View.GONE);
            }else{
                views.setViewVisibility(R.id.hasLinkWidget, View.VISIBLE);

                Intent onClickOpenBrowserFillIntent = new Intent();
                onClickOpenBrowserFillIntent.putExtra(LINK_ITEM, idea.link);
                views.setOnClickFillInIntent(R.id.ideaWidget, onClickOpenBrowserFillIntent);
            }

        }

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
