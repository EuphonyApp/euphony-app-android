package euphony.com.euphony;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pR0 on 27-06-2016.
 */
public class BandDetails extends Fragment {

    TextView location, genre, subGenre, date, manager, num_members, membersBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.band_details, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        location = (TextView) getView().findViewById(R.id.location);
        genre = (TextView) getView().findViewById(R.id.genre);
        subGenre = (TextView) getView().findViewById(R.id.sub_genre);
        date = (TextView) getView().findViewById(R.id.date);
        manager = (TextView) getView().findViewById(R.id.manager);
        num_members = (TextView) getView().findViewById(R.id.members);
        membersBtn = (TextView) getView().findViewById(R.id.members_btn);

        location.setText(ProfileFrag.band.getLocation());
        genre.setText(ProfileFrag.band.getGenre());
        date.setText(ProfileFrag.band.getFoundedOn());
        manager.setText(ProfileFrag.band.getManager());
        num_members.setText(ProfileFrag.band.getMembers().size());

        String temp = "";
        int i;
        for(i= 0; i < ProfileFrag.band.getSubgenre().size() - 1; ++i) {
            temp += temp + ProfileFrag.band.getSubgenre().get(i) + ",";
        }

        temp += ProfileFrag.band.getSubgenre().get(i);
        subGenre.setText(temp);

        membersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MembersList.class);
                i.putExtra("band_id", ProfileFrag.band.get_id());
                startActivity(i);
            }
        });
    }
}
