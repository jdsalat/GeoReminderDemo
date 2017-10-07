package com.georeminder.src.database;

/**
 * Created by Javed.Salat on 5/20/2016.
 */

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.georeminder.src.utils.ClientLogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteHelper";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "geo_fence.db";
    public final static String DATABASE_PATH = "/data/data/com.georeminder.src/databases/";
    public static final int DATABASE_VERSION = 1;

    public AssetDatabaseOpenHelper(Context myContext) {
        super(myContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = myContext;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            printLog("db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if (!dbExist1) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        } catch (SQLiteException e) {
        }
        return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if (file.exists()) {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase() throws SQLException {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void printLog(String msg) {
        ClientLogs.printLogs(ClientLogs.errorLogType, TAG, msg);
    }
}
