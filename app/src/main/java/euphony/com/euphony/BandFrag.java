package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 24-06-2016.
 */
public class BandFrag extends Fragment implements RestClient.ResultReadyCallback {
    String curCity;
    RestClient restClient;
    RecyclerView bandsView;
    BandAdapter bandAdapter;
    TextView heading;
    RelativeLayout progress, firstTime;
    TextView loadText;
    PrefManager prefManager;

    public MainActivity mainActivity;

    RippleView cityBtn;
    public List<Band> bands = new ArrayList<Band>();

    Intent i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.bands_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadText = (TextView) getView().findViewById(R.id.loading_text);
        loadText.setText("Looking for Bands");
        progress = (RelativeLayout) getView().findViewById(R.id.progress);
        firstTime = (RelativeLayout) getView().findViewById(R.id.first_time);

        bandsView = (RecyclerView) getView().findViewById(R.id.band_view);
        bandsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        heading = (TextView) getActivity().findViewById(R.id.heading);
        curCity = mainActivity.curCity;

        restClient = RestClient.getInstance();
        prefManager = new PrefManager(Application.ctx);

        if(curCity.isEmpty()) {
            if(prefManager.getType().equals("artist")) {
                mainActivity.curCity = prefManager.getLocation();
                curCity = mainActivity.curCity;

                heading.setText(curCity);
                progress.setVisibility(View.VISIBLE);
                restClient.getBands(curCity, this);
            }
            else {
                firstTime.setVisibility(View.VISIBLE);
            }
        } else {
            heading.setText(curCity);
            progress.setVisibility(View.VISIBLE);
            restClient.getBands(curCity, this);
        }

        cityBtn = (RippleView) getActivity().findViewById(R.id.select_city);
        cityBtn.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                i = new Intent(getActivity(), CitySelectActivity.class);
                startActivityForResult(i, 1);
            }
        });
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
        if(id.equals("no_bands")) {
            progress.setVisibility(View.GONE);
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No Bands found for current city. Try another!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bans, int a) {
        if(bans != null && bans.size() != 0) {
            bands.addAll(bans);
            bandAdapter = new BandAdapter(bands, getActivity(), "");
            bandsView.setAdapter(bandAdapter);
            progress.setVisibility(View.GONE);
        }
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
                mainActivity.curCity = data.getStringExtra("city");
                curCity = mainActivity.curCity;

                heading.setText(curCity);
                progress.setVisibility(View.VISIBLE);
                firstTime.setVisibility(View.GONE);
                restClient.getBands(data.getStringExtra("city"), this);
            }
        }
    }
}
