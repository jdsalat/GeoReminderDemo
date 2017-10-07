package com.georeminder.src.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Javed.Salat on 3/17/2016.
 */
public abstract class ClientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * This method will be responsible in replacing a fragment in a particular View Group.
     *
     * @param containerViewId
     * @param fragment
     */

    public void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        // fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    /**
     * This method will be responsible in adding a fragment to a particular View Group.
     *
     * @param containerViewId
     * @param fragment
     */
    public void appendFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }


    public void replaceFragmentWithTag(int containerViewId, Fragment fragment, String fragmentTagName) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragmentTagName);
        fragmentTransaction.commit();
    }
}
