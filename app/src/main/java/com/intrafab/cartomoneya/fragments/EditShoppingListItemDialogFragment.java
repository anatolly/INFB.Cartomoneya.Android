package com.intrafab.cartomoneya.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.data.ShoppingListItem;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 14/06/15.
 */
public class EditShoppingListItemDialogFragment extends DialogFragment {
    public static final String ARG_SHOPPING_ITEM = "arg_shopping_item";
    private EditShoppingListItemDialogListener mListener;
    private ShoppingListItem mItem;
    private EditText mEdShoppingListItem;

    public interface EditShoppingListItemDialogListener {
        public void onItemEdited(ShoppingListItem item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mItem = getArguments().getParcelable(ARG_SHOPPING_ITEM);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.shopping_list_item_edit_dialog, null);
        mEdShoppingListItem = (EditText) view.findViewById(R.id.etShoppingListItem);
        mEdShoppingListItem.setText(mItem.getName());
        mEdShoppingListItem.setSelection(mEdShoppingListItem.getText().length());
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mListener != null) {
                    mItem.setName(mEdShoppingListItem.getText().toString());
                    mListener.onItemEdited(mItem);
                }
                hideKeyboard();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hideKeyboard();
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (EditShoppingListItemDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditShoppingListItemDialogListener");
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdShoppingListItem.getWindowToken(), 0);
    }
}
