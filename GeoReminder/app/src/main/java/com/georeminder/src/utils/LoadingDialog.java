package com.georeminder.src.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.georeminder.src.R;

/**
 * Created by Javed.Salat on 16-Sep-16.
 */
public class LoadingDialog {
    static Dialog progressDialog = null;
    private static AlertDialog showSimpleDialog;


    public static void showDialog(Context context) {
        progressDialog = new Dialog(context, R.style.MyDialogStyle);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_wheel);
        progressDialog.setTitle(context.getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
