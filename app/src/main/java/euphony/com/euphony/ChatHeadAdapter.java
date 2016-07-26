package euphony.com.euphony;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 20-06-2016.
 */
public class ChatHeadAdapter extends RecyclerView.Adapter<ChatHeadAdapter.ChatHeadHolder> {

    private List<ChatDetails> chats;
    private static RecyclerViewClickListener clickListener;
    private int clicked = -1;

    Context ctx;

    public class ChatHeadHolder extends RecyclerView.ViewHolder {
        CircleImageView pic;
        TextView unread;
        RelativeLayout bg;

        public ChatHeadHolder(View v) {
            super(v);

            pic = (CircleImageView) v.findViewById(R.id.chat_head);
            unread = (TextView) v.findViewById(R.id.no_messages);
            bg = (RelativeLayout) v.findViewById(R.id.headBg);


        }
    }

    public ChatHeadAdapter(List<ChatDetails> chats, Context ctx, RecyclerViewClickListener clickListener) {
        this.ctx = ctx;
        this.chats  = chats;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public ChatHeadHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.chat_head, parent, false);

        return new ChatHeadHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatHeadHolder holder, final int position) {

        final ChatHeadHolder iholder = holder;
        holder.bg.setSelected(false);

        if(!chats.get(position).getPic().isEmpty())
            Picasso.with(ctx).load(chats.get(position).getPic()).error(R.drawable.ak).into(holder.pic);
        else
            holder.pic.setImageResource(R.drawable.ak);

        if(chats.get(position).getUnread() == 0)
            holder.unread.setVisibility(View.GONE);
        else {
            holder.unread.setVisibility(View.VISIBLE);
            holder.unread.setText(chats.get(position).getUnread());
        }

        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked = position;
                iholder.bg.setSelected(true);
                clickListener.recyclerViewClicked(chats.get(clicked).getId(), clicked);

            }
        });

        if(position == 0)
            holder.bg.performClick();
    }

}
