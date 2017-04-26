package vn.efode.vts.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

import vn.efode.vts.MainActivity;
import vn.efode.vts.R;
import vn.efode.vts.ScheduleHistoryActivity;

/**
 * Created by Tri on 4/17/17.
 */

public class MessageService extends FirebaseMessagingService{


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Gson gson = new GsonBuilder().create();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            JSONObject jsonObj = new JSONObject(remoteMessage.getData());

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String,String> outputMap = null;
            try {
                outputMap = gson.fromJson(jsonObj.getString("content"), type);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("JsonArraz",outputMap.get("description"));


            // Remind if there is any schedule will start in next 60 minutes
            if ( remoteMessage.getData().get("type").equals("remind")) {
                sendNotificationRemind(outputMap);
            }
            Log.d("onMessageReceived", remoteMessage.getData().toString());



        }
        Log.d("Ongoing_","true");

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param dataMap
     */
    public void sendNotificationRemind(Map<String, String> dataMap) {
        Log.d("sendNotificationTest","true");
        BitmapDrawable bitmapdraw;
        bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ketxemarker);
        Intent intent = new Intent(this, ScheduleHistoryActivity.class);
        Intent cancelIntent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent cancel = PendingIntent.getActivity(this, 1 /* Request code */, cancelIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ketxemarker)
                .setLargeIcon(bitmapdraw.getBitmap())
                .addAction(R.drawable.cast_ic_mini_controller_skip_prev, "Back", cancel)
                .addAction(R.drawable.cast_ic_mini_controller_skip_next, "Go", pendingIntent)
                .setOngoing(true)
                .setContentTitle(dataMap.get("description"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Thời gian " + dataMap.get("intend_start_time") + " \n Địa điểm " + dataMap.get("start_point_address")))
                .setContentText("Thời gian " + dataMap.get("intend_start_time"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




}
