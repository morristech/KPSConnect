package me.msfjarvis.kpsconnect;

import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.app.Notification;
import android.media.RingtoneManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "KPS Connect";
        String notificationText = "Test notification";

        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }

        Notification.Builder NotifyBldr = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager NotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        NotifyMgr.notify(1, NotifyBldr.build());
    }
}
