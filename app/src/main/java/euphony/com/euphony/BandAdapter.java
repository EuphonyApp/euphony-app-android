package euphony.com.euphony;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 27-06-2016.
 */
public class BandAdapter extends RecyclerView.Adapter<BandAdapter.BandHolder> {

    List<Band> bands;
    Context context;
    String my = "";
    PrefManager prefManager;

    public class BandHolder extends RecyclerView.ViewHolder {
        TextView name, genre, members;
        CircleImageView pic;
        RippleView bandRipple;

        public BandHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            genre = (TextView) v.findViewById(R.id.genre);
            members = (TextView) v.findViewById(R.id.members);
            pic = (CircleImageView) v.findViewById(R.id.pic);

            bandRipple = (RippleView) v.findViewById(R.id.artist_ripple);

        }
    }

    public BandAdapter(List<Band> bands, Context ctx, String my) {
        this.bands = bands;
        context = ctx;
        this.my = my;
        prefManager = new PrefManager(Application.ctx);
    }

    @Override
    public int getItemCount() {
        return bands.size();
    }

    @Override
    public BandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bands_row, parent, false);
        return new BandHolder(v);
    }

    @Override
    public void onBindViewHolder(BandHolder holder, final int position) {
        holder.name.setText(bands.get(position).getName());
        holder.members.setText("" + bands.get(position).getMembers().size() + "members");
        holder.genre.setText(bands.get(position).getGenre());
        if(!bands.get(position).getPic().isEmpty())
            Picasso.with(context).load(bands.get(position).getPic()).error(R.drawable.ak).into(holder.pic);
        else
            holder.pic.setImageResource(R.drawable.ak);

        if(my.equals("my"))
            prefManager.setBand(bands.get(position).get_id());
        holder.bandRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("type", "band");
                i.putExtra("id", bands.get(position).get_id());

                context.startActivity(i);
            }
        });

    }
}
