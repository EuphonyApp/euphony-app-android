package euphony.com.euphony;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by pR0 on 16-06-2016.
 */public class PrefManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "pro.com.euphony.User";

    // All Shared Preferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_G_ID = "g_id";
    private static final String KEY_F_ID = "f_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PIC = "pic";
    private static final String KEY_TYPE = "type";
    private static final String KEY_BAND = "band";
    private static final String KEY_LOCATION = "location";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLogin(String id, String type, String location, String name, String g_id, String f_id, String email, String pic) {
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_LOCATION, location);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_G_ID, g_id);
        editor.putString(KEY_F_ID, f_id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PIC, pic);

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public String getType() {
        return pref.getString(KEY_TYPE, null);
    }
    public void setType(String type) {editor.putString(KEY_TYPE, type);
        editor.commit();
    }

    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public void setId(String id) { editor.putString(KEY_ID, id);
    editor.commit();
    }


    public void setLogin() {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public String getLocation() {
        return pref.getString(KEY_LOCATION, null);
    }

    public void setLocation(String location) {editor.putString(KEY_LOCATION, location);
    editor.commit();}

    public void setName(String keyName) {
        editor.putString(KEY_NAME, keyName);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public String getPic() {
        return pref.getString(KEY_PIC, "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();

        profile.put("id", pref.getString(KEY_ID, null));
        profile.put("type", pref.getString(KEY_TYPE, null));
        profile.put("name", pref.getString(KEY_NAME, null));
        profile.put("g_id", pref.getString(KEY_G_ID, null));
        profile.put("f_id", pref.getString(KEY_F_ID, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("pic", pref.getString(KEY_PIC, null));

        return profile;
    }

    public void setBand(String keyBand) {
       editor.putString(KEY_BAND, keyBand);
        editor.commit();
    }

    public String getBand() {
        return pref.getString(KEY_BAND, null);
    }
}

