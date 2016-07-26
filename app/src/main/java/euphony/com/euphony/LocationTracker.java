package euphony.com.euphony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.PowerManager;
import android.util.Log;

import java.util.List;

/**
 * Created by pR0 on 28-06-2016.
 */
public class LocationTracker extends BroadcastReceiver implements RestClient.ResultReadyCallback {
    PrefManager prefManager;

        private PowerManager.WakeLock wakeLock;

        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pow = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pow.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wakeLock.acquire();

            prefManager = new PrefManager(Application.ctx);
            LocationProvider locationProvider = LocationProvider.getInstance();
            locationProvider.configureIfNeeded(context);

            Location currentLocation = locationProvider.getCurrentLocation();

            if(currentLocation != null) {
                Log.e("Loc", "" + currentLocation.getLatitude());
                Log.e("Loc", "" + currentLocation.getLongitude());

                // Send new location to backend. // this will be different for you
                RestClient.getInstance().updateLocation(prefManager.getId(), (float) currentLocation.getLongitude(),
                        (float) currentLocation.getLatitude(), LocationTracker.this);
            }
        }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Band band) {

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
        if(wakeLock.isHeld())
            wakeLock.release();
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bands, int a) {

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
