package com.wojbeg.boredapp.widget;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.wojbeg.boredapp.App;
import com.wojbeg.boredapp.R;
import com.wojbeg.boredapp.model.Idea;
import com.wojbeg.boredapp.repo.local.Repository;
import com.wojbeg.boredapp.repo.remote.BoredApi;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class WidgetService extends Service {

    private static final String CHANNEL_ID = "NewsChannelID";
    private static final String CHANNEL_NAME = "NewsChannelName";
    private static final String CHANNEL_DESC = "Bored App news Widget";

    public static final String INTENT_FILTER = "boredapp.filter";

    private CompositeDisposable disposable;

    @Inject
    Context appContext;

    @Inject
    Repository repository;

    @Inject
    public WidgetService(){
    }

    private BroadcastReceiver receiver = null;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();

        disposable = new CompositeDisposable();
        createNotificationChannel();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_twotone_lightbulb_24)
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.downloadingData))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        startForeground(1, notification);

        disposable.add(
                repository.getIdea()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleResult, this::handleError
                        )
        );

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        disposable.dispose();

        deleteReceiver();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void deleteReceiver(){
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    private void handleResult(Idea idea){

        stopForeground(true);

        String contentText = getText(R.string.ideaParticipantsTitle) + String.valueOf(idea.participants) + ", " +
                getText(R.string.ideaTypeTitle) + idea.type + ", " + getText(R.string.ideaPriceTitle) + idea.price
                + ", " + getText(R.string.ideaAccessibilityTitle) + idea.accessibility;

        PendingIntent deletePendingIntent = getDeletePending();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                repository.insert(idea)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        disposable.add(d);
                                    }

                                    @Override
                                    public void onComplete() {
                                        try {
                                            handleSaved();
                                        } catch (PendingIntent.CanceledException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        try {
                                            handleSaveError(e);
                                        } catch (PendingIntent.CanceledException canceledException) {
                                            canceledException.printStackTrace();
                                        }
                                    }
                                }
                        );

            }
        };

        registerReceiver(receiver, new IntentFilter(INTENT_FILTER));

        Intent saveIntent = new Intent(INTENT_FILTER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, saveIntent, 0);



        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_twotone_lightbulb_24)
                .setContentTitle(idea.activity)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_twotone_blue_favorite_24, getString(R.string.saveIdea),
                        pendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);

    }

    private Intent getDeleteIntent(){

        Intent deleteIntent = new Intent(this, NewAppWidget.class);
        deleteIntent.setAction(NewAppWidget.JUST_DELETE);

        return deleteIntent;
    }

    private PendingIntent getDeletePending() {

        return PendingIntent.getBroadcast(this, 0, getDeleteIntent(), 0);
    }

    private void handleSaved() throws PendingIntent.CanceledException {

        clearNotification();
        Toast toastSaved = Toast.makeText(this, R.string.taskSaved, Toast.LENGTH_SHORT);
        toastSaved.setGravity(Gravity.CENTER, 0, 0);
        toastSaved.show();
        getDeletePending().send();

    }

    private void handleError(Throwable t) throws PendingIntent.CanceledException {

        clearNotification();
        Toast.makeText(this, getString(R.string.errorMessage), Toast.LENGTH_LONG).show();
        getDeletePending().send();
    }

    private void handleSaveError(Throwable t) throws PendingIntent.CanceledException {

        clearNotification();
        Toast.makeText(this, getString(R.string.taskNotSaved), Toast.LENGTH_LONG).show();
        getDeletePending().send();
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(CHANNEL_DESC);

            channel.setSound(null, null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void clearNotification(){
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        stopForeground(true);

    }


}

