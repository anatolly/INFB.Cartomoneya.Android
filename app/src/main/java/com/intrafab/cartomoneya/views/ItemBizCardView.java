package com.intrafab.cartomoneya.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.adapters.BizCardAdapter;
import com.intrafab.cartomoneya.data.BizCard;
import com.squareup.picasso.Picasso;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 24/05/15.
 */
public class ItemBizCardView extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private BizCard mItem;
    private BizCardAdapter.OnClickListener mListener;

    private ImageView mImageThumbnail;
    private TextView mTextViewCardName;
    private TextView mTextViewJobTitle;
    private RelativeLayout mItemLayout;
    private View rootView;

    public ImageView getThumbnail() {
        return mImageThumbnail;
    }

    public ItemBizCardView(View view, BizCardAdapter.OnClickListener listener) {
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
        mTextViewJobTitle = (TextView) view.findViewById(R.id.textViewJobTitle);
        mItemLayout = (RelativeLayout) view.findViewById(R.id.rlItemLayout);
    }

    public void setItem(BizCard itemShopCard) {
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

    private void fillView(BizCard item) {
        if (item == null)
            return;

        String name;
        String jobTitle = null;
        if (item.getPersonage() != null) {
            if (!TextUtils.isEmpty(item.getPersonage().getName())) {
                name = item.getPersonage().getName();
            } else {
                name = getContext().getString(R.string.business_card_name_unknown);
            }

            if (!TextUtils.isEmpty(item.getPersonage().getJobTitle())) {
                jobTitle = item.getPersonage().getJobTitle();
            }
        } else {
            if (!TextUtils.isEmpty(item.getName())) {
                name = item.getName();
            } else {
                name = getContext().getString(R.string.business_card_name_unknown);
            }

            if (!TextUtils.isEmpty(item.getNotes())) {
                jobTitle = item.getNotes();
            }
        }

        mTextViewCardName.setText(name);
        if (!TextUtils.isEmpty(jobTitle)) {
            mTextViewJobTitle.setText(jobTitle);
            mTextViewJobTitle.setVisibility(View.VISIBLE);
        } else {
            mTextViewJobTitle.setVisibility(View.GONE);
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
