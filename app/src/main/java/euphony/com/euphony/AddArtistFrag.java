package euphony.com.euphony;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pR0 on 04-07-2016.
 */
public class AddArtistFrag extends Fragment implements RecyclerViewClickListener, RestClient.ResultReadyCallback, TextWatcher, View.OnClickListener {

    public BandRegisterActivity bandRegisterActivity;
    EditText searchArtist;
    RecyclerView artistList;
    AddArtistAdapter adapter;
    String clicked = "";

    ImageView drummer, guitarist, bassist, vocalist, custom;
    ProgressView loader;
    ImageView nextPage, backPage;

    RestClient restClient;
    PrefManager prefManager;

    HashMap<String, String> members = new HashMap<String, String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        bandRegisterActivity = (BandRegisterActivity) getActivity();
        return inflater.inflate(R.layout.add_members, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchArtist = (EditText) getView().findViewById(R.id.member_search);
        searchArtist.addTextChangedListener(this);
        searchArtist.setVisibility(View.GONE);
        artistList = (RecyclerView) getView().findViewById(R.id.artist_list);
        artistList.setLayoutManager(new LinearLayoutManager(getActivity()));
        artistList.setVisibility(View.GONE);

        ((ImageView) getActivity().findViewById(R.id.circle1)).setImageResource(R.drawable.circle_white);
        ((ImageView) getActivity().findViewById(R.id.circle2)).setImageResource(R.drawable.circle_blue);
        ((ImageView) getActivity().findViewById(R.id.circle3)).setImageResource(R.drawable.circle_white);

        drummer = (ImageView) getView().findViewById(R.id.drummer);
        guitarist = (ImageView) getView().findViewById(R.id.guitarist);
        vocalist = (ImageView) getView().findViewById(R.id.vocalist);
        bassist = (ImageView) getView().findViewById(R.id.bassist);
        custom = (ImageView) getView().findViewById(R.id.custom);

        loader = (ProgressView) getView().findViewById(R.id.progress);
        loader.setVisibility(View.GONE);

        nextPage = (ImageView) getView().findViewById(R.id.next_page);
        backPage = (ImageView) getView().findViewById(R.id.back_page);

        nextPage.setOnClickListener(this);
        backPage.setOnClickListener(this);
        drummer.setOnClickListener(this);
        guitarist.setOnClickListener(this);
        bassist.setOnClickListener(this);
        custom.setOnClickListener(this);
        vocalist.setOnClickListener(this);

        backPage.setVisibility(View.VISIBLE);
        nextPage.setVisibility(View.GONE);
        restClient = RestClient.getInstance();

        prefManager = new PrefManager(Application.ctx);

        if(bandRegisterActivity.update.equals("update")) {
            if(bandRegisterActivity.band.getPositions().contains("drummer")) {
                drummer.setBackgroundResource(R.drawable.circle_white);
                drummer.setClickable(false);
                nextPage.setVisibility(View.VISIBLE);
            }
            if(bandRegisterActivity.band.getPositions().contains("guitarist")) {
                guitarist.setBackgroundResource(R.drawable.circle_white);
                guitarist.setClickable(false);
                nextPage.setVisibility(View.VISIBLE);
            }
            if(bandRegisterActivity.band.getPositions().contains("bassist")) {
                bassist.setBackgroundResource(R.drawable.circle_white);
                bassist.setClickable(false);
                nextPage.setVisibility(View.VISIBLE);
            }
            if(bandRegisterActivity.band.getPositions().contains("vocalist")) {
                vocalist.setBackgroundResource(R.drawable.circle_white);
                vocalist.setClickable(false);
                nextPage.setVisibility(View.VISIBLE);
            }
            if(bandRegisterActivity.band.getPositions().contains("custom")) {
                custom.setBackgroundResource(R.drawable.circle_white);
                custom.setClickable(false);
                nextPage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void recyclerViewClicked(String id, int click) {
        artistList.setVisibility(View.INVISIBLE);
        searchArtist.setVisibility(View.INVISIBLE);
        nextPage.setVisibility(View.VISIBLE);

        if(!clicked.isEmpty()) {
                bandRegisterActivity.band.getMembers().add(id);

            if(clicked.equals("drummer")) {
                bandRegisterActivity.band.getPositions().add("drummer");
                drummer.setBackgroundResource(R.drawable.circle_white);
                drummer.setClickable(false);
            }
            else if(clicked.equals("vocalist")) {
                bandRegisterActivity.band.getPositions().add("vocalist");
                vocalist.setBackgroundResource(R.drawable.circle_white);
                vocalist.setClickable(false);
            }
            else if(clicked.equals("bassist")) {
                bandRegisterActivity.band.getPositions().add("bassist");
                bassist.setBackgroundResource(R.drawable.circle_white);
                bassist.setClickable(false);
            }
            else if(clicked.equals("guitarist")) {
                bandRegisterActivity.band.getPositions().add("guitarist");
                guitarist.setBackgroundResource(R.drawable.circle_white);
                guitarist.setClickable(false);
            }
            else if(clicked.equals("custom")) {
                bandRegisterActivity.band.getPositions().add("custom");
                custom.setBackgroundResource(R.drawable.circle_white);
                custom.setClickable(false);
            }
        }
    }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Band band) {

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
        if(id.equals("not_found")) {
            artistList.setVisibility(View.INVISIBLE);
            loader.setVisibility(View.GONE);
        }
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bands, int a) {

    }

    @Override
    public void resultReady(List<ChatDetails> chats, boolean x) {

    }

    @Override
    public void resultReady(List<Message> messages, String a) {

    }

    @Override
    public void resultReady_users(List<UserMin> users) {
        if(users != null) {
            adapter = new AddArtistAdapter(users, getActivity(), this);
            artistList.setAdapter(adapter);
            artistList.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchString = s.toString();
        if(searchString.isEmpty())
            artistList.setVisibility(View.INVISIBLE);
        loader.setVisibility(View.VISIBLE);

        restClient.getArtistByName(searchString, prefManager.getId(), this);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if(v == drummer) {
            clicked = "drummer";
            searchArtist.setVisibility(View.VISIBLE);
        } else if(v == vocalist) {
            clicked = "vocalist";
            searchArtist.setVisibility(View.VISIBLE);
        } else if(v == bassist) {
            clicked = "bassist";
            searchArtist.setVisibility(View.VISIBLE);
        } else if(v == guitarist) {
            clicked = "guitarist";
            searchArtist.setVisibility(View.VISIBLE);
        } else if(v == custom) {
            clicked = "custom";
            searchArtist.setVisibility(View.VISIBLE);
        } else if(v == backPage) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.band_form_container, new BandFormFrag()).commit();
        } else if(v == nextPage) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.band_form_container, new SocialChannelFrag()).commit();
        }
    }
}
