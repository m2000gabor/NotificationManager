package com.example.android.databasebasic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationMaker {

    private String NOTIFICATION_CHANNEL_ID;

    public void setNOTIFICATION_CHANNEL_ID(String NOTIFICATION_CHANNEL_ID) {
        this.NOTIFICATION_CHANNEL_ID = NOTIFICATION_CHANNEL_ID;
    }

    public String getNOTIFICATION_CHANNEL_ID() {
        return NOTIFICATION_CHANNEL_ID;
    }


}
