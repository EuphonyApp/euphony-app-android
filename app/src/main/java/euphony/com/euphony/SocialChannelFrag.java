package euphony.com.euphony;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 04-07-2016.
 */
public class SocialChannelFrag extends Fragment implements View.OnClickListener, RestClient.ResultReadyCallback{

    BandRegisterActivity bandRegisterActivity;
    ImageView fb, twitter, utube, gPlus, sCloud, backPage, nextPage;
    EditText enterLink;
    TextView save;
    RestClient restClient;

    String clicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        bandRegisterActivity = (BandRegisterActivity) getActivity();
        return inflater.inflate(R.layout.social_links_form, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fb = (ImageView) getView().findViewById(R.id.fbLink);
        twitter = (ImageView) getView().findViewById(R.id.twitter_link);
        utube = (ImageView) getView().findViewById(R.id.utube_link);
        gPlus = (ImageView) getView().findViewById(R.id.gp_link);
        sCloud = (ImageView) getView().findViewById(R.id.sc_link);
        backPage = (ImageView) getView().findViewById(R.id.back_page);
        nextPage = (ImageView) getActivity().findViewById(R.id.next_page);

        ((ImageView) getActivity().findViewById(R.id.circle1)).setImageResource(R.drawable.circle_white);
        ((ImageView) getActivity().findViewById(R.id.circle2)).setImageResource(R.drawable.circle_white);
        ((ImageView) getActivity().findViewById(R.id.circle3)).setImageResource(R.drawable.circle_blue);


        restClient = RestClient.getInstance();

        enterLink = (EditText) getView().findViewById(R.id.enter_link);
        save = (TextView) getView().findViewById(R.id.save);

        enterLink.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        utube.setOnClickListener(this);
        gPlus.setOnClickListener(this);
        sCloud.setOnClickListener(this);
        backPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);

        super.onActivityCreated(savedInstanceState);
        if(bandRegisterActivity.band !=null) {
            if(bandRegisterActivity.band.getFb_page() != null && !bandRegisterActivity.band.getFb_page().isEmpty()) {
                fb.setAlpha(0.6f);
                fb.setClickable(false);
            }
            if(bandRegisterActivity.band.getTwitter() != null && !bandRegisterActivity.band.getTwitter().isEmpty()) {
                twitter.setAlpha(0.6f);
                twitter.setClickable(false);
            }
            if(bandRegisterActivity.band.getUtube() != null && !bandRegisterActivity.band.getUtube().isEmpty()) {
                utube.setAlpha(0.6f);
                utube.setClickable(false);
            }
            if(bandRegisterActivity.band.getGplus() != null && !bandRegisterActivity.band.getGplus().isEmpty()) {
                gPlus.setAlpha(0.6f);
                gPlus.setClickable(false);
            }
            if(bandRegisterActivity.band.getScloud() != null && !bandRegisterActivity.band.getScloud().isEmpty()) {
                sCloud.setAlpha(0.6f);
                sCloud.setClickable(false);
            }
        }

    }

    @Override
    public void onClick(View v) {
        enterLink.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
        if(v == fb)
            clicked = "fb";
        else if(v == twitter)
            clicked = "twitter";
        else if(v == utube)
            clicked = "utube";
        else if(v == gPlus)
            clicked = "gplus";
        else if(v == sCloud)
            clicked = "scloud";
        else if(v == save) {
            if(enterLink.getText().toString().isEmpty())
                SuperActivityToast.create(getActivity(), "Please enter a link!!", SuperToast.Duration.SHORT,
                        Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
            else {
                if(!enterLink.getText().toString().matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
                    SuperActivityToast.create(getActivity(), "Please enter a valid link!!", SuperToast.Duration.SHORT,
                            Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
                } else {
                    if(clicked.equals("fb")) {
                        bandRegisterActivity.band.setFb_page(enterLink.getText().toString());
                        fb.setAlpha(0.6f);
                        fb.setClickable(false);
                    }
                    if(clicked.equals("twitter")) {
                        bandRegisterActivity.band.setTwitter(enterLink.getText().toString());
                        twitter.setAlpha(0.6f);
                        twitter.setClickable(false);
                    }
                    if(clicked.equals("utube")) {
                        bandRegisterActivity.band.setUtube(enterLink.getText().toString());
                        utube.setAlpha(0.6f);
                        utube.setClickable(false);
                    }
                    if(clicked.equals("gplus")) {
                        bandRegisterActivity.band.setGplus(enterLink.getText().toString());
                        gPlus.setAlpha(0.6f);
                        gPlus.setClickable(false);
                    }
                    if(clicked.equals("scloud")) {
                        bandRegisterActivity.band.setScloud(enterLink.getText().toString());
                        sCloud.setAlpha(0.6f);
                        sCloud.setClickable(false);
                    }
                }
            }
        } else if(v == backPage) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.band_form_container, new AddArtistFrag()).commit();
        } else if(v == nextPage) {

            if(bandRegisterActivity.update.equals("update")) {
                restClient.updateBand(bandRegisterActivity.band, this);
            } else {
                restClient.createBand(getActivity(), bandRegisterActivity.band, this);
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
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
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
}
