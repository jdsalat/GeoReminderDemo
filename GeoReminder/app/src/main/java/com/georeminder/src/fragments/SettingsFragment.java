package com.georeminder.src.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.georeminder.src.R;
import com.georeminder.src.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends ClientBaseFragment implements
        CompoundButton.OnCheckedChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();
    @BindView(R.id.cs_sound)
    SwitchCompat csSound;
    @BindView(R.id.cs_vibrate)
    SwitchCompat csVibrate;
    PreferenceUtil preferenceUtil;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        csSound.setOnCheckedChangeListener(this);
        csVibrate.setOnCheckedChangeListener(this);
        preferenceUtil = PreferenceUtil.newInstance(getContext());
        SharedPreferences preferences = preferenceUtil.getSharedPreference();
        if (preferences.getBoolean(PreferenceUtil.IS_SOUND_ON, true)) {
            csSound.setChecked(true);
        } else {
            csSound.setChecked(false);
        }
        if (preferences.getBoolean(PreferenceUtil.IS_VIBRATE_ON, true)) {
            csVibrate.setChecked(true);
        } else {
            csVibrate.setChecked(false);
        }
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = preferenceUtil.getSharedPreferenceEditor();
        switch (buttonView.getId()) {
            case R.id.cs_sound:
                editor.putBoolean(PreferenceUtil.IS_SOUND_ON, isChecked);
                editor.commit();
                break;
            case R.id.cs_vibrate:
                editor.putBoolean(PreferenceUtil.IS_VIBRATE_ON, isChecked);
                editor.commit();
                break;
        }

    }


}
