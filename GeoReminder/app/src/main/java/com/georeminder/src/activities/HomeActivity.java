package com.georeminder.src.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.georeminder.src.GeoFencingActivity;
import com.georeminder.src.R;
import com.georeminder.src.fragments.ListGeoFencingFragment;
import com.georeminder.src.utils.ClientLogs;
import com.google.android.gms.common.GooglePlayServicesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends ClientMainActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        replaceFragment(R.id.content_body, ListGeoFencingFragment.newInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
            overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.fab)
    public void onClick() {
        Intent intent = new Intent(this, GeoFencingActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        printLog("googlePlayServicesCode = " + googlePlayServicesCode);
        if (googlePlayServicesCode == 1 || googlePlayServicesCode == 2 || googlePlayServicesCode == 3) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCode, this, 0).show();
        }
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }
}
