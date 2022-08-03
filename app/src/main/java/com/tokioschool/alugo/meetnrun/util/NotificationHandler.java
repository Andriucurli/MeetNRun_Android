package com.tokioschool.alugo.meetnrun.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.NotificationsActivity;

public class NotificationHandler {

    private static final String DEFAULT_CHANNEL_ID = "meetnrun_channel";
    private static final int APPOINTMENTCREATED_NOTIFICATIONID = 123;

    public static void generateChannel(Context context){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_ID, importance);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getNotificationManager(context);
                notificationManager.createNotificationChannel(channel);
            }
    }

    public static Notification generateSimpleNotification(Context context){

        Intent snoozeIntent = new Intent(context, NotificationsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(snoozeIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_48px)
                .setContentTitle("You have new notifications")
                .setContentText("Please, check your notifications, your appointments may have changes.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        return builder.build();
    }


    public static NotificationManager getNotificationManager(Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }



}
