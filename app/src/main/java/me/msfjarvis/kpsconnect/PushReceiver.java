package me.msfjarvis.kpsconnect;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {
    String notificationTitle = "KPS Connect";
    String notificationText = "Test notification";
    String urlToLoad = "https://khaitanpublicschool.com/blog";

    @Override
    public void onReceive(Context context, Intent intent) {

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
        Intent myIntent = new Intent(context, BlogActivity.class);
        myIntent.putExtra("url",urlToLoad);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, myIntent, flags);
        Notification noti = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_arrow_back_black_24dp)
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
