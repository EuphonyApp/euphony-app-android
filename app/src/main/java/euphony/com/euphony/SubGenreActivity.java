package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pR0 on 27-06-2016.
 */
public class SubGenreActivity extends AppCompatActivity {

    CardView addBtn, cancel;
    EditText subGenre1, subGenre2, subGenre3;
    List<String> subGenre = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.sub_genre_layout);

    addBtn = (CardView) findViewById(R.id.add_btn);
    cancel = (CardView) findViewById(R.id.cancel);
    subGenre1 = (EditText) findViewById(R.id.sub_genre1);
    subGenre2 = (EditText) findViewById(R.id.sub_genre2);
    subGenre3 = (EditText) findViewById(R.id.sub_genre3);

        if(getIntent() != null) {
            if(getIntent().getStringArrayExtra("list") != null) {
                subGenre = getIntent().getStringArrayListExtra("list");

                subGenre1.setText(subGenre.get(0));
                if (subGenre.size() >= 2)
                    subGenre2.setText(subGenre.get(1));
                if (subGenre.size() == 3)
                    subGenre3.setText(subGenre.get(2));
            }

            subGenre = new ArrayList<String>();

        }

    addBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(subGenre1.getText().toString().isEmpty() && subGenre2.getText().toString().isEmpty()
                    && subGenre3.getText().toString().isEmpty()) {

                SuperActivityToast.create(SubGenreActivity.this, "Please enter a Subgenre!!", SuperToast.Duration.SHORT,
                        Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
            } else {

                if(!subGenre1.getText().toString().isEmpty())
                    subGenre.add(subGenre1.getText().toString());

                if(!subGenre2.getText().toString().isEmpty())
                    subGenre.add(subGenre2.getText().toString());


                if(!subGenre3.getText().toString().isEmpty())
                    subGenre.add(subGenre3.getText().toString());

                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("data", (ArrayList<String>) subGenre);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    });
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    });
}
}
