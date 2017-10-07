package com.georeminder.src.database;

import android.content.Context;
import android.database.Cursor;

import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.utils.ClientLogs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javed.Salat on 5/29/2016.
 */
public class ProfileDAO implements Serializable {
    private static final long serialVersionUID = -5115174269443168183L;
    private static final String TAG = "ProfileDAO";
    private static ProfileDAO profileDAO = null;
    private Context mContext;
    private ProfileDBInitializer profileDBInitializer;

    public ProfileDAO(Context mContext) {
        this.mContext = mContext;
        this.profileDBInitializer = ProfileDBInitializer.newInstance(mContext);
    }

    public static ProfileDAO newInstance(Context context) {
        if (profileDAO == null) {
            profileDAO = new ProfileDAO(context);
        }
        return profileDAO;
    }

    public boolean insertGeoFenceDetail(GeoNamesBean geoNamesBean) {
        this.profileDBInitializer.insertGeoFenceDetail(geoNamesBean);
        return true;
    }

    public List<GeoNamesBean> getGeoNames() {
        List<GeoNamesBean> profileBeans = new ArrayList<>();
        GeoNamesBean geoNamesBean;
        Cursor profileCursor = this.profileDBInitializer.getProfileBean();

        if (profileCursor.moveToFirst()) {
            do {
                geoNamesBean = new GeoNamesBean();
                geoNamesBean.setId(profileCursor.getString(0));
                geoNamesBean.setGeoFencingName(profileCursor.getString(1));
                geoNamesBean.setLatitude(profileCursor.getFloat(2));
                geoNamesBean.setLongitude(profileCursor.getFloat(3));
                geoNamesBean.setGeoAddress(profileCursor.getString(4));
                geoNamesBean.setGeoFencingRadius(profileCursor.getFloat(5));
                geoNamesBean.setNotes(profileCursor.getString(6));
                profileBeans.add(geoNamesBean);

            } while (profileCursor.moveToNext());
        }


        return profileBeans;
    }

    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }

    public void deleteGeoFence(String geoId) {
        this.profileDBInitializer.deleteGeoFence(geoId);
    }

    public void deleteGeofenceAll() {
        this.profileDBInitializer.deleteGeofenceAll();
    }

    public void updateGeoFenceDetails(GeoNamesBean geoNamesBean) {
        this.profileDBInitializer.updateGeoFenceDetails(geoNamesBean);
    }
}
