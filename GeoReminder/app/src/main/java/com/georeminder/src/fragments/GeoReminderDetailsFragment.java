package com.georeminder.src.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.georeminder.src.R;
import com.georeminder.src.beans.GeoNamesBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GeoReminderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeoReminderDetailsFragment extends ClientBaseFragment {


    @BindView(R.id.tv_reminder_address)
    TextView tvReminderAddress;
    @BindView(R.id.tv_reminder_notes)
    EditText tvReminderNotes;

    private GeoNamesBean geoNamesBean = null;
    static GeoReminderDetailsFragment geoReminderDetailsFragment = null;

    public static GeoReminderDetailsFragment newInstance(GeoNamesBean geoNamesBean) {

            geoReminderDetailsFragment = new GeoReminderDetailsFragment(geoNamesBean);

        return geoReminderDetailsFragment;
    }

    public GeoReminderDetailsFragment(GeoNamesBean geoNamesBean) {
        this.geoNamesBean = geoNamesBean;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_geo_reminder_details, container, false);
        ButterKnife.bind(this, rootView);
        tvReminderAddress.setText(geoNamesBean.getGeoAddress() != null ? geoNamesBean.getGeoAddress() : "");
        tvReminderNotes.setText(geoNamesBean.getNotes() != null ? geoNamesBean.getNotes() : "");
        tvReminderNotes.setEnabled(false);
        String radius = (geoNamesBean.getGeoFencingRadius() / 1000) + " " + getString(R.string.Lable_Away);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setSubtitle(radius);

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
