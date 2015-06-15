package com.intrafab.cartomoneya.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.ShoppingListItemAdapter;
import com.intrafab.cartomoneya.data.ShoppingListItem;

/**
 * Created by mono on 06/06/15.
 */
public class ItemShoppingListItemView extends RecyclerView.ViewHolder
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ShoppingListItem mItem;
    private final ShoppingListItemAdapter.OnClickListener mListener;
    private View rootView;
    private CheckBox mCbItem;
    private ImageView mIvEdit;
    private ImageView mIvDelete;

    public ItemShoppingListItemView(View view, ShoppingListItemAdapter.OnClickListener listener) {
        super(view);

        rootView = view;

        itemView.setOnClickListener(this);
        mListener = listener;

        setupChildren(view);
        rootView.setOnClickListener(this);
    }

    private void setupChildren(View view) {
        mCbItem = (CheckBox) view.findViewById(R.id.cbItem);
        mIvEdit = (ImageView) view.findViewById(R.id.ivEdit);
        mIvDelete = (ImageView) view.findViewById(R.id.ivDelete);

        mCbItem.setOnCheckedChangeListener(this);
        mIvEdit.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
    }

    public void setItem(ShoppingListItem item) {
        this.mItem = item;
        fillView(item);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            if (view.getId() == R.id.ivDelete) {
                mListener.onItemDelete(mItem);
            } else if (view.getId() == R.id.ivEdit) {
                mListener.onItemEdit(mItem);
            }
        }
    }

    public Context getContext() {
        if (rootView == null)
            return null;

        return rootView.getContext();
    }

    private void fillView(ShoppingListItem item) {
        if (item == null)
            return;

        // TODO: fill data for child views

        if (!TextUtils.isEmpty(item.getName())) {
            mCbItem.setText(item.getName());
        } else {
            mCbItem.setText(R.string.shopping_list_item_empty);
        }

        if (item.isDone()) {
            setChecked(mCbItem, true);
            mCbItem.setPaintFlags(mCbItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            setChecked(mCbItem, false);
            mCbItem.setPaintFlags(mCbItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private void setChecked(CheckBox checkBox, boolean value) {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(value);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
        if (mListener != null) {
            mListener.onCheckChange(mItem, value);
        }
    }
}
