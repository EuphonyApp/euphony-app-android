package euphony.com.euphony;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pR0 on 25-06-2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView artist, venue;
    FragmentTransaction fragmentTransaction;
    ArtistFormFrag artist_form = new ArtistFormFrag();
    VenueFormFrag venue_form = new VenueFormFrag();
    Bundle b = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        artist = (TextView) findViewById(R.id.artist);
        venue = (TextView) findViewById(R.id.venue);

        artist.setBackgroundColor(Color.parseColor("#20BAF5"));
        artist.setPadding(80, 20, 80, 20);
        venue.setBackgroundResource(R.drawable.round_rec_bound);
        venue.setPadding(80, 20, 80, 20);

        b.putString("for", "register");

        artist_form.setArguments(b);
        venue_form.setArguments(b);

        artist.setOnClickListener(this);
        venue.setOnClickListener(this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.form_container, artist_form);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        FragmentTransaction f = this.getSupportFragmentManager().beginTransaction();

        if(v == artist) {

            artist.setBackgroundColor(Color.parseColor("#20BAF5"));
            artist.setPadding(80, 20, 80, 20);
            venue.setBackgroundResource(R.drawable.round_rec_bound);
            venue.setPadding(80, 20, 80, 20);

            f.replace(R.id.form_container, artist_form);
        } else if(v == venue) {

            artist.setBackgroundResource(R.drawable.round_rec_bound);
            artist.setPadding(80, 20, 80, 20);
            venue.setBackgroundColor(Color.parseColor("#20BAF5"));
            venue.setPadding(80, 20, 80, 20);

            f.replace(R.id.form_container, venue_form);
        }

        f.commit();
    }
}