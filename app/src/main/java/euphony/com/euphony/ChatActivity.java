package euphony.com.euphony;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pR0 on 20-06-2016.
 */
public class ChatActivity extends AppCompatActivity implements RestClient.ResultReadyCallback, RecyclerViewClickListener {
    RecyclerView heads, chats;
    IntentFilter f_message = new IntentFilter("euphony.com.euphony.MESSAGE");
    IntentFilter f_status = new IntentFilter("status");

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ListenerService.MyBinder myBinder = (ListenerService.MyBinder) service;
            listenerService = myBinder.getService();
            serviceBound = true;
            Log.e("bounded", "service");
        }
    };

    ImageView back, search, send;
    List<ChatDetails> users = new ArrayList<ChatDetails>();
    List<Message> messages = new ArrayList<Message>();

    TextView name;

    RestClient restClient;
    PrefManager prefManager;
    ChatHeadAdapter chatHeadAdapter;
    ChatsAdapter chatsAdapter;
    EditText box;
    RelativeLayout progress;

    String chattingWith = "";

    ListenerService listenerService;
    boolean serviceBound = false;
    BroadcastReceiver listener;

    ChatDetails current;

    int prevClicked = -1;

    @Override
    protected void onCreate(Bundle savedInstanceSaved) {
        super.onCreate(savedInstanceSaved);
        setContentView(R.layout.chat_activity);

        if(getIntent().getStringArrayListExtra("details") != null) {
            ArrayList<String> cur = getIntent().getStringArrayListExtra("details");
            current = new ChatDetails(cur.get(1), cur.get(0), cur.get(2), 0);


            Log.e("gfdchkgcjcycgcjgc", cur.get(1) +  " " + cur.get(0) +  " " + cur.get(2));
        }

        progress = (RelativeLayout) findViewById(R.id.progress);
        name = (TextView) findViewById(R.id.name);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        send = (ImageView) findViewById(R.id.send);

        heads = (RecyclerView) findViewById(R.id.chat_head_list);
        chats = (RecyclerView) findViewById(R.id.messages);

        LinearLayoutManager chatManager = new LinearLayoutManager(this);
        chatManager.setReverseLayout(true);
        chats.setLayoutManager(chatManager);
        heads.setLayoutManager(new LinearLayoutManager(this));

        prefManager = new PrefManager(Application.ctx);
        box = (EditText) findViewById(R.id.message_box);

        restClient = RestClient.getInstance();
        progress.setVisibility(View.VISIBLE);
        restClient.getChatDetails(prefManager.getId(), ChatActivity.this);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!box.getText().toString().isEmpty()) {
                    try {
                        JSONObject msg = new JSONObject();
                        msg.put("message", box.getText().toString());
                        msg.put("frm", prefManager.getId());
                        if (!chattingWith.isEmpty()) {
                            msg.put("to", chattingWith);
                        } else {
                            Log.e("Error ", "retrieving id");
                        }

                        Message message = new Message(box.getText().toString(), chattingWith, prefManager.getId());
                        box.setText("");

                        if(messages.size() == 0) {
                            messages.add(message);
                            chatsAdapter = new ChatsAdapter(messages, prefManager.getId());
                            chats.setAdapter(chatsAdapter);
                        } else {
                            messages.add(0, message);
                            chatsAdapter.notifyDataSetChanged();
                        }

                        Log.e("saved", "" + msg.getString("message"));
                        listenerService.sendMessage(msg);
                        restClient.saveMessage("no", message, ChatActivity.this);

                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void resultReady(Artist artist) {

    }

    @Override
    public void resultReady(Band band) {

    }

    @Override
    public void resultReady(List<ChatDetails> chats, boolean x) {
        if(chats.size() != 0) {
            progress.setVisibility(View.GONE);
            Log.e("dvad", "" + chats.size());

            if(getIntent().getStringArrayListExtra("details") != null) {
                int i;

                for(i = 0; i < chats.size(); ++i) {
                    if(chats.get(i).getId().equals(current.getId())) {
                        break;
                    }
                }
                if(i == chats.size()) {
                    chats.add(0, current);
                    Log.e("contains", "no_current");
                } else {
                    Log.e("contains", "current");

                    chats.remove(i);
                    chats.add(0, current);
                }
            }

            users.addAll(chats);
            chatHeadAdapter =  new ChatHeadAdapter(users, this, this);
            heads.setAdapter(chatHeadAdapter);
        } else {
            Log.e("csvsvdv", "" + 0);
        }
    }

    @Override
    public void resultReady(String id) {
        if(id.equals("saved")) {
            Log.e("saved", "message");
        } else
            Log.e("not", "saved");
        if(id.equals("no_chats")) {
            Log.e("chats", "no");
            if(current != null) {
                users.add(current);
                name.setText(current.getName());
                chatHeadAdapter = new ChatHeadAdapter(users, this, this);
                heads.setAdapter(chatHeadAdapter);
                progress.setVisibility(View.GONE);
            } else {
                Intent returnIntent = new Intent();
                setResult(5, returnIntent);
                finish();
            }
        }

        if(id.equals("no_messages")) {

        }
    }

    @Override
    public void resultReady(String result, String type) {

    }

    @Override
    public void resultReady(List<Band> bands, int a) {

    }

    @Override
    public void resultReady(List<Message> messages, String a) {
        if(messages.size() != 0) {
            this.messages = messages;
            Collections.reverse(messages);

            chatsAdapter = new ChatsAdapter(this.messages, prefManager.getId());
            chats.setAdapter(chatsAdapter);
        }
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
    public void resultReady(Boolean t, String type) {

    }

    @Override
    public void resultReady(Venue venue) {

    }

    @Override
    public void resultReady(List<Artist> artists) {

    }

    @Override
    public void recyclerViewClicked(String id, int clicked) {

        if(prevClicked != -1 && prevClicked != clicked) {
            heads.getLayoutManager().findViewByPosition(prevClicked).findViewById(R.id.headBg).setSelected(false);
        }

        //heads.getLayoutManager().findViewByPosition(clicked).findViewById(R.id.no_messages).setVisibility(View.GONE);
        //chatHeadAdapter.notifyItemChanged(clicked);

        name.setText(users.get(clicked).getName());
        chattingWith = id;
        prevClicked = clicked;
        restClient.getMessages(prefManager.getId(), id, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = new Intent(this, ListenerService.class);
        bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);

         listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("euphony.com.euphony.MESSAGE")) {
                    if(intent.getStringExtra("from").equals(chattingWith)) {

                        Message message = new Message(intent.getStringExtra("data"), prefManager.getId(), chattingWith);
                        messages.add(0, message);

                        chatsAdapter.notifyDataSetChanged();
                        abortBroadcast();

                    } else {
                        for(int i = 0; i < users.size(); ++i) {
                            if(users.get(i).getId().equals(intent.getStringExtra("from"))) {
                                int x = users.get(i).getUnread();
                                users.get(i).setUnread(x + 1);
                                chatHeadAdapter.notifyItemChanged(i);

                                Message message = new Message(intent.getStringExtra("data"), prefManager.getId(),
                                        users.get(i).getId());
                                break;
                            }
                        }
                    }
                } else if(intent.getAction().equals("status")) {

                }
            }
        };

        f_message.setPriority(2);
        registerReceiver(listener, f_message);
        registerReceiver(listener, f_status);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        f_message.setPriority(2);
        registerReceiver(listener, f_message);
        registerReceiver(listener, f_status);
    }

}