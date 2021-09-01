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
            notificationChannel.setGroup(NOTIFICATION_GROUP);
        }
        Intent mainIntent = new Intent(getApplicationContext(), MainViewModel.class);
        intent.putExtras(extras);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mainActivityNotifier =
                new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent);

        notificationManager.notify(1, mainActivityNotifier.build());
    }
}