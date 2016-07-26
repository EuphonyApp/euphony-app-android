package euphony.com.euphony;

import android.app.*;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, FacebookCallback<LoginResult> {

    final int RC_SIGN_IN = 1;

    RelativeLayout r;
    RippleView glogin, flogin;
    LoginButton login;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    RestClient restClient;

    Intent i;
    PrefManager prefManager = new PrefManager(euphony.com.euphony.Application.ctx);


    Gson gsonObject;
    String jsonObject;
    JSONObject o;

    Artist ar;

    List<Artist> artists;
    GoogleSignInAccount acc;
    AccessToken a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopService(new Intent(this, MyFirebaseInstanceIDService.class));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        r = (RelativeLayout) findViewById(R.id.loading);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        artists = new ArrayList<Artist>();
        restClient = RestClient.getInstance();

        restClient.setContext(getBaseContext());

      /*  Artist a = new Artist();

        a.setType("artist");
        a.setName("svdsa");
        a.setEmail("");
        a.setPic("");
        a.setF_id("sasv");
        a.setG_id("sas");
        a.setLocation("sdsa");
        a.setGenre("sadsd");
        a.setSubGenre(Arrays.asList("aads", "adss"));
        a.setUtube("");
        a.setFbPage("");
        a.setFollowers(new ArrayList<String>());
        a.setFollowing(new ArrayList<String>());

        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);
        artists.add(a);

        restClient.setArt(artists, new RestClient.ResultReadyCallback() {
            @Override
            public void resultReady(Artist artist) {

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
            public void resultReady(Band band) {

            }
        }); */

        //artists.add("jnknk", "kk", "jnj", "jnjn", "kmk", "mkkm", "sfv", "afvsf", "fva", "afa")
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        SuperActivityToast.create(LoginActivity.this, "Error in connecting to Google play service", SuperToast.Duration.SHORT,
                                Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        callbackManager = CallbackManager.Factory.create();
        login = (LoginButton) findViewById(R.id.fake);
        login.setReadPermissions(Arrays.asList("public_profile", "email"));
        flogin = (RippleView) findViewById(R.id.login_f);
        glogin = (RippleView) findViewById(R.id.login_g);


        glogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                signIn();
            }
        });
        flogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (AccessToken.getCurrentAccessToken() != null) {

                    LoginManager.getInstance().registerCallback(callbackManager, LoginActivity.this);
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                } else {
                    login.performClick();
                }
            }
        });

        restClient = RestClient.getInstance();

        gsonObject = new Gson();

        login.registerCallback(callbackManager, this);

    }

    public void onClick(View v) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
           callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GoogleSIgnIN", "" + result.isSuccess());
        acc = result.getSignInAccount();
        ar = new Artist();

        if(result.isSuccess()) {
            r.setVisibility(View.VISIBLE);
            flogin.setClickable(false);
            glogin.setClickable(false);
            restClient.getId("no", acc.getId().toString(), new RestClient.ResultReadyCallback() {
                @Override
                public void resultReady(Artist artist) {

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
                public void resultReady(String str) {
                    r.setVisibility(View.GONE);

                    if(!str.equals("null")) {
                        String arr[] = str.split("-");

                        arr[1] = arr[1].toLowerCase();
                        prefManager.createLogin(arr[0], arr[1], arr[2], arr[3], "", "", "", acc.getPhotoUrl().toString());
                        Log.e("got pic", arr[3]);
                        prefManager.setBand("");

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {


                        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);

                        prefManager.createLogin("", "", "", acc.getDisplayName(), acc.getId(), "no", acc.getEmail(), acc.getPhotoUrl().toString());

                        startActivity(i);
                        finish();
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
            });
        } else {
            SuperActivityToast.create(LoginActivity.this, "Google Login Failed! Try Again", SuperToast.Duration.SHORT,
                    Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();

        }
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        r.setVisibility(View.VISIBLE);
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        ar = new Artist();
                        o = object;
                        r.setVisibility(View.VISIBLE);
                        flogin.setClickable(false);
                        glogin.setClickable(false);

                       restClient.getId(loginResult.getAccessToken().getUserId(), "no", new RestClient.ResultReadyCallback() {
                           @Override
                           public void resultReady(Artist artist) {

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
                           public void resultReady(String str) {
                               Log.e("heres is id", str);
                               r.setVisibility(View.GONE);

                               if (!str.equals("null")) {
                                   Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                   String arr[] = str.split("-");
                                   arr[1] = arr[1].toLowerCase();

                                   prefManager.createLogin(arr[0], arr[1], arr[2], arr[3], "", "", "", "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId().toString() + "/picture");
                                   prefManager.setBand("");

                                   startActivity(i);
                                   finish();
                               } else {

                                   try {
                                       Log.e("Id at login", str);
                                       Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                                       Log.e("Name", o.getString("name"));
                                       String email;

                                       if(o.has("email"))
                                           email = o.getString("email");
                                       else
                                           email = "";

                                       prefManager.createLogin("", "", "", o.getString("name"), "no",
                                               loginResult.getAccessToken().getUserId(), email, "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId().toString() + "/picture");
                                       prefManager.setBand("");
                                       startActivity(i);
                                       finish();
                                   } catch (JSONException e) {
                                       e.printStackTrace();
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
                       });
                    }
                });
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        SuperActivityToast.create(LoginActivity.this, "Facebook Login cancelled", SuperToast.Duration.SHORT,
                Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();

    }

    @Override
    public void onError(FacebookException error) {
            SuperActivityToast.create(LoginActivity.this, "Facebook Login Failed! Try Again!", SuperToast.Duration.SHORT,
                    Style.getStyle(Style.BLACK, SuperToast.Animations.FADE)).show();
            Log.e("errror login", "" + error);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://euphony.com.euphony/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://euphony.com.euphony/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }
}