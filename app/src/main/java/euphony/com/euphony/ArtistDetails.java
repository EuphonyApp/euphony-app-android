package euphony.com.euphony;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

/**
 * Created by pR0 on 27-06-2016.
 */
public class ArtistDetails extends Fragment {

    TextView location,genre, subGenre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.artist_detail, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        location = (TextView) getView().findViewById(R.id.location);
        genre = (TextView) getView().findViewById(R.id.genre);
        subGenre = (TextView) getView().findViewById(R.id.sub_genre);

        location.setText(ProfileFrag.artist.getLocation());
        genre.setText(ProfileFrag.artist.getGenre());
        String temp = "";
        int i;
        for(i= 0; i < ProfileFrag.artist.getSubGenre().size() - 1; ++i) {
            temp += temp + ProfileFrag.artist.getSubGenre().get(i) + ",";
        }

        temp += ProfileFrag.artist.getSubGenre().get(i);
        subGenre.setText(temp);
    }
}
