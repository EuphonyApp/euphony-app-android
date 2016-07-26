package euphony.com.euphony;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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
 * Created by pR0 on 24-06-2016.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistHolder> {
    List<Artist> artists;
    Context context;

    public class ArtistHolder extends RecyclerView.ViewHolder {
        TextView name, genre, dist, type;
        CircleImageView pic;


        RippleView artistRipple;

        public ArtistHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            genre = (TextView) v.findViewById(R.id.genre);
            dist = (TextView) v.findViewById(R.id.dist);
            pic = (CircleImageView) v.findViewById(R.id.pic);

            type = (TextView) v.findViewById(R.id.type);

            artistRipple = (RippleView) v.findViewById(R.id.artist_ripple);

        }
    }

    public ArtistAdapter(List<Artist> artists, Context ctx) {
        this.artists = artists;
        context = ctx;
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_row, parent, false);
        return new ArtistHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, final int position) {
        holder.name.setText(artists.get(position).getName());
        holder.genre.setText(artists.get(position).getGenre());
        if(!artists.get(position).getPic().isEmpty())
            Picasso.with(context).load(artists.get(position).getPic()).error(R.drawable.ak).into(holder.pic);
        else
            holder.pic.setImageResource(R.drawable.ak);

        holder.type.setText(artists.get(position).getType());
        holder.dist.setText((artists.get(position).getDis()));

        holder.artistRipple.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(context, DetailsActivity.class);
                i.putExtra("type", "artist");
                i.putExtra("id", artists.get(position).getId());
                context.startActivity(i);
            }
        });
    }
}
