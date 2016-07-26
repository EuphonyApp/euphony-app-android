package euphony.com.euphony;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Stack;

/**
 * Created by pR0 on 27-06-2016.
 */
public class DetailsActivity extends FragmentActivity {

    String id;
    ImageView back;
    Bundle b = new Bundle();
    ProfileFrag profileFrag;
    VenueProfFrag venueProfFrag;
    PrefManager prefManager;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        prefManager = new PrefManager(Application.ctx);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setBand("");
                finish();
            }
        });

        profileFrag = new ProfileFrag();
        venueProfFrag = new VenueProfFrag();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(getIntent() != null) {

            id = getIntent().getStringExtra("id");

            if(getIntent().getStringExtra("type").equals("artist")) {

                b.putString("type", "artist");
                b.putString("id", id);
                profileFrag.setArguments(b);

                fragmentTransaction.add(R.id.profile, profileFrag).commit();
            } else if(getIntent().getStringExtra("type").equals("band")) {

                b.putString("type", "band");
                b.putString("id", id);
                profileFrag.setArguments(b);

                fragmentTransaction.add(R.id.profile, profileFrag).commit();
            } else if(getIntent().getStringArrayExtra("type").equals("venue")) {

                b.putString("id", id);
                venueProfFrag.setArguments(b);

                fragmentTransaction.add(R.id.profile, venueProfFrag).commit();
            }
        }
    }
    public DatePickerDialog.OnDateSetListener ds =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        }
    };
}