package euphony.com.euphony;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 04-07-2016.
 */
public class AddArtistAdapter extends RecyclerView.Adapter<AddArtistAdapter.AddArtistHolder> {

    List<UserMin> artists;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;

    public class AddArtistHolder extends RecyclerView.ViewHolder {
        TextView name, genre;
        CircleImageView pic;
        ImageView add;

        public AddArtistHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            genre = (TextView) v.findViewById(R.id.genre);
            pic = (CircleImageView) v.findViewById(R.id.pic);
            add = (ImageView) v.findViewById(R.id.add_btn);
        }
    }

    public AddArtistAdapter(List<UserMin> artists, Context ctx, RecyclerViewClickListener recyclerViewClickListener) {
        this.artists = artists;
        context = ctx;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    @Override
    public AddArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_artist_row, parent, false);
        return new AddArtistHolder(v);
    }

    @Override
    public void onBindViewHolder(AddArtistHolder holder, final int position) {
        if(!artists.get(position).getPic().isEmpty())
            Picasso.with(context).load(artists.get(position).getPic()).error(R.drawable.ak).into(holder.pic);
        else
            holder.pic.setImageResource(R.drawable.ak);

        holder.name.setText(artists.get(position).getName());
        holder.genre.setText(artists.get(position).getGenre());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("clovked", "gere");
                recyclerViewClickListener.recyclerViewClicked(artists.get(position).get_id(), 1);
            }
        });
    }
}
