package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pR0 on 25-06-2016.
 */

public class ArtistFormFrag extends Fragment implements RestClient.ResultReadyCallback {

    EditText type, location, contact;
    com.rey.material.widget.Spinner genre;
    TextView subGenre, continueBtn;
    RestClient restClient;
    PrefManager pref;
    String gnr = "";
    GifImageView loading;
    List<String> subGenreList = new ArrayList<String>();

    Artist self = new Artist();

    ArrayAdapter<String> dataAdapter;
    String all = "Select,Alternative Rock,Ambient,Blues,Classical,Country,Deep House," +
            "Drum & Bass,Electronic,Folk,Hip-Hop & Rap,Indie,Jazz,Metal,Pop,Reggae,Rock,Techno,Trap,Trip Hop,Other";

    List<String> spinnerList = new ArrayList<String>();

    String temp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        temp = getArguments().getString("for");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.artist_form_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinnerList = Arrays.asList(all.split(","));

        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_lay, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        loading = (GifImageView) getView().findViewById(R.id.loader);

        type = (EditText) getView().findViewById(R.id.type);
        location = (EditText) getView().findViewById(R.id.location);
        genre = (com.rey.material.widget.Spinner) getView().findViewById(R.id.genre);

        subGenre = (TextView) getView().findViewById(R.id.sub_genre);
        contact = (EditText) getView().findViewById(R.id.contact);

        subGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubGenreActivity.class);
                if(subGenreList.size() != 0)
                    i.putStringArrayListExtra("list", (ArrayList<String>) subGenreList);
                startActivityForResult(i, 1);
            }
        });
        genre.setAdapter(dataAdapter);
        genre.setOnItemSelectedListener(new com.rey.material.widget.Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(com.rey.material.widget.Spinner parent, View view, int position, long id) {
                gnr = parent.getSelectedItem().toString();
                if(gnr.equals("Select"))
                    gnr = "";
            }
        });

        continueBtn = (TextView) getView().findViewById(R.id.con_btn);

        restClient = RestClient.getInstance();
        pref = new PrefManager(Application.ctx);

        if(temp.equals("update")) {
            continueBtn.setText("Update");
            restClient.getArtist(pref.getId(), this);
        }

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeText = type.getText().toString(), locText = location.getText().toString(), genreText = gnr,
                        subGenreText = subGenre.getText().toString(), contactText = contact.getText().toString();
                if(typeText.isEmpty() || locText.isEmpty() || genreText.isEmpty() || subGenreText.isEmpty() || contactText.isEmpty()) {
                    SuperActivityToast.create(getActivity(), "Please fill the form completely!", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                } else {
                    if(!contactText.matches("^[789]\\d{9}$"))
                        SuperActivityToast.create(getActivity(), "Please enter valid mobile number!", SuperToast.Duration.SHORT,
                                Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();

                    else {
                        if(temp.equals("update")) {
                            self.setType(typeText);
                            self.setGenre(genreText);
                            self.setSubGenre(subGenreList);
                            self.setLocation(locText);
                            self.setContact(contactText);

                            pref.setLocation(locText);
                            restClient.updateArtist(self, ArtistFormFrag.this);

                        } else {
                            Artist artist = new Artist();
                            HashMap<String, String> details = pref.getUserDetails();
                            pref.setType("artist");

                            artist.setName(details.get("name"));
                            artist.setEmail(details.get("email"));
                            artist.setF_id(details.get("f_id"));
                            artist.setG_id(details.get("g_id"));
                            artist.setPic(details.get("pic"));
                            artist.setType(typeText);
                            artist.setGenre(genreText);
                            artist.setSubGenre(subGenreList);
                            artist.setLocation(locText);
                            artist.setContact(contactText);
                            artist.setUtube("");
                            artist.setFollowing(new ArrayList<String>());
                            artist.setFollowers(new ArrayList<String>());

                            pref.setLocation(locText);

                            continueBtn.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);
                            restClient.createArtist(getActivity(), artist, ArtistFormFrag.this);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void resultReady(Artist artist) {
        if(artist != null) {
            self = artist;
            subGenreList = artist.getSubGenre();

            type.setText(artist.getType());
            location.setText(artist.getLocation());
            genre.setSelection(spinnerList.indexOf(artist.getGenre()));
            contact.setText(artist.getContact());

            String temp = "";

            for(int i = 0; i < subGenreList.size(); ++i)
                temp += subGenreList.get(i) + " ";

            subGenre.setText(temp);
        }
    }

    @Override
    public void resultReady(Boolean t, String type) {

    }

    @Override
    public void resultReady(Venue venue) {

    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void resultReady(String id) {
        if(id != null) {
            if(id.equals("updated")) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {

            if(resultCode == Activity.RESULT_OK) {
                subGenreList = data.getStringArrayListExtra("data");
                String temp = "";

                for(int i = 0; i < subGenreList.size(); ++i)
                    temp += subGenreList.get(i) + " ";

                subGenre.setText(temp);
            }
        }
    }
}