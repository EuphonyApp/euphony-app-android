package euphony.com.euphony;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by pR0 on 28-06-2016.
 */
public class LocationProvider {

    private static LocationProvider instance = null;
    private static Context context;

    public static final int ONE_MINUTE = 1000 * 60;
    public static final int FIVE_MINUTES = ONE_MINUTE * 5;

    private static Location currentLocation;

    private LocationProvider() {

    }

    public static LocationProvider getInstance() {
        if (instance == null) {
            instance = new LocationProvider();
        }

        return instance;
    }

    public void configureIfNeeded(Context ctx) {
        if (context == null) {
            context = ctx;
            configureLocationUpdates();
        }
    }

    private void configureLocationUpdates() {
        Log.e("location", "updates");
        final LocationRequest locationRequest = createLocationRequest();
        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                startLocationUpdates(googleApiClient, locationRequest);
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        googleApiClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }
        });

        googleApiClient.connect();
    }

    private static LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(FIVE_MINUTES);
        locationRequest.setSmallestDisplacement(0);
        return locationRequest;
    }

    private static void startLocationUpdates(GoogleApiClient client, LocationRequest request) {
        if (ActivityCompat.checkSelfPermission(Application.ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Application.ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                Log.e("hgchgchg", ""+location.getLatitude());
                Log.e("vgvhgvg", ""+location.getLongitude());
            }
        });
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }
}
