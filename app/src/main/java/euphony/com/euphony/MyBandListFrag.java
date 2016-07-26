package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
 * Created by pR0 on 04-07-2016.
 */
public class MyBandListFrag extends Fragment implements RestClient.ResultReadyCallback {

    RestClient restClient;
    RecyclerView bandsView;
    BandAdapter bandAdapter;
    TextView no_band, firstBand;
    RelativeLayout loader;
    PrefManager prefManager;

    RippleView addBtn;
    public List<Band> bands = new ArrayList<Band>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.bands_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefManager = new PrefManager(Application.ctx);

        no_band = (TextView) getView().findViewById(R.id.no_band);
        firstBand = (TextView) getView().findViewById(R.id.first_band);

        bandsView = (RecyclerView) getView().findViewById(R.id.band_view);
        bandsView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addBtn = (RippleView) getActivity().findViewById(R.id.add_band);
        addBtn.setVisibility(View.GONE);

        loader = (RelativeLayout) getView().findViewById(R.id.progress);
        restClient = RestClient.getInstance();
        loader.setVisibility(View.VISIBLE);
        restClient.getBandList(prefManager.getId(), this);

        addBtn.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(getActivity(), BandRegisterActivity.class);
                i.putExtra("what", "register");
                startActivityForResult(i, 1);
            }
        });

        firstBand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BandRegisterActivity.class);
                i.putExtra("what", "register");
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
        if(id.equals("no_my_bands")) {
            no_band.setVisibility(View.VISIBLE);
            firstBand.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
            bandsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bans, int a) {
        if(bans != null) {
            bands.addAll(bans);
            bandAdapter = new BandAdapter(bands, getActivity(), "my");
            loader.setVisibility(View.GONE);
            bandsView.setAdapter(bandAdapter);
            bandsView.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.VISIBLE);
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

                bandsView.setVisibility(View.GONE);
                no_band.setVisibility(View.GONE);
                firstBand.setVisibility(View.GONE);
                loader.setVisibility(View.VISIBLE);
                restClient.getBandList(prefManager.getId(), this);
            }
        }
    }
}
