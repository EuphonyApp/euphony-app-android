package euphony.com.euphony;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pR0 on 28-06-2016.
 */
public class UpdateActivity extends AppCompatActivity {

    ImageView back;
    TextView type;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        back = (ImageView) findViewById(R.id.back);
        type = (TextView) findViewById(R.id.type);

        prefManager = new PrefManager(Application.ctx);

        type.setText(prefManager.getType().toUpperCase());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        if(prefManager.getType().equals("artist")) {
            android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            ArtistFormFrag artistFrag = new ArtistFormFrag();
            Bundle b = new Bundle();
            b.putString("for", "update");
            artistFrag.setArguments(b);

            fm.add(R.id.form_container, artistFrag);
            fm.commit();
        } else {
            android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            VenueFormFrag venueProfFrag = new VenueFormFrag();
            Bundle b = new Bundle();
            b.putString("for", "update");
            venueProfFrag.setArguments(b);

            fm.add(R.id.form_container, venueProfFrag);
            fm.commit();
        }
    }
}
