package euphony.com.euphony;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.RowId;
import java.util.List;

/**
 * Created by pR0 on 22-06-2016.
 */
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> {

    public List<Message> messages;
    public String cur_id;
    PrefManager prefManager;

    public class ChatsViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        CardView back;

        public ChatsViewHolder(View v) {
            super(v);

            message = (TextView) v.findViewById(R.id.message);
            back = (CardView) v.findViewById(R.id.back);
        }
    }

    public ChatsAdapter(List<Message> messages, String cur_id) {
        this.messages = messages;
        this.cur_id = cur_id;
        prefManager = new PrefManager(Application.ctx);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ChatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatsViewHolder holder, int position) {

        if(messages.get(position).getFrm().equals(cur_id)) {
            holder.back.setCardBackgroundColor(Color.parseColor("#20baf5"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.back.setLayoutParams(params);
            holder.message.setTextColor(Color.parseColor("#1d1d1d"));

        } else {
            holder.back.setCardBackgroundColor(Color.parseColor("#1d1d1d"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.back.setLayoutParams(params);
            holder.message.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.message.setText(messages.get(position).getData());
    }
}