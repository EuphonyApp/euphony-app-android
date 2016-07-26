package euphony.com.euphony;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by pR0 on 24-06-2016.
 */
public class NotificationFrag extends Fragment implements RestClient.ResultReadyCallback {

    RecyclerView notificationView;
    NotificationAdapter adapter;
    PrefManager prefManager;
    RelativeLayout progress;

    RestClient restClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.notification_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restClient = RestClient.getInstance();
        progress = (RelativeLayout) getView().findViewById(R.id.progress);

        prefManager = new PrefManager(Application.ctx);
        notificationView = (RecyclerView) getView().findViewById(R.id.notify_container);
        notificationView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progress.setVisibility(View.VISIBLE);
        restClient.getNotifications(prefManager.getId(), this);
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
        Log.e("reached here", "notify");
        progress.setVisibility(View.GONE);
        Snackbar.make(getActivity().findViewById(android.R.id.content), "No Notifications Yet!!", Snackbar.LENGTH_LONG)
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

    }

    @Override
    public void resultReady_booking(List<Booking> bookings) {

    }

    @Override
    public void resultReady_notify(List<Notifications> notifications) {
        if(notifications != null || notifications.size() != 0) {
            adapter = new NotificationAdapter(notifications, getActivity());
            notificationView.setAdapter(adapter);
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void resultReady_int(int i) {

    }
}