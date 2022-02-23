package com.wojbeg.boredapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.wojbeg.boredapp.R;

public abstract class BoredWidgetsProvider extends AppWidgetProvider {

    public static final String CLICK_ACTION = "WIDGET_CLICKED";
    public static final String JUST_DELETE = "DELETE";

    protected static boolean serviceRunning = false;

    protected static void startGetIdeaService(Context context){
        Intent serviceIntent = new Intent(context, WidgetService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        }else{
            context.startService(serviceIntent);
        }
        serviceRunning = true;
    }

    protected static void stopGetIdeaService(Context context){
        context.stopService(new Intent(context, WidgetService.class));
        serviceRunning = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        switch (intent.getAction()){
            case CLICK_ACTION:

                if(!serviceRunning){
                    startGetIdeaService(context);
                }
                break;

            case JUST_DELETE:

                stopGetIdeaService(context);
                break;
        }

    }

    @Override
    public void onDisabled(Context context) {
        stopGetIdeaService(context);
        super.onDisabled(context);
    }

    public static void configureGetIdea(int idToClick, Class<? extends BoredWidgetsProvider> className, Context context, RemoteViews views){
        Intent intent = new Intent(context, className);
        intent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(idToClick, pendingIntent);

    }




}
