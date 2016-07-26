package euphony.com.euphony;

import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 24-06-2016.
 */
public class ArtistFrag extends Fragment implements RestClient.ResultReadyCallback, TextWatcher {

    RecyclerView artist_view;
    ArtistAdapter adapter;
    List<Artist> artists = new ArrayList<Artist>();
    List<Artist> filteredArtists = new ArrayList<Artist>();
    RestClient restClient;
    PrefManager prefManager;
    RelativeLayout progress;
    Location current;

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.artist_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        artist_view = (RecyclerView) getView().findViewById(R.id.artist_view);
        artist_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        prefManager = new PrefManager(Application.ctx);

        restClient = RestClient.getInstance();
        progress = (RelativeLayout) getView().findViewById(R.id.progress);
        getActivity().findViewById(R.id.search).setVisibility(View.GONE);

        if(mainActivity.locationProvider != null)
            current = mainActivity.locationProvider.getCurrentLocation();
        else
            current = null;

        if(current != null) {
            progress.setVisibility(View.VISIBLE);
            restClient.getNearArtist(current.getLatitude(), current.getLongitude(), prefManager.getId(), this);
        } else {

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Cannot retrieve Location. Please come back later!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }

        restClient.getAllArtists(prefManager.getId(), this);
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
    public void resultReady(List<Artist> arts) {
        if(arts != null && arts.size() != 0) {
            ((ImageView) getActivity().findViewById(R.id.search)).setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            this.artists = arts;
            adapter = new ArtistAdapter(artists, getActivity());
            artist_view.setAdapter(adapter);
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.e("token", token);
            if(token != null) {
                restClient.updateGcm(prefManager.getId(), token, this);
            }
        } else {
            Log.e("Error in resltready", "" + arts.size());
        }
    }

    @Override
    public void resultReady(String id) {
        if(id.equals("no_nearby")) {
            progress.setVisibility(View.GONE);
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Sorry! No Artists Found in the vicinity!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();

            String token = FirebaseInstanceId.getInstance().getToken();
            Log.e("token", token);
            if(token != null) {
                restClient.updateGcm(prefManager.getId(), token, this);
            }

        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //loader here
        String str = s.toString();
        if(str.isEmpty()) {
            adapter = new ArtistAdapter(artists, getActivity());
            if(adapter == null)
                Log.e("hgfdsxdcfggd", "ftdghgmbh,");
            artist_view.setAdapter(adapter);
        }
        else {

            for (int i = 0; i < artists.size(); ++i) {
                if (artists.get(i).getName().contains(str))
                    filteredArtists.add(artists.get(i));
            }
            if(filteredArtists.size() != 0) {
                adapter = new ArtistAdapter(filteredArtists, getActivity());
                artist_view.setAdapter(adapter);
            } else {
                /*Snackbar.make(getActivity().findViewById(android.R.id.content), "No Artisrs Found!!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show(); */
                Log.e("err", ""+filteredArtists.size());

                //artist_view.removeAllViews();
            }
        }

        //loader delete here

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
