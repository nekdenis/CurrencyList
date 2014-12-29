package com.github.nekdenis.currencylist.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.github.nekdenis.currencylist.R;
import com.github.nekdenis.currencylist.activity.MainActivity;

public class NotificationUtil {

    public static final int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private static final int WEATHER_NOTIFICATION_ID = 11;

    public static void showNewUpdateNotification(Context context, String notificatonMessage) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        long lastSync = prefs.getLong(Constants.PREFERENCES_LAST_NOTIFICATION_KEY, 0);

        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {

            String title = context.getString(R.string.app_name);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(notificatonMessage);

            Intent resultIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(WEATHER_NOTIFICATION_ID, mBuilder.build());


            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(Constants.PREFERENCES_LAST_NOTIFICATION_KEY, System.currentTimeMillis());
            editor.commit();
        }
    }
}