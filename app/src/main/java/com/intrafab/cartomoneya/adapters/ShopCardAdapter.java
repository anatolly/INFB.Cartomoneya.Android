package com.intrafab.cartomoneya.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.data.ShopCard;
import com.intrafab.cartomoneya.views.ItemShopCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artemiy Terekhov on 07.05.2015.
 */
public class ShopCardAdapter extends RecyclerView.Adapter<ItemShopCardView> {

    private OnClickListener mListener;
    private List<ShopCard> mListItems = new ArrayList<ShopCard>();

    public interface OnClickListener {
        public void onClickItem(ShopCard itemShopCard);
    }

    public ShopCardAdapter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemShopCardView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_card_list_item, parent, false);

        return new ItemShopCardView(view, mListener);
    }

    @Override
    public void onBindViewHolder(ItemShopCardView viewHolder, int i) {
        final ShopCard item = mListItems.get(i);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public void add(ShopCard item) {
        mListItems.add(item);
    }

    public void addAll(List<ShopCard> items) {
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
