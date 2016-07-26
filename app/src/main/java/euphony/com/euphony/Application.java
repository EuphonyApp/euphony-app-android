package euphony.com.euphony;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by pR0 on 11-06-2016.
 */
public final class Application extends android.app.Application {

    public static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        ctx = getApplicationContext();
        LocationProvider locationProvider = LocationProvider.getInstance();
        locationProvider.configureIfNeeded(this);
        Log.e("application", "bhjk");

        FontsOverride.setDefaultFOnt(this, "SERIF", "fonts/roboto_thin.ttf");
        FontsOverride.setDefaultFOnt(this, "SANS_SERIF", "fonts/roboto.otf");
    }
}