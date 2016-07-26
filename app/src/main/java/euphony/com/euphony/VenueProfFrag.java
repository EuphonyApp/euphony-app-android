package euphony.com.euphony;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pR0 on 27-06-2016.
 */
public class VenueProfFrag extends Fragment implements RestClient.ResultReadyCallback, View.OnClickListener {

    String id;
    PrefManager manager;
    TextView name, type, location, capacity, contact, follow;
    ImageView edit, fb_link, backImage, chat;
    CircleImageView profile_pic;
    RestClient restClient;
    ColorMatrix matrix;
    ColorMatrixColorFilter filter;
    Venue venue;
    RelativeLayout progress;
    GifImageView followLoad;
    MapView mapView;
    GoogleMap map;
    LatLng loc;
    String link;

    // newInstance constructor for creating fragment with arguments
    public static VenueProfFrag newInstance(String id) {
        VenueProfFrag fragmentFirst = new VenueProfFrag();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
            id = getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {


        View v = inflater.inflate(R.layout.venue_profile, viewGroup, false);
        mapView = (MapView) v.findViewById(R.id.venue_map);
        mapView.onCreate(SavedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manager = new PrefManager(Application.ctx);
        restClient = new RestClient();

        name = (TextView) getView().findViewById(R.id.name);
        type = (TextView) getView().findViewById(R.id.type);
        location = (TextView) getView().findViewById(R.id.location);
        capacity = (TextView) getView().findViewById(R.id.capacity);
        contact = (TextView) getView().findViewById(R.id.contact);
        follow = (TextView) getView().findViewById(R.id.follow_btn);
        fb_link = (ImageView) getView().findViewById(R.id.fb_link);

        progress = (RelativeLayout) getView().findViewById(R.id.progress);

        backImage = (ImageView) getView().findViewById(R.id.back_image);
        profile_pic = (CircleImageView) getView().findViewById(R.id.pic);
        chat = (ImageView) getView().findViewById(R.id.chat);

        followLoad = (GifImageView) getView().findViewById(R.id.follow_load);
        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        filter = new ColorMatrixColorFilter(matrix);


        edit = (ImageView) getView().findViewById(R.id.edit);

        edit.setOnClickListener(this);
        chat.setOnClickListener(this);
        follow.setOnClickListener(this);

        progress.setVisibility(View.VISIBLE);
        restClient.getVenue(id, this);
    }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Band band) {

    }

    @Override
    public void resultReady(Boolean t, String type) {
        if(t) {
            if(type.equals("follow")) {
                follow.setText("Unfollow");
                follow.setBackgroundColor(Color.parseColor("#20baf5"));
            } else {
                follow.setText("Follow");
                follow.setBackgroundResource(R.drawable.round_rec_bound);
            }

            followLoad.setVisibility(View.GONE);
            follow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void resultReady(Venue venue) {
        if (venue != null) {
            this.venue = venue;

            loc = getLocationFromAddress(getActivity(), venue.getLocation());
            if(location != null) {
                MarkerOptions options = new MarkerOptions();
                options.position(loc);
                options.title(venue.getName());
                map.addMarker(options);

                CameraUpdate update = CameraUpdateFactory.newLatLng(loc);
                CameraUpdate updateZOom = CameraUpdateFactory.zoomBy(4);
                map.moveCamera(update);
                map.animateCamera(updateZOom);
            }

            name.setText(venue.getName());
            location.setText(venue.getLocation());
            type.setText("Venue");
            capacity.setText(venue.getMinCapacity() + " - " + venue.getMaxCapacity());
            contact.setText(venue.getContacts());

            if (!venue.getPic().isEmpty()) {
                Picasso.with(getActivity()).load(venue.getPic()).error(R.drawable.ak).into(profile_pic);
                Picasso.with(getActivity()).load(venue.getPic()).error(R.drawable.ak).into(backImage);
            } else {
                profile_pic.setImageResource(R.drawable.ak);
                backImage.setImageResource(R.drawable.ak);
            }


            backImage.setColorFilter(filter);
            Bitmap image = new BlurBuilder().blur(backImage);
            backImage.setImageDrawable(new BitmapDrawable(getActivity().getResources(), image));

            if (id.equals(manager.getId())) {
                follow.setVisibility(View.GONE);
                chat.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else {
                edit.setVisibility(View.GONE);
                chat.setVisibility(View.VISIBLE);
                restClient.followOrNot(manager.getId(), id, this);
            }
        }
    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void resultReady(String id) {
        if(id.equals("yes")) {
            follow.setText("Unfollow");
            follow.setBackgroundColor(Color.parseColor("#20baf5"));
            follow.setVisibility(View.VISIBLE);
        } else {
            follow.setText("Follow");
            follow.setBackgroundResource(R.drawable.round_rec_bound);
            follow.setVisibility(View.VISIBLE);
        }

        progress.setVisibility(View.GONE);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                restClient.getVenue(id, this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == edit) {
            Intent i = new Intent(getActivity(), UpdateActivity.class);
            startActivityForResult(i, 1);
        } else if(v == follow) {
            follow.setVisibility(View.GONE);
            followLoad.setVisibility(View.VISIBLE);

            if(follow.getText().toString().equals("Follow")) {
                restClient.follow(id, manager.getId(), this);
            } else if(follow.getText().toString().equals("Unfollow")) {
                restClient.unfollow(id, manager.getId(), this);
            }
        } else if(v == chat) {
            if (venue != null) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                ArrayList<String> a = new ArrayList<String>();
                a.add(venue.getPic());
                a.add(venue.getName());
                a.add(id);
                i.putStringArrayListExtra("details", a);
                startActivity(i);
            } else {
                Log.e("Venue", "found null");
            }
        } else if(v == fb_link) {
            link = "Facebook Page";
            show("fb");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = geocoder.getFromLocationName(strAddress, 4);
            if(address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }

    public void show(String type) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View linkVIew = li.inflate(R.layout.links_edit, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(linkVIew);
        final EditText linkBox = (EditText) linkVIew.findViewById(R.id.link);
        final TextView linkName = (TextView) linkVIew.findViewById(R.id.link_name);
        linkName.setText(link);
        linkBox.setText(venue.getFbPage());

        if(manager.getId().equals(id)) {
            builder.setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    venue.setFbPage(linkBox.getText().toString());
                    restClient.updateVenue(venue, VenueProfFrag.this);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            linkBox.setFocusable(false);
            linkBox.setEnabled(false);

            builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();;
    }
}
