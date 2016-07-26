package euphony.com.euphony;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 03-07-2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> implements RestClient.ResultReadyCallback {

    public List<Notifications> notifications;
    public Context context;
    RestClient restClient;
    int position = -1;
    PrefManager prefManager;

    public NotificationAdapter(List<Notifications> notifications, Context context) {
        this.context = context;
        this.notifications = notifications;
        restClient = RestClient.getInstance();
        prefManager = new PrefManager(Application.ctx);
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_row, parent, false);

        return new NotificationHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, final int position) {
        if(notifications.get(position).getOption().equals("no")) {
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        } else {
            holder.accept.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
        }

        if(notifications.get(position).getType().contains("follow") || notifications.get(position).getType().contains("jamming")||
                (notifications.get(position).getType().contains("booking") && prefManager.getType().equals("venue")))
            holder.route.setVisibility(View.GONE);

        holder.details.setText(notifications.get(position).getDetails());
        if(!notifications.get(position).getPic().isEmpty())
            Picasso.with(context).load(notifications.get(position).getPic()).error(R.drawable.ak).into(holder.pic);
        else
            holder.pic.setImageResource(R.drawable.ak);

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notifications.get(position).getType().contains("jamming")) {
                    restClient.acceptJam(notifications.get(position).get_id(), NotificationAdapter.this);
                } else if(notifications.get(position).getType().contains("booking")) {
                    restClient.acceptBooking(notifications.get(position).get_id(), NotificationAdapter.this);
                }
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notifications.get(position).getType().contains("jamming")) {
                    NotificationAdapter.this.position = position;
                    restClient.rejectJam(notifications.get(position).get_id(), NotificationAdapter.this);
                } else if(notifications.get(position).getType().contains("booking")) {
                    NotificationAdapter.this.position = position;
                    restClient.rejectBooking(notifications.get(position).get_id(), NotificationAdapter.this);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
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
        if(id.equals("no_jam_found")) {

        } else if(id.equals("no_booking_found")) {

        } else if(id.equals("booking_accepted")) {
            if(position != -1)
                notifications.remove(position);
        } else if(id.equals("booking_accepted")) {
            if(position != -1)
                notifications.remove(position);
        } else if(id.equals("jam_accepted")) {
            if(position != -1)
                notifications.remove(position);
        } else if(id.equals("jam_rejected")) {
            if(position != -1)
                notifications.remove(position);
        }

        notifyItemChanged(position);
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

    public class NotificationHolder extends RecyclerView.ViewHolder {
        CircleImageView pic;
        TextView details;
        ImageView accept, reject, route;

        public NotificationHolder(View v) {
            super(v);
            pic = (CircleImageView) v.findViewById(R.id.pic);
            details = (TextView) v.findViewById(R.id.details);
            accept = (ImageView) v.findViewById(R.id.accept);
            reject = (ImageView) v.findViewById(R.id.reject);

            route = (ImageView) v.findViewById(R.id.route);
        }
    }



}
