package midas.coinmarket.controller.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import midas.coinmarket.R;
import midas.coinmarket.SplashActivity;

public class FCMReceiveMessage extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("data")) {
                String jsonData = remoteMessage.getData().get("data");
                if (jsonData.length() > 0) {
                    sendNotification("Open Coin Market.");
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            sendNotification("Open Coin Market.");
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis() /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(getResources().getColor(R.color.green))
                .setContentTitle(getString(R.string.app_name) + "'s message.")
                .setContentIntent(pendingIntent)
                .setContentText(messageBody)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, builder.build());
    }
}