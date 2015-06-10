package com.intrafab.cartomoneya.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.ShopOfferAdapter;
import com.intrafab.cartomoneya.data.ShopOffer;
import com.squareup.picasso.Picasso;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 06/06/15.
 */
public class ItemShopOfferView extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private ShopOffer mItem;
    private ShopOfferAdapter.OnClickListener mListener;

    private View rootView;
    private ImageView mIvShopOfferFrontImage;
    private TextView mTvShopOfferTitle;

    public ItemShopOfferView(View view, ShopOfferAdapter.OnClickListener listener) {
        super(view);

        rootView = view;

        itemView.setOnClickListener(this);
        mListener = listener;

        setupChildren(view);
        rootView.setOnClickListener(this);
    }

    private void setupChildren(View view) {
        mTvShopOfferTitle = (TextView) view.findViewById(R.id.tvShopOfferText);
        mIvShopOfferFrontImage = (ImageView) view.findViewById(R.id.ivShopOfferFrontImage);
    }

    public void setItem(ShopOffer itemShopOffer) {
        mItem = itemShopOffer;

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

    private void fillView(ShopOffer item) {
        if (item == null)
            return;

        if (!TextUtils.isEmpty(item.getName())) {
            mTvShopOfferTitle.setText(item.getName());
        } else {
            mTvShopOfferTitle.setText(R.string.shop_offer_title_unknown);
        }

        if (!TextUtils.isEmpty(item.getFrontImagePath())) {
            if (!TextUtils.isEmpty(item.getFrontImagePath())) {
                Picasso.with(getContext())
                        .load(item.getFrontImagePath())
                        .placeholder(R.mipmap.ic_default_card)
                        .error(R.mipmap.ic_default_card)
                        .into(mIvShopOfferFrontImage);
            } else {
                mIvShopOfferFrontImage.setImageResource(R.mipmap.ic_default_card);
            }
        }

    }
}
