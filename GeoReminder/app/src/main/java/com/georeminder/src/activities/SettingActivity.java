package com.georeminder.src.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.georeminder.src.R;
import com.georeminder.src.fragments.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends ClientMainActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        replaceFragment(R.id.content_body, SettingsFragment.newInstance());
    }
}
