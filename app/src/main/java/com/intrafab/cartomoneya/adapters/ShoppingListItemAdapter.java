package com.intrafab.cartomoneya.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intrafab.cartomoneya.R;
import com.intrafab.cartomoneya.data.ShoppingListItem;
import com.intrafab.cartomoneya.views.ItemShoppingListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mono on 06/06/15.
 */
public class ShoppingListItemAdapter extends RecyclerView.Adapter<ItemShoppingListItemView> {

    private OnClickListener mListener;
    private List<ShoppingListItem> mListItems = new ArrayList<ShoppingListItem>();

    public ShoppingListItemAdapter(OnClickListener listener) {
        mListener = listener;
    }


    public interface OnClickListener {
        public void onClickItem(ShoppingListItem itemShopCard);
        public void onNewItemCreated(ShoppingListItem itemShopCard);
    }

    @Override
    public ItemShoppingListItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_shopping_list_item, parent, false);

        return new ItemShoppingListItemView(view, mListener);
    }

    @Override
    public void onBindViewHolder(ItemShoppingListItemView viewHolder, int i) {
        final ShoppingListItem item = mListItems.get(i);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public void add(ShoppingListItem item) {
        mListItems.add(item);
    }

    public void addAll(List<ShoppingListItem> items) {
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
