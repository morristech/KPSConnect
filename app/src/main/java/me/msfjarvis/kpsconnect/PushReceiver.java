package me.msfjarvis.kpsconnect;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.app.Notification;
import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "KPS Connect";
        String notificationText = "Test notification";
        String urlToLoad = "https://khaitanpublicschool.com/blog";

        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }
        if (intent.getStringExtra("title") != null){
            notificationTitle = intent.getStringExtra("title");
        }
        if (intent.getStringExtra("url") != null){
            urlToLoad = intent.getStringExtra("url");
            Log.d("TAG",urlToLoad);
        }

        Notification.Builder NotifyBldr = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager NotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        NotifyMgr.notify(0,NotifyBldr.build());
    }
}
