package com.georeminder.src.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.georeminder.src.beans.GeoNamesBean;
import com.georeminder.src.utils.ClientLogs;


/**
 * Created by Javed.Salat on 5/29/2016.
 */
public class ProfileDBInitializer {
    private static final String TAG = "ProfileDBInitializer";
    private static ProfileDBInitializer profileDBInitializer = null;
    private Context mContext;
    AssetDatabaseOpenHelper assetDatabaseOpenHelper = null;

    public ProfileDBInitializer(Context mContext) {
        this.mContext = mContext;
        this.assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(mContext);
    }

    public static ProfileDBInitializer newInstance(Context context) {
        if (profileDBInitializer == null) {
            profileDBInitializer = new ProfileDBInitializer(context);
        }
        return profileDBInitializer;
    }

    public void insertGeoFenceDetail(GeoNamesBean geoNamesBean) {
        StringBuilder profileStringBuilder = new StringBuilder();
        profileStringBuilder.append("insert into geo_fence_table(id, geo_name, latitude, longitude, geo_address, radius, notes) values(?,?,?,?,?,?,?)");
        printLog(profileStringBuilder.toString());
        SQLiteDatabase db = null;
        try {
            this.assetDatabaseOpenHelper.createDatabase();

            db = this.assetDatabaseOpenHelper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL(profileStringBuilder.toString(), new String[]{geoNamesBean.getId(), geoNamesBean.getGeoFencingName(), geoNamesBean.getLatitude() + "", geoNamesBean.getLongitude() + "", geoNamesBean.getGeoAddress(), geoNamesBean.getGeoFencingRadius() + "", geoNamesBean.getNotes()});

        } catch (Exception e) {
            printLog(e.getMessage());
        } finally {
            if (db != null) {
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
                this.assetDatabaseOpenHelper.closeDataBase();
            }
        }

    }

    public Cursor getProfileBean() {
        Cursor cursor = null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" select id, geo_name, latitude, longitude, geo_address, radius, notes from geo_fence_table ");

        SQLiteDatabase sqLiteDatabase = null;
        try {
            assetDatabaseOpenHelper.createDatabase();
            sqLiteDatabase = assetDatabaseOpenHelper.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery(stringBuilder.toString(), new String[]{});
            cursor.moveToFirst();
        } catch (Exception e) {
            printLog(e.getMessage());
        } finally {
           /* if (sqLiteDatabase != null)
                sqLiteDatabase.close();*/
        }

        return cursor;
    }


    private static void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }

    public void deleteGeoFence(String geoId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("delete from geo_fence_table where id=?");
        SQLiteDatabase sqLiteDatabase = null;
        try {
            this.assetDatabaseOpenHelper.createDatabase();

            sqLiteDatabase = this.assetDatabaseOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(stringBuilder.toString(), new String[]{geoId});


        } catch (Exception e) {
            printLog(e.getMessage());
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
                this.assetDatabaseOpenHelper.closeDataBase();
            }
        }

    }

    public void deleteGeofenceAll() {

        StringBuilder smsBuilder = new StringBuilder();
        smsBuilder.append("delete from geo_fence_table");
        SQLiteDatabase sqLiteDatabase = null;
        try {
            this.assetDatabaseOpenHelper.createDatabase();

            sqLiteDatabase = this.assetDatabaseOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(smsBuilder.toString(), new String[]{});

        } catch (Exception e) {
            printLog(e.getMessage());
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
                this.assetDatabaseOpenHelper.closeDataBase();
            }
        }

    }

    public void updateGeoFenceDetails(GeoNamesBean geoNamesBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("update geo_fence_table set geo_name=?, latitude=?, longitude=? ,geo_address=? , radius =?, notes=? where id=?");
        SQLiteDatabase sqLiteDatabase = null;
        try {
            this.assetDatabaseOpenHelper.createDatabase();

            sqLiteDatabase = this.assetDatabaseOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(stringBuilder.toString(), new String[]{geoNamesBean.getGeoFencingName(), geoNamesBean.getLatitude() + "", geoNamesBean.getLongitude() + "", geoNamesBean.getGeoAddress(), geoNamesBean.getGeoFencingRadius() + "", geoNamesBean.getNotes(), geoNamesBean.getId() + ""});
        } catch (Exception e) {
            printLog(e.getMessage());
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
                this.assetDatabaseOpenHelper.closeDataBase();
            }
        }
    }
}
