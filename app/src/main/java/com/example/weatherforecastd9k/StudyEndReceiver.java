package com.example.weatherforecastd9k;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class StudyEndReceiver extends BroadcastReceiver {
    public static final String ACTION_STUDY_END = "com.example.myapplication.ACTION_STUDY_END";
    private static final String CHANNEL_ID = "study_end_channel";
    private static final int NOTIFICATION_ID = 1;

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_STUDY_END.equals(intent.getAction())) {
            // Create notification
            createNotificationChannel(context);
            showNotification(context);
        }
    }

    /**
     * Create a notification channel, applicable to Android 8.0 and above
     * @param context Context
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Background music notification";
            String description = "Playing background music";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register channel
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Show notification
     * @param context Context
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private void showNotification(Context context) {
        // Create a jump intent after clicking the notification. Here it jumps to MainActivity, which can be modified according to the actual situation
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Background music notification")
                .setContentText("Playing background music")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}