package euphony.com.euphony;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rey.material.widget.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener, RestClient.ResultReadyCallback {
    TabLayout tabLayout;
    TextView heading;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawerLayout;
    int prev = -1, save = 0;
    int total = 0;

    RippleView selectCity, addBand;
    boolean search;
    ImageView searchBtn, menu;
    EditText searchField;
    LinearLayout searchLay;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    public LocationProvider locationProvider;

    FloatingActionButton chatBtn;
    TextView numberMessages;

    ListenerService listenerService;
    public String curCity = "";

    BroadcastReceiver listener;

    LocationManager lm;
    PrefManager prefManager;
    RestClient restClient;
    Fragment frag;
    int ICONS[] = {R.drawable.ak, R.drawable.ic_time, R.drawable.ic_group, R.drawable.ic_logout};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        prefManager = new PrefManager(Application.ctx);
        restClient = RestClient.getInstance();

        restClient.setContext(getBaseContext());
        Log.e("ytpe", prefManager.getType());

        startService(new Intent(this, ListenerService.class));

        setOnGps();

        locationProvider = LocationProvider.getInstance();
        locationProvider.configureIfNeeded(this);

        if(prefManager.getType().equals("artist")) {
            Log.e("hgvhhjbhj", "pro");
            startLocationTracker();
        }

        chatBtn = (FloatingActionButton) findViewById(R.id.chat_btn);
        numberMessages = (TextView) findViewById(R.id.no_messages);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);
                startActivityForResult(i, 1);
            }
        });

        search = false;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerReview);
        recyclerView.setHasFixedSize(true);

        adapter = new MenuAdapter(ICONS, this, this);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        searchLay = (LinearLayout) findViewById(R.id.search_lay);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        searchField = (EditText) findViewById(R.id.searchField);
        searchBtn = (ImageView) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = true;
                searchBtn.setVisibility(View.GONE);
                searchLay.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.GONE);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        heading = (TextView) findViewById(R.id.heading);

        heading.setText("Nearby");

        selectCity = (RippleView) findViewById(R.id.select_city);
        selectCity.setVisibility(View.GONE);
        addBand = (RippleView) findViewById(R.id.add_band);
        addBand.setVisibility(View.GONE);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_nearby));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notify));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_user));

        if(prefManager.getType().equals("artist"))
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_band));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        heading.setText("Nearby");
                        searchBtn.setVisibility(View.VISIBLE);
                        addBand.setVisibility(View.GONE);
                        replaceFragment(new NearbyFrag());
                    }
                    break;
                    case 1: {
                        Log.e("clicked hre", "bt got profile");
                        heading.setText("Notifications");
                        searchBtn.setVisibility(View.GONE);
                        selectCity.setVisibility(View.GONE);
                        addBand.setVisibility(View.GONE);
                        replaceFragment(new NotificationFrag());
                    }
                    break;
                    case 2: {
                        heading.setText("Profile");
                        searchBtn.setVisibility(View.GONE);
                        selectCity.setVisibility(View.GONE);
                        addBand.setVisibility(View.GONE);
                        if (prefManager.getType().equals("artist"))
                            replaceFragment(ProfileFrag.newInstance(prefManager.getId(), "artist"));
                        else
                            replaceFragment(VenueProfFrag.newInstance(prefManager.getId()));
                    }
                    break;
                    case 3: {
                        heading.setText("My Bands");
                        searchBtn.setVisibility(View.GONE);
                        addBand.setVisibility(View.VISIBLE);
                        selectCity.setVisibility(View.GONE);
                        replaceFragment(new MyBandListFrag());
                    }
                    break;
                    default: {
                    }
                }
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.blue);
                tab.getIcon().mutate().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.white);
                tab.getIcon().mutate().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.blue);
        tabLayout.getTabAt(0).getIcon().mutate().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void recyclerViewClicked(String id, int clicked) {

        if(prev != -1)
            layoutManager.findViewByPosition(prev).findViewById(R.id.background).setSelected(false);

        //layoutManager.findViewByPosition(clicked).findViewById(R.id.background).setBackgroundColor(Color.parseColor("#1c1c1c"));
        drawerLayout.closeDrawer(GravityCompat.START);
        layoutManager.findViewByPosition(clicked).findViewById(R.id.background).setSelected(false);
        prev = clicked;
        if(clicked == 3) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            prefManager.clearSession();
            stopService(new Intent(MainActivity.this, MyFirebaseInstanceIDService.class));
            stopService(new Intent(MainActivity.this, ListenerService.class));
            if(alarmManager !=null)
                alarmManager.cancel(pendingIntent);
            startActivity(i);
            finish();
        } else if(clicked == 1) {
            Intent i = new Intent(MainActivity.this, EventsActivity.class);
            startActivity(i);
        } else if(clicked == 2) {
            Intent i = new Intent(MainActivity.this, FollowersActivity.class);
            startActivity(i);
        }
    }

    public void replaceFragment(Fragment f) {
        android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.main_container, f);
        fm.commit();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public void setOnGps() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

            if (isAirplaneModeOn(this)) {
                Log.e("airpalane", "enabled");
            } else {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        save = 1;
                        //get gps
                    }
                });
                dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                        paramDialogInterface.dismiss();
                        finish();

                    }
                });

                dialog.setMessage("Please enable GPS to use the app");
                dialog.setCancelable(false);
                dialog.setTitle("Cannot get Your Location!!");
                dialog.setIcon(R.drawable.ic_logo);
                dialog.show();
            }
        } else {
            locationProvider = LocationProvider.getInstance();
            locationProvider.configureIfNeeded(this);
            Log.e("location","starts herre");
            save = 1;
        }
    }

    private void startLocationTracker() {
        // Configure the LocationTracker's broadcast receiver to run every 5 minutes.
        Intent intent = new Intent(this, LocationTracker.class);
         alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
         pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                LocationProvider.FIVE_MINUTES, pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, MyFirebaseInstanceIDService.class));
        Intent i = new Intent(this, ListenerService.class);
        startService(i);

        restClient.getUnread(prefManager.getId(), this);

        listener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("euphony.com.euphony.MESSAGE")) {
                    int num = Integer.parseInt(numberMessages.getText().toString());
                    numberMessages.setText(String.valueOf(num + 1));
                    numberMessages.setVisibility(View.VISIBLE);
                    ++total;

                    Log.e("got message", "MainActitivyi");
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setAutoCancel(true)
                            .setContentTitle("New Message")
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
                    abortBroadcast();
                }
            }
        };

        IntentFilter f = new IntentFilter("euphony.com.euphony.MESSAGE");
        f.setPriority(3);
        registerReceiver(listener, f);
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
            Log.e("no of messsages", "" + i);
            numberMessages.setText(String.valueOf(i));

        if(i != 0)
            numberMessages.setVisibility(View.VISIBLE);
        else {
            numberMessages.setText("0");
            numberMessages.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        total = 0;

        IntentFilter f = new IntentFilter("euphony.com.euphony.MESSAGE");
        f.setPriority(3);
        registerReceiver(listener, f);

        if(save == 1) {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                locationProvider = LocationProvider.getInstance();
                locationProvider.configureIfNeeded(this);
                Log.e("location", "startes");

                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.main_container, new NearbyFrag()).commit();
                save = 0;
            } else
                setOnGps();

        }
    }
    @Override
    public void onPause() {

        super.onPause();

        unregisterReceiver(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == 5) {
                SuperActivityToast.create(this, "No Saved Conversations!!", SuperToast.Duration.SHORT,
                        Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                numberMessages.setText("0");
                numberMessages.setVisibility(View.INVISIBLE);
            }
        }
    }
}
