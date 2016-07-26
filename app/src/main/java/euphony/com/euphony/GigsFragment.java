package euphony.com.euphony;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;

import java.util.List;

/**
 * Created by pR0 on 01-07-2016.
 */
public class GigsFragment extends Fragment implements RestClient.ResultReadyCallback {

    StackView gigsStack;
    PrefManager prefManager;
    StackAdapter stackAdapter;
    RestClient restClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {

        return inflater.inflate(R.layout.events_stack_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefManager = new PrefManager(Application.ctx);
        restClient = RestClient.getInstance();

        gigsStack = (StackView) getView().findViewById(R.id.gigs_layout);
        if(prefManager.getId().equals("artist")) {
            restClient.getBookingsArtist(prefManager.getId(), this);
        } else {
            restClient.getBookingsVenue(prefManager.getId(), this);
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
        if(bookings != null && bookings.size() != 0) {
            stackAdapter = new StackAdapter(getActivity(), R.layout.gigs_card, bookings);
            gigsStack.setAdapter(stackAdapter);
        }
    }

    @Override
    public void resultReady_notify(List<Notifications> notifications) {

    }

    @Override
    public void resultReady_int(int i) {

    }
}
