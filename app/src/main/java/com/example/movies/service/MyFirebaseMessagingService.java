package com.example.movies.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.movies.MainActivity;
import com.example.movies.MainViewModel;
import com.example.movies.MovieDetailsActivity;
import com.example.movies.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String NOTIFICATION_CHANNEL_ID = "notifications";
    private LocalBroadcastManager broadcastManager;
    private String NOTIFICATION_GROUP = "com.example.string.NOTIFICATION_GROUP";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Intent intent = remoteMessage.toIntent();
        intent.setAction(remoteMessage.getNotification().getClickAction());
        notificationSettings(intent);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    private void notificationSettings(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return;
        String body = extras.getString("gcm.notification.body", "");
        String title = extras.getString("gcm.notification.title", "");
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("MOVIES_CHANNEL");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //main activity button intent
        Intent refreshIntent = new Intent(getApplicationContext(), MainViewModel.class);
        refreshIntent.putExtras(extras);
        PendingIntent refreshPendingIntent =
                PendingIntent.getActivity(this, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //movie list activity intent
        /**Intent movieListIntent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
        movieListIntent.putExtras(extras);
        PendingIntent listPendingIntent = PendingIntent.getActivity(this, 1, movieListIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        NotificationCompat.Builder mainActivityNotifier =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(defaultSound)
                        .setContentIntent(refreshPendingIntent)
                        .setGroup(NOTIFICATION_GROUP);
        /**NotificationCompat.Builder movieListNotifier =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(defaultSound)
                        .setContentIntent(listPendingIntent)
                        .setGroup(NOTIFICATION_GROUP);*/
        notificationManager.notify(0, mainActivityNotifier.build());
//        notificationManager.notify(1, movieListNotifier.build());
    }
}