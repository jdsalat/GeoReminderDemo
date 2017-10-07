package com.georeminder.src.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Javed.Salat on 3/17/2016.
 */
public abstract class ClientBaseFragment extends Fragment {
    public static final String TAG = "Client-BaseFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Commented below line as app was crashing at the time of ViewPager load
         * during language toggle. As per Android doc, ViewPager itself is a Fragment
         * so it's clear case of loading a fragment inside another fragment. Hence
         * remove setRetainInstance to false;
         */
        setRetainInstance(false);
        setHasOptionsMenu(true);
    }

    /**
     * This method is being used by TabGeneralFragment. In this case
     * onActivityResult method of TabGeneralFragment will receive the control
     * only up on iterating through the childfragments. This peculiar behavior
     * is due to Nested fragments ViewPager.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    /**
     * Shows a {@link Toast} message.
     *
     * @param message An string representing a message to be shown.
     */
    protected void showToastMessage(String message) {
    }

    /**
     * This method will be responsible in replacing a fragment in a particular View Group.
     *
     * @param containerViewId
     * @param fragment
     */
    public void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        /*Removing the back stack because it is giving blank screen on back button*/
        //fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    public void replaceFragmentWithBackStack(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        /*Removing the back stack because it is giving blank screen on back button*/
        fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    public void replaceFragments(int[] containerViewId, Fragment[] fragment) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < containerViewId.length; i++) {
            //ClientLogs.printLogs(ClientLogs.errorLogType,"ICDS", ""+containerViewId[i]);
            //ClientLogs.printLogs(ClientLogs.errorLogType,"ICDS", ""+fragment[i]);
            fragmentTransaction.replace(containerViewId[i], fragment[i]);
        }
        fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    /**
     * This method will be responsible in adding a fragment to a particular View Group.
     *
     * @param containerViewId
     * @param fragment
     */
    public void appendFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    public void appendFragments(int[] containerViewId, Fragment[] fragment) {
        FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < containerViewId.length; i++) {
            fragmentTransaction.add(containerViewId[i], fragment[i]);
        }
        fragmentTransaction.addToBackStack(null);//add the transaction to the back stack so the user can navigate back
        fragmentTransaction.commit();
    }

    public void showToastMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
