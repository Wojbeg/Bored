package com.wojbeg.boredapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.ui.Main.MainActivity;

public class IdeasListWidget extends BoredWidgetsProvider {

    private static final String TAG = IdeasListWidget.class.getSimpleName();

    public static final String LINK_ITEM = "LINK_ITEM";
    public static final String ACTION_OPEN_LINK = "ACTION_OPEN_LINK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ideas_list_widget);

        configureAdapter(context, views, appWidgetId);

        configureOnListClick(context, views);

        configureOpenMain(context, views);
        configureGetIdea(R.id.getNewIdeaWidgetBtn, IdeasListWidget.class, context, views);
        configureRefreshBtn(context, views, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ideasListViewWidget);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(ACTION_OPEN_LINK.equals(intent.getAction())){
            String link = intent.getStringExtra(LINK_ITEM);

            Intent openLink = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            openLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openLink);
        }

    }

    private static void configureAdapter(Context context, RemoteViews views, int appWidgetId){
        Intent adapter = new Intent(context, ListWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        adapter.setData(Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.ideasListViewWidget, adapter);
        views.setEmptyView(R.id.ideasListViewWidget, R.id.emptyInformation);

    }

    private static void configureOpenMain(Context context, RemoteViews views){
        final Intent startMain = new Intent(context, MainActivity.class);
        PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, 0, startMain, 0);
        views.setOnClickPendingIntent(R.id.start_app, openAppPendingIntent);
    }

    private static void configureRefreshBtn(Context context, RemoteViews views, int appWidgetId){
        final Intent refreshListWidget = new Intent(context, IdeasListWidget.class);
        refreshListWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        refreshListWidget.putExtra( AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId } );
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshListWidget, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.refreshBtn, refreshPendingIntent);
    }

    private static void configureOnListClick(Context context, RemoteViews views){
        Intent ideasClickIntent = new Intent(context, IdeasListWidget.class);
        ideasClickIntent.setAction(ACTION_OPEN_LINK);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 1, ideasClickIntent,0);
        views.setPendingIntentTemplate(R.id.ideasListViewWidget, clickPendingIntent);

    }

}
