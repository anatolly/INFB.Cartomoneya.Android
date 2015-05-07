package com.intrafab.cartomoneya.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.ShopCardAdapter;
import com.intrafab.cartomoneya.data.ShopCard;
import com.squareup.picasso.Picasso;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ItemShopCardView extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private ShopCard mItem;
    private ShopCardAdapter.OnClickListener mListener;

    private ImageView mImageThumbnail;
    private TextView mTextViewCardName;
    private TextView mTextViewTime;
    private RelativeLayout mItemLayout;
    private View rootView;

    public ImageView getThumbnail() {
        return mImageThumbnail;
    }

    public ItemShopCardView(View view, ShopCardAdapter.OnClickListener listener) {
        super(view);

        rootView = view;

        itemView.setOnClickListener(this);
        mListener = listener;

        setupChildren(view);
        rootView.setOnClickListener(this);
    }

    private void setupChildren(View view) {
        mImageThumbnail = (ImageView) view.findViewById(R.id.ivThumbnail);
        mTextViewCardName = (TextView) view.findViewById(R.id.textViewName);
        mTextViewTime = (TextView) view.findViewById(R.id.textViewTime);
        mItemLayout = (RelativeLayout) view.findViewById(R.id.rlItemLayout);
    }

    public void setItem(ShopCard itemShopCard) {
        mItem = itemShopCard;

        fillView(mItem);
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

    private void fillView(ShopCard item) {
        if (item == null)
            return;

        if (!TextUtils.isEmpty(item.getName())) {
            mTextViewCardName.setText(item.getName());
        } else {
            mTextViewCardName.setText(R.string.shopping_card_name_unknown);
        }

        if (!TextUtils.isEmpty(item.getFrontImageFile())) {
            Picasso.with(getContext())
                    .load(item.getFrontImageFile())
                    .placeholder(R.mipmap.ic_default_card)
                    .error(R.mipmap.ic_default_card)
                    .into(mImageThumbnail);
        } else {
            mImageThumbnail.setImageResource(R.mipmap.ic_default_card);
        }

    }

}
