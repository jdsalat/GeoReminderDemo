package com.georeminder.src.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.georeminder.src.R;
import com.georeminder.src.activities.GeoReminderDetailsActivity;
import com.georeminder.src.adapters.GeoFenceListAdapter;
import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.database.ProfileDAO;
import com.georeminder.src.utils.ClientLogs;
import com.georeminder.src.utils.ConstantUtils;
import com.georeminder.src.utils.GeofenceController;
import com.georeminder.src.utils.LoadingDialog;
import com.georeminder.src.utils.ViewInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Javed.Salat on 13-Sep-16.
 */
public class ListGeoFencingFragment extends ClientBaseFragment implements ViewInterface, GeoFenceListAdapter.OnCLickItemButtonListener {
    private static final String TAG = ListGeoFencingFragment.class.getSimpleName();
    static ListGeoFencingFragment listGeoFencingFragment = null;
    @BindView(R.id.noData)
    TextView noData;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<GeoNamesBean> geoNamesBeanList;
    ProfileDAO profileDAO;
    GeoFenceListAdapter geoFenceListAdapter;
    int position;

    public static ListGeoFencingFragment newInstance() {
        if (listGeoFencingFragment == null) {
            listGeoFencingFragment = new ListGeoFencingFragment();
        }
        return listGeoFencingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rowView = inflater.inflate(R.layout.f_list_geo_fence, container, false);
        ButterKnife.bind(this, rowView);
        showLoadingDialog();
        init();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("com.geofence.src.app"));
        return rowView;

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            printLog("in On Recive");
            if (intent.getStringExtra(ConstantUtils.ACTION_ADD_BROADCAST) != null && intent.getStringExtra(ConstantUtils.ACTION_ADD_BROADCAST).equalsIgnoreCase(ConstantUtils.ACTION_ADD_BROADCAST)) {
                init();
            } else if (intent.getStringExtra(ConstantUtils.ACTION_DELETE_BROADCAST) != null && intent.getStringExtra(ConstantUtils.ACTION_DELETE_BROADCAST).equalsIgnoreCase(ConstantUtils.ACTION_DELETE_BROADCAST)) {
                showLoadingDialog();
                position = findPositionFromList(intent.getStringExtra(ConstantUtils.PREF_GEO_ID));
                removeDataFromList();
            }


        }
    };

    private int findPositionFromList(String geoId) {
        int i = 0;
        int listPos = -1;
        for (GeoNamesBean geoNamesBean : geoNamesBeanList) {
            if (geoNamesBean.getId().equalsIgnoreCase(geoId)) {
                listPos = i;
            } else {
                i++;
            }
        }
        return listPos;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }


    private void init() {
        this.profileDAO = ProfileDAO.newInstance(getContext());
        geoNamesBeanList = this.profileDAO.getGeoNames();
        if (geoNamesBeanList.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            geoFenceListAdapter = new GeoFenceListAdapter(getContext(), geoNamesBeanList, ListGeoFencingFragment.this);
            recyclerView.setAdapter(geoFenceListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                }
            });
        }
        dismissLoadingDialog();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = geoFenceListAdapter.getPosition();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        printLog("position::" + position);
        switch (item.getItemId()) {
            case GeoFenceListAdapter.iD:
                this.position = position;
                showLoadingDialog();
                GeofenceController.getInstance(getContext()).removeGeofences(geoNamesBeanList.get(position).getId(), geofenceControllerListener);
                //removeDataFromList(position);
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void removeDataFromList() {
        if (!geoNamesBeanList.isEmpty()) {
            this.profileDAO = ProfileDAO.newInstance(getContext());
            this.profileDAO.deleteGeoFence(geoNamesBeanList.get(this.position).getId() + "");
            Toast.makeText(getContext(), geoNamesBeanList.get(this.position).getGeoFencingName() + " " + getString(R.string.deleteToast), Toast.LENGTH_SHORT).show();
            this.geoNamesBeanList.remove(position);
            this.geoFenceListAdapter.notifyDataSetChanged();

            if (geoNamesBeanList.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
            }
        } else {
            noData.setVisibility(View.VISIBLE);
        }
        dismissLoadingDialog();
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }


    private GeofenceController.GeofenceControllerListener geofenceControllerListener = new GeofenceController.GeofenceControllerListener() {
        @Override
        public void onGeofencesUpdated() {
            removeDataFromList();
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

    @Override
    public void showLoadingDialog() {
        LoadingDialog.showDialog(getContext());
    }

    @Override
    public void dismissLoadingDialog() {
        LoadingDialog.dismissDialog();

    }

    @Override
    public void onItemButtonClick(int position) {
        Intent geoReminderActivity = new Intent(getActivity(), GeoReminderDetailsActivity.class);
        geoReminderActivity.putExtra("geoNameBean", geoNamesBeanList.get(position));
        startActivity(geoReminderActivity);
        this.getActivity().overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
    }
}
