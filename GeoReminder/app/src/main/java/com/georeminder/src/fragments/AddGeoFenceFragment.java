package com.georeminder.src.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.georeminder.src.R;
import com.georeminder.src.adapters.PlaceAutocompleteAdapter;
import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.database.ProfileDAO;
import com.georeminder.src.utils.ClientLogs;
import com.georeminder.src.utils.ConstantUtils;
import com.georeminder.src.utils.GeofenceController;
import com.georeminder.src.utils.LoadingDialog;
import com.georeminder.src.utils.ViewInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Javed.Salat on 13-Sep-16.
 */
public class AddGeoFenceFragment extends ClientBaseFragment implements ViewInterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AlertDialogFragment.NoticeDialogListener {


    private static final String TAG = AddGeoFenceFragment.class.getSimpleName();
    static AddGeoFenceFragment addGeoFenceFragment = null;
    @BindView(R.id.fragment_add_geofence_name)
    EditText fragmentAddGeofenceName;
    @BindView(R.id.fragment_add_geofence_radius)
    EditText fragmentAddGeofenceRadius;
    @BindView(R.id.autocomplete_places)
    AutoCompleteTextView autocompletePlaces;
    @BindView(R.id.et_notes)
    EditText etNotes;
    @BindView(R.id.add_geo_container_layout)
    RelativeLayout addGeoContainerLayout;
    private LatLng latLng;
    private String geoAddress;
    GeoNamesBean geoNamesBean;
    Geofence geofence;
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutocompleteAdapter mAdapter;

    //private AutoCompleteTextView mAutocompleteView;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    public static AddGeoFenceFragment newInstance() {
        if (addGeoFenceFragment == null) {
            addGeoFenceFragment = new AddGeoFenceFragment();
        }
        return addGeoFenceFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.dialog_add_geofence, container, false);
        ButterKnife.bind(this, rowView);

        return rowView;

    }

    @OnClick(R.id.btn_geo_fence)
    public void onClick() {
        if (!fragmentAddGeofenceName.getText().toString().equalsIgnoreCase("") && fragmentAddGeofenceName.getText().toString() != null &&
                !fragmentAddGeofenceRadius.getText().toString().equalsIgnoreCase("") && fragmentAddGeofenceRadius.getText().toString() != null) {
            showLoadingDialog();
            geoNamesBean = new GeoNamesBean();
            geoNamesBean.setId(System.currentTimeMillis() + "");
            geoNamesBean.setGeoFencingName(fragmentAddGeofenceName.getText().toString());
            geoNamesBean.setGeoFencingRadius(Float.parseFloat(fragmentAddGeofenceRadius.getText().toString()) * 1000.0f);
            geoNamesBean.setLatitude(latLng.latitude);
            geoNamesBean.setLongitude(latLng.longitude);
            geoNamesBean.setGeoAddress(geoAddress);
            geoNamesBean.setNotes(etNotes.getText().toString());
            geofence = geofence(geoNamesBean);
            GeofenceController.getInstance(getContext()).addGeofence(geoNamesBean, geofenceControllerListener, geofence);

        } else {
            Toast.makeText(getContext(), "Please enter details!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataAndCloseActivity() {
        ProfileDAO profileDAO = ProfileDAO.newInstance(getContext());
        boolean result = profileDAO.insertGeoFenceDetail(geoNamesBean);
        if (result) {
            sendMessage();
            Toast.makeText(getContext(), geoNamesBean.getGeoFencingName() + " " + getString(R.string.addToast), Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
            this.getActivity().finish();
        } else {
            Toast.makeText(getContext(), getString(R.string.ToastError), Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
        }

    }


    private void sendMessage() {
        printLog("Broadcasting message");
        Intent intent = new Intent("com.geofence.src.app");
        intent.putExtra(ConstantUtils.ACTION_ADD_BROADCAST, ConstantUtils.ACTION_ADD_BROADCAST);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }


    public Geofence geofence(GeoNamesBean geoNamesBean) {

        return new Geofence.Builder()
                .setRequestId(geoNamesBean.getId())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setCircularRegion(geoNamesBean.getLatitude(), geoNamesBean.getLongitude(), geoNamesBean.getGeoFencingRadius())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    private GeofenceController.GeofenceControllerListener geofenceControllerListener = new GeofenceController.GeofenceControllerListener() {
        @Override
        public void onGeofencesUpdated() {
            saveDataAndCloseActivity();
        }

        @Override
        public void onError() {
            showErrorToast();
        }
    };

    private void showErrorToast() {
        dismissLoadingDialog();
        Toast.makeText(getActivity(), getActivity().getString(R.string.ToastError), Toast.LENGTH_SHORT).show();
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }


    @Override
    public void showLoadingDialog() {
        LoadingDialog.showDialog(getContext());
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.dismissDialog();
    }


    /**
     * Code for AutoCompleteText
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API).build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        printLog("connected");
        /*Autocomplete code*/
        autocompletePlaces.setOnItemClickListener(mAutocompleteClickListener);
// Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        autocompletePlaces.setAdapter(mAdapter);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        printLog("onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getContext(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        if (isNetworkAvailable()) {

        } else {
            AlertDialogFragment alertDialog = new AlertDialogFragment();
            alertDialog.setCancelable(false);
            alertDialog.show(this.getChildFragmentManager(), "dialog");
        }
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                this.getActivity().finish();
            }
            return false;
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see GeoDataApi#getPlaceById(GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            printLog("Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            printLog("Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                printLog("Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            printLog("Place: " + place.getName());
            latLng = place.getLatLng();
            printLog("Latitude:" + latLng.latitude);
            printLog("Longitude:" + latLng.longitude);
            geoAddress = place.getAddress().toString();
            printLog("Address:" + geoAddress);
            places.release();
        }
    };

    @Override
    public void onDialogPositiveClick() {

    }

    @Override
    public void onDialogNegativeClick() {
        this.getActivity().finish();
    }


    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
