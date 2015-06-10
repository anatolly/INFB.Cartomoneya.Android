package com.intrafab.cartomoneya.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.data.ShopOffer;
import com.intrafab.cartomoneya.views.ItemShopOfferView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasily Laushkin <vaslinux@gmail.com> on 06/06/15.
 */
public class ShopOfferAdapter extends RecyclerView.Adapter<ItemShopOfferView> {
    private OnClickListener mListener;
    private List<ShopOffer> mListItems = new ArrayList<ShopOffer>();

    public interface OnClickListener {
        public void onClickItem(ShopOffer itemShopCard);
    }

    public ShopOfferAdapter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemShopOfferView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_shop_offer_list_item, parent, false);

        return new ItemShopOfferView(view, mListener);
    }

    @Override
    public void onBindViewHolder(ItemShopOfferView viewHolder, int i) {
        final ShopOffer offer = mListItems.get(i);
        viewHolder.setItem(offer);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public void add(ShopOffer item) {
        mListItems.add(item);
    }

    public void addAll(List<ShopOffer> items) {
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
