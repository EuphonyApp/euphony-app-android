package euphony.com.euphony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pR0 on 30-06-2016.
 */
public class GridAdapter extends BaseAdapter {

    List<UserMin> users = new ArrayList<UserMin>();
    Context ctx;

    public GridAdapter(List<UserMin> users, Context context) {
        this.users = users;
        ctx = context;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CircleImageView pic;
        TextView name, type, genre;

        if(convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.follower_following_row, null);
        }

        pic = (CircleImageView) convertView.findViewById(R.id.profile_pic);
        name = (TextView) convertView.findViewById(R.id.name);
        type = (TextView) convertView.findViewById(R.id.type);
        genre = (TextView) convertView.findViewById(R.id.genre);

        if(!users.get(position).getPic().isEmpty())
            Picasso.with(ctx).load(users.get(position).getPic()).error(R.drawable.ak).into(pic);
        else
            pic.setImageResource(R.drawable.ak);

        name.setText(users.get(position).getName());
        type.setText(users.get(position).getType());
        genre.setText(users.get(position).getGenre());

        return convertView;
    }
}