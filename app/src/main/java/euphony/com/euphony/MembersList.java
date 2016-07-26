package euphony.com.euphony;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rey.material.widget.ProgressView;

import java.util.List;

/**
 * Created by pR0 on 27-06-2016.
 */
public class MembersList extends AppCompatActivity implements RestClient.ResultReadyCallback {

    ImageView back;
    RecyclerView members;
    RestClient restClient;
    RelativeLayout progressView;
    ArtistAdapter adapter;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.members_list_activity);

        progressView = (RelativeLayout) findViewById(R.id.progress);
        members = (RecyclerView) findViewById(R.id.members_list);
        members.setLayoutManager(new LinearLayoutManager(this));

        restClient = RestClient.getInstance();
        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressView.setVisibility(View.VISIBLE);
        restClient.getBandMembers(getIntent().getStringExtra("band_id"), this);
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
        adapter = new ArtistAdapter(artists, getApplicationContext());
        members.setAdapter(adapter);
        progressView.setVisibility(View.GONE);
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

    }
}
