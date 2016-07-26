package euphony.com.euphony;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pR0 on 21-06-2016.
 */
public class MsgsNotificationReciever extends BroadcastReceiver implements RestClient.ResultReadyCallback {

    int total = 0;
    RestClient restClient;
    @Override
    public void onReceive(Context context, Intent intent) {
        PrefManager prefManager = new PrefManager(Application.ctx);
        ++total;
        restClient = RestClient.getInstance();
        Message message = new Message(intent.getStringExtra("data"),prefManager.getId(), intent.getStringExtra("from"));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("You got new messages")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(total + " unread messages");

        Intent notificationIntent = new Intent(context, ChatActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent i = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(i);

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(1, mBuilder.build());
    }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Boolean t, String type) {

    }

    @Override
    public void resultReady(Venue venue) {

    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void resultReady(String id) {
        if(id.equals("saved")) {
            Log.e("saved", "success");
        }
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bands, int a) {

    }

    @Override
    public void resultReady(Band band) {

    }

    @Override
    public void resultReady(List<ChatDetails> chats, boolean x) {

    }

    @Override
    public void resultReady(List<Message> messages, String a) {

    }

    @Override
    public void resultReady_users(List<UserMin> users) {

    }

    @Override
    public void resultReady_booking(List<Booking> bookings) {

    }

    @Override
    public void resultReady_notify(List<Notifications> notifications) {

    }

    @Override
    public void resultReady_int(int i) {

    }
}
