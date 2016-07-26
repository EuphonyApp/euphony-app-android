package euphony.com.euphony;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.location.places.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.http.HTTP;

/**
 * Created by pR0 on 25-06-2016.
 */
public class VenueFormFrag extends Fragment implements RestClient.ResultReadyCallback, AdapterView.OnItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    EditText name, max, min, contact;
    TextView continueBtn;
    ImageView getPic;
    RestClient restClient;
    PrefManager pref;
    GifImageView loading;

   AutoCompleteTextView location;

    Venue self = new Venue();
    String temp;


    private static final  String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "";

    public PlacesAutoCompleteAdapter mAdapter;

    HandlerThread mHandlerThread;
    Handler mThreadHandler;

    public VenueFormFrag() {
        // Required empty public constructor

        if (mThreadHandler == null) {
            // Initialize and start the HandlerThread
            // which is basically a Thread with a Looper
            // attached (hence a MessageQueue)
            mHandlerThread = new HandlerThread(TAG, android.os.Process.THREAD_PRIORITY_BACKGROUND);
            mHandlerThread.start();
            Log.e("startees", "thread");

            // Initialize the Handler
            mThreadHandler = new Handler(mHandlerThread.getLooper()) {
                @Override
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 1) {
                        ArrayList<String> results = mAdapter.resultList;

                        if (results != null && results.size() > 0) {
                            mAdapter.notifyDataSetChanged();
                        }
                        else {
                            mAdapter.notifyDataSetInvalidated();
                        }
                    }
                }
            };
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp = getArguments().getString("for");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.venue_form_frag, viewGroup, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = (EditText) getView().findViewById(R.id.name);
        location = (AutoCompleteTextView) getView().findViewById(R.id.location);
        mAdapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplte_list);
        location.setAdapter(mAdapter);

        loading = (GifImageView) getView().findViewById(R.id.loader);

        location.setThreshold(2);
        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            }
        });

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String value = s.toString();

                // Remove all callbacks and messages
                mThreadHandler.removeCallbacksAndMessages(null);

                // Now add a new one
                mThreadHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Background thread

                        mAdapter.resultList = mAdapter.mPlaceAPI.autocomplete(value);

                        // Footer
                        if (mAdapter.resultList.size() > 0)
                            mAdapter.resultList.add("footer");

                        // Post to Main Thread
                        mThreadHandler.sendEmptyMessage(1);
                    }
                }, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //doAfterTextChanged();
            }
        });;

        max = (EditText) getView().findViewById(R.id.max_capacity);
        min = (EditText) getView().findViewById(R.id.min_capacity);
        contact = (EditText) getView().findViewById(R.id.contact);

        getPic = (ImageView) getView().findViewById(R.id.getPic);

        continueBtn = (TextView) getView().findViewById(R.id.con_btn);

        restClient = RestClient.getInstance();
        pref = new PrefManager(Application.ctx);

        if (temp.equals("update")) {
            continueBtn.setText("Update");
            restClient.getVenue(pref.getId(), this);

        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString(), locText = location.getText().toString(), maxText = max.getText().toString(),
                        minText = min.getText().toString(), contactText = contact.getText().toString();
                if (nameText.isEmpty() || locText.isEmpty() || maxText.isEmpty() || minText.isEmpty() || contactText.isEmpty()) {
                    SuperActivityToast.create(getActivity(), "Please fill the form completely!", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                } else {

                    if (!contactText.matches("^[789]\\d{9}$"))
                        SuperActivityToast.create(getActivity(), "Please enter valid mobile number!", SuperToast.Duration.SHORT,
                                Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                    else {
                        if (temp.equals("update")) {
                            self.setLocation(locText);
                            self.setName(nameText);
                            self.setMaxCapacity(maxText);
                            self.setMinCapacity(minText);
                            self.setContacts(contactText);

                            pref.setLocation(locText);
                            pref.setName(nameText);

                            restClient.updateVenue(self, VenueFormFrag.this);
                        } else {
                            Venue venue = new Venue();
                            HashMap<String, String> details = pref.getUserDetails();
                            pref.setType("venue");

                            venue.setEmail(details.get("email"));
                            venue.setF_id(details.get("f_id"));
                            venue.setG_id(details.get("g_id"));
                            venue.setPic(details.get("pic"));
                            venue.setLocation(locText);
                            venue.setMinCapacity(minText);
                            venue.setMaxCapacity(maxText);
                            venue.setContacts(contactText);
                            venue.setName(nameText);

                            pref.setLocation(locText);

                            continueBtn.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            restClient.createVenue(getActivity(), venue, VenueFormFrag.this);
                        }
                    }
                }
            }
        });
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

    }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Boolean t, String type) {

    }

    @Override
    public void resultReady(Venue venue) {
        if (venue != null) {
            self = venue;

            name.setText(venue.getName());
            location.setText(venue.getLocation());
            max.setText(venue.getMaxCapacity());
            min.setText(venue.getMinCapacity());
            contact.setText(venue.getContacts());
        }
    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void resultReady(String id) {
        if (id != null) {
            if (id.equals("updated")) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            } else if(id.equals("error")){

                loading.setVisibility(View.GONE);
                continueBtn.setVisibility(View.VISIBLE);

                SuperActivityToast.create(getActivity(), "Error! Please Try Again", SuperToast.Duration.SHORT,
                        Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
            } else {
                pref.setId(id);
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        }

    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bands, int a) {

    }

    @Override
    public void resultReady(Band band) {

    }

    @Override
    public void resultReady(List<ChatDetails> chats, boolean x) {

    }

    @Override
    public void resultReady(List<Message> messages, String a) {

    }

    @Override
    public void resultReady_users(List<UserMin> users) {

    }

    @Override
    public void resultReady_booking(List<Booking> bookings) {

    }

    @Override
    public void resultReady_notify(List<Notifications> notifications) {

    }

    @Override
    public void resultReady_int(int i) {

    }

    class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

        ArrayList<String> resultList;

        Context mContext;
        int mResource;

        PlaceAPI mPlaceAPI = new PlaceAPI();

        public PlacesAutoCompleteAdapter(Context context, int resource) {
            super(context, resource);

            mContext = context;
            mResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (position != (resultList.size() - 1))
                view = inflater.inflate(R.layout.autocomplte_list, null);
            else
                view = inflater.inflate(R.layout.autocomplete_google_logo, null);
            //}
            //else {
            //    view = convertView;
            //}

            if (position != (resultList.size() - 1)) {
                TextView autocompleteTextView = (TextView) view.findViewById(R.id.text_list);
                autocompleteTextView.setText(resultList.get(position));
            }
            else {
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                // not sure what to do <img draggable="false" class="emoji" alt="" src="https://s.w.org/images/core/emoji/72x72/1f600.png">
            }

            return view;
        }

        @Override
        public int getCount() {
            // Last item will be the footer
            return resultList.size();
        }

        @Override
        public String getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        Log.e("reach here", "filter");

                        resultList = mPlaceAPI.autocomplete(constraint.toString());

                        resultList.add("footer");
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };

            return filter;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Get rid of our Place API Handlers
        if (mThreadHandler != null) {
            mThreadHandler.removeCallbacksAndMessages(null);
            mHandlerThread.quit();
        }
    }
}
