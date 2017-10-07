package com.georeminder.src.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.georeminder.src.R;
import com.georeminder.src.activities.GeoReminderDetailsActivity;
import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.database.ProfileDAO;
import com.georeminder.src.utils.PreferenceUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javed.Salat on 9/15/2016.
 */
public class GeoIntentService extends IntentService {
    private final String TAG = GeoIntentService.class.getName();
    PreferenceUtil preferenceUtil = null;

    public GeoIntentService() {
        super("GeoIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if (event != null) {
            if (event.hasError()) {
                onError(event.getErrorCode());
            } else {
                int transition = event.getGeofenceTransition();
                if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL || transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    List<String> geofenceIds = new ArrayList<>();
                    for (Geofence geofence : event.getTriggeringGeofences()) {
                        geofenceIds.add(geofence.getRequestId());
                    }
                    if (transition == Geofence.GEOFENCE_TRANSITION_ENTER || transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                        onEnteredGeofences(geofenceIds);
                    }
                }
            }
        }
    }

    private void onEnteredGeofences(List<String> geofenceIds) {
        GeoNamesBean geoNamesBeans = null;
        for (String geofenceId : geofenceIds) {
            String geofenceName = "";

            // Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreference

            List<GeoNamesBean> geoNamesBeanList = ProfileDAO.newInstance(getApplicationContext()).getGeoNames();
            for (GeoNamesBean geoNamesBean : geoNamesBeanList) {

                if (geoNamesBean.getId().equals(geofenceId)) {
                    geoNamesBeans = geoNamesBean;
                    geofenceName = geoNamesBean.getGeoFencingName();
                    break;
                }
            }

            // Set the notification text and send the notification
            String contextText = String.format(this.getResources().getString(R.string.Notification_Text), geofenceName);

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, GeoReminderDetailsActivity.class);
            intent.putExtra("geoNameBean", geoNamesBeans);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            Notification notification = new NotificationCompat.Builder(this)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_icon_app)
                    .setContentTitle(this.getResources().getString(R.string.Notification_Title))
                    .setContentText(contextText)
                    .setContentIntent(pendingNotificationIntent)
                    .setStyle(new NotificationCompat.InboxStyle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
            preferenceUtil = PreferenceUtil.newInstance(getApplicationContext());
            SharedPreferences preferences = preferenceUtil.getSharedPreference();
            if (preferences.getBoolean(PreferenceUtil.IS_SOUND_ON, true)) {
                notification.sound = alarmSound;
            }
            if (preferences.getBoolean(PreferenceUtil.IS_VIBRATE_ON, true)) {
                notification.vibrate = pattern;
            }
            notificationManager.notify(0, notification);

        }
    }

    private void onError(int i) {
        Log.e(TAG, "Geofencing Error: " + i);
        Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_SHORT).show();
    }
}
