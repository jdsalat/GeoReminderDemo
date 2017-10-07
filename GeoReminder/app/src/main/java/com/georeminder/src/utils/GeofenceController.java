package com.georeminder.src.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.services.GeoIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javed.Salat on 9/15/2016.
 */
public class GeofenceController {
    private static final String TAG = GeofenceController.class.getSimpleName();
    private static GeofenceController INSTANCE;

    private Context context;
    private GoogleApiClient googleApiClient;
    private GeofenceControllerListener listener;
    private Geofence geofenceToAdd;
    private String namedGeofencesToRemove;


    public static GeofenceController getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController(context);
        }
        return INSTANCE;
    }

    public GeofenceController(Context context) {
        this.context = context;
    }

    public interface GeofenceControllerListener {
        void onGeofencesUpdated();

        void onError();
    }

    private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        googleApiClient.connect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionAddListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Intent intent = new Intent(context, GeoIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(googleApiClient, getAddGeofencingRequest(), pendingIntent);
            result.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        saveGeofence();
                    } else {
                        printLog("Registering geofence failed: " + status.getStatusMessage() + " : " + status.getStatusCode());
                        sendError();
                    }
                }
            });
        }

        @Override
        public void onConnectionSuspended(int i) {
            printLog("Connecting to GoogleApiClient suspended.");
            sendError();
        }
    };

    private void saveGeofence() {
        if (listener != null) {
            listener.onGeofencesUpdated();
        }
    }

    public void addGeofence(GeoNamesBean geoNamesBean, GeofenceControllerListener listener, Geofence geofence) {
        //this.namedGeofenceToAdd = namedGeofence;
        this.geofenceToAdd = geofence;
        this.listener = listener;

        connectWithCallbacks(connectionAddListener);
    }

    private GeofencingRequest getAddGeofencingRequest() {
        List<Geofence> geofencesToAdd = new ArrayList<>();
        geofencesToAdd.add(geofenceToAdd);
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofencesToAdd);
        return builder.build();
    }

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            printLog("Connecting to GoogleApiClient failed.");
            sendError();
        }
    };

    private void sendError() {
        if (listener != null) {
            listener.onError();
        }
    }

    /*TO remove geofence*/
    public void removeGeofences(String namedGeofencesToRemove, GeofenceControllerListener listener) {
        this.namedGeofencesToRemove = namedGeofencesToRemove;
        this.listener = listener;

        connectWithCallbacks(connectionRemoveListener);
    }

    private GoogleApiClient.ConnectionCallbacks connectionRemoveListener = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            List<String> removeIds = new ArrayList<>();
            removeIds.add(namedGeofencesToRemove);

            if (removeIds.size() > 0) {
                PendingResult<Status> result = LocationServices.GeofencingApi.removeGeofences(googleApiClient, removeIds);
                result.setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            removeSavedGeofences();
                        } else {
                            printLog("Removing geofence failed: " + status.getStatusMessage());
                            sendError();
                        }
                    }
                });
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            printLog("Connecting to GoogleApiClient suspended.");
            sendError();
        }
    };

    private void removeSavedGeofences() {
        if (listener != null) {
            listener.onGeofencesUpdated();
        }
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }

}
