package com.intrafab.cartomoneya.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.intrafab.cartomoneya.R;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 12/06/15.
 */
public class ProgressDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.loaging));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
