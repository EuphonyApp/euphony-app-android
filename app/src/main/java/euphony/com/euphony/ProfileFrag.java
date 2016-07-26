package euphony.com.euphony;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.DatePicker;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by pR0 on 24-06-2016.
 */
public class ProfileFrag extends Fragment implements RestClient.ResultReadyCallback, View.OnClickListener, android.app.DatePickerDialog.OnDateSetListener, android.app.TimePickerDialog.OnTimeSetListener {

    ImageView backImage, profile_pic, fb, twitter, gplus, scloud, utube;
    String id, typeText;
    String day, time, global;
    RelativeLayout progress;
    GifImageView followLoad, bookLoad;

    // newInstance constructor for creating fragment with arguments
    public static ProfileFrag newInstance(String id, String type) {
        ProfileFrag fragmentFirst = new ProfileFrag();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("type", type);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    TextView name, type, follow, jam, book;
    ImageView chat, edit;

    LinearLayout other;
    PrefManager prefManager;


    android.app.DatePickerDialog datePickerDialog;
    android.app.TimePickerDialog timePickerDialog;

    Calendar c = Calendar.getInstance();

    public static Artist artist = new Artist();
    public static Band band = new Band();
    //public static Venue venue = new Venue();

    FragmentTransaction fragmentTransaction;
    RestClient restClient;

    ColorMatrix matrix;
    ColorMatrixColorFilter filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            id = getArguments().getString("id");
            typeText = getArguments().getString("type");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.profile_frag, viewGroup, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        progress = (RelativeLayout) getView().findViewById(R.id.progress);
        bookLoad = (GifImageView) getView().findViewById(R.id.book_load);
        followLoad = (GifImageView) getView().findViewById(R.id.follow_load);

        restClient = RestClient.getInstance();

        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        filter = new ColorMatrixColorFilter(matrix);

        backImage = (ImageView) getView().findViewById(R.id.back_image);
        profile_pic = (ImageView) getView().findViewById(R.id.profile_pic);

        name = (TextView) getView().findViewById(R.id.name);
        type = (TextView) getView().findViewById(R.id.type);
        edit = (ImageView) getView().findViewById(R.id.edit);

        fb = (ImageView) getView().findViewById(R.id.fbLink);
        twitter = (ImageView) getView().findViewById(R.id.twitter_link);
        utube = (ImageView) getView().findViewById(R.id.utube_link);
        scloud = (ImageView) getView().findViewById(R.id.sc_link);
        gplus = (ImageView) getView().findViewById(R.id.gp_link);

        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        utube.setOnClickListener(this);
        scloud.setOnClickListener(this);
        gplus.setOnClickListener(this);

        chat = (ImageView) getView().findViewById(R.id.chat);
        follow = (TextView) getView().findViewById(R.id.follow_btn);
        jam = (TextView) getView().findViewById(R.id.jam_btn);
        book = (TextView) getView().findViewById(R.id.book_btn);

        chat.setOnClickListener(this);
        follow.setOnClickListener(this);
        jam.setOnClickListener(this);
        book.setOnClickListener(this);

        prefManager = new PrefManager(Application.ctx);

        other = (LinearLayout) getView().findViewById(R.id.other_profile);

        if(typeText.equals("artist")) {
            progress.setVisibility(View.VISIBLE);
            restClient.getArtist(id, this);
        } else if(typeText.equals("band")) {
            progress.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
            jam.setVisibility(View.GONE);
            restClient.getBand(id, this);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeText.equals("artist")) {
                    Intent i = new Intent(getActivity(), UpdateActivity.class);
                    startActivityForResult(i, 1);
                } else if(typeText.equals("band")) {
                    Intent i = new Intent(getActivity(), BandRegisterActivity.class);
                    i.putExtra("what", "register");
                    getActivity().startActivityForResult(i, 2);
                }
            }
        });

    }

    @Override
    public void resultReady(Artist art) {
        if(art != null) {
            artist = art;

            name.setText(art.getName());
            type.setText(art.getType());

            if(!art.getPic().isEmpty()) {
                Picasso.with(getActivity()).load(art.getPic()).error(R.drawable.ak).into(profile_pic);
                Picasso.with(getActivity()).load(art.getPic()).error(R.drawable.ak).into(backImage);
            } else {
                profile_pic.setImageResource(R.drawable.ak);
                backImage.setImageResource(R.drawable.ak);
            }


            backImage.setColorFilter(filter);
            Bitmap image = new BlurBuilder().blur(backImage);
            backImage.setImageDrawable(new BitmapDrawable(getActivity().getResources(), image));
            fragmentTransaction.add(R.id.details_container, new ArtistDetails()).commit();

            if(id.equals(prefManager.getId())) {
                other.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else {
                chat.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                restClient.followOrNot(prefManager.getId(), id, this);
            }

        }
    }

    @Override
    public void resultReady(Band bnd) {
        if(bnd != null) {
            this.band = bnd;

            name.setText(bnd.getName());
            type.setText(bnd.getType());

            if(!bnd.getPic().isEmpty()) {

                Picasso.with(getActivity()).load(bnd.getPic()).error(R.drawable.ak).into(profile_pic);
                Picasso.with(getActivity()).load(bnd.getPic()).error(R.drawable.ak).into(backImage);
            } else {
                profile_pic.setImageResource(R.drawable.ak);
                backImage.setImageResource(R.drawable.ak);
            }

            backImage.setColorFilter(filter);
            Bitmap image = new BlurBuilder().blur(backImage);
            backImage.setImageDrawable(new BitmapDrawable(getActivity().getResources(), image));

            fragmentTransaction.add(R.id.details_container, new BandDetails()).commit();

            if(id.equals(prefManager.getBand())) {
                other.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            } else {
                other.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                restClient.followOrNot(prefManager.getId(), id, this);
            }

        }
    }

    @Override
    public void resultReady(Boolean t, String type) {
        if(t) {
            if(type.equals("follow")) {
                follow.setText("Unfollow");
                follow.setBackgroundColor(Color.parseColor("#20baf5"));
            } else {
                follow.setText("Follow");
                follow.setBackgroundResource(R.drawable.round_rec_bound);
            }
        }

        followLoad.setVisibility(View.GONE);
        follow.setVisibility(View.VISIBLE);
    }

    @Override
    public void resultReady(Venue venue) {

    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void resultReady(String id) {

        follow.setVisibility(View.VISIBLE);
        if(id.equals("yes")) {
            follow.setText("Unfollow");
            follow.setBackgroundColor(Color.parseColor("#20baf5"));
            if(prefManager.getType().equals("venue")) {
                jam.setVisibility(View.GONE);
                book.setVisibility(View.VISIBLE);

                restClient.getBookedOrNot(prefManager.getId(), this.id, this);
            }
            else {
                book.setVisibility(View.GONE);
                if(typeText.equals("artist")) {
                    jam.setVisibility(View.VISIBLE);
                    restClient.jammingOrNot(prefManager.getId(), this.id, this);
                } else {
                    jam.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                }
            }

        } else if(id.equals("no")) {
            follow.setText("Follow");
            follow.setBackgroundResource(R.drawable.round_rec_bound);

            if(prefManager.getType().equals("venue")) {
                jam.setVisibility(View.GONE);
                book.setVisibility(View.VISIBLE);

                restClient.getBookedOrNot(prefManager.getId(), this.id, this);
            }
            else {
                book.setVisibility(View.GONE);
                if(typeText.equals("artist")) {
                    jam.setVisibility(View.VISIBLE);
                    restClient.jammingOrNot(prefManager.getId(), this.id, this);
                } else {
                    jam.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                }
            }
        } else if(id.equals("sent_booking")) {
            book.setText("Cancel");
            book.setBackgroundColor(Color.parseColor("#20baf5"));
            bookLoad.setVisibility(View.GONE);
            book.setVisibility(View.VISIBLE);
        } else if(id.equals("sent_jam")) {
            jam.setText("Cancel");
            jam.setBackgroundColor(Color.parseColor("#20baf5"));
            bookLoad.setVisibility(View.GONE);
            jam.setVisibility(View.VISIBLE);
        } else if(id.equals("booking_cancel")) {
            book.setText("Book");
            book.setBackgroundResource(R.drawable.round_rec_bound);
            bookLoad.setVisibility(View.GONE);
            book.setVisibility(View.VISIBLE);
        } else if(id.equals("jam_cancel")) {
            jam.setText("Jam");
            jam.setBackgroundResource(R.drawable.round_rec_bound);
            bookLoad.setVisibility(View.GONE);
            jam.setVisibility(View.VISIBLE);
        }

        other.setVisibility(View.VISIBLE);
    }

    @Override
    public void resultReady(String result, String type) {
        if(type.equals("bookOrNot")) {
                book.setText(result);
                if(result.equals("Book"))
                    book.setBackgroundResource(R.drawable.round_rec_bound);
                else
                    book.setBackgroundColor(Color.parseColor("#20baf5"));
        } else if(type.equals("jamOrNot")) {
            jam.setText(result);
            if(result.equals("Jam"))
                jam.setBackgroundResource(R.drawable.round_rec_bound);
            else
                jam.setBackgroundColor(Color.parseColor("#20baf5"));
        }
        progress.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {

        Calendar c = Calendar.getInstance();
        datePickerDialog = new android.app.DatePickerDialog(getActivity(), ProfileFrag.this,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Select Date");

        timePickerDialog = new android.app.TimePickerDialog(getActivity(), ProfileFrag.this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.setTitle("Select Time");

        if(v == chat) {
            if(artist != null) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                ArrayList<String> a = new ArrayList<String>();
                a.add(artist.getPic());
                a.add(artist.getName());
                a.add(id);
                i.putStringArrayListExtra("details", a);
                startActivity(i);
            } else {
                Log.e("Artist", "found null");
            }

        } else if(v == follow) {
            follow.setVisibility(View.INVISIBLE);
            followLoad.setVisibility(View.VISIBLE);

            if(follow.getText().toString().equals("Follow")) {
                restClient.follow(id, prefManager.getId(), this);
            } else if(follow.getText().toString().equals("Unfollow")) {
                restClient.unfollow(id, prefManager.getId(), this);
            }
        } else if(v == book) {
            book.setVisibility(View.INVISIBLE);
            bookLoad.setVisibility(View.VISIBLE);

            if(book.getText().equals("Cancel")) {
                restClient.cancelBooking(id, prefManager.getId(), this);
            } else if(book.getText().equals("Book")){
                datePickerDialog.show();
                global = "book";
            }
        } else if(v == jam) {
            jam.setVisibility(View.INVISIBLE);
            bookLoad.setVisibility(View.VISIBLE);

            if(jam.getText().equals("Cancel")) {
                restClient.cancelJam(prefManager.getId(), id, this);
            } else if(jam.getText().equals("Jam")){
                global = "jam";
                datePickerDialog.show();
            }
        } else if(v == fb) {
            if(typeText.equals("band"))
               show("Facebook Page Link", band.getFb_page());
            else
               show("Facebook Page Link", artist.getFbPage());
        } else if(v == twitter) {
            if(typeText.equals("band"))
                show("Twitter Link", band.getTwitter());
            else
                show("Twitter Link", artist.getTwitter());
        } else if(v == gplus) {
            if(typeText.equals("band"))
                show("Google plus Link", band.getGplus());
            else
                show("Google plus Link", artist.getGplus());
        } else if(v == scloud) {
            if(typeText.equals("band"))
                show("Sound Cloud Link", band.getScloud());
            else
                show("Sound CLoud Link", artist.getScloud());

        } else if(v == utube) {
            if(typeText.equals("band"))
                show("Youtube Channel", band.getUtube());
            else
                show("Youtube Channel", artist.getUtube());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                restClient.getArtist(id, this);
            }
        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        day = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(hourOfDay > 12) {
            time = (hourOfDay - 12) + ":" + minute + " Pm";
            Log.e("time is", time);
        } else if(hourOfDay == 12) {
            time = (hourOfDay) + ":" + minute + "Pm";
            Log.e("time is", time);
        } else if(hourOfDay == 0) {
            time = "12" + ":" + minute + " Am";
            Log.e("time is", time);
        } else if(hourOfDay < 12) {
            time = (hourOfDay) + ":" + minute + " Am";
            Log.e("time is", time);
        }

        if(global.equals("book")) {
            if(typeText.equals("artist"))
                restClient.bookArtist(id, prefManager.getId(), day, time, this);
            else if(typeText.equals("band"))
                restClient.bookBand(id, prefManager.getId(), day, time, this);
        } else if(global.equals("jam")){
            restClient.sendJamRequest(prefManager.getId(), id, day, time, this);
        }
    }

    public void show(final String link, final String linkText) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View linkVIew = li.inflate(R.layout.links_edit, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText linkBox = (EditText) linkVIew.findViewById(R.id.link);
        final TextView linkName = (TextView) linkVIew.findViewById(R.id.link_name);

        linkName.setText(link);
        linkBox.setText(linkText);
        builder.setView(linkVIew);

        if(prefManager.getId().equals(id)) {
            builder.setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(link.contains("Facebook"))
                        artist.setFbPage(linkBox.getText().toString());
                    else if(link.contains("Youtube"))
                        artist.setUtube(linkBox.getText().toString());
                    else if(link.contains("Google"))
                        artist.setGplus(linkBox.getText().toString());
                    else if(link.contains("Twitter"))
                        artist.setTwitter(linkBox.getText().toString());
                    else if(link.contains("Sound"))
                        artist.setScloud(linkBox.getText().toString());

                    restClient.updateArtist(artist, ProfileFrag.this);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        } else {
            linkBox.setFocusable(false);
            linkBox.setEnabled(false);

            builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();;
    }
}