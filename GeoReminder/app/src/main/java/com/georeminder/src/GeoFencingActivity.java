package com.georeminder.src;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.georeminder.src.activities.ClientMainActivity;
import com.georeminder.src.fragments.AddGeoFenceFragment;
import com.georeminder.src.fragments.AlertDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GeoFencingActivity extends ClientMainActivity implements AlertDialogFragment.NoticeDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fencing);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        replaceFragment(R.id.content_body, AddGeoFenceFragment.newInstance());
    }

    @Override
    public void onDialogPositiveClick() {

    }

    @Override
    public void onDialogNegativeClick() {
        this.finish();
    }
}
