package euphony.com.euphony;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

/**
 * Created by pR0 on 28-05-2016.
 */
public class RestClient {
    private static RestClient instance = null;
    private ResultReadyCallback callback;

    private static final String BASE_URL = "http://euphony-app.herokuapp.com";
    private UserApi service;
    Artist artist = new Artist();
    Venue venue = new Venue();
    Context context;

    boolean success = false;
    String message;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        service = retrofit.create(UserApi.class);
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void showErrorActivity() {
        Intent i = new Intent(context, ErrorActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

   /* public void getAll(String curId, ResultReadyCallback c) {
        setCallback(c);

        Call<List<User>> u = service.getAll(curId);

        u.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response) {
                if (response.isSuccess()) {
                    Log.e("got all mixed", "response");
                    if (response.body() != null) {
                        callback.resultReady(response.body());
                    } else {
                        Log.e("Not getting mixed", "Check");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST3", t.getMessage());

            }
        });
    } */

    public void getSomeUsers(List<String> ids, String curId, ResultReadyCallback c) {
        setCallback(c);

        Call<List<UserMin>> u = service.getSomeUsers(ids, curId);

        u.enqueue(new Callback<List<UserMin>>() {
            @Override
            public void onResponse(Response<List<UserMin>> response) {
                if(response.isSuccess()) {
                    Log.e("got some users", "response");
                    if(response.body() != null) {
                        //callback.resultReady(response.body(), 0);
                    } else {
                        Log.e("Not got the users", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    public void setCallback(ResultReadyCallback callback) {
        this.callback = callback;
    }

    public void createArtist(final Context ctx, Artist user1, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.createArtist(user1);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try {
                        String id = response.body().string().replace("\"", "");
                        Log.e("id is", id);
                        callback.resultReady(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");

                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
                callback.resultReady("error");
            }
        });
    }

    public void createBand(final Context ctx, Band user1, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.createBand(user1);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try {
                        String id = response.body().string().replace("\"", "");
                        Log.e("id is", id);
                        callback.resultReady(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void createVenue(final Context ctx, Venue user1, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.createVenue(user1);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try {
                        String id = response.body().string().replace("\"", "");
                        Log.e("venue id is", id);
                        callback.resultReady(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e("REST", t.getMessage());
                callback.resultReady("error");
            }
        });
    }


    public void setArt( List<Artist> a, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.artists(a);
        u.enqueue((new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                message = response.message();
                if(success) {
                    callback.resultReady(success, "artist");
                    Log.e("message for update", message);
                } else {
                    Log.e("cannot update", "artist");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST", t.getMessage());
            }
        }));
    }

    public void follow(String follow, String userId, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.follow(follow, userId);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                message = response.message();
                if(success) {
                    callback.resultReady(success, "follow");
                    Log.e("followed", message);
                } else {
                    Log.e("cannot follow", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
                //Log.e("REST4", t.getMessage());

            }
        });
    }

    public void unfollow(String unfollow, String userId, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.unfollow(unfollow, userId);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                message = response.message();
                if(success) {
                    callback.resultReady(success, "unfollow");
                    Log.e("followed", message);
                } else {
                    Log.e("cannot follow", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void getId(String f_id, String g_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.getId(f_id, g_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try{

                        String str = response.body().string().replace("\"", "");
                        callback.resultReady(str);
                        Log.e("id", str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Error in id", "Error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST5", t.getMessage());

                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    public void getArtist(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<Artist> u = service.getArtist(id);
        u.enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Response<Artist> response) {
                if(response.isSuccess()) {
                    Log.e("got the artist", "dfghj");
                    callback.resultReady(response.body());
                } else {
                    Log.e("Error:", "In fetching user");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
            }
        });
    }

    public void getVenue(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<Venue> v = service.getVenue(id);
        v.enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Response<Venue> response) {
                if (response.isSuccess()) {
                    Log.e("got the venue", "dfghj");
                    callback.resultReady(response.body());
                } else {
                    Log.e("Error:", "In fetching user");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("didnt got the venue", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void getBand(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<Band> u = service.getBand(id);
        u.enqueue(new Callback<Band>() {
            @Override
            public void onResponse(Response<Band> response) {
                if(response.isSuccess()) {
                    Log.e("got the band", "dfghj");
                    callback.resultReady(response.body());
                } else {
                    Log.e("Error:", "In fetching user band");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
                Log.e("didnt got the bans", t.getMessage());

            }
        });
    }

    /* public void getUsersInPart(String cur_id, int total, int offset, String type, ResultReadyCallback c) {
        setCallback(c);
        Call<List<User>> u = service.getUsersInPart(cur_id, total, offset, type);

        u.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Response<List<User>> response) {
                if (response.isSuccess()) {
                    Log.e("got all types", "response");
                    if (response.body() != null) {
                        Log.e("vhbjn", response.body() + "");
                        callback.resultReady(response.body());
                    } else {
                        Log.e("Not getting types", "Check");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST3", t.getMessage());

            }
        });
    } */

    public void getBandList(String curId, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Band>> u = service.getBandList(curId);

        u.enqueue(new Callback<List<Band>>() {
            @Override
            public void onResponse(Response<List<Band>> response) {
                if(response.isSuccess()) {
                    Log.e("got some users", "response");
                    if(response.body() != null) {
                        if(response.body().size() != 0)
                            callback.resultReady(response.body(), 1);
                        else
                            callback.resultReady("no_my_bands");
                    } else {
                        Log.e("Not got the bands", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
                showErrorActivity();

            }
        });
    }

    public void addMember(String id, String band_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.addMember(id, band_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                try {
                    String msg = response.body().string().replace("\"", "");
                    callback.resultReady(success, msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(success) {
                    callback.resultReady(success, "added");
                    Log.e("added", message);
                } else {
                    Log.e("cannot follow", "error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
            }
        });
    }

    public void getArtistByName(String name, String cur_id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<UserMin>> u = service.getArtistByName(name, cur_id);

        u.enqueue(new Callback<List<UserMin>>() {
            @Override
            public void onResponse(Response<List<UserMin>> response) {
                if(response.isSuccess()) {
                    Log.e("got some users", "response");
                    if(response.body() != null) {
                        if(response.body().size() != 0)
                            callback.resultReady_users(response.body());
                        else
                            callback.resultReady("not_found");
                    } else {
                        Log.e("Not got the users", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
            }
        });
    }

    public void getChatDetails(String curId, ResultReadyCallback c) {
        setCallback(c);

        Call<List<ChatDetails>> u = service.getConversation(curId);

        u.enqueue(new Callback<List<ChatDetails>>() {
            @Override
            public void onResponse(Response<List<ChatDetails>> response) {
                if(response.isSuccess()) {
                    Log.e("got the chats", "response");
                    if(response.body() != null) {

                        if(response.body().size() != 0)
                            callback.resultReady(response.body(), true);
                        else {
                            Log.e("Chat name", "mk");
                            callback.resultReady("no_chats");
                        }
                    } else {
                        Log.e("Not got the chats", "SomeUsers");

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ///Log.e("REST4", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void getMessages(String curId, String id , ResultReadyCallback c) {
        setCallback(c);

        Call<List<Message>> u = service.getMessages(curId, id);

        u.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Response<List<Message>> response) {
                if(response.isSuccess()) {
                    Log.e("got the messages", "response");
                    if(response.body() != null) {
                        if(response.body().size() != 0)
                            callback.resultReady(response.body(), "");
                        else
                            callback.resultReady("no_messages");
                    } else {
                        Log.e("Not got the messages", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void saveMessage(String read, Message message, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.saveMessage(read, message);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try {
                        String saved = response.body().string().replace("\"", "");
                        Log.e("status", saved);
                        callback.resultReady(saved);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void getNearArtist(Double latitude, Double longitude, String id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Artist>> u = service.getNearArtist(latitude, longitude, id);

        u.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Response<List<Artist>> response) {
                if(response.isSuccess()) {
                    Log.e("got some users", "response");
                    if(response.body() != null) {
                        if(response.body().size() != 0)
                            callback.resultReady(response.body());
                        else
                            callback.resultReady("no_nearby");
                    } else {
                        Log.e("Not got the users", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void getBands(String city, final ResultReadyCallback c) {
        setCallback(c);

        Call<List<Band>> u = service.getBands(city);

        u.enqueue(new Callback<List<Band>>() {
            @Override
            public void onResponse(Response<List<Band>> response) {
                if(response.isSuccess()) {
                    Log.e("got some users", "response");
                    if(response.body() != null) {
                        if(response.body().size() != 0)
                            callback.resultReady(response.body(), 0);
                        else
                            callback.resultReady("no_bands");
                    } else {
                        Log.e("Not got the bands", "SomeUsers");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void followOrNot(String cur_id, String id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.getFollowOrNot(cur_id, id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    try {
                        String follow = response.body().string().replace("\"", "");
                        Log.e("follow or not", follow);
                        callback.resultReady(follow);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getAllArtists(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Artist>> u = service.getAllArtists(id);

        u.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Response<List<Artist>> response) {
                if(response.isSuccess()) {
                   Log.e("got some users", "");
                        //Log.e("reaching here", response.body().get(0).getName()  );
                        callback.resultReady(response.body());
                } else {
                    Log.e("error", "error error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("REST4", t.getMessage());
            }
        });
    }

    public void updateArtist(Artist artist, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.updateArtist(artist);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {

                        String saved = "updated";
                        callback.resultReady(saved);
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
                Log.e("REST", t.getMessage());
            }
        });

    }

    public void updateVenue(Venue venue, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.updateVenue(venue);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                        String saved = "updated";
                        Log.e("status", saved);
                        callback.resultReady(saved);
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
                showErrorActivity();
            }
        });

    }

    public void updateBand(Band band, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.updateBand(band);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    String saved = "updated";
                    Log.e("status", saved);
                    callback.resultReady(saved);
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
                showErrorActivity();
            }
        });

    }

    public void updateLocation(String id, Float longitude, Float latitude, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.updateLocation(id, longitude, latitude);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    String saved = "updated";
                    Log.e("status", saved);
                    callback.resultReady(saved);
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });

    }

    public void stopTracking(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.stopTracking(id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if(success) {
                    try {
                        String id = response.body().string().replace("\"", "");
                        Log.e("venue id is", id);
                        callback.resultReady(id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e("REST", t.getMessage());
            }
        });
    }

    public void getFollowers(String cur_id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<UserMin>> u = service.getFollowers(cur_id);
        u.enqueue(new Callback<List<UserMin>>() {
            @Override
            public void onResponse(Response<List<UserMin>> response) {
                if(response.isSuccess()) {
                    Log.e("success in ", "getting users" + response.body().size());
                    if(response.body().size() != 0)
                        callback.resultReady_users(response.body());
                    else
                        callback.resultReady("no_followers");
                } else {
                    Log.e("error", "retireving users");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Errror", t.getMessage());
            }
        });
    }


    public void getFollowing(String cur_id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<UserMin>> u = service.getFollowing(cur_id);
        u.enqueue(new Callback<List<UserMin>>() {
            @Override
            public void onResponse(Response<List<UserMin>> response) {
                if(response.isSuccess()) {
                    Log.e("success in ", "getting users" + response.body().size());
                    if(response.body().size() != 0)
                        callback.resultReady_users(response.body());
                    else
                        callback.resultReady("no_following");
                } else {
                    Log.e("error", "retireving users");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Errror", t.getMessage());
            }
        });
    }

    public void bookArtist(String artistId, String venueId, String date, String time, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.bookArtist(artistId, venueId, date, time);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    Log.e("success" ,"booked artist");
                    callback.resultReady("sent_booking");
                } else {
                    Log.e("error", "artist booking");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error booking", t.getMessage());
                showErrorActivity();
            }
        });
    }


    public void bookBand(String bandId, String venueId, String date, String time, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.bookArtist(bandId, venueId, date, time);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    Log.e("success" ,"booked band");
                    callback.resultReady("sent_booking");
                } else {
                    Log.e("error", "band booking");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error booking", t.getMessage());
                showErrorActivity();
            }
        });
    }


    public void getBookingsArtist(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Booking>> u = service.getBookingsArtist(id);
        u.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Response<List<Booking>> response) {
                if(response.isSuccess()) {
                        Log.e("success" ,"got booking");
                    callback.resultReady_booking(response.body());
                } else {
                    Log.e("error", "artist booking");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error booking", t.getMessage());
                showErrorActivity();
            }
        });
    }


    public void getBookingsVenue(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Booking>> u = service.getBookingsVenue(id);
        u.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Response<List<Booking>> response) {
                if(response.isSuccess()) {
                    Log.e("success" ,"got booking");
                    callback.resultReady_booking(response.body());
                } else {
                    Log.e("error", "artist booking");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error booking", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void getBookedOrNot(String cur_id, String id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.getBookedOrNot(cur_id, id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status, "bookOrNot");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cannot create", "Already created");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void jammingOrNot(String cur_id, String id, ResultReadyCallback c) {
            setCallback(c);

            Call<ResponseBody> u = service.getJammingOrNot(cur_id, id);
            u.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response) {
                    success = response.isSuccess();
                    if (success) {
                        try {
                            String status = response.body().string().replace("\"", "");
                            Log.e("id is", status);
                            callback.resultReady(status, "jamOrNot");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("cant get", "cant get");
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                    Log.e("REST", t.getMessage());
                }
            });
        }


    public void acceptJam(String notification_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.acceptJam(notification_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }


    public void rejectJam(String notification_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.rejectJam(notification_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void acceptBooking(String notification_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.acceptBooking(notification_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }


    public void rejectBooking(String notification_id, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.rejectBooking(notification_id);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void cancelBooking(String booked, String bookedBy, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.cancelBooking(booked, bookedBy);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void cancelJam(String sender, String sentTo, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.cancelJam(sender, sentTo);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                success = response.isSuccess();
                if (success) {
                    try {
                        String status = response.body().string().replace("\"", "");
                        Log.e("id is", status);
                        callback.resultReady(status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("cant get", "cant get");
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.e("REST", t.getMessage());
            }
        });
    }

    public void sendJamRequest(String sender, String sentTo, String date, String time, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.sendJamRequest(sender, sentTo, date, time);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    Log.e("success" ,"booked artist");
                    callback.resultReady("sent_jam");
                } else {
                    Log.e("error", "artist booking");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
            }
        });
    }

    public void getNotifications(String id, ResultReadyCallback c) {
        setCallback(c);

        Call<List<Notifications>> u = service.getNotification(id);

        u.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Response<List<Notifications>> response) {
                if(response.isSuccess()) {
                    Log.e("got snotification", "" + response.body().size());
                    //Log.e("reaching here", response.body().get(0).getName()  );
                    if(response.body().size() == 0)
                        callback.resultReady("");
                    else
                        callback.resultReady_notify(response.body());
                } else {
                    Log.e("error", "error error");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorActivity();
            }
        });
    }

    public void updateGcm(String id, String token, ResultReadyCallback c) {
        setCallback(c);

        Call<ResponseBody> u = service.updateGcm(id, token);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    Log.e("success" ,"updated gcm");
                    callback.resultReady("updated");
                } else {
                    Log.e("error", "gcm_key");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error booking", t.getMessage());
            }
        });
    }

    public void getBandMembers(String id, ResultReadyCallback c) {
        setCallback(c);
        Call<List<Artist>> u = service.getMembers(id);
        u.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Response<List<Artist>> response) {
                if(response.isSuccess()) {
                    Log.e("got members", "" + response.body().size());
                    callback.resultReady(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error in members", t.getMessage());
                showErrorActivity();
            }
        });
    }

    public void  uploadProfilePic(MultipartBody.Part file, ResultReadyCallback c) {
        setCallback(c);
        Call<ResponseBody> u = service.uploadProfilePicture(file);
        u.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if(response.isSuccess()) {
                    try {
                        String url = response.body().string().replace("\"", "");
                        Log.e("url is", url);
                        callback.resultReady(url, "pic_url");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error in members", t.getMessage());
            }
        });
    }

    public void  getUnread(String id, ResultReadyCallback c) {
        setCallback(c);
        Call<Integer> u = service.getUnread(id);
        u.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response) {
                if(response.isSuccess()) {
                        callback.resultReady_int(response.body().intValue());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("error in members", t.getMessage());
            }
        });
    }


    public static RestClient getInstance() {
        if(instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public interface ResultReadyCallback {

        public void resultReady(Artist artist);
        public void resultReady(Band band);
        public void resultReady(Boolean t, String type);
        public void resultReady(Venue venue);
        public void resultReady(List<Artist> artists);
        public void resultReady(String id);
        public void resultReady(String result, String type);
        public void resultReady(List<Band> bands, int a);
        public void resultReady(List<ChatDetails> chats, boolean x);
        public void resultReady(List<Message> messages, String a);
        public void resultReady_users(List<UserMin> users);
        public void resultReady_booking(List<Booking> bookings);
        public void resultReady_notify(List<Notifications> notifications);
        public void resultReady_int(int i);
    }
}