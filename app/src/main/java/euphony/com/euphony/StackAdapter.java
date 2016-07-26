package euphony.com.euphony;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 01-07-2016.
 */
public class StackAdapter extends ArrayAdapter<Booking> {

    private List<Booking> bookings;
    private Context context;
    private PrefManager prefManager;

    public StackAdapter(Context context, int textViewResourceId, List<Booking> bookings) {
        super(context, textViewResourceId, bookings);

        this.context = context;
        this.bookings = bookings;
        prefManager = new PrefManager(Application.ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.gigs_card, null);
        }

        TextView name = (TextView) v.findViewById(R.id.name), type = (TextView) v.findViewById(R.id.type),
                date = (TextView) v.findViewById(R.id.date), time = (TextView) v.findViewById(R.id.time);

        Booking b = bookings.get(position);

        if(b != null) {
            if(prefManager.getType().equals("artist")) {
                name.setText(b.getBookedBy());
                type.setText("Venue");
            } else {
                name.setText(b.getBooked());
                type.setText(b.getType());
            }

            date.setText(b.getDate());
            time.setText(b.getTime());
        }

        return v;
    }
}
