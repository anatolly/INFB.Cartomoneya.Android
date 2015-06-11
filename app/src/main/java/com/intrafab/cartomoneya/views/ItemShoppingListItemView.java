package com.intrafab.cartomoneya.views;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.ShoppingListItemAdapter;
import com.intrafab.cartomoneya.data.ShoppingListItem;

/**
 * Created by mono on 06/06/15.
 */
public class ItemShoppingListItemView extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private ShoppingListItem mItem;
    private final ShoppingListItemAdapter.OnClickListener mListener;
    private View rootView;
    private CheckBox mCbItem;
    private ImageView mIvEdit;
    private ImageView mTvDelete;

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
        mTvDelete = (ImageView) view.findViewById(R.id.ivDelete);
    }

    public void setItem(ShoppingListItem item) {
        this.mItem = item;
        fillView(item);
    }

    @Override
    public void onClick(View view) {
        if (mItem != null && mListener != null) {
            mListener.onClickItem(mItem);
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
            mCbItem.setChecked(true);
            mCbItem.setPaintFlags(mCbItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mCbItem.setChecked(false);
            mCbItem.setPaintFlags(mCbItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
