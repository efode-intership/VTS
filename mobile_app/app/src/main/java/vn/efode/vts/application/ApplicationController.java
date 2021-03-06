package vn.efode.vts.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import vn.efode.vts.model.Schedule;
import vn.efode.vts.model.User;

/**
 * Created by Tuan on 04/04/2017.
 */

public class ApplicationController extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static final String TAG = "VolleyPatterns";//Log or request TAG
    private RequestQueue mRequestQueue;//Global request queue for Volley
    private static ApplicationController sInstance;//A singleton instance of the application class for easy access in other places

    /**
     * Shared preference user session key's name.
     */
    public static String USER_SESSION = "user_session";


    /**
     * Shared preference schedule session key's name.
     */
    public static String SCHEDULE_SESSION = "schedule_session";

    /**
     * Common shared preference.
     */
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize sharedPreferences instance
        sharedPreferences = getSharedPreferences( getPackageName() + "_storage", MODE_PRIVATE);
        Realm.init(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        // initialize the singleton
        sInstance = this;



    }

    /**
     * @return co.pixelmatter.meme.ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Get current user.
     * @return current user.
     */
    public static User getCurrentUser() {
        String userJson = ApplicationController.sharedPreferences.getString(USER_SESSION, null);
        User user = null;
        if (userJson != null) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            user = gson.fromJson(userJson, User.class);
        }
        return user;
    }

    /**
     * Get current user.
     * @return current user.
     */
    public static Schedule getActiveSchudule() {
        String scheduleJson = ApplicationController.sharedPreferences.getString(SCHEDULE_SESSION, null);
        Schedule schedule = null;
        if (scheduleJson != null) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            schedule = gson.fromJson(scheduleJson, Schedule.class);
        }
        return schedule;
    }
}
