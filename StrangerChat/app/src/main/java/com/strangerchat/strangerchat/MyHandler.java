package com.strangerchat.strangerchat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.microsoft.windowsazure.notifications.NotificationsHandler;

/**
 * Created by Christian on 31-05-2015.
 */
public class MyHandler extends NotificationsHandler{

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    static public GuiActivity GUIActivity;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");

        if (nhMessage.contains("Stranger found")) {
            GUIActivity.getPersonChatrooms();

        }
        sendNotification(nhMessage);

        //GUIActivity.DialogNotify("Received Notification",nhMessage);
    }

    @Override
    public void onRegistered(Context context, String gcmRegistrationId) {
        super.onRegistered(context, gcmRegistrationId);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // if chat message update chatroom

        if (msg.contains("Message from"))
        {
            // send broadcast to update chat
            sendBroadcast("Msg",msg,msg.substring(13));

        }


        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, GuiActivity.class), 0);

        Vibrator v = (Vibrator) GUIActivity.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("StrangerChat")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendBroadcast(String intentTag, String msg, String fromName) {


        Intent broadcastIntent = new Intent (intentTag);
        broadcastIntent.putExtra("msg", msg);
        broadcastIntent.putExtra("fromname", fromName);

        LocalBroadcastManager.getInstance(ctx).sendBroadcast(broadcastIntent);

        }
    }
