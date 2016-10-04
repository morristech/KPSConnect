package me.msfjarvis.kpsconnect.recievers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import me.msfjarvis.kpsconnect.R;

public class PushReceiver extends BroadcastReceiver {
    private String notificationTitle = "KPS Connect";
    private String notificationText = "Test notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }
        if (intent.getStringExtra("title") != null){
            notificationTitle = intent.getStringExtra("title");
        }
        Intent myIntent = new Intent("me.msfjarvis.kpsconnect.MainActivity");
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, myIntent, flags);
        Notification noti = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, noti);
    }
}
