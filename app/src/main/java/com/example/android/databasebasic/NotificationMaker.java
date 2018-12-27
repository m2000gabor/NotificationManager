package com.example.android.databasebasic;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationMaker {

    private String NOTIFICATION_CHANNEL_ID ="nincs";

    public void setNOTIFICATION_CHANNEL_ID(String NOTIFICATION_CHANNEL_ID) {
        this.NOTIFICATION_CHANNEL_ID = NOTIFICATION_CHANNEL_ID;
    }

    public String getNOTIFICATION_CHANNEL_ID() {
        return NOTIFICATION_CHANNEL_ID;
    }

    void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && NOTIFICATION_CHANNEL_ID.equals("nincs")) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            String chId = "CHANNEL_ID";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(chId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NOTIFICATION_CHANNEL_ID=chId;
        }
    }
    void makeANoti(Context mthis){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(mthis, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mthis, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mthis, "CHANNEL_ID")
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mthis);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(33, mBuilder.build());
    }

    void MakeNotiAtTime(Context mThis,long timeInMillis){
        //pendingIntent
        Intent intent = new Intent(mThis, NotificationIntentService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mPendingIntent = PendingIntent.getService(mThis,0,intent,0);

        //setAlarm
        AlarmManager mAlarmManager = mThis.getSystemService(AlarmManager.class);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,timeInMillis+5,mPendingIntent);
    }

}
