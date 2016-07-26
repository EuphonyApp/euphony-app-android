package euphony.com.euphony;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 10-06-2016.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private int icons[];
    private String names[];
    public int selected = -1;
    Context ctx;
    RecyclerViewClickListener clickListener;
    PrefManager pref;

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView item;
        RelativeLayout bg;
        CircleImageView circleImageView;

        
        public ViewHolder(View v) {
            super(v);

            item = (ImageView) v.findViewById(R.id.menu_icon);
            bg = (RelativeLayout) v.findViewById(R.id.background);
            circleImageView = (CircleImageView) v.findViewById(R.id.pic);
        }
    }

    public MenuAdapter(int ICONS[], Context c, RecyclerViewClickListener clickListener) {
        icons = ICONS;
        ctx = c;
        this.clickListener = clickListener;
        pref = new PrefManager(Application.ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ViewHolder ihol = holder;

        if(position == 0) {

            holder.item.setVisibility(View.GONE);
            if(!pref.getPic().isEmpty())
                Picasso.with(ctx).load(pref.getPic()).error(R.drawable.ak).into(holder.circleImageView);
            else
                holder.circleImageView.setImageResource(R.drawable.ak);
        }

        else {

            holder.item.setImageResource(icons[position]);
            holder.circleImageView.setVisibility(View.GONE);
        }

        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position != 0) {
                    ihol.bg.setSelected(true);
                    clickListener.recyclerViewClicked("", position);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return icons.length ;
    }
}
