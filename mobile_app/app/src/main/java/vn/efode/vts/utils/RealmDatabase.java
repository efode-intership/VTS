package vn.efode.vts.utils;

import android.util.Log;

import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import vn.efode.vts.model.ScheduleActive;

/**
 * Created by Tuan on 10/05/2017.
 */

public class RealmDatabase {
    private static Realm realm = Realm.getDefaultInstance();


    public RealmDatabase() {
    }

    /**
     * Luu data tren device
     */
    public static void storageOnDiviceRealm(ScheduleActive mactvite) {
        realm.beginTransaction();
        ScheduleActive realmSchedule = realm.copyToRealm(mactvite);
        realm.commitTransaction();
        Log.d("INSERT_OFFLINE","aaaa");
    }

    public static RealmResults<ScheduleActive> getListData(){
        RealmQuery<ScheduleActive> query = realm.where(ScheduleActive.class);
        RealmResults<ScheduleActive> result1 = query.findAll();
        return result1;
    }

    /**
     * Delete data in Realm
     * @param target
     */
    public static void deteleFirst(int target){
        RealmResults<ScheduleActive> results = realm.where(ScheduleActive.class).findAll();
        realm.beginTransaction();
        // remove a single object
        ScheduleActive a = results.get(target);
        a.deleteFromRealm();
        realm.commitTransaction();
    }

    public static void deteleAll(){
        RealmResults<ScheduleActive> results = realm.where(ScheduleActive.class).findAll();
        realm.beginTransaction();
        // Delete all matches
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }


    public static void uploadOfflineDatatoServer(int index){
        Gson gson = new Gson();

        String json  = gson.toJson(realm.copyFromRealm(RealmDatabase.getListData()));
        Log.d("JSONNNNNNNNNNNNNNNNAA",String.valueOf(json));

    }

}
