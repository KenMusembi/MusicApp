package com.nextken.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;

public class CreateNotification {
    public static final String CHANNEL_ID = "channel";
    public static final String ACTIONPREVIOUS = "actionprevious";
    public static final String CHANNEL_PLAY = "actionplay";
    public static final String CHANNEL_NEXT  = "actionnext";

    public  static Notification notification;

    public static void createNotification(Context context, File items, int playbutton, int pos, int size){
        if(Build.VERSION.SDK_INT>+Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.music_playing)
                    .setContentTitle("Giddem")
                    .setContentText("Burna Boy")
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
    }
}
