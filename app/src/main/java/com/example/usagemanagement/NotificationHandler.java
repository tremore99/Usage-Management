package com.example.usagemanagement;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "shop_notification_channel";
    private final int NOTIFICATION_ID = 0;

    private NotificationManager mManager;
    private Context mContext;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Usage Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.WHITE);
        channel.setDescription("Notifications from Usage application");

        this.mManager.createNotificationChannel(channel);
    }


    public void update(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID).setContentTitle("Usage application").setContentText(message + " meg lett változtatva").setSmallIcon(R.drawable.ic_baseline_info_24);

        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void delete(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID).setContentTitle("Usage application").setContentText(message + " törölve lett").setSmallIcon(R.drawable.ic_baseline_info_24);

        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

}
