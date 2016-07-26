package euphony.com.euphony;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by pR0 on 01-07-2016.
 */
public class EventsActivity extends AppCompatActivity {

    ImageView back;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.gigs_container, new GigsFragment()).commit();
    }
}
