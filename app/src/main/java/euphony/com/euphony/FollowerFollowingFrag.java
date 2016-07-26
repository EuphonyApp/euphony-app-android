package euphony.com.euphony;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 30-06-2016.
 */
public class FollowerFollowingFrag extends Fragment implements RestClient.ResultReadyCallback {

    public static FollowerFollowingFrag newInstance(String what) {
        FollowerFollowingFrag followerFollowingFrag = new FollowerFollowingFrag();
        Bundle args = new Bundle();
        args.putString("what", what);
        followerFollowingFrag.setArguments(args);
        return followerFollowingFrag;
    }

    String what;

    GridView gridView;
    GridAdapter gridAdapter;
    RestClient restClient;
    PrefManager prefManager;

    List<UserMin> users = new ArrayList<UserMin>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            what = getArguments().getString("what");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.followers_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefManager = new PrefManager(Application.ctx);
        restClient = RestClient.getInstance();

        gridView = (GridView) getView().findViewById(R.id.grid);
        if(what.equals("followers")) {
            restClient.getFollowers(prefManager.getId(), this);
        } else if(what.equals("following")) {
            restClient.getFollowing(prefManager.getId(), this);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailsActivity.class);
                String type = users.get(position).getType();

                if(type.equals("venue") || type.equals("band"))
                    i.putExtra("type", type);
                else
                    i.putExtra("type", "artist");

                i.putExtra("id", users.get(position).get_id());
                startActivity(i);
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

        getActivity().findViewById(R.id.progress).setVisibility(View.GONE);

        if(id.equals("no_followers"))
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No One Followed You yet!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        else if(id.equals("no_following"))
            Snackbar.make(getActivity().findViewById(android.R.id.content), "You Haven't Followed Anyone Yet!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
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
        if(users != null && users.size() != 0) {
            this.users = users;
            gridAdapter = new GridAdapter(users, getActivity());
            getActivity().findViewById(R.id.progress).setVisibility(View.GONE);
        }

        gridView.setAdapter(gridAdapter);
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
