package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

/**
 * Created by pR0 on 27-06-2016.
 */
public class CitySelectActivity extends Activity {

    CardView continueBtn, cancel;
    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_activity);

        continueBtn = (CardView) findViewById(R.id.con_btn);
        cancel = (CardView) findViewById(R.id.cancel);
        city = (EditText) findViewById(R.id.city);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.getText().toString().isEmpty()) {

                    SuperActivityToast.create(CitySelectActivity.this, "Please enter a city name", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("city", city.getText().toString());
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
