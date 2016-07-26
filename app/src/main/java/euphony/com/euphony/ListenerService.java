package euphony.com.euphony;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by pR0 on 21-06-2016.
 */
public class ListenerService extends Service {

    private com.github.nkzawa.socketio.client.Socket socket;
    MyBinder myBinder = new MyBinder();
    PrefManager prefManager = new PrefManager(Application.ctx);

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("bind", "binded");
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v("bind", "rebinded");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("rejined", "unbinded");
        return true;
    }

    @Override
    public void onCreate() {
        try {
            socket = IO.socket("https://euphony-app.herokuapp.com");
            Log.e("connected", "socket");
            socket.emit("userId", prefManager.getId());

            final String MESSAGE = "euphony.com.euphony.MESSAGE";
            final String STATUS = "status";

            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    try {
                        Log.e("message", "" + args.length);

                        JSONObject object = (JSONObject) args[0];
                        String data = object.getString("message");
                        String from = object.getString("frm");

                        Intent i = new Intent("message");
                        i.putExtra("data", data);
                        i.putExtra("from", from);

                        i.setAction(MESSAGE);

                        sendOrderedBroadcast(i, null);

                        Log.e("data", data);
                        Log.e("from", from);

                    } catch (JSONException e) {
                        Log.e("here", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            socket.on("status", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];

                    try {
                        String online = object.getString("online");

                        Intent i = new Intent("status");
                        i.putExtra("online", online);
                        i.setAction(STATUS);

                        LocalBroadcastManager.getInstance(ListenerService.this).sendBroadcast(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        if(!socket.connected()) {
            socket.connect();

            socket.emit("userId", prefManager.getId());

            final String MESSAGE = "euphony.com.euphony.MESSAGE";
            final String STATUS = "status";

            socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    try {
                        Log.e("message", "" + args.length);

                        JSONObject object = (JSONObject) args[0];
                        String data = object.getString("message");
                        String from = object.getString("frm");

                        Intent i = new Intent("message");
                        i.putExtra("data", data);
                        i.putExtra("from", from);

                        i.setAction(MESSAGE);

                        sendOrderedBroadcast(i, null);

                        Log.e("data", data);
                        Log.e("from", from);

                    } catch (JSONException e) {
                        Log.e("here", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });

            socket.on("status", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject object = (JSONObject) args[0];

                    try {
                        String online = object.getString("online");

                        Intent i = new Intent("status");
                        i.putExtra("online", online);
                        i.setAction(STATUS);

                        LocalBroadcastManager.getInstance(ListenerService.this).sendBroadcast(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    public class MyBinder extends Binder {
        ListenerService getService() {
            return ListenerService.this;
        }
    }

    public void sendMessage(JSONObject data) {

        Log.e("sending", "messages");
        socket.emit("message", data);
    }

    public void setOnline(String id) {
        socket.emit("online", id);
    }

    public void getStatus(String id) {
        socket.emit("status", id);
    }
}
