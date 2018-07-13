package midas.coinmarket.controller.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import midas.coinmarket.R;
import midas.coinmarket.SplashActivity;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Create notification", "Start create notification");
        createNotification(context);
    }

    private void createNotification(Context context) {
        Intent resultIntent = new Intent(context, SplashActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setContentText(context.getString(R.string.msg_content_notification));
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        mNotifyMgr.notify((int) System.currentTimeMillis(), mBuilder.build());
    }
}
