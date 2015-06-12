package com.intrafab.cartomoneya.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.data.BusinessCard;
import com.intrafab.cartomoneya.data.BusinessCardPopulated;
import com.intrafab.cartomoneya.views.ItemBizCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 24/05/15.
 */
public class BizCardAdapter extends RecyclerView.Adapter<ItemBizCardView> {

    private OnClickListener mListener;
    private List<BusinessCardPopulated> mListItems = new ArrayList<BusinessCardPopulated>();

    public interface OnClickListener {
        public void onClickItem(BusinessCardPopulated itemShopCard);
    }

    public BizCardAdapter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemBizCardView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_bizcard_list_item, parent, false);

        return new ItemBizCardView(view, mListener);
    }

    @Override
    public void onBindViewHolder(ItemBizCardView viewHolder, int i) {
        final BusinessCardPopulated item = mListItems.get(i);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public void add(BusinessCardPopulated item) {
        mListItems.add(item);
    }

    public void addAll(List<BusinessCardPopulated> items) {
        if (items != null)
            mListItems.addAll(items);
        notifyDataSetChanged();
    }

    public int size() {
        return mListItems.size();
    }

    public void clear() {
        mListItems.clear();
    }
}
