package com.georeminder.src.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.georeminder.src.R;
import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.fragments.GeoReminderDetailsFragment;
import com.georeminder.src.utils.ClientLogs;
import com.georeminder.src.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeoReminderDetailsActivity extends ClientMainActivity {
    private static final String TAG = GeoReminderDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    GeoNamesBean geoNamesBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_reminder_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        geoNamesBean = (GeoNamesBean) getIntent().getSerializableExtra("geoNameBean");
        setTitle(geoNamesBean.getGeoFencingName());
        replaceFragment(R.id.content_body, GeoReminderDetailsFragment.newInstance(geoNamesBean));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_geo_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            sendMessage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        printLog("Broadcasting message");
        Intent intent = new Intent("com.geofence.src.app");
        intent.putExtra(ConstantUtils.ACTION_DELETE_BROADCAST, ConstantUtils.ACTION_DELETE_BROADCAST);
        intent.putExtra(ConstantUtils.PREF_GEO_ID, geoNamesBean.getId());
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        this.finish();
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }

}
